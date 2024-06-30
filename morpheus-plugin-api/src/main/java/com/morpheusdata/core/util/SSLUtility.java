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

import javax.net.ssl.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

class SSLUtility  {

	private static HostnameVerifier oldHostnameVerifier;
	private static TrustManager[] oldTrustManagers;
	private static HostnameVerifier newHostnameVerifier;
	private static TrustManager[] newTrustManagers;

	static {
		java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	static void trustAllHostnames() {
		if (isDeprecatedSSLProtocol()) {
			oldTrustAllHostnames();
		} else {
			newTrustAllHostnames();
		}
	}

	static void trustAllHttpsCertificates() {
		if (isDeprecatedSSLProtocol()) {
			//println 'DEPPP'
			oldTrustAllHttpsCertificates(null);
		} else {
			//println 'NOT DEPPP'
			newTrustAllHttpsCertificates(null);
		}
	}

	private static boolean isDeprecatedSSLProtocol() {
		return "com.sun.net.ssl.internal.www.protocol" == System.getProperty("java.protocol.handler.pkgs");
	}

	private static void oldTrustAllHostnames() {
		if (oldHostnameVerifier == null) {
			oldHostnameVerifier = new OldFakeHostnameVerifier();
		}
		HttpsURLConnection.setDefaultHostnameVerifier(oldHostnameVerifier);
	}

	static class OldFakeHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	private static void oldTrustAllHttpsCertificates(KeyStore keyStore) {
		SSLContext context;
		if(true || oldTrustManagers == null) {
			oldTrustManagers = new TrustManager[]{new OldFakeX509TrustManager()};
		}
		try {
			context = SSLContext.getInstance("SSL");
			KeyManager[] keyManagers = null;
			if(keyStore != null) {
				KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				keyFactory.init(keyStore, "password".toCharArray());
				keyManagers = keyFactory.getKeyManagers();
			}
			context.init(keyManagers, oldTrustManagers, new SecureRandom());
		} catch (GeneralSecurityException gse) {
			throw new IllegalStateException(gse.getMessage());
		}
		HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
	}

	private static void newTrustAllHostnames() {
		if (true || newHostnameVerifier == null) {
			newHostnameVerifier = new FakeHostnameVerifier();
		}
		HttpsURLConnection.setDefaultHostnameVerifier(newHostnameVerifier);
	}

	private static void newTrustAllHttpsCertificates(KeyStore keyStore) {
		SSLContext context;
		if(true || newTrustManagers == null) {
			newTrustManagers = new TrustManager[]{ new FakeX509TrustManager() };
		}
		try {
			context = SSLContext.getInstance("SSL");
			KeyManager[] keyManagers = null;
			if(keyStore != null) {
				KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				keyFactory.init(keyStore, "password".toCharArray());
				keyManagers = keyFactory.getKeyManagers();
			}
			context.init(keyManagers, newTrustManagers, new SecureRandom());
		} catch (GeneralSecurityException gse) {
			throw new IllegalStateException(gse.getMessage());
		}
		HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
	}

	static class OldFakeX509TrustManager implements X509TrustManager {
		private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

		boolean isClientTrusted(X509Certificate[] chain) { return (true); }

		boolean isServerTrusted(X509Certificate[] chain) {
			//println 'PPPPPPPEEEEEEs'
			return (true);
		}

		public X509Certificate[] getAcceptedIssuers() {
			return (_AcceptedIssuers);
		}

		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	static class FakeHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	static class FakeX509TrustManager implements X509TrustManager {
		private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

		public void checkClientTrusted(X509Certificate[] chain, String authType) {}

		public void checkServerTrusted(X509Certificate[] chain, String authType) {
			//println 'POOOOOOOPs'
		}

		public X509Certificate[] getAcceptedIssuers() {
			//println 'POPOSDPOASP'
			return (_AcceptedIssuers);
		}
	}
}
