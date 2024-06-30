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

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpRequestBase;
import org.codehaus.groovy.runtime.NullObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Internal Utility Class for OAUTH Signatures within {@link RestApiUtil}. This should not be directly accessed.
 *
 * @since 0.8.0
 * @author Brian Wheeler, David Estes
 */
class OauthUtility {
	static Logger log = LoggerFactory.getLogger(OauthUtility.class);
	static void signOAuthRequestPlainText(HttpRequestBase request, String consumerKey, String consumerSecret, String userKey, String userSecret, RestApiUtil.RestOptions requestOpts) {

		//request info
		String requestMethod = request.getMethod();
		String requestScheme = request.getURI().getScheme();
		String requestHost = request.getURI().getHost();
		String requestPath = request.getURI().getPath();
		String requestUuid = encodeAsHex(java.util.UUID.randomUUID().toString());

		String requestTimestamp = Long.toString(System.currentTimeMillis() / 1000L);
		//build auth query params for signing
		LinkedHashMap<CharSequence,CharSequence> queryHeaders = new LinkedHashMap<>();
		if(requestOpts.queryParams != null) {
			for(String queryKey : requestOpts.queryParams.keySet()) {
				queryHeaders.put(queryKey,requestOpts.queryParams.get(queryKey));
			}
		}

		queryHeaders.put("oauth_consumer_key",consumerKey);
		queryHeaders.put("oauth_token",userKey);
		queryHeaders.put("oauth_signature_method","PLAINTEXT");
		//sort the query params
		String queryString = buildQueryString(queryHeaders);
		//signing input
		String signatureBase = encodeOauth(requestMethod) + "&" + encodeOauth(requestScheme + requestHost + requestPath) + "&" + encodeOauth(queryString);
		log.debug("signature base: {}",signatureBase);
		//signature
		String signatureKey = encodeOauth(consumerSecret) + "&" + encodeOauth(userSecret);
		log.debug("signature key: {}",signatureKey);
		//build oauth header
		ArrayList<String> oauthHeaders = new ArrayList<>();

		//realm -

		oauthHeaders.add("oauth_consumer_key=\"" + consumerKey + "\"");
		oauthHeaders.add("oauth_nonce=\"" + requestUuid + "\"");
		oauthHeaders.add("oauth_signature_method=\"PLAINTEXT\"");
		oauthHeaders.add("oauth_timestamp=\"" + requestTimestamp + "\"");
		oauthHeaders.add("oauth_token=\"" + userKey + "\"");
		oauthHeaders.add("oauth_signature=\"" + signatureKey + "\"");
		//add the auth header
		if(requestOpts.headers == null) {
			requestOpts.headers = new LinkedHashMap<>();
		}
		requestOpts.headers.put("Authorization","OAuth " + String.join(", ",oauthHeaders));
		//done
	}

	static void signOAuthRequestPlainText(HttpRequestBase request, String consumerKey, String consumerSecret, String userKey, String userSecret, HttpApiClient.RequestOptions requestOpts) {

		//request info
		String requestMethod = request.getMethod();
		String requestScheme = request.getURI().getScheme();
		String requestHost = request.getURI().getHost();
		String requestPath = request.getURI().getPath();
		String requestUuid = encodeAsHex(java.util.UUID.randomUUID().toString());

		String requestTimestamp = Long.toString(System.currentTimeMillis() / 1000L);
		//build auth query params for signing
		LinkedHashMap<CharSequence,CharSequence> queryHeaders = new LinkedHashMap<>();
		if(requestOpts.queryParams != null) {
			for(CharSequence queryKey : requestOpts.queryParams.keySet()) {
				queryHeaders.put(queryKey,requestOpts.queryParams.get(queryKey));
			}
		}

		queryHeaders.put("oauth_consumer_key",consumerKey);
		queryHeaders.put("oauth_token",userKey);
		queryHeaders.put("oauth_signature_method","PLAINTEXT");
		//sort the query params
		String queryString = buildQueryString(queryHeaders);
		//signing input
		String signatureBase = encodeOauth(requestMethod) + "&" + encodeOauth(requestScheme + requestHost + requestPath) + "&" + encodeOauth(queryString);
		log.debug("signature base: {}",signatureBase);
		//signature
		String signatureKey = encodeOauth(consumerSecret) + "&" + encodeOauth(userSecret);
		log.debug("signature key: {}",signatureKey);
		//build oauth header
		ArrayList<String> oauthHeaders = new ArrayList<>();

		//realm -

		oauthHeaders.add("oauth_consumer_key=\"" + consumerKey + "\"");
		oauthHeaders.add("oauth_nonce=\"" + requestUuid + "\"");
		oauthHeaders.add("oauth_signature_method=\"PLAINTEXT\"");
		oauthHeaders.add("oauth_timestamp=\"" + requestTimestamp + "\"");
		oauthHeaders.add("oauth_token=\"" + userKey + "\"");
		oauthHeaders.add("oauth_signature=\"" + signatureKey + "\"");
		//add the auth header
		if(requestOpts.headers == null) {
			requestOpts.headers = new LinkedHashMap<>();
		}
		requestOpts.headers.put("Authorization","OAuth " + String.join(", ",oauthHeaders));
		//done
	}


	static private String buildQueryString(Map<CharSequence,CharSequence> query) {
		Map<CharSequence, CharSequence> copy = new TreeMap<>(query); //sorts it
		ArrayList<CharSequence> items = new ArrayList<>();
		for (Map.Entry<CharSequence, CharSequence> entry : copy.entrySet()) {
			items.add(encodeOauth(entry.getKey()) + "=" + encodeOauth(entry.getValue()));
		}
		return String.join("&",items);
	}

	static private CharSequence encodeOauth(CharSequence str) {
		String rtn = "";
		if(str != null) {
			try {
				rtn = java.net.URLEncoder.encode(str.toString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error("Error encoding URL {}",e.getMessage(),e);
			}
			rtn = rtn.replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
		}
		return rtn;
	}


	// Expects an array/list of numbers
	static private String encodeAsHex(String theTarget) {
		try {
			//THIS IS THE COOLEST STACK OVERFLOW ANSWER I HAVE EVER FOUND https://stackoverflow.com/questions/923863/converting-a-string-to-hexadecimal-in-java
			return String.format("%x", new BigInteger(1, theTarget.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			//not likely
			log.error("Unsupported Encoding Exception Occurred",e);
			return null;
		}
	}
}
