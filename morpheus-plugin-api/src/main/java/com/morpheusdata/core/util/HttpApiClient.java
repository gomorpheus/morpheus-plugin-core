/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.model.NetworkProxy;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.DefaultHttpResponseParser;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
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
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Utility methods for calling external APIs in a standardized way.
 *
 * @author David Estes
 * @since 0.8.0
 */
public class HttpApiClient {

	/**
	 * The HTTP client used to make requests.
	 */
	HttpClient httpClient;

	/**
	 * The connection manager for the HTTP client.
	 */
	HttpClientConnectionManager connectionManager;

	/**
	 * The cookie store for the HTTP client.
	 */
	BasicCookieStore cookieStore = new BasicCookieStore();

	/**
	 * Sets a throttle rate (in milliseconds) between HTTP Calls. This is used to slow down queries to the remote server.
	 */
	public Long throttleRate = 0L;

	/**
	 * The network proxy to use for requests.
	 */
	public NetworkProxy networkProxy;

	/**
	 * The last time a call was made to the API. Used for throttling.
	 */
	private Date lastCallTime;

	/**
	 * The logger for this class.
	 */
	static protected Logger log = LoggerFactory.getLogger(HttpApiClient.class);

	/**
	 * The default connection timeout for the HTTP client.
	 */
	static final Integer WEB_CONNECTION_TIMEOUT = 120 * 1000;

	/**
	 * Make a POST request to an API with the specified URL, path, and credentials. This method is intended for simple POST requests.
	 * @param url The base URL of the server to which the request is sent. This should include the protocol (e.g., "https://").
	 * @param path The specific path or endpoint on the server to which the request is made. This path is appended to the base URL.
	 * @param username The username used for basic authentication with the server. This can be null if authentication is not required.
	 * @param password The password used for basic authentication with the server. This should match the provided username. Can be null if authentication is not required.
	 * @return A {@link ServiceResponse} containing the response from the server. This response includes the status code, headers, and content.
	 * @throws URISyntaxException If the provided URL or path is invalid or cannot be correctly parsed into a URI.
	 * @throws Exception If an error occurs during the execution of the request, such as an I/O error, authentication failure, or invalid response.
	 */
	public ServiceResponse callApi(String url, String path, String username, String password) throws URISyntaxException, Exception {
		return callApi(url, path, username, password, new RequestOptions(), "POST");
	}

	/**
	 * Make a POST request to an API with the specified URL, path, credentials, and {@link RequestOptions}.
	 * @param url The base URL of the server to which the request is sent. This should include the protocol (e.g., "https://"). Any path or query params
	 *            included in the URL will be maintained when appending the path and query params supplied in other arguments in this method.
	 * @param path The specific path or endpoint on the server to which the request is made. This path is appended to the base URL.
	 * @param username The username used for basic authentication with the server. This can be null if authentication is not required.
	 * @param password The password used for basic authentication with the server. This should match the provided username. Can be null if authentication is not required.
	 * @param opts The {@link RequestOptions} object containing various options for the request, such as headers, query parameters, and the request body.
	 * @return A {@link ServiceResponse} containing the response from the server. This response includes the status code, headers, and content.
	 * @throws URISyntaxException If the provided URL or path is invalid or cannot be correctly parsed into a URI.
	 * @throws Exception If an error occurs during the execution of the request, such as an I/O error, authentication failure, or invalid response.
	 */
	public ServiceResponse callApi(String url, String path, String username, String password, RequestOptions opts) throws URISyntaxException, Exception {
		return callApi(url, path, username, password, opts, "POST");
	}

	/**
	 * Pause the request if a sleep period is required to enforce a throttle rate between API calls.
	 */
	private void sleepIfNecessary() {
		try {
			Long tmpThrottleRate = throttleRate;
			if (tmpThrottleRate != null && tmpThrottleRate > 0) {
				if (lastCallTime != null) {
					Date now = new Date();
					Long timeDiff = now.getTime() - lastCallTime.getTime();
					tmpThrottleRate = tmpThrottleRate - timeDiff;
					if (tmpThrottleRate > 0) {
						Thread.sleep(tmpThrottleRate);
					}
				} else {
					Thread.sleep(tmpThrottleRate);
				}

			}
		} catch (InterruptedException ignore) {

		}

	}

