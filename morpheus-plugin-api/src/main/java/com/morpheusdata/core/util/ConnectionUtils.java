package com.morpheusdata.core.util;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.Container;
import com.morpheusdata.model.NetworkProxy;
import com.morpheusdata.response.ServiceResponse;

import java.net.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * This class provides a few static utility methods for verify reachability to target apis/hosts that may be used by third
 * party provider implementations
 *
 * @author David Estes
 */
public class ConnectionUtils {
	static Boolean testHostConnectivity(String hostname) {
		return testHostConnectivity(hostname,null,true,true,null);
	}

	static Boolean testHostConnectivity(String hostname, Integer port) {
		return testHostConnectivity(hostname,port,true,true,null);
	}

	static Boolean testHostConnectivity(String hostname, Integer port, Boolean doPingTest) {
		return testHostConnectivity(hostname,port,doPingTest,true,null);
	}

	public static Boolean testHostConnectivity(String hostname, Integer port, Boolean doPingTest, Boolean doSocketTest, NetworkProxy networkProxy) {
		Boolean rtn = false;
		if(doPingTest == true && networkProxy == null) {
			try {
				String command = "ping -c 1 " + hostname;
				Process process = Runtime.getRuntime().exec(command);
				process.waitFor(60000, TimeUnit.MILLISECONDS);
				Integer exitValue = process.exitValue();
				if(exitValue == 0) {
					rtn = true;
				}

			} catch(Exception e) {
//				log.warn("test host connection failed: ${hostname} ${e.message}")
			}
		}
		if(rtn == false && doSocketTest == true && port != null) {
			if(port == -1) {
				port = 80;
			}
			if(networkProxy != null) {
				Socket testSocket = null;
				try {
					Integer soTimeout = 20000;
					InetSocketAddress proxyHost = new InetSocketAddress(networkProxy.getProxyHost(), networkProxy.getProxyPort());
					Proxy serverProxy = new Proxy(java.net.Proxy.Type.SOCKS, proxyHost);
					testSocket = new Socket(serverProxy);
					testSocket.setSoTimeout(soTimeout);
					testSocket.connect(new InetSocketAddress(hostname, port), soTimeout);
					rtn = true;
				} catch(Exception ex) {
//					log.debug("host connectivity proxy check failed ${hostname} ${port} - ${ex.getMessage()}")
				} finally {
					if(testSocket != null) { try{ testSocket.close();} catch(Exception eb){}}
				}
			} else {
				Socket testSocket = null;
				try {
					Integer soTimeout = 10000;
					testSocket = new Socket();
					testSocket.setSoTimeout(soTimeout);
					testSocket.connect(new InetSocketAddress(hostname, port), soTimeout);
					rtn = true;
				} catch(Exception ex) {
//					log.debug("host connectivity check failed ${hostname} ${port} - ${ex.getMessage()}")
				} finally {
					if(testSocket != null) { try{ testSocket.close();} catch(Exception eb){}}
				}
			}
		}
		return rtn;
	}
}
