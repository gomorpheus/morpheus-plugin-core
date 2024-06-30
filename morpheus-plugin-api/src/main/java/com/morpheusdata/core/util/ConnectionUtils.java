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

import com.morpheusdata.model.NetworkProxy;

import java.net.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * This class provides a few static utility methods for verify reachability to target apis/hosts that may be used by third
 * party provider implementations.
 *
 *
 * @author David Estes
 * @since 0.8.0
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

	public static boolean testHostConnectivity(String hostname, Integer port, Boolean doPingTest, Boolean doSocketTest, NetworkProxy networkProxy) {
		boolean rtn = false;
		if(doPingTest && networkProxy == null) {
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
		if(!rtn && doSocketTest && port != null) {
			if(port == -1) {
				port = 80;
			}
			boolean noProxy = networkProxy != null && networkProxy.getNoProxy() != null && Arrays.stream(networkProxy.getNoProxy().split("[,|\\s]+")).anyMatch(it -> it.equalsIgnoreCase(hostname));
			if(networkProxy != null && !noProxy) {
				Socket testSocket = null;
				try {
					Integer soTimeout = 20000;
					InetSocketAddress proxyHost = new InetSocketAddress(networkProxy.getProxyHost(), networkProxy.getProxyPort());
					Proxy serverProxy = new Proxy(java.net.Proxy.Type.HTTP, proxyHost);
					testSocket = new Socket(serverProxy);
					testSocket.setSoTimeout(soTimeout);
					testSocket.connect(new InetSocketAddress(hostname, port), soTimeout);
					rtn = true;
				} catch(Exception ignored) {
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
				} catch(Exception ignored) {
//					log.debug("host connectivity check failed ${hostname} ${port} - ${ex.getMessage()}")
				} finally {
					if(testSocket != null) { try{ testSocket.close();} catch(Exception eb){}}
				}
			}
		}
		return rtn;
	}
}