	/**
	 * Make a request to an API with the specified URL, path, credentials, {@link RequestOptions}, and HTTP request method.
	 * @param url The base URL of the server to which the request is sent. This should include the protocol (e.g., "https://"). Any path or query params
	 *            included in the URL will be maintained when appending the path and query params supplied in other arguments in this method.
	 * @param path The specific path or endpoint on the server to which the request is made. This path is appended to the base URL.
	 * @param username The username used for basic authentication with the server. This can be null if authentication is not required.
	 * @param password The password used for basic authentication with the server. This should match the provided username. Can be null if authentication is not required.
	 * @param opts The {@link RequestOptions} object containing various options for the request, such as headers, query parameters, and the request body. The Request body
	 *             can be one of {@link Map}, {@link String}, byte[], or {@link InputStream}.
	 * @param method The HTTP method to be used for the request, one of "HEAD", "GET", "POST", "PUT", "PATCH", or "DELETE".
	 * @return A {@link ServiceResponse} containing the response from the server. This response includes the status code, headers, and content.
	 * @throws URISyntaxException If the provided URL or path is invalid or cannot be correctly parsed into a URI.
	 * @throws Exception If an error occurs during the execution of the request, such as an I/O error, authentication failure, or invalid response.
	 */
	public ServiceResponse callApi(String url, final String path, String username, String password, RequestOptions opts, String method) throws URISyntaxException, Exception {
		log.debug("Calling Api: {} - {}", url, path);
		ServiceResponse rtn = new ServiceResponse();
		LinkedHashMap<String, Object> data = new LinkedHashMap<>();
		rtn.setData(data);
		URIBuilder uriBuilder = new URIBuilder(url);
		try {

			sleepIfNecessary();
			lastCallTime = new Date();

			String existingPath = uriBuilder.getPath();
			// retain path on base url if one exists
			String newPath = path;
			if (path != null && path.length() > 0) {
				if (existingPath != null && existingPath.length() > 0 && !path.startsWith(existingPath)) {
					if (existingPath.endsWith("/") && path.startsWith("/")) {
						existingPath = existingPath.substring(0, existingPath.length() - 1);
					} else if (!existingPath.endsWith("/") && !path.startsWith("/")) {
						existingPath += "/";
					}
					newPath = existingPath + path;
				}
				uriBuilder.setPath(newPath);
			}
			if (opts.queryParams != null && !opts.queryParams.isEmpty()) {
				for (CharSequence queryKey : opts.queryParams.keySet()) {
					uriBuilder.addParameter(queryKey.toString(), opts.queryParams.get(queryKey).toString());
				}
			}

			HttpRequestBase request;
			switch (method) {
				case "HEAD":
					request = new HttpHead(uriBuilder.build());
					break;
				case "PUT":
					request = new HttpPut(uriBuilder.build());
					break;
				case "POST":
					request = new HttpPost(uriBuilder.build());
					break;
				case "PATCH":
					request = new HttpPatch(uriBuilder.build());
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
			if (username != null && username.length() > 0 && password != null && password.length() > 0) {
				String creds = username + ":" + password;
				String credHeader = "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());
				request.addHeader("Authorization", credHeader);
			}

			//if bearer token
			if (opts.apiToken != null) {
				int newLine = opts.apiToken.indexOf('\n');
				if (newLine > -1)
					opts.apiToken = opts.apiToken.substring(0, newLine);
				request.addHeader("Authorization", "Bearer " + opts.apiToken);
			}
			//if its oauth signing
			if (opts.oauth != null) {
				OauthUtility.signOAuthRequestPlainText(request, opts.oauth.consumerKey, opts.oauth.consumerSecret, opts.oauth.apiKey, opts.oauth.apiSecret, opts);
			}

			// Headers
			if (opts.headers == null || opts.headers.isEmpty() || !opts.headers.containsKey("Content-Type")) {
				request.addHeader("Content-Type", "application/json");
			}

			if (opts.headers != null && !opts.headers.isEmpty()) {
				for (CharSequence headerKey : opts.headers.keySet()) {
					String headerValue = opts.headers.get(headerKey) != null ? opts.headers.get(headerKey).toString() : "";
					request.addHeader(headerKey.toString(), headerValue);
				}
			}

			if (opts.body != null) {
				HttpEntityEnclosingRequestBase postRequest = (HttpEntityEnclosingRequestBase) request;
				if (opts.body instanceof Map) {
					if (opts.contentType == "form") {
						List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
						Map<String, Object> bodyMap = (Map<String, Object>) opts.body;
						for (String key : bodyMap.keySet()) {
							Object v = bodyMap.get(key);
							Object rowValue;
							if (v instanceof CharSequence) {
								rowValue = v;
							} else {
								rowValue = v.toString();
							}
							urlParameters.add(new BasicNameValuePair(key, rowValue.toString()));
						}
						postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
					} else if (opts.contentType == "multi-part-form") {
						MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
						String rowBoundary = "--" + java.util.UUID.randomUUID().toString() + "--";
						Map<String, Object> bodyMap = (Map<String, Object>) opts.body;
						for (String key : bodyMap.keySet()) {
							Object v = bodyMap.get(key);
							//if multiples..
							if (v instanceof Collection) {
								for (String rowValue : (Collection<String>) v) {
									StringBody rowBody = new StringBody(rowValue.toString(), ContentType.create("text/plain", "UTF-8"));
									entityBuilder.addPart(key, rowBody);
								}
							} else {
								Object rowValue;
								//convert it
								if (v instanceof CharSequence) {
									rowValue = v;
								} else {
									rowValue = v.toString();
								}
								StringBody rowBody = new StringBody(rowValue.toString(), ContentType.create("text/plain", "UTF-8"));
								entityBuilder.addPart(key, rowBody);
							}
						}
						entityBuilder.setContentType(ContentType.MULTIPART_FORM_DATA);
						entityBuilder.setBoundary(rowBoundary);
						postRequest.setEntity(entityBuilder.build());
						//replace the header
						if (request.containsHeader("Content-Type")) {
							//append the boundary
							Header currentType = request.getFirstHeader("Content-Type");
							String newValue = currentType.getValue();
							newValue = newValue + "; boundary=" + rowBoundary;
							request.setHeader("Content-Type", newValue);
						}
					} else {
						ObjectMapper mapper = new ObjectMapper();
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
						mapper.setDateFormat(df);
						mapper.registerModule(new SimpleModule().addSerializer(CharSequence.class, new GStringJsonSerializer()));
						postRequest.setEntity(new StringEntity(mapper.writeValueAsString(opts.body)));
					}
				} else if (opts.body instanceof byte[]) {
					postRequest.setEntity(new ByteArrayEntity((byte[]) opts.body));
				} else if (opts.body instanceof InputStream) {
					postRequest.setEntity(new InputStreamEntity((InputStream) (opts.body), opts.contentLength != null ? opts.contentLength : -1));
				} else {
					postRequest.setEntity(new StringEntity(opts.body.toString()));
				}
			}

			opts.targetUri = uriBuilder.build();
			withClient(opts, (HttpClient client, BasicCookieStore cookieStore) -> {
				CloseableHttpResponse response = null;
				try {

					response = (CloseableHttpResponse) client.execute(request);
					if (response.getStatusLine().getStatusCode() <= 399) {
						for (Header header : response.getAllHeaders()) {
							rtn.addHeader(header.getName(), header.getValue());
						}


						for (Header header : response.getHeaders("Set-Cookie")) {
							Map<String, String> cookies = extractCookie(header.getValue());
							for (String cookieKey : cookies.keySet()) {
								BasicClientCookie cookie = new BasicClientCookie(cookieKey, cookies.get(cookieKey));
								cookie.setPath("/");
								cookie.setDomain(request.getURI().getHost());
								cookieStore.addCookie(cookie);
							}
						}


						HttpEntity entity = response.getEntity();

						if (entity != null) {
							rtn.setContent(EntityUtils.toString(entity));
							if (!opts.suppressLog) {
								log.debug("results of SUCCESSFUL call to {}/{}, results: {}", url, path, rtn.getContent());
							}
						} else {
							rtn.setContent(null);
						}


						rtn.setSuccess(true);
						rtn.setStatusCode(Integer.toString(response.getStatusLine().getStatusCode()));
					} else {
						if (response.getEntity() != null) {
							rtn.setContent(EntityUtils.toString(response.getEntity()));
						}
						rtn.setSuccess(false);
						rtn.setErrorCode(Integer.toString(response.getStatusLine().getStatusCode()));
						log.warn("path: {} error: {} - {}", path, rtn.getErrorCode(), rtn.getContent());
					}
				} catch (Exception ex) {
					try {
						log.error("Error occurred processing the response for {} : {}{}", uriBuilder.build().toString(), ex.getMessage(), ex);
						rtn.setError("Error occurred processing the response for " + uriBuilder.build().toString() + " : " + ex.getMessage());
					} catch (URISyntaxException uie) {
						log.error("Error occurred processing the response for {} : {}", "invalid uri", ex.getMessage(), ex);
						rtn.setError("Error occurred processing the response for invalid uri  : " + ex.getMessage());

					}

					rtn.setSuccess(false);
				} finally {
					lastCallTime = new Date();
					if (response != null) {
						try {
							response.close();
						} catch (IOException ignored) {
							//ignored exception
						}
					}
				}
			});

		} catch (SSLProtocolException sslEx) {
			log.error("Error Occurred calling API (SSL Exception): {}", sslEx.getMessage(), sslEx);
			rtn.addError("sslHandshake", "SSL Handshake Exception (is SNI Misconfigured): " + sslEx.getMessage());
			rtn.setSuccess(false);
		} catch (Exception e) {
			log.error("Error Occurred calling API: " + e.getMessage(), e);
			rtn.addError("error", e.getMessage());
			rtn.setSuccess(false);
		}
		return rtn;
	}

	/**
	 * Executes an HTTP request with streaming capabilities to a specified URL and path. This method is intended for
	 * scenarios where large amounts of data need to be transferred, such as uploading or downloading files, without
	 * fully loading the data into memory. The method handles authentication and custom request options, and returns
	 * the raw HTTP response for further processing.
	 *
	 * @param url The base URL of the server to which the request is sent. This should include the protocol (e.g., "https://"). Any path or query params
	 *            included in the URL will be maintained when appending the path and query params supplied in other arguments in this method.
	 * @param path The specific path or endpoint on the server to which the request is made. This path is appended to the base URL.
	 * @param username The username used for basic authentication with the server. This can be null if authentication is not required.
	 * @param password The password used for basic authentication with the server. This should match the provided username. Can be null if authentication is not required.
	 * @param body The request body to be sent to the server.
	 * @param opts The {@link RequestOptions} object containing various options for the request, such as headers, query parameters.
	 * @param method The HTTP method to be used for the request, one of "HEAD", "GET", "POST", "PUT", "PATCH", or "DELETE".
	 * @return A {@link ServiceResponse} containing the {@link CloseableHttpResponse} from the server. This response
	 *         can be used to read the server's response, including status codes, headers, and content.
	 * @throws URISyntaxException If the provided URL or path is invalid or cannot be correctly parsed into a URI.
	 * @throws Exception If an error occurs during the execution of the request, such as an I/O error, authentication failure, or invalid response.
	 */
	public ServiceResponse<CloseableHttpResponse> callStreamApi(String url, final String path, String username, String password, String body, RequestOptions opts, String method) throws URISyntaxException, Exception {
		opts.body = body;
		return callStreamApi(url, path, username, password, opts, method);
	}

	/**
	 * Executes an HTTP request with streaming capabilities to a specified URL and path. This method is intended for
	 * scenarios where large amounts of data need to be transferred, such as uploading or downloading files, without
	 * fully loading the data into memory. The method handles authentication and custom request options, and returns
	 * the raw HTTP response for further processing.
	 *
	 * @param url The base URL of the server to which the request is sent. This should include the protocol (e.g., "https://"). Any path or query params
	 *            included in the URL will be maintained when appending the path and query params supplied in other arguments in this method.
	 * @param path The specific path or endpoint on the server to which the request is made. This path is appended to the base URL.
	 * @param username The username used for basic authentication with the server. This can be null if authentication is not required.
	 * @param password The password used for basic authentication with the server. This should match the provided username. Can be null if authentication is not required.
	 * @param body The request body to be sent to the server.
	 * @param opts The {@link RequestOptions} object containing various options for the request, such as headers, query parameters.
	 * @param method The HTTP method to be used for the request, one of "HEAD", "GET", "POST", "PUT", "PATCH", or "DELETE".
	 * @return A {@link ServiceResponse} containing the {@link CloseableHttpResponse} from the server. This response
	 *         can be used to read the server's response, including status codes, headers, and content.
	 * @throws URISyntaxException If the provided URL or path is invalid or cannot be correctly parsed into a URI.
	 * @throws Exception If an error occurs during the execution of the request, such as an I/O error, authentication failure, or invalid response.
	 */
	public ServiceResponse<CloseableHttpResponse> callStreamApi(String url, final String path, String username, String password, InputStream body, RequestOptions opts, String method) throws URISyntaxException, Exception {
		opts.body = body;
		return callStreamApi(url, path, username, password, opts, method);
	}

	/**
	 * Executes an HTTP request with streaming capabilities to a specified URL and path. This method is intended for
	 * scenarios where large amounts of data need to be transferred, such as uploading or downloading files, without
	 * fully loading the data into memory. The method handles authentication and custom request options, and returns
	 * the raw HTTP response for further processing.
	 *
	 * @param url The base URL of the server to which the request is sent. This should include the protocol (e.g., "https://"). Any path or query params
	 *            included in the URL will be maintained when appending the path and query params supplied in other arguments in this method.
	 * @param path The specific path or endpoint on the server to which the request is made. This path is appended to the base URL.
	 * @param username The username used for basic authentication with the server. This can be null if authentication is not required.
	 * @param password The password used for basic authentication with the server. This should match the provided username. Can be null if authentication is not required.
	 * @param body The request body to be sent to the server.
	 * @param opts The {@link RequestOptions} object containing various options for the request, such as headers, query parameters.
	 * @param method The HTTP method to be used for the request, one of "HEAD", "GET", "POST", "PUT", "PATCH", or "DELETE".
	 * @return A {@link ServiceResponse} containing the {@link CloseableHttpResponse} from the server. This response
	 *         can be used to read the server's response, including status codes, headers, and content.
	 * @throws URISyntaxException If the provided URL or path is invalid or cannot be correctly parsed into a URI.
	 * @throws Exception If an error occurs during the execution of the request, such as an I/O error, authentication failure, or invalid response.
	 */
	public ServiceResponse<CloseableHttpResponse> callStreamApi(String url, final String path, String username, String password, byte[] body, RequestOptions opts, String method) throws URISyntaxException, Exception {
		opts.body = body;
		return callStreamApi(url, path, username, password, opts, method);
	}

	/**
	 * Executes an HTTP request with streaming capabilities to a specified URL and path. This method is intended for
	 * scenarios where large amounts of data need to be transferred, such as uploading or downloading files, without
	 * fully loading the data into memory. The method handles authentication and custom request options, and returns
	 * the raw HTTP response for further processing.
	 *
	 * @param url The base URL of the server to which the request is sent. This should include the protocol (e.g., "https://"). Any path or query params
	 *            included in the URL will be maintained when appending the path and query params supplied in other arguments in this method.
	 * @param path The specific path or endpoint on the server to which the request is made. This path is appended to the base URL.
	 * @param username The username used for basic authentication with the server. This can be null if authentication is not required.
	 * @param password The password used for basic authentication with the server. This should match the provided username. Can be null if authentication is not required.
	 * @param opts The {@link RequestOptions} object containing various options for the request, such as headers, query parameters, and the request body. The Request body
	 *             can be one of {@link Map}, {@link String}, byte[], or {@link InputStream}.
	 * @param method The HTTP method to be used for the request, one of "HEAD", "GET", "POST", "PUT", "PATCH", or "DELETE".
	 * @return A {@link ServiceResponse} containing the {@link CloseableHttpResponse} from the server. This response
	 *         can be used to read the server's response, including status codes, headers, and content.
	 * @throws URISyntaxException If the provided URL or path is invalid or cannot be correctly parsed into a URI.
	 * @throws Exception If an error occurs during the execution of the request, such as an I/O error, authentication failure, or invalid response.
	 */
	public ServiceResponse<CloseableHttpResponse> callStreamApi(String url, final String path, String username, String password, RequestOptions opts, String method) throws URISyntaxException, Exception {
		ServiceResponse<CloseableHttpResponse> rtn = new ServiceResponse<>();
		URIBuilder uriBuilder = new URIBuilder(url);
		try {

			sleepIfNecessary();
			lastCallTime = new Date();

			String existingPath = uriBuilder.getPath();
			// retain path on base url if one exists`
			String newPath = path;
			if (path != null && path.length() > 0) {
				if (existingPath != null && existingPath.length() > 0 && !path.startsWith(existingPath)) {
					if (existingPath.endsWith("/") && path.startsWith("/")) {
						existingPath = existingPath.substring(0, existingPath.length() - 1);
					} else if (!existingPath.endsWith("/") && !path.startsWith("/")) {
						existingPath += "/";
					}
					newPath = existingPath + path;
				}
				uriBuilder.setPath(newPath);
			}
			if (opts.queryParams != null && !opts.queryParams.isEmpty()) {
				for (CharSequence queryKey : opts.queryParams.keySet()) {
					uriBuilder.addParameter(queryKey.toString(), opts.queryParams.get(queryKey).toString());
				}
			}

			HttpRequestBase request;
			switch (method) {
				case "HEAD":
					request = new HttpHead(uriBuilder.build());
					break;
				case "PUT":
					request = new HttpPut(uriBuilder.build());
					break;
				case "POST":
					request = new HttpPost(uriBuilder.build());
					break;
				case "PATCH":
					request = new HttpPatch(uriBuilder.build());
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
			if (username != null && username.length() > 0 && password != null && password.length() > 0) {
				String creds = username + ":" + password;
				String credHeader = "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());
				request.addHeader("Authorization", credHeader);
			}

			//if bearer token
			if (opts.apiToken != null) {
				int newLine = opts.apiToken.indexOf('\n');
				if (newLine > -1)
					opts.apiToken = opts.apiToken.substring(0, newLine);
				request.addHeader("Authorization", "Bearer " + opts.apiToken);
			}
			//if its oauth signing
			if (opts.oauth != null) {
				OauthUtility.signOAuthRequestPlainText(request, opts.oauth.consumerKey, opts.oauth.consumerSecret, opts.oauth.apiKey, opts.oauth.apiSecret, opts);
			}

			// Headers
			if (opts.headers == null || opts.headers.isEmpty() || !opts.headers.containsKey("Content-Type")) {
				request.addHeader("Content-Type", "application/json");
			}

			if (opts.headers != null && !opts.headers.isEmpty()) {
				for (CharSequence headerKey : opts.headers.keySet()) {
					request.addHeader(headerKey.toString(), opts.headers.get(headerKey).toString());
				}
			}

			if (opts.body != null) {
				HttpEntityEnclosingRequestBase postRequest = (HttpEntityEnclosingRequestBase) request;
				if (opts.body instanceof Map) {
					if (opts.contentType == "form") {
						List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
						Map<String, Object> bodyMap = (Map<String, Object>) opts.body;
						for (String key : bodyMap.keySet()) {
							Object v = bodyMap.get(key);
							Object rowValue;
							if (v instanceof CharSequence) {
								rowValue = v;
							} else {
								rowValue = v.toString();
							}
							urlParameters.add(new BasicNameValuePair(key, rowValue.toString()));
						}
						postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
					} else if (opts.contentType == "multi-part-form") {
						MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
						String rowBoundary = "--" + java.util.UUID.randomUUID().toString() + "--";
						Map<String, Object> bodyMap = (Map<String, Object>) opts.body;
						for (String key : bodyMap.keySet()) {
							Object v = bodyMap.get(key);
							//if multiples..
							if (v instanceof Collection) {
								for (String rowValue : (Collection<String>) v) {
									StringBody rowBody = new StringBody(rowValue.toString(), ContentType.create("text/plain", "UTF-8"));
									entityBuilder.addPart(key, rowBody);
								}
							} else {
								Object rowValue;
								//convert it
								if (v instanceof CharSequence) {
									rowValue = v;
								} else {
									rowValue = v.toString();
								}
								StringBody rowBody = new StringBody(rowValue.toString(), ContentType.create("text/plain", "UTF-8"));
								entityBuilder.addPart(key, rowBody);
							}
						}
						entityBuilder.setContentType(ContentType.MULTIPART_FORM_DATA);
						entityBuilder.setBoundary(rowBoundary);
						postRequest.setEntity(entityBuilder.build());
						//replace the header
						if (request.containsHeader("Content-Type")) {
							//append the boundary
							Header currentType = request.getFirstHeader("Content-Type");
							String newValue = currentType.getValue();
							newValue = newValue + "; boundary=" + rowBoundary;
							request.setHeader("Content-Type", newValue);
						}
					} else {
						ObjectMapper mapper = new ObjectMapper();
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
						mapper.setDateFormat(df);
						postRequest.setEntity(new StringEntity(mapper.writeValueAsString(opts.body)));

					}
				} else if (opts.body instanceof byte[]) {
					postRequest.setEntity(new ByteArrayEntity((byte[]) opts.body));
				} else if (opts.body instanceof InputStream) {
					postRequest.setEntity(new InputStreamEntity((InputStream) (opts.body), opts.contentLength != null ? opts.contentLength : -1));
				} else {
					postRequest.setEntity(new StringEntity(opts.body.toString()));
				}
			}

			withClient(opts, (HttpClient client, BasicCookieStore cookieStore) -> {

				CloseableHttpResponse response = null;
				try {

					response = (CloseableHttpResponse) client.execute(request);
					if (response.getStatusLine().getStatusCode() <= 399) {
						for (Header header : response.getAllHeaders()) {
							rtn.addHeader(header.getName(), header.getValue());
						}


						for (Header header : response.getHeaders("Set-Cookie")) {
							Map<String, String> cookies = extractCookie(header.getValue());
							for (String cookieKey : cookies.keySet()) {
								BasicClientCookie cookie = new BasicClientCookie(cookieKey, cookies.get(cookieKey));
								cookie.setPath("/");
								cookie.setDomain(request.getURI().getHost());
								cookieStore.addCookie(cookie);
							}
						}


						HttpEntity entity = response.getEntity();

						if (entity != null) {
							rtn.setData(response);
							if (!opts.suppressLog) {
								log.debug("results of SUCCESSFUL call to {}/{}, results: {}", url, path, rtn.getContent());
							}
						} else {
							rtn.setContent(null);
						}


						rtn.setSuccess(true);
					} else {
						if (response.getEntity() != null) {
							rtn.setData(response);
						}
						rtn.setSuccess(false);
						rtn.setErrorCode(Integer.toString(response.getStatusLine().getStatusCode()));
						log.warn("path: {} error: {} - {}", path, rtn.getErrorCode(), rtn.getContent());
					}
				} catch (Exception ex) {
					try {
						log.error("Error occurred processing the response for {} : {}{}", uriBuilder.build().toString(), ex.getMessage(), ex);
						rtn.setError("Error occurred processing the response for " + uriBuilder.build().toString() + " : " + ex.getMessage());
					} catch (URISyntaxException uie) {
						log.error("Error occurred processing the response for {} : {}", "invalid uri", ex.getMessage(), ex);
						rtn.setError("Error occurred processing the response for invalid uri  : " + ex.getMessage());

					}

					rtn.setSuccess(false);
				} finally {
					lastCallTime = new Date();

				}
			});

		} catch (SSLProtocolException sslEx) {
			log.error("Error Occurred calling API (SSL Exception): {}", sslEx.getMessage(), sslEx);
			rtn.addError("sslHandshake", "SSL Handshake Exception (is SNI Misconfigured): " + sslEx.getMessage());
			rtn.setSuccess(false);
		} catch (Exception e) {
			log.error("Error Occurred calling API: " + e.getMessage(), e);
			rtn.addError("error", e.getMessage());
			rtn.setSuccess(false);
		}
		return rtn;
	}


	/**
	 * Make a POST request to a JSON API with the specified URL and path.
	 * @param url The base URL of the server to which the request is sent. This should include the protocol (e.g., "https://").
	 * @param path The specific path or endpoint on the server to which the request is made. This path is appended to the base URL.
	 * @return A {@link ServiceResponse} containing the response from the server. This response includes the status code, headers, and content.
	 * @throws URISyntaxException If the provided URL or path is invalid or cannot be correctly parsed into a URI.
	 * @throws Exception If an error occurs during the execution of the request, such as an I/O error, authentication failure, or invalid response.
	 */
	public ServiceResponse callJsonApi(String url, String path) throws URISyntaxException, Exception {
		return callJsonApi(url, path, null, null, new RequestOptions(), "POST");
	}

	/**
	 * Make a POST request to a JSON API with the specified URL, path, and {@link RequestOptions}.
	 * @param url The base URL of the server to which the request is sent. This should include the protocol (e.g., "https://"). Any path or query params
	 * @param path The specific path or endpoint on the server to which the request is made. This path is appended to the base URL.
	 * @param opts The {@link RequestOptions} object containing various options for the request, such as headers, query parameters, and the request body.
	 * @return A {@link ServiceResponse} containing the response from the server. This response includes the status code, headers, and content.
	 * @throws URISyntaxException If the provided URL or path is invalid or cannot be correctly parsed into a URI.
	 * @throws Exception If an error occurs during the execution of the request, such as an I/O error, authentication failure, or invalid response.
	 */
	public ServiceResponse callJsonApi(String url, String path, RequestOptions opts) throws URISyntaxException, Exception {
		return callJsonApi(url, path, null, null, opts, "POST");
	}

	/**
	 * Make a request to a JSON API with the specified URL, path, {@link RequestOptions} and HTTP method.
	 * @param url The base URL of the server to which the request is sent. This should include the protocol (e.g., "https://"). Any path or query params
	 * @param path The specific path or endpoint on the server to which the request is made. This path is appended to the base URL.
	 * @param opts The {@link RequestOptions} object containing various options for the request, such as headers, query parameters, and the request body.
	 * @param method The HTTP method to be used for the request, one of "HEAD", "GET", "POST", "PUT", "PATCH", or "DELETE".
	 * @return A {@link ServiceResponse} containing the response from the server. This response includes the status code, headers, and content.
	 * @throws URISyntaxException If the provided URL or path is invalid or cannot be correctly parsed into a URI.
	 * @throws Exception If an error occurs during the execution of the request, such as an I/O error, authentication failure, or invalid response.
	 */
	public ServiceResponse callJsonApi(String url, String path, RequestOptions opts, String method) throws URISyntaxException, Exception {
		return callJsonApi(url, path, null, null, opts, method);
	}

	/**
	 * Make a POST request to a JSON API with the specified URL, path, username, password, and {@link RequestOptions}.
	 * @param url The base URL of the server to which the request is sent. This should include the protocol (e.g., "https://"). Any path or query params
	 * @param path The specific path or endpoint on the server to which the request is made. This path is appended to the base URL.
	 * @param username The username used for basic authentication with the server. This can be null if authentication is not required.
	 * @param password The password used for basic authentication with the server. This should match the provided username. Can be null if authentication is not required.
	 * @param opts The {@link RequestOptions} object containing various options for the request, such as headers, query parameters, and the request body.
	 * @return A {@link ServiceResponse} containing the response from the server. This response includes the status code, headers, and content.
	 * @throws URISyntaxException If the provided URL or path is invalid or cannot be correctly parsed into a URI.
	 * @throws Exception If an error occurs during the execution of the request, such as an I/O error, authentication failure, or invalid response.
	 */
	public ServiceResponse callJsonApi(String url, String path, String username, String password, RequestOptions opts) throws URISyntaxException, Exception {
		return callJsonApi(url, path, username, password, opts, "POST");
	}

	/**
	 * Make a request to a JSON API with the specified URL, path, username, password, {@link RequestOptions}, and HTTP method.
	 * @param url The base URL of the server to which the request is sent. This should include the protocol (e.g., "https://"). Any path or query params
	 * @param path The specific path or endpoint on the server to which the request is made. This path is appended to the base URL.
	 * @param username The username used for basic authentication with the server. This can be null if authentication is not required.
	 * @param password The password used for basic authentication with the server. This should match the provided username. Can be null if authentication is not required.
	 * @param opts The {@link RequestOptions} object containing various options for the request, such as headers, query parameters, and the request body.
	 * @param method The HTTP method to be used for the request, one of "HEAD", "GET", "POST", "PUT", "PATCH", or "DELETE".
	 * @return A {@link ServiceResponse} containing the response from the server. This response includes the status code, headers, and content.
	 * @throws URISyntaxException If the provided URL or path is invalid or cannot be correctly parsed into a URI.
	 * @throws Exception If an error occurs during the execution of the request, such as an I/O error, authentication failure, or invalid response.
	 */
	public ServiceResponse callJsonApi(String url, String path, String username, String password, RequestOptions opts, String method) throws URISyntaxException, Exception {
		//encode the body
		Object body = opts != null ? (opts.body) : null;
		String bodyType = opts != null ? opts.contentType : null;

		if (body != null && (bodyType == null || (!bodyType.equals("form") && !bodyType.equals("multi-part-form") && !bodyType.equals("application/octet-stream"))) && !(body instanceof String)) {
			ObjectMapper mapper = new ObjectMapper();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
			mapper.setDateFormat(df);
			mapper.registerModule(new SimpleModule().addSerializer(CharSequence.class, new GStringJsonSerializer()));
			opts.body = mapper.writeValueAsString(opts.body);
		}

		//execute
		com.morpheusdata.response.ServiceResponse rtn = callApi(url, path, username, password, opts, method);
		rtn.setData(new LinkedHashMap());

		if (rtn.getContent() != null && rtn.getContent().length() > 0) {
			try {
				//parse the json in jackson and remove groovy
				ObjectMapper mapper = new ObjectMapper();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
				mapper.setDateFormat(df);
				TypeReference<Map<String,Object>> typeRefMap = new TypeReference<Map<String,Object>>() {
				};
				TypeReference<List<Map<String,Object>>> typeRefList = new TypeReference<List<Map<String,Object>>>() {
				};
				try {
					rtn.setData(mapper.readValue(rtn.getContent(), typeRefMap));
				} catch(Exception e) {
					try {
						rtn.setData(mapper.readValue(rtn.getContent(), typeRefList));
					} catch(Exception e2) {
						log.debug("Error parsing API response JSON: ${e}", e);
					}
				}


				//rtn.setData(new JsonSlurper().parseText(rtn.getContent()));
			} catch (Exception e) {
				log.debug("Error parsing API response JSON: ${e}", e);
			}
		}
		return rtn;
	}

	@Deprecated(since="1.1.5")
	public ServiceResponse callXmlApi(String url, String path, RequestOptions opts) throws URISyntaxException, Exception {
		return callXmlApi(url, path, null, null, opts, "POST");
	}

	@Deprecated(since="1.1.5")
	public ServiceResponse callXmlApi(String url, String path, RequestOptions opts, String method) throws URISyntaxException, Exception {
		return callXmlApi(url, path, null, null, opts, method);
	}

	@Deprecated(since="1.1.5")
	public ServiceResponse callXmlApi(String url, String path, String username, String password, RequestOptions opts) throws URISyntaxException, Exception {
		return callXmlApi(url, path, username, password, opts, "POST");
	}


	@Deprecated(since="1.1.5")
	public ServiceResponse callXmlApi(String url, String path, String username, String password, RequestOptions opts, String method) throws URISyntaxException, Exception {
		//encode the body
		Object body = opts != null ? (opts.body) : null;
		String bodyType = opts != null ? opts.contentType : null;

		if (body != null) {
			if (body instanceof String) {
				opts.body = body.toString().getBytes("UTF-8");
			}
			//add xml content type if not set
			opts.headers = addRequiredHeader(opts.headers, "Content-Type", "application/xml");
		}
		//execute
		ServiceResponse rtn = callApi(url, path, username, password, opts, method);
		rtn.setData(new LinkedHashMap());
		if (rtn.getContent() != null && rtn.getContent().length() > 0) {
			try {
				Class<?> xmlSlurperClass = Class.forName("groovy.util.XmlSlurper");
				Object xmlSlurper = xmlSlurperClass.getDeclaredConstructor(boolean.class,boolean.class).newInstance(false, true);
				rtn.setData(xmlSlurperClass.getMethod("parseText", String.class).invoke(xmlSlurper, rtn.getContent()));
			} catch (Exception e) {
				log.debug("Error parsing API response XML: " + e.getMessage(), e);
			}
		}
		return rtn;
	}

	/**
	 * Add a required header to the headers map if it does not already exist.
	 * @param headers
	 * @param name
	 * @param value
	 * @return
	 */
	public Map<CharSequence, CharSequence> addRequiredHeader(Map<CharSequence, CharSequence> headers, String name, String value) {
		if (headers == null) {
			headers = new LinkedHashMap<>();
		}
		headers.putIfAbsent(name, value);
		return headers;
	}

	/**
	 * Extracts a cookie from a raw cookie string.
	 * @param rawCookie
	 * @return
	 */
	Map<String, String> extractCookie(String rawCookie) {
		if (rawCookie == null || rawCookie.length() == 0) return null;
		String[] cookieArgs = rawCookie.split("=");
		String name = cookieArgs[0];
		String data = rawCookie.split(name + "=")[1].split(";")[0];
		String value = "";
		if (data != null && data.length() > 0) {
			value = data.substring(1, data.length() - 1);
		}
		Map<String, String> cookieMap = new LinkedHashMap<>();
		cookieMap.put(name, value);
		return cookieMap;
	}

	private void withClient(RequestOptions opts, WithClientFunction withClientFunction) {

		Boolean ignoreSSL = opts.ignoreSSL;
		if (httpClient != null) {
			withClientFunction.method(httpClient, cookieStore);
			return;
		} else {
			HttpClientBuilder clientBuilder = HttpClients.custom();


			RequestConfig.Builder reqConfigBuilder = RequestConfig.custom();
			reqConfigBuilder.setCookieSpec(CookieSpecs.STANDARD);
			if (opts.connectionTimeout != null) {
				reqConfigBuilder.setConnectTimeout(opts.connectionTimeout);
				reqConfigBuilder.setConnectionRequestTimeout(opts.connectionTimeout);
			}
			if (opts.readTimeout != null) {
				reqConfigBuilder.setSocketTimeout(opts.readTimeout);
			}
			clientBuilder.setDefaultRequestConfig(reqConfigBuilder.build());


			clientBuilder.setDefaultCookieStore(cookieStore);
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
			if (ignoreSSL) {
				try {
					SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
					sslContextBuilder = sslContextBuilder.loadTrustMaterial(null, new TrustStrategy() {
						@Override
						public boolean isTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
							return true;
						}
					});
					if (opts.clientCertKeyStore != null) {
						try {
							sslcontext = sslContextBuilder.loadKeyMaterial(opts.clientCertKeyStore, "".toCharArray()).build();
						} catch (Exception e) {
							log.error("Error creating keystore {}", e.getMessage(), e);
						}
					} else {
						sslcontext = sslContextBuilder.build();
					}
				} catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException ignored) {
					//Are we sure this should be ignored?
				}
				sslConnectionFactory = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER) {

					@Override
					protected void prepareSocket(SSLSocket socket) {

						List<SNIServerName> serverNames = Collections.<SNIServerName>emptyList();
						SSLParameters sslParams = socket.getSSLParameters();
						sslParams.setServerNames(serverNames);
						socket.setSSLParameters(sslParams);
					}

					@Override
					public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException, ConnectTimeoutException {
						if (socket instanceof SSLSocket) {
							try {
								String[] enabledProtocols = {"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"};
								SSLSocket sslSocket = (SSLSocket) socket;
								sslSocket.setEnabledProtocols(enabledProtocols);
							} catch (Exception ex) {
								log.error("We have an unhandled exception when attempting to connect to {} ignoring SSL errors", host, ex);
							}
						}
						return super.connectSocket(opts.timeout != null ? opts.timeout : WEB_CONNECTION_TIMEOUT, socket, host, remoteAddress, localAddress, context);
					}
				};
			} else {
				sslcontext = SSLContexts.createSystemDefault();
				sslConnectionFactory = new SSLConnectionSocketFactory(sslcontext) {
					@Override
					public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException, ConnectTimeoutException {
						if (socket instanceof SSLSocket) {
								String[] enabledProtocols = {"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"};
								SSLSocket sslSocket = (SSLSocket) socket;
								sslSocket.setEnabledProtocols(enabledProtocols);
						}

						return super.connectSocket(opts.timeout != null ? opts.timeout : WEB_CONNECTION_TIMEOUT, socket, host, remoteAddress, localAddress, context);
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
							if (count > 100) {
								return true;
							}
							return false;

						}

					};
				}
			};

