package com.morpheusdata.vmware.util

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*
import java.security.cert.*
import javax.net.ssl.*
import java.security.spec.*
import java.security.interfaces.*

class SSLUtility  {

	private static HostnameVerifier oldHostnameVerifier
	private static TrustManager[] oldTrustManagers
	private static HostnameVerifier newHostnameVerifier
	private static TrustManager[] newTrustManagers

	static {
		Security.addProvider(new BouncyCastleProvider())
	}

	private static boolean isDeprecatedSSLProtocol() {
		return "com.sun.net.ssl.internal.www.protocol" == System.getProperty("java.protocol.handler.pkgs")
	}

	private static void oldTrustAllHostnames() {
		if (oldHostnameVerifier == null) {
			oldHostnameVerifier = new OldFakeHostnameVerifier()
		}
		HttpsURLConnection.setDefaultHostnameVerifier(oldHostnameVerifier)
	}

	private static void oldTrustAllHttpsCertificates(KeyStore keyStore) {
		SSLContext context
		if(true || oldTrustManagers == null) {
			oldTrustManagers = [new OldFakeX509TrustManager()] as TrustManager[]
		}
		try {
			context = SSLContext.getInstance("SSL")
			def keyManagers
			if(keyStore) {
				def keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
				keyFactory.init(keyStore, 'password'.toCharArray())
				keyManagers = keyFactory.getKeyManagers()
			}
			context.init(keyManagers, oldTrustManagers, new SecureRandom())
		} catch (GeneralSecurityException gse) {
			throw new IllegalStateException(gse.getMessage())
		}
		HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory())
	}

	private static void newTrustAllHostnames() {
		if (true || newHostnameVerifier == null) {
			newHostnameVerifier = new FakeHostnameVerifier()
		}
		HttpsURLConnection.setDefaultHostnameVerifier(newHostnameVerifier)
	}

	private static void newTrustAllHttpsCertificates(KeyStore keyStore) {
		SSLContext context
		if(true || newTrustManagers == null) {
			newTrustManagers = [new FakeX509TrustManager()] as TrustManager[]
		}
		try {
			context = SSLContext.getInstance("SSL")
			def keyManagers
			if(keyStore) {
				def keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
				keyFactory.init(keyStore, 'password'.toCharArray())
				keyManagers = keyFactory.getKeyManagers()
			}
			context.init(keyManagers, newTrustManagers, new SecureRandom())
		} catch (GeneralSecurityException gse) {
			throw new IllegalStateException(gse.getMessage())
		}
		HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory())
	}

	static void trustSelfSignedSSL() {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS")
			X509TrustManager tm = new X509TrustManager() {

				void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}

				void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}

				X509Certificate[] getAcceptedIssuers() {
					return null
				}
			}
			TrustManager[] newTrustManagers = [tm]
			ctx.init(null, newTrustManagers	, null)
			SSLContext.setDefault(ctx)
		} catch (Exception ex) {
			ex.printStackTrace()
		}
	}

	static void trustAllHostnames() {
		if (isDeprecatedSSLProtocol()) {
			oldTrustAllHostnames()
		} else {
			newTrustAllHostnames()
		}
	}

	static void trustAllHttpsCertificates() {
		if (isDeprecatedSSLProtocol()) {
			//println 'DEPPP'
			oldTrustAllHttpsCertificates(null)
		} else {
			//println 'NOT DEPPP'
			newTrustAllHttpsCertificates(null)
		}
	}

	static void trustAllHttpsCertificatesWithCert() {
		def keystore = loadKeyStore()
		if (isDeprecatedSSLProtocol()) {
			oldTrustAllHttpsCertificates(keystore)
		} else {
			newTrustAllHttpsCertificates(keystore)
		}
	}

	static class OldFakeHostnameVerifier implements HostnameVerifier {
		boolean verify(String hostname, SSLSession session) {
			return (true)
		}
	}

	static class OldFakeX509TrustManager implements X509TrustManager {
		private static final X509Certificate[] _AcceptedIssuers = [] as X509Certificate[]

		boolean isClientTrusted(X509Certificate[] chain) { return (true) }

		boolean isServerTrusted(X509Certificate[] chain) {
			//println 'PPPPPPPEEEEEEs'
			return (true)
		}

		X509Certificate[] getAcceptedIssuers() { return (_AcceptedIssuers) }

		void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			throw new UnsupportedOperationException("Not supported yet.")
		}

		void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			throw new UnsupportedOperationException("Not supported yet.")
		}
	}

	static class FakeHostnameVerifier implements HostnameVerifier {
		boolean verify(String hostname, SSLSession session) { return (true) }
	}

	static class FakeX509TrustManager implements X509TrustManager {
		private static final X509Certificate[] _AcceptedIssuers = [] as X509Certificate[]

		void checkClientTrusted(X509Certificate[] chain, String authType) {}

		void checkServerTrusted(X509Certificate[] chain, String authType) {
			//println 'POOOOOOOPs'
		}

		X509Certificate[] getAcceptedIssuers() {
			//println 'POPOSDPOASP'
			return (_AcceptedIssuers)
		}
	}

	static loadKeyStore() {
		def certBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(extensionCert) //.decodeBase64()
		def keyBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(extensionKey) //.decodeBase64()
		def cert = generateCertificateFromDER(certBytes)
		def key = generatePrivateKeyFromDER(keyBytes)
		def keystore = KeyStore.getInstance("JKS")
		keystore.load(null)
		keystore.setCertificateEntry('com.morpheus.vmware.extension', cert)
		keystore.setKeyEntry('com.morpheus.vmware.extension', key, 'password'.toCharArray(), [cert] as java.security.cert.Certificate[])
		return keystore
	}

	protected static RSAPrivateKey generatePrivateKeyFromDER(byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
		def spec = new PKCS8EncodedKeySpec(keyBytes)
		KeyFactory factory = KeyFactory.getInstance("RSA")
		return (RSAPrivateKey)factory.generatePrivate(spec)
	}

	protected static X509Certificate generateCertificateFromDER(byte[] certBytes) throws CertificateException {
		def factory = CertificateFactory.getInstance("X.509")
		return (X509Certificate)factory.generateCertificate(new ByteArrayInputStream(certBytes))
	}

	static extensionCert = '''MIIDbjCCAlYCCQDwBZ8uJseP2jANBgkqhkiG9w0BAQsFADB5MQswCQYDVQQGEwJV
	UzETMBEGA1UECBMKQ2FsaWZvcm5pYTESMBAGA1UEBxMJU2FuIE1hdGVvMREwDwYD
			VQQKEwhNb3JwaGV1czERMA8GA1UECxMIbW9ycGhldXMxGzAZBgNVBAMTEnd3dy5n
	b21vcnBoZXVzLmNvbTAeFw0xNjAxMjAwNDQyMzlaFw0yNjAxMTcwNDQyMzlaMHkx
			CzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMRIwEAYDVQQHEwlTYW4g
	TWF0ZW8xETAPBgNVBAoTCE1vcnBoZXVzMREwDwYDVQQLEwhtb3JwaGV1czEbMBkG
			A1UEAxMSd3d3LmdvbW9ycGhldXMuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8A
	MIIBCgKCAQEA3TL1MCOLnd0CSVq2Ntqciyu2Z7G9ibtceYp/igq03iTt6ns/j2bO
	WrLBXOK1eSB0r8wtweEDGwQ7quoMsmK8RCZdAINzfnpe+Pt15ap7IJFGIxpHR7GW
9um+KOSP0GcD8PlevQdji3YLqUOorx6jwN/pZWaQy2bRvxqZCkTf5GT7lhnt3NY4
	L+56jkJrFgW7gJcxAAo0KIQcDG/KWQNxOJagUfo6D9BIcg6ITpJYv4MkNVZvJWNR
1dqnygcFy8/gc2ExJbxMuMzfW1ZCm3lft7mBXj4axIQ5aWZUvRu5T0osVYcIgx0F
	dmWCwGVn9eVs0/n43lGkagxwsg84bGMb+QIDAQABMA0GCSqGSIb3DQEBCwUAA4IB
	AQBrE3KUZAFdHTZto+4a6zHZlU2ceeUYV6z3+BnMmLLASY8VhIrRsAnUqL22nFTJ
			OnpQGnzsnP1gQYv2Q5HyodaEwv23XKFYkcArK1XVlJzVdbH2OWis378k06IFFsDd
	RdGqgFFd7xo+aQDwYVIeVmKZIaFiHHOL2NrlglmfEHrZSuCD/LluLT63UJALkkar
	UUia06/4TFwYYjdvVibsNKSNOEdd7+DwbxW8X1JB8n4rxz2130c/HnyoI/EdukcI
9zKXwVwCSPVzjkYADZOkTAFhIJHQa1njWiMa60dOv1p9/x+drww9n02bwQTPHTai
	sAhsSo5Ykbr8ilG+G51nGorq
'''

	static extensionKey = '''MIIEpAIBAAKCAQEA3TL1MCOLnd0CSVq2Ntqciyu2Z7G9ibtceYp/igq03iTt6ns/
	j2bOWrLBXOK1eSB0r8wtweEDGwQ7quoMsmK8RCZdAINzfnpe+Pt15ap7IJFGIxpH
	R7GW9um+KOSP0GcD8PlevQdji3YLqUOorx6jwN/pZWaQy2bRvxqZCkTf5GT7lhnt
3NY4L+56jkJrFgW7gJcxAAo0KIQcDG/KWQNxOJagUfo6D9BIcg6ITpJYv4MkNVZv
	JWNR1dqnygcFy8/gc2ExJbxMuMzfW1ZCm3lft7mBXj4axIQ5aWZUvRu5T0osVYcI
	gx0FdmWCwGVn9eVs0/n43lGkagxwsg84bGMb+QIDAQABAoIBAQCY8/SRHfMb7Mf4
+y3GTnIy1b4ig8gBJjpynK9GP9MDTNvq6OBxg3ArTTHhza4YnLWzHeeCCdqA4vNz
	YrX238k5AqIKidrh2TI0zoSek7ziLzIlcbVaQNWX1Nc2JZ3ASIt0JKs64JIXzD+S
	AoPw26n1457bDAoRa/6bO4QZkriSj2vp+NH1Kx97W6yeVt0k260F9k3poy0tvhlE
	z36YBfpVpELRwiZFwSyjzhMLwsMGPL7MbVXYItzb1hq4j8Dyoc0uJ5C87q/EieLO
	f105j49Oq7SxrBmtUDMYWlM1KJ8I36wPHJctj5ik/GqECQxYOhFc9YD0nhsb6ESx
	b6jIO5/RAoGBAPkzQ5v3ibU6xG9tzivYowjmC3ckRCV6u4BqkiA3iRvOGHyAhECM
	YU/ZRBKHPR2hvSGt2AGY26ar30sVHpRM6PMkOGkUycZu15kGxiy/COzFqTH1luNh
	Zvr8/vuNUmoZJvC49j6lPtpK2BiSuqNRKNAHMqcYv1qvowJWynbWiOcFAoGBAOM8
	GOIyO8Ig92e1rvjnjG8Qrvc8eW8e4fA7z4it97UnQPxhu7T3xLX3RJaz1shmcGc+
			+cY8r/PLzOadPzR0f3KsXAfbzc6hQtuA+Cp2uQ68J4JLME0LH+hslnbdQQp3AI8h
	BwuH6Ceyw1xbr+JSgZfUHKNfS1NcHABO5SNto8tlAoGADCA/ePZpoAT1DAsGzkFj
	ZYp0Il8EZeJ7/zqwU5sAM/cqX9yNEusBzTXgRxqHkcqN/naMHT/H6GtSmT+01jiL
2VD44bweOWapXQvkVscQ8xyHKCQwLG5P6YSoD6uHyBvjNjF54gB+d3bO3xs8s7e+
	SakgANuGbC5Bu1pnzbKQxNUCgYAPoINpkuDPMesxw6nbzji3LqOJtyv2u134YnSc
			RrKBT8jFUodRI7TweqfJ4WZE896JCyisPGIxVvOpbGj7V4OoRDg0Bia6Lhbl95GZ
	gf6qB8CD0l3o/nncRxN16zNc3+A296N/ZIJPsJiE0n1fCCOHaHvrtFL+0ZfPYZ2a
	eSDJjQKBgQDsCFLg7U7Eo7qtZhVlSxMuR5B/FE3MIgF8oQcjqKBVi6ldRk/pRLKH
	kbp1kW+54axiu2cEU4AlpSoA7MVNmuYultS318r0siPZv5uno1LiRd507rSzzv8W
	SY5wmkrNvCzs6tJmK4czMUEBUiW6vYzCgXW5Rrw6Jlvih+jzxBHyxA==
			'''

}
