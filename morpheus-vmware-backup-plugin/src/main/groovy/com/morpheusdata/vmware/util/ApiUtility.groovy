package com.morpheusdata.vmware.util

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.apache.commons.beanutils.PropertyUtils
import org.apache.http.*
import org.apache.http.auth.AuthScope
import org.apache.http.auth.NTCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.UrlEncodedFormEntity
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
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.entity.StringEntity
import org.apache.http.impl.DefaultHttpResponseFactory
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.client.ProxyAuthenticationStrategy
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
import org.apache.http.message.BasicNameValuePair
import org.apache.http.message.LineParser
import org.apache.http.protocol.HttpContext
import org.apache.http.ssl.SSLContexts
import org.apache.http.util.CharArrayBuffer
import org.apache.http.util.EntityUtils

import javax.net.ssl.*
import javax.xml.bind.DatatypeConverter
import java.lang.reflect.InvocationTargetException
import java.security.*
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.interfaces.RSAPrivateKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.util.zip.GZIPInputStream

@Slf4j
class ApiUtility {

	static Integer WEB_CONNECTION_TIMEOUT = 120 * 1000
	static Integer WEB_SOCKET_TIMEOUT = 120 * 1000

	static callJsonApi(String url, String path, Map opts = [:], String method = 'POST') {
		callJsonApi(url, path, null, null, opts, method)
	}

	static callJsonApi(String url, String path, String username, String password, Map opts = [:], String method = 'POST') {
		//encode the body
		if(opts.body && opts.bodyType != 'form' && !(opts.body instanceof String) )
			opts.body = opts.body.encodeAsJson().toString()
		//execute
		def rtn = callApi(url, path, username, password, opts, method)
		rtn.data = [:]
		if(rtn.content?.length() > 0) {
			try {
				rtn.data = new JsonSlurper().parseText(rtn.content)
			} catch(e) {
				log.debug("Error parsing API response JSON: ${e}", e)
			}
		}
		return rtn
	}

	static callXmlApi(String url, String path, Map opts = [:], String method = 'POST') {
		callXmlApi(url, path, null, null, opts, method)
	}

	static callXmlApi(String url, String path, String username, String password, Map opts = [:], String method = 'POST') {
		//encode the body
		if(opts.body) {
			opts.body = opts.body.getBytes('UTF-8')
			//add xml content type if not set
			opts.headers = addRequiredHeader(opts.headers, 'Content-Type', 'application/xml')
		}
		//execute
		def rtn = callApi(url, path, username, password, opts, method)
		rtn.data = [:]
		if(rtn.content?.length() > 0) {
			try {
				rtn.data =  new groovy.util.XmlSlurper(false,true).parseText(rtn.content)
			} catch(e) {
				log.debug("Error parsing API response XML: ${e}", e)
			}
		}
		return rtn
	}

	static addRequiredHeader(Map headers, String name, String value) {
		def rtn = headers ?: [:]
		if(rtn[name] == null)
			rtn[name] = value
		return rtn
	}

	static parseHttpResponse(HttpResponse response) {
		def rtn = [success:false]
		if(response) {
			rtn.statusCode = response.getStatusLine().getStatusCode()
			def entity = response.getEntity()
			if(entity) {
				def contentTypeHeader = entity.getContentType()
				rtn.contentType = contentTypeHeader ? contentTypeHeader.getValue() : 'text'
				rtn.content = EntityUtils.toString(entity)
				//switch on content type
				if(rtn.contentType && rtn.contentType.startsWith('application/json')) {
					try { rtn.data = new groovy.json.JsonSlurper().parseText(rtn.content) } catch(e) { rtn.data = rtn.content }
				} else if(rtn.contentType && rtn.contentType.startsWith('application/xml') || rtn.contentType.startsWith('application/x-gzip')) {
					try { rtn.data = new groovy.util.XmlSlurper().parseText(rtn.content) } catch(e) { rtn.data = rtn.content }
				} else {
					rtn.data = rtn.content
				}
			}
			if(rtn.statusCode > 399) {
				rtn.errorCode = rtn.statusCode
				rtn.errorOutput = rtn.content ?: "HTTP Response Status: ${rtn.statusCode}"
			} else {
				rtn.success = true
			}
		}
		return rtn
	}

