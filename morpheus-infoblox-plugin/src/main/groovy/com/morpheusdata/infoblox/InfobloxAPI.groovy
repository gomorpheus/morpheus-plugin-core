package com.morpheusdata.infoblox

import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.response.ServiceResponse
import groovy.util.logging.Slf4j
import org.apache.commons.beanutils.PropertyUtils
import org.apache.http.*
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.*
import org.apache.http.client.utils.URIBuilder
import org.apache.http.config.MessageConstraints
import org.apache.http.config.Registry
import org.apache.http.config.RegistryBuilder
import org.apache.http.conn.ConnectTimeoutException
import org.apache.http.conn.HttpConnectionFactory
import org.apache.http.conn.ManagedHttpClientConnection
import org.apache.http.conn.routing.HttpRoute
import org.apache.http.conn.socket.ConnectionSocketFactory
import org.apache.http.conn.socket.PlainConnectionSocketFactory
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.SSLContextBuilder
import org.apache.http.conn.ssl.TrustStrategy
import org.apache.http.conn.ssl.X509HostnameVerifier
import org.apache.http.entity.StringEntity
import org.apache.http.impl.DefaultHttpResponseFactory
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.BasicHttpClientConnectionManager
import org.apache.http.impl.conn.DefaultHttpResponseParser
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory
import org.apache.http.io.HttpMessageParser
import org.apache.http.io.HttpMessageParserFactory
import org.apache.http.io.HttpMessageWriterFactory
import org.apache.http.io.SessionInputBuffer
import org.apache.http.message.BasicHeader
import org.apache.http.message.BasicLineParser
import org.apache.http.message.LineParser
import org.apache.http.protocol.HttpContext
import org.apache.http.ssl.SSLContexts
import org.apache.http.util.CharArrayBuffer
import org.apache.http.util.EntityUtils

import javax.net.ssl.*
import java.lang.reflect.InvocationTargetException
import java.security.cert.X509Certificate

@Slf4j
class InfobloxAPI {
	static Integer WEB_CONNECTION_TIMEOUT = 120 * 1000

	ServiceResponse callApi(String url, String path, String username, String password, Map opts = [:], String method = 'POST') {
		def rtn = new ServiceResponse()
		try {
			def uriBuilder = new URIBuilder("${url}/${path}")
			if(opts.query) {
				opts.query?.each { k, v ->
					uriBuilder.addParameter(k, v?.toString())
				}
			}

			HttpRequestBase request
			switch(method) {
				case 'HEAD':
					request = new HttpHead(uriBuilder.build())
					break
				case 'PUT':
					request = new HttpPut(uriBuilder.build())
					break
				case 'POST':
					request = new HttpPost(uriBuilder.build())
					break
				case 'GET':
					request = new HttpGet(uriBuilder.build())
					break
				case 'DELETE':
					request = new HttpDelete(uriBuilder.build())
					break
				default:
					throw new Exception('method was not specified')
			}
			if(username && password) {
				def creds = "${username}:${password}"
				request.addHeader('Authorization',"Basic ${creds.getBytes().encodeBase64().toString()}".toString())
			}

			// Headers
			if(!opts.headers || !opts.headers['Content-Type']) {
				request.addHeader('Content-Type', 'application/json')
			}
			opts.headers?.each { k, v ->
				request.addHeader(k, v)
			}

			if (opts.body) {
				HttpEntityEnclosingRequestBase postRequest = (HttpEntityEnclosingRequestBase)request
				postRequest.setEntity(new StringEntity(opts.body.encodeAsJson().toString()));
			}

			withClient(opts) { HttpClient client, BasicCookieStore basicCookieStore ->
				CloseableHttpResponse response = client.execute(request)
				try {
					if(response.statusLine.statusCode <= 399) {
						response.allHeaders.each { h ->
							println "header - $h.name = $h.value"
							rtn.addHeader(h.name.toString(), h.value)
						}
						rtn.cookies = [ibapauth: basicCookieStore?.cookies?.find{it.name == 'ibapauth'}?.value]

						HttpEntity entity = response.entity
						if(entity) {
							rtn.content = EntityUtils.toString(entity)
							if(!opts.suppressLog) {
								log.debug("results of SUCCESSFUL call to {}/{}, results: {}",url,path,rtn.content ?: '')
							}
						} else {
							rtn.content = null
						}
						rtn.success = true
					} else {
						if(response.entity) {
							rtn.content = EntityUtils.toString(response.entity)
						}
						rtn.success = false
						rtn.errorCode = response.statusLine.statusCode.toString()
						log.warn("path: ${path} error: ${rtn.errorCode} - ${rtn.content}")
					}
				} catch(ex) {
					log.error "Error occurred processing the response for ${url}/${path} : ${ex.message}", ex
					rtn.setError("Error occurred processing the response for ${url}/${path} : ${ex.message}")
					rtn.success = false
				} finally {
					if(response) {
						response.close()
					}
				}
			}
		} catch(SSLProtocolException sslEx) {
			log.error("Error Occurred calling Infoblox API (SSL Exception): ${sslEx.message}",sslEx)
			rtn.addError('sslHandshake',  "SSL Handshake Exception (is SNI Misconfigured): ${sslEx.message}")
			rtn.success = false
		} catch (e) {
			log.error("Error Occurred calling Infoblox API: ${e.message}",e)
			rtn.addError('error', e.message)
			rtn.success = false
		}
		return rtn
	}

