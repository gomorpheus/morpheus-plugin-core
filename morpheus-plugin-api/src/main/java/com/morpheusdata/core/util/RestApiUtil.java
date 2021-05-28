package com.morpheusdata.core.util;
import com.morpheusdata.response.ServiceResponse;
import groovy.json.JsonOutput;
import groovy.json.JsonSlurper;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.DefaultHttpResponseParser;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Utility methods for calling external APIs in a standardized way.
 *
 * @author David Estes
 * @since 0.8.0
 */
public class RestApiUtil {
	static Logger log = LoggerFactory.getLogger(RestApiUtil.class);

	static final Integer WEB_CONNECTION_TIMEOUT = 120 * 1000;

	static ServiceResponse callApi(String url, String path, String username, String password) throws URISyntaxException, Exception {
		return callApi(url,path,username,password,new RestOptions(),"POST");
	}

	static ServiceResponse callApi(String url, String path, String username, String password, RestOptions opts) throws URISyntaxException, Exception {
		return callApi(url,path,username,password,opts,"POST");
	}

	static ServiceResponse callApi(String url, String path, String username, String password, RestOptions opts, String method) throws URISyntaxException, Exception {
		ServiceResponse rtn = new ServiceResponse();
		LinkedHashMap<String,Object> data = new LinkedHashMap<>();
		rtn.setData(data);

		try {

			URIBuilder uriBuilder = new URIBuilder(url + "/" + path);
			if(opts.queryParams != null && !opts.queryParams.isEmpty()) {
				for(String queryKey : opts.queryParams.keySet()) {
					uriBuilder.addParameter(queryKey, opts.queryParams.get(queryKey));
				}
			}

			HttpRequestBase request;
			switch(method) {
				case "HEAD":
					request = new HttpHead(uriBuilder.build());
					break;
				case "PUT":
					request = new HttpPut(uriBuilder.build());
					break;
				case "POST":
					request = new HttpPost(uriBuilder.build());
					break;
				case "GET":
					request = new HttpGet(uriBuilder.build());
					break;
				case "DELETE":
					request = new HttpDelete(uriBuilder.build());
					break;
				default:
					throw new Exception("method was not specified");
			}
			if(username != null && username.length() > 0 && password != null && password.length() > 0) {
				String creds = username + ":" + password;
				String credHeader = "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());
				request.addHeader("Authorization",credHeader);
			}

			//if bearer token
			if(opts.apiToken != null) {
				int newLine = opts.apiToken.indexOf('\n');
				if(newLine > -1)
					opts.apiToken = opts.apiToken.substring(0, newLine);
				request.addHeader("Authorization", "Bearer " + opts.apiToken);
			}
			//if its oauth signing
			if(opts.oauth != null) {
				OauthUtility.signOAuthRequestPlainText(request, opts.oauth.consumerKey, opts.oauth.consumerSecret, opts.oauth.apiKey, opts.oauth.apiSecret, opts);
			}

			// Headers
			if(opts.headers == null || opts.headers.isEmpty() || !opts.headers.containsKey("Content-Type")) {
				request.addHeader("Content-Type", "application/json");
			}

			if(opts.headers != null && !opts.headers.isEmpty()) {
				for (String headerKey : opts.headers.keySet()) {
					request.addHeader(headerKey, opts.headers.get(headerKey));
				}
			}


			if (opts.body != null) {
				HttpEntityEnclosingRequestBase postRequest = (HttpEntityEnclosingRequestBase)request;
				postRequest.setEntity(new StringEntity(JsonOutput.toJson(opts.body)));
			}

			withClient(opts,(HttpClient client) -> {

				CloseableHttpResponse response = null;
				try {
					response = (CloseableHttpResponse)client.execute(request);
					if(response.getStatusLine().getStatusCode() <= 399) {
						for(Header header : response.getAllHeaders()) {
							rtn.addHeader(header.getName(), header.getValue());
						}


						for(Header header : response.getHeaders("Set-Cookie")) {
							Map<String,String> cookies = extractCookie(header.getValue());
							for(String cookieKey : cookies.keySet()) {
								rtn.addCookie(cookieKey, cookies.get(cookieKey));
							}
						}


						HttpEntity entity = response.getEntity();
						if(entity != null) {
							rtn.setContent(EntityUtils.toString(entity));
							if(!opts.suppressLog) {
								log.debug("results of SUCCESSFUL call to {}/{}, results: {}",url,path,rtn.getContent());
							}
						} else {
							rtn.setContent(null);
						}
						data.put("httpClient",client);
						rtn.setSuccess(true);
					} else {
						if(response.getEntity() != null) {
							rtn.setContent(EntityUtils.toString(response.getEntity()));
						}
						rtn.setSuccess(false);
						rtn.setErrorCode(Integer.toString(response.getStatusLine().getStatusCode()));
						log.warn("path: {} error: {} - {}",path,rtn.getErrorCode(),rtn.getContent());
					}
				} catch(Exception ex) {
					log.error("Error occurred processing the response for {}/{} : {}",url,path,ex.getMessage(), ex);
					rtn.setError("Error occurred processing the response for " + url + "/" + path + " : " + ex.getMessage());
					rtn.setSuccess(false);
				} finally {
					if(response != null) {
						try {
							response.close();
						} catch (IOException ignored) {
							//ignored exception
						}
					}
				}
			});

		} catch(SSLProtocolException sslEx) {
			log.error("Error Occurred calling API (SSL Exception): {}",sslEx.getMessage(),sslEx);
			rtn.addError("sslHandshake",  "SSL Handshake Exception (is SNI Misconfigured): ${sslEx.message}");
			rtn.setSuccess(false);
		} catch (Exception e) {
			log.error("Error Occurred calling API: ${e.message}",e);
			rtn.addError("error", e.getMessage());
			rtn.setSuccess(false);
		}
		return rtn;
	}