	static callApi(String url, String path, String username, String password, Map opts = [:], String method = 'POST') {
		def rtn = [success:false, headers:[:], cookies:[:]]
		try {
			URIBuilder uriBuilder = new URIBuilder("${url}")
			uriBuilder.setPath(path)
			if(opts.query) {
				opts.query?.each { k, v ->
					uriBuilder.addParameter(k, v?.toString())
				}
			}
			if(opts.queryList) {
				opts.queryList?.each { row ->
					uriBuilder.addParameter(row.key, row.value?.toString())
				}
			}
			//get the request
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
				case 'PATCH':
					request = new HttpPatch(uriBuilder.build())
					break
				default:
					throw new Exception('method was not specified')
			}
			//if basic auth info - add it
			if(username && password) {
				def creds = "${username}:${password}"
				request.addHeader('Authorization', "Basic ${creds.getBytes().encodeBase64().toString()}".toString())
			}
			if(opts.apiToken) {
				def newLine = opts.apiToken.indexOf('\n')
				if(newLine > -1)
					opts.apiToken = opts.apiToken.substring(0, newLine)
				request.addHeader('Authorization', 'Bearer ' + opts.apiToken)
			}

			//add headers
			if(!opts.headers || !opts.headers['Content-Type']) {
				request.addHeader('Content-Type', 'application/json')
			}
			opts.headers?.each { k, v ->
				request.addHeader(k, v)
			}
			if(opts.cookies?.size() > 0) {
				def cookieList = opts.cookies?.collect{ key, value -> "${key}=${value}" }
				request.addHeader('Cookie', cookieList.join('; '))
			}
			//set the body
			if(opts.body) {
				HttpEntityEnclosingRequestBase postRequest = (HttpEntityEnclosingRequestBase)request
				if(opts.body instanceof Map) {
					if(opts.bodyType == 'form') {
						List<NameValuePair> urlParameters = new ArrayList<NameValuePair>()
						opts.body?.each { k, v ->
							urlParameters.add(new BasicNameValuePair(k, v))
						}
						postRequest.setEntity(new UrlEncodedFormEntity(urlParameters))
					} else {
						postRequest.setEntity(new StringEntity(opts.body.encodeAsJson().toString()))
					}
				} else if(opts.body instanceof byte[]) {
					postRequest.setEntity(new ByteArrayEntity(opts.body))
				} else {
					postRequest.setEntity(new StringEntity(opts.body))
				}
			}
			//make the call
			withClient(opts) { HttpClient client ->
				CloseableHttpResponse response = client.execute(request)
				try {
					rtn.statusCode = response.getStatusLine().getStatusCode()
					if(response.getStatusLine().getStatusCode() <= 399) {
						response.getAllHeaders().each { h ->
							rtn.headers["${h.name}"] = h.value
							if(h.name == 'Set-Cookie') {
								def dataStart = h.value.indexOf('=')
								def dataEnd = h.value.indexOf(';')
								if(dataStart > -1 && dataEnd > -1) {
									def cookieRow = [:]
									def cookieName = h.value.substring(0, dataStart)
									cookieRow.value = h.value.substring(dataStart + 1, dataEnd)
									//extra info
									def cookieConfig = h.value?.tokenize(';')
									if(cookieConfig?.size() > 1) {
										//add extras
										cookieConfig.each { cookieOption ->
											def cookieTokens = cookieOption.tokenize('=')
											def optionName = cookieTokens?.size() > 0 ? cookieTokens[0]?.trim() : null
											if(cookieTokens?.size() > 1) {
												if(optionName != cookieName)
													cookieRow[optionName] = cookieTokens[1]
											} else if(cookieTokens?.size() > 0) {
												cookieRow[optionName] = true
											}
										}
									}
									rtn.cookies[cookieName] = cookieRow
								}
							}
						}
						if(response.containsHeader("Location")) {
							rtn.location = response.getFirstHeader("Location").value
						}
						HttpEntity entity = response.getEntity()
						if(entity) {
							if(opts.stream == true && opts.streamProcessor) {
								//process the stream
								opts.streamProcessor.call(response)
							} else {
								if(response.containsHeader("Content-Type") && response.getFirstHeader("Content-Type").value == 'application/x-gzip') {
									GZIPInputStream gzipIs = new GZIPInputStream(response.entity.getContent())
									rtn.content = gzipIs.text
								} else {
									rtn.content = EntityUtils.toString(entity);
								}
								if(!opts.suppressLog) {
									log.debug("results of SUCCESSFUL call to {}/{}, results: {}",url,path,rtn.content ?: '')
								}
							}
						} else {
							rtn.content = null
						}
						rtn.success = true
					} else {
						if(response.getEntity()) {
							rtn.content = EntityUtils.toString(response.getEntity());
						}
						rtn.success = false

						rtn.errorCode = response.getStatusLine().getStatusCode()
						log.warn("Failure in call ${url} path: ${path} error: ${rtn.errorCode} - ${rtn.content}")
					}
				} catch(ex) {
					log.error "Error occurred processing the response for ${url}/${path} : ${ex.message}", ex
					rtn.error = "Error occurred processing the response for ${url}/${path} : ${ex.message}"
					rtn.success = false
				} finally {
					if(response) {
						response.close()
					}
				}
			}
		} catch(javax.net.ssl.SSLProtocolException sslEx) {
			log.error("Error Occurred calling web API (SSL Exception): ${sslEx.message}", sslEx)
			rtn.error = "SSL Handshake Exception (is SNI Misconfigured): ${sslEx.message}"
			rtn.success = false
		} catch(SocketTimeoutException e) {
			log.error("Error Occurred calling web API: ${url} ${path} - ${e.message}")
			rtn.error = e.message
			rtn.success = false
		} catch(Exception e) {
			log.error("Error Occurred calling web API: ${url} ${path} - ${e.message}", e)
			rtn.error = e.message
			rtn.success = false
		}
		return rtn
	}

	static withClient(Map opts = [:], Closure cl) {
		def ignoreSSL = (opts.ignoreSSL == null || opts.ignoreSSL == true)
		HttpClientBuilder clientBuilder = HttpClients.custom()
		if(opts.noRedirects) {
			println "disable redirects"
			clientBuilder.disableRedirectHandling()
		}

		if(opts.connectionTimeout || opts.readTimeout) {
			def reqConfigBuilder = RequestConfig.custom()

			if(opts.connectTimeout) {
				reqConfigBuilder.setConnectTimeout(opts.connectTimeout)
				reqConfigBuilder.setConnectionRequestTimeout(opts.connectTimeout)
			}
			if(opts.readTimeout) {
				reqConfigBuilder.setSocketTimeout(opts.readTimeout)
			}
			clientBuilder.setDefaultRequestConfig(reqConfigBuilder.build())
		}

		clientBuilder.setHostnameVerifier(new X509HostnameVerifier() {
			public boolean verify(String host, SSLSession sess) { return true }
			public void verify(String host, SSLSocket ssl) {}
			public void verify(String host, String[] cns, String[] subjectAlts) {}
			public void verify(String host, X509Certificate cert) {}
		})
		SSLConnectionSocketFactory sslConnectionFactory
		SSLContext sslcontext
		if(ignoreSSL) {
			if(opts.cert && opts.privateKey) {
				// certs are being used
				// TODO : Handle cacert
				KeyStore keystore = createKeyStore(opts.privateKey, opts.cert);
				sslcontext = new SSLContextBuilder().loadKeyMaterial(keystore, ''.toCharArray()).loadTrustMaterial(null, new TrustStrategy() {
					@Override
					boolean isTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
						return true
					}
				}).build()
			} else {
				sslcontext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
					@Override
					boolean isTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
						return true
					}
				}).build()
			}

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
				public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context)
						throws IOException, ConnectTimeoutException {
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
			if(opts.cert && opts.privateKey) {
				// certs are being used
				// TODO : Handle cacert
				KeyStore keystore = createKeyStore(opts.privateKey, opts.cert);
				sslcontext = new SSLContextBuilder().loadKeyMaterial(keystore, ''.toCharArray()).build()
			} else {
				sslcontext = SSLContexts.createSystemDefault()
			}
			sslConnectionFactory = new SSLConnectionSocketFactory(sslcontext) {
				@Override
				public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context)
						throws IOException, ConnectTimeoutException {
					if(socket instanceof SSLSocket) {
						try {
							socket.setEnabledProtocols(['SSLv3', 'TLSv1', 'TLSv1.1', 'TLSv1.2'] as String[])
							PropertyUtils.setProperty(socket, "host", host.getHostName());
						} catch(NoSuchMethodException ex) {}
						catch(IllegalAccessException ex) {}
						catch(InvocationTargetException ex) {}
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
				return new DefaultHttpResponseParser(ibuffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints ?: MessageConstraints.DEFAULT) {
					@Override
					protected boolean reject(final CharArrayBuffer line, int count) {
						//We need to break out of forever head reads
						if(count > 100) {
							return true
						}
						return false
					}
				}
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
		if(opts.proxySettings) {
			def proxySettings = opts.proxySettings
			def proxyHost = proxySettings.proxyHost
			def proxyPort = proxySettings.proxyPort
			if(proxyHost && proxyPort) {
				log.debug "proxy detected at ${proxyHost}:${proxyPort}"
				def proxyUser = proxySettings.proxyUser
				def proxyPassword = proxySettings.proxyPassword
				def proxyWorkstation = proxySettings.proxyWorkstation ?: null
				def proxyDomain = proxySettings.proxyDomain ?: null
				clientBuilder.setProxy(new HttpHost(proxyHost, proxyPort))
				if(proxyUser) {
					CredentialsProvider credsProvider = new BasicCredentialsProvider()
					NTCredentials ntCreds = new NTCredentials(proxyUser, proxyPassword, proxyWorkstation, proxyDomain)
					credsProvider.setCredentials(new AuthScope(proxyHost, proxyPort), ntCreds)
					clientBuilder.setDefaultCredentialsProvider(credsProvider)
					clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy())
				}
			}
		}
		HttpClient client = clientBuilder.build()
		try {
			cl.call(client)
		} finally {
			connectionManager.shutdown()
		}
	}

	private static KeyStore createKeyStore(String privateKeyPem, String certificatePem, String password='')
			throws Exception, KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		final X509Certificate[] cert = createCertificates(certificatePem);
		final KeyStore keystore = KeyStore.getInstance("JKS");
		keystore.load(null);
		// Import private key
		final PrivateKey key = createPrivateKey(privateKeyPem);
		keystore.setKeyEntry('privateKeyPem', key, password.toCharArray(), cert);
		return keystore;
	}

	private static PrivateKey createPrivateKey(String privateKeyPem) throws Exception {
		InputStream inputStream = new ByteArrayInputStream(privateKeyPem.getBytes("UTF-8"))
		final BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		String s = r.readLine();
		if (s == null || (!s.contains("BEGIN RSA PRIVATE KEY") && !s.contains("BEGIN PRIVATE KEY"))) {
			r.close();
			throw new IllegalArgumentException("No PRIVATE KEY found");
		}
		final StringBuilder b = new StringBuilder();
		s = "";
		while (s != null) {
			if (s.contains("END RSA PRIVATE KEY") || s.contains("END PRIVATE KEY")) {
				break;
			}
			b.append(s);
			s = r.readLine();
		}
		r.close();
		final String hexString = b.toString();
		final byte[] bytes = DatatypeConverter.parseBase64Binary(hexString);
		return generatePrivateKeyFromDER(bytes);
	}

	private static X509Certificate[] createCertificates(String certificatePem) throws Exception {
		final List<X509Certificate> result = new ArrayList<X509Certificate>();
		InputStream inputStream = new ByteArrayInputStream(certificatePem.getBytes("UTF-8"))
		final BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		String s = r.readLine();
		if (s == null || !s.contains("BEGIN CERTIFICATE")) {
			r.close();
			throw new IllegalArgumentException("No CERTIFICATE found");
		}
		StringBuilder b = new StringBuilder();
		while (s != null) {
			if (s.contains("END CERTIFICATE")) {
				String hexString = b.toString();
				final byte[] bytes = DatatypeConverter.parseBase64Binary(hexString);
				X509Certificate cert = generateCertificateFromDER(bytes);
				result.add(cert);
				b = new StringBuilder();
			} else {
				if (!s.startsWith("----")) {
					b.append(s);
				}
			}
			s = r.readLine();
		}
		r.close();

		return result.toArray(new X509Certificate[result.size()]);
	}

	private static RSAPrivateKey generatePrivateKeyFromDER(byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
		final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		final KeyFactory factory = KeyFactory.getInstance("RSA");
		return (RSAPrivateKey) factory.generatePrivate(spec);
	}

	private static X509Certificate generateCertificateFromDER(byte[] certBytes) throws CertificateException {
		final CertificateFactory factory = CertificateFactory.getInstance("X.509");
		return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
	}

}