	private withClient(opts=[:], Closure cl) {
		def ignoreSSL = true

		HttpClientBuilder clientBuilder = HttpClients.custom()
		clientBuilder.setHostnameVerifier(new X509HostnameVerifier() {
			public boolean verify(String host, SSLSession sess) {
				return true
			}

			public void verify(String host, SSLSocket ssl) {

			}

			public void verify(String host, String[] cns, String[] subjectAlts) {

			}

			public void verify(String host, X509Certificate cert) {

			}

		})
		SSLConnectionSocketFactory sslConnectionFactory
		SSLContext sslcontext
		if(ignoreSSL) {
			sslcontext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
					return true
				}
			}).build()
			sslConnectionFactory = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER) {

				@Override
				protected void prepareSocket(SSLSocket socket) {
					if(opts.ignoreSSL) {
						PropertyUtils.setProperty(socket, "host", null);
						List<SNIServerName> serverNames  = Collections.<SNIServerName> emptyList();
						SSLParameters sslParams = socket.getSSLParameters();
						sslParams.setServerNames(serverNames);
						socket.setSSLParameters(sslParams);
					}
				}
				@Override
				public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException, ConnectTimeoutException {
					if(socket instanceof SSLSocket) {
						try {
							socket.setEnabledProtocols(['SSLv3', 'TLSv1', 'TLSv1.1', 'TLSv1.2'] as String[])
							SSLSocket sslSocket = (SSLSocket)socket

							log.debug "hostname: ${host?.getHostName()}"
							PropertyUtils.setProperty(socket, "host", host.getHostName());
						} catch (NoSuchMethodException ex) {}
						catch (IllegalAccessException ex) {}
						catch (InvocationTargetException ex) {}
						catch (Exception ex) {
							log.error "We have an unhandled exception when attempting to connect to ${host} ignoring SSL errors", ex
						}
					}
					return super.connectSocket(WEB_CONNECTION_TIMEOUT, socket, host, remoteAddress, localAddress, context)
				}
			}
		} else {
			sslcontext = SSLContexts.createSystemDefault()
			sslConnectionFactory = new SSLConnectionSocketFactory(sslcontext) {
				@Override
				public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException, ConnectTimeoutException {
					if(socket instanceof SSLSocket) {
						try {
							socket.setEnabledProtocols(['SSLv3', 'TLSv1', 'TLSv1.1', 'TLSv1.2'] as String[])
							PropertyUtils.setProperty(socket, "host", host.getHostName());
						} catch(NoSuchMethodException ex) {
						}
						catch(IllegalAccessException ex) {
						}
						catch(InvocationTargetException ex) {
						}
					}
					return super.connectSocket(opts.timeout ?: 30000, socket, host, remoteAddress, localAddress, context)
				}
			}
		}


		HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {

			@Override
			public HttpMessageParser<HttpResponse> create(SessionInputBuffer ibuffer, MessageConstraints constraints) {
				LineParser lineParser = new BasicLineParser() {

					@Override
					public Header parseHeader(final CharArrayBuffer buffer) {
						try {
							return super.parseHeader(buffer);
						} catch (ParseException ex) {
							return new BasicHeader(buffer.toString(), null);
						}
					}

				};
				return new DefaultHttpResponseParser(
					ibuffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints ?: MessageConstraints.DEFAULT) {

					@Override
					protected boolean reject(final CharArrayBuffer line, int count) {
						//We need to break out of forever head reads
						if(count > 100) {
							return true
						}
						return false;

					}

				};
			}
		}

		clientBuilder.setSSLSocketFactory(sslConnectionFactory)
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
			.register("https", sslConnectionFactory)
			.register("http", PlainConnectionSocketFactory.INSTANCE)
			.build();

		HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

		HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
			requestWriterFactory, responseParserFactory);
		BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(registry, connFactory)
		clientBuilder.setConnectionManager(connectionManager)

		HttpClient client = clientBuilder.build()
		try {
			cl.call(client)
		} finally {
			connectionManager.shutdown()
		}

	}

	void shutdownClient(Map opts) {
		// FIXME Shutdown/close client
	}
}