	static ServiceResponse callJsonApi(String url, String path) throws URISyntaxException, Exception {
		return callJsonApi(url, path, null, null, new RestOptions(), "POST");
	}

	static ServiceResponse callJsonApi(String url, String path, RestOptions opts) throws URISyntaxException, Exception {
		return callJsonApi(url, path, null, null, opts, "POST");
	}

	static ServiceResponse callJsonApi(String url, String path, RestOptions opts, String method) throws URISyntaxException, Exception {
		return callJsonApi(url, path, null, null, opts, method);
	}

	static ServiceResponse callJsonApi(String url, String path, String username, String password, RestOptions opts) throws URISyntaxException, Exception {
		return callJsonApi(url,path,username,password,opts,"POST");
	}

	static ServiceResponse callJsonApi(String url, String path, String username, String password, RestOptions opts, String method) throws URISyntaxException, Exception {
		//encode the body
		Object body = opts != null ? (opts.body) : null;
		String bodyType = opts != null ? opts.contentType : null;

		if(body != null && !bodyType.equals("form") && !bodyType.equals("multi-part-form") && !(body instanceof String) ) {
			opts.body = JsonOutput.toJson(opts.body);
		}

		//execute
		ServiceResponse rtn = callApi(url, path, username, password, opts, method);
		rtn.setData(new LinkedHashMap());

		if(rtn.getContent() != null && rtn.getContent().length() > 0) {
			try {
				//need a java based JSON Slurper hmmmm
				rtn.setData(new JsonSlurper().parseText(rtn.getContent()));
			} catch(Exception e) {
				log.debug("Error parsing API response JSON: ${e}", e);
			}
		}
		return rtn;
	}

	static Map<String,String> addRequiredHeader(Map<String,String> headers, String name, String value) {
		if(headers == null) {
			headers = new LinkedHashMap<>();
		}
		headers.putIfAbsent(name,value);
		return headers;
	}

	static Map<String, String> extractCookie(String rawCookie) {
		if(rawCookie == null || rawCookie.length() == 0) return null;
		String[] cookieArgs = rawCookie.split("=");
		String name = cookieArgs[0];
		String data = rawCookie.split(name + "=")[1].split(";")[0];
		String value = "";
		if(data != null && data.length() > 0) {
			value = data.substring(1,data.length() - 1);
		}
		Map<String,String> cookieMap = new LinkedHashMap<>();
		cookieMap.put(name,value);
		return cookieMap;
	}

