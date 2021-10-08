package com.morpheusdata.cloud

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.cloudresourcemanager.CloudResourceManager
import com.google.api.services.cloudresourcemanager.model.ListProjectsResponse
import com.google.api.services.compute.*
import com.google.api.services.compute.model.*
import groovy.util.logging.Slf4j
import org.apache.commons.beanutils.PropertyUtils
import org.apache.http.*
import org.apache.http.client.HttpClient
import org.apache.http.client.config.*
import org.apache.http.config.*
import org.apache.http.conn.*
import org.apache.http.conn.socket.*
import org.apache.http.conn.ssl.*
import org.apache.http.impl.*
import org.apache.http.impl.client.*
import org.apache.http.impl.conn.*
import org.apache.http.impl.io.*
import org.apache.http.io.*
import org.apache.http.message.*
import org.apache.http.protocol.*
import org.apache.http.util.*

import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocket
import java.lang.reflect.InvocationTargetException
import java.security.cert.X509Certificate

@Slf4j
class GoogleApiService {

	static defaultTimeout = 60000

	static listProjects(Map apiConfig) {
		def rtn = [success:false, projects:[]]
		try {
			CloudResourceManager cloudResourceManager = getGoogleCloudResourceManager(apiConfig)
			ListProjectsResponse response = cloudResourceManager.projects().list().execute()
			rtn.projects = response.getProjects()?.findAll { it.getLifecycleState() == 'ACTIVE' }
			rtn.success = true
		} catch(GoogleJsonResponseException e2) {
			log.error("listProjects error: ${e2}", e2)
			if(e2.details?.errors) {
				rtn.error = e2.details.errors.collect { it.message }.flatten()?.toString()
			} else {
				rtn.error = e2.message
			}
		} catch(e) {
			log.error("listProjects error: ${e}", e)
		}
		return rtn
	}

	static listRegions(Map apiConfig, projectId) {
		def rtn = [success:false, networks:[]]
		try {
			Compute computeClient = getGoogleComputeClient(apiConfig)
			Compute.Regions.List regions = computeClient.regions().list(projectId)
			RegionList list = regions.execute()
			rtn.regions =  list.getItems()
			rtn.success = true
		} catch(e) {
			log.error("listRegions error: ${e}", e)
		}
		return rtn
	}

	private static Compute getGoogleComputeClient(apiConfig) {
		GoogleCredential credential = getGoogleCredentials(apiConfig, Collections.singleton(ComputeScopes.COMPUTE))
		def clientConfig = [:]
		HttpClient httpClient = createHttpClient(clientConfig)
		ApacheHttpTransport httpTransport = new ApacheHttpTransport(httpClient)
		JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance()

		Compute client =  new Compute.Builder(
				httpTransport, JSON_FACTORY, null).setApplicationName("Morpheus/1.0")
				.setHttpRequestInitializer(credential).build()

		log.debug("GCP Compute URL: ${client.baseUrl}")
		return client
	}

	private static CloudResourceManager getGoogleCloudResourceManager(apiConfig) {
		GoogleCredential credential = getGoogleCredentials(apiConfig, Collections.singleton(com.google.api.services.cloudresourcemanager.CloudResourceManagerScopes.CLOUD_PLATFORM))
		def clientConfig = [:]
		HttpClient httpClient = createHttpClient(clientConfig)
		ApacheHttpTransport httpTransport = new ApacheHttpTransport(httpClient)
		JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance()

		CloudResourceManager client =  new CloudResourceManager.Builder(
				httpTransport, JSON_FACTORY, null).setApplicationName("Morpheus/1.0")
				.setHttpRequestInitializer(credential).build()

		log.debug("GCP Compute URL: ${client.baseUrl}")
		return client
	}

	private static GoogleCredential getGoogleCredentials(apiConfig, scopes) {
		InputStream is = getJsonCredInputStream(apiConfig)
		GoogleCredential.fromStream(is).createScoped(scopes)
	}

	private static getJsonCredInputStream(apiConfig) {
		def projectId = apiConfig.projectId ?: ''
		def clientEmail = apiConfig.clientEmail
		def privateKey = apiConfig.privateKey?.replace('\r','\\r')?.replace('\n','\\n')


		String credentialsString = """
{
  "type": "service_account",
  "project_id": "${projectId}",
  "private_key_id": "",
  "private_key": "${privateKey}",
  "client_email": "${clientEmail}",
  "client_id": "",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "https://accounts.google.com/o/oauth2/token",
  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs"
}
"""
		InputStream is = new ByteArrayInputStream( credentialsString.getBytes())
	}

	static createHttpClient(config = [:]) {
		def clientBuilder = HttpClients.custom()
		def timeout = config.timeout ?: defaultTimeout
		clientBuilder.setHostnameVerifier(new X509HostnameVerifier() {
			boolean verify(String host, SSLSession sess) {
				return true
			}

			void verify(String host, SSLSocket ssl) {}

			void verify(String host, String[] cns, String[] subjectAlts) {}

			void verify(String host, X509Certificate cert) {}
		})
		SSLContext sslcontext = SSLContexts.createSystemDefault()
		//ignoreSSL(sslcontext)
		SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslcontext) {
			@Override
			Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException, ConnectTimeoutException {
				if(socket instanceof SSLSocket) {
					try {
						socket.setEnabledProtocols(['SSLv3', 'TLSv1', 'TLSv1.1', 'TLSv1.2'] as String[])
						PropertyUtils.setProperty(socket, "host", host.getHostName())
					} catch(NoSuchMethodException ex) {
					}
					catch(IllegalAccessException ex) {
					}
					catch(InvocationTargetException ex) {
					}
				}
				return super.connectSocket(timeout, socket, host, remoteAddress, localAddress, context)
			}
		}
		HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {
			@Override
			HttpMessageParser<HttpResponse> create(SessionInputBuffer ibuffer, MessageConstraints constraints) {
				LineParser lineParser = new BasicLineParser() {
					@Override
					Header parseHeader(final CharArrayBuffer buffer) {
						try {
							return super.parseHeader(buffer)
						} catch (ParseException ex) {
							return new BasicHeader(buffer.toString(), null)
						}
					}
				}
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
		def registry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("https", sslConnectionFactory)
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.build()
		def requestWriterFactory = new DefaultHttpRequestWriterFactory()
		def connFactory = new ManagedHttpClientConnectionFactory(
				requestWriterFactory, responseParserFactory)
		def connectionManager = new BasicHttpClientConnectionManager(registry, connFactory)
		clientBuilder.setConnectionManager(connectionManager)
		def requestConfig = RequestConfig.custom().setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout)
				.setSocketTimeout(timeout).build()
		clientBuilder.setDefaultRequestConfig(requestConfig)
		HttpClient client = clientBuilder.build()
		return client
	}
}