			clientBuilder.setSSLSocketFactory(sslConnectionFactory);
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslConnectionFactory)
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.build();

			HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

			HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
				requestWriterFactory, responseParserFactory);
			BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(registry, connFactory);
			clientBuilder.setConnectionManager(connectionManager);
			boolean noProxy = networkProxy != null && networkProxy.getNoProxy() != null && Arrays.stream(networkProxy.getNoProxy().split("[,|\\s]+")).anyMatch(it -> it.equalsIgnoreCase(opts.targetUri.getHost()));
			if (networkProxy != null && !noProxy) {
				String proxyHost = networkProxy.getProxyHost();
				Integer proxyPort = networkProxy.getProxyPort();
				if (proxyHost != null && proxyPort != null) {
					log.debug("proxy detected at {}:{}", proxyHost, proxyPort);
					String proxyUser = networkProxy.getProxyUser();
					String proxyPassword = networkProxy.getProxyPassword();
					String proxyWorkstation = networkProxy.getProxyWorkstation() != null ? networkProxy.getProxyWorkstation() : null;
					String proxyDomain = networkProxy.getProxyDomain() != null ? networkProxy.getProxyDomain() : null;
					clientBuilder.setProxy(new HttpHost(proxyHost, proxyPort));
					if (proxyUser != null) {
						CredentialsProvider credsProvider = new BasicCredentialsProvider();
						NTCredentials ntCreds = new NTCredentials(proxyUser, proxyPassword, proxyWorkstation, proxyDomain);
						credsProvider.setCredentials(new AuthScope(proxyHost, proxyPort), ntCreds);

						clientBuilder.setDefaultCredentialsProvider(credsProvider);
						clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
					}
				}
			}

			HttpClient client = clientBuilder.build();
			httpClient = client;
			this.connectionManager = connectionManager;

			withClientFunction.method(client, cookieStore);
		}

	}

	/**
	 * Shutdown the client connection manager
	 */
	public void shutdownClient() {
		if (connectionManager != null) {
			try {
				connectionManager.shutdown();
			} catch (Exception ex) {
				log.error("Error Shutting Down Keep-Alive {}", ex.getMessage(), ex);
			}
		}
	}

	@FunctionalInterface
	public static interface WithClientFunction {
		void method(HttpClient client, BasicCookieStore cookieStore);
	}

	/**
	 * Options for making an HTTP request.
	 */
	public static class RequestOptions {

		/**
		 * The body of the request.
		 */
		public Object body;

		/**
		 * The content type of the request body.
		 */
		public String contentType; //bodyType originally

		/**
		 * The headers to include in the request.
		 */
		public Map<CharSequence, CharSequence> headers;

		/**
		 * The query parameters to include in the request.
		 */
		public Map<CharSequence, CharSequence> queryParams;

		/**
		 * Suppress logging of the request and response.
		 */
		public Boolean suppressLog = true;

		/**
		 * Ignore SSL certificate errors.
		 */
		public Boolean ignoreSSL = true;

		/**
		 * The connection timeout in milliseconds. Used when ignoreSSL is false.
		 */
		public Integer timeout = 30000;

		/**
		 * The connection timeout in milliseconds.
		 */
		public Integer connectionTimeout = null;

		/**
		 * The read timeout in milliseconds.
		 */
		public Integer readTimeout = null;

		/**
		 * The content length of the request body.
		 */
		public Long contentLength = null;

		/**
		 * Oauth options for signing the request.
		 */
		public OauthOptions oauth;

		/**
		 * The API token to include in the request.
		 */
		public String apiToken;

		/**
		 * Override the client used for the request.
		 */
		public HttpClient httpClient;

		/**
		 * Override the connection manager used for the client.
		 */
		public HttpClientConnectionManager connectionManager;

		/**
		 * URI of the request.
		 */
		public URI targetUri;

		/**
		 * KeyStore for client certificate
		 */
		public KeyStore clientCertKeyStore;

		/**
		 * Oauth options for signing the request.
		 */
		public static class OauthOptions {

			/**
			 * Oauth Version
			 */
			public String version;

			/**
			 * Oauth Consumer Key
			 */
			public String consumerKey;

			/**
			 * Oauth Consumer Secret
			 */
			public String consumerSecret;

			/**
			 * Oauth Token
			 */
			public String apiKey;

			/**
			 * Oauth Token Secret
			 */
			public String apiSecret;
		}
	}
}