	private static void withClient(RestOptions opts, WithClientFunction withClientFunction) {
		Boolean ignoreSSL = opts.ignoreSSL;

		HttpClientBuilder clientBuilder = HttpClients.custom();
		clientBuilder.setHostnameVerifier(new X509HostnameVerifier() {
			public boolean verify(String host, SSLSession sess) {
				return true;
			}

			public void verify(String host, SSLSocket ssl) {

			}

			public void verify(String host, String[] cns, String[] subjectAlts) {

			}

			public void verify(String host, X509Certificate cert) {

			}

		});
		SSLConnectionSocketFactory sslConnectionFactory;
		SSLContext sslcontext = null;
		if(ignoreSSL) {
			try {
				sslcontext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
					@Override
					public boolean isTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
						return true;
					}
				}).build();
			} catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException ignored) {
				//;
			}
			sslConnectionFactory = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER) {

				@Override
				protected void prepareSocket(SSLSocket socket) {
					try {
						PropertyUtils.setProperty(socket, "host", null);
					} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
						e.printStackTrace();
					}
					List<SNIServerName> serverNames  = Collections.<SNIServerName> emptyList();
						SSLParameters sslParams = socket.getSSLParameters();
						sslParams.setServerNames(serverNames);
						socket.setSSLParameters(sslParams);
				}
				@Override
				public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException, ConnectTimeoutException {
					if(socket instanceof SSLSocket) {
						try {
							String[] enabledProtocols = {"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"};
							SSLSocket sslSocket = (SSLSocket)socket;
							sslSocket.setEnabledProtocols(enabledProtocols);
							PropertyUtils.setProperty(socket, "host", host.getHostName());
						} catch (Exception ex) {
							log.error("We have an unhandled exception when attempting to connect to {} ignoring SSL errors",host, ex);
						}
					}
					return super.connectSocket(WEB_CONNECTION_TIMEOUT, socket, host, remoteAddress, localAddress, context);
				}
			};
		} else {
			sslcontext = SSLContexts.createSystemDefault();
			sslConnectionFactory = new SSLConnectionSocketFactory(sslcontext) {
				@Override
				public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException, ConnectTimeoutException {
					if(socket instanceof SSLSocket) {
						try {
							String[] enabledProtocols = {"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"};
							SSLSocket sslSocket = (SSLSocket)socket;
							sslSocket.setEnabledProtocols(enabledProtocols);
							PropertyUtils.setProperty(socket, "host", host.getHostName());
						} catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
							//;
						}
					}

					return super.connectSocket(opts.timeout != null ? opts.timeout : 30000, socket, host, remoteAddress, localAddress, context);
				}
			};
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
						ibuffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints != null ? constraints : MessageConstraints.DEFAULT) {
					@Override
					protected boolean reject(final CharArrayBuffer line, int count) {
						//We need to break out of forever head reads
						if(count > 100) {
							return true;
						}
						return false;

					}

				};
			}
		};

		clientBuilder.setSSLSocketFactory(sslConnectionFactory);
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("https", sslConnectionFactory)
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.build();

		HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

		HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
				requestWriterFactory, responseParserFactory);
		BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(registry, connFactory);
		clientBuilder.setConnectionManager(connectionManager);
		HttpClient client = clientBuilder.build();
		try {
			withClientFunction.method(client);
		} finally {
			connectionManager.shutdown();
		}
	}

	@FunctionalInterface
	public static interface WithClientFunction {
		void method(HttpClient client);
	}

	public static class RestOptions {
		Object body;
		String contentType; //bodyType originally
		Map<String,String> headers;
		Map<String,String> queryParams;
		Boolean suppressLog = true;
		Boolean keepAlive=false;
		Boolean ignoreSSL=true;
		Integer timeout = 30000;
		OauthOptions oauth;
		String apiToken;
		HttpClient httpClient; //optional pass the client

		public static class OauthOptions {
			String version;
			String consumerKey;
			String consumerSecret;
			String apiKey;
			String apiSecret;
		}
	}
}
