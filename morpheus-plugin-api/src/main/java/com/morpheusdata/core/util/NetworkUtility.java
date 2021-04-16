package com.morpheusdata.core.util;

import com.morpheusdata.model.*;
import org.apache.commons.net.util.SubnetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for Network operations.
 *
 * @author David Estes
 * @since 0.8.0
 */
public class NetworkUtility {

	static Logger log = LoggerFactory.getLogger(NetworkUtility.class);

	static final Long defaultPingTimeout = 3000L;

	static private Pattern ip4AddressPattern = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");
	static private Pattern ip4CidrPattern = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}/\\d{1,2}$");

	static public Boolean validateIpAddr(String addr) {
		Matcher matcher = ip4AddressPattern.matcher(addr);
		return matcher.matches();
	}

	static public Boolean validateIpAddrOrCidr(String addr) {
		return validateCidr(addr) || validateIpAddr(addr);
	}

	static public Boolean validateCidr(String addr) {
		Matcher matcher = ip4CidrPattern.matcher(addr);
		return matcher.matches();
	}

	static public CidrInfo getNetworkCidrConfig(String cidr) {
		CidrInfo rtn = new CidrInfo();
		try {
			if(cidr.indexOf(':') > -1) {
				//ipv6
				//TODO
			} else {
				SubnetUtils.SubnetInfo subnetInfo = new SubnetUtils(cidr).getInfo();
				rtn.address = cidrToAddress(cidr);
				rtn.config.netmask = subnetInfo.getNetmask();
				rtn.config.ipCount = subnetInfo.getAddressCountLong();
				rtn.config.ipFreeCount = rtn.config.ipCount;
				CidrInfo.CidrRange range = new CidrInfo.CidrRange();
				range.startAddress = subnetInfo.getLowAddress();
				range.endAddress = subnetInfo.getHighAddress();
				rtn.ranges.add(range);
			}
		} catch(Exception e) {
			log.warn("error parsing network cidr: {}", e.getMessage(),e);
		}
		return rtn;
	}

	static public class CidrInfo {
		public ArrayList<CidrRange> ranges = new ArrayList<>();
		public CidrConfig config = new CidrConfig();
		public String address;
		static class CidrConfig {
			public String netmask;

			public Long ipCount;
			public Long ipFreeCount;
		}
		static class CidrRange {
			public String startAddress;
			public String endAddress;
		}

	}

	static public Integer getIpAddressCountBetween(String startAddress, String endAddress) {
		Integer cnt = 0;
		try {
			if(validateIpAddr(startAddress) && validateIpAddr(endAddress)) {
				String[] parts = startAddress.split("\\.");
				Long startValue = 0L;
				for(int x = parts.length - 1;x >= 0;x--) {
					Long part = Long.parseLong(parts[x]);
					Integer index = (parts.length - 1) - x;
					startValue += part * (long)Math.pow(256d, (double)index);
				}

				parts = endAddress.split("\\.");
				Long endValue = 0L;
				for(int x = parts.length - 1;x >= 0;x--) {
					Long part = Long.parseLong(parts[x]);
					Integer index = (parts.length - 1) - x;
					endValue += part * (long)Math.pow(256d, (double)index);
				}

				cnt = (int)Math.abs(startValue - endValue);
			}
		} catch(Exception e) {
			log.error("error in calculating address count for {}, {}",startAddress,endAddress,e);
		}
		return cnt;
	}

	//gets a cidr string for the actual address and mask
	static public String networkToCidr(String address, String netmask) {
		String rtn = null;
		try {
			SubnetUtils.SubnetInfo subnetInfo = new SubnetUtils(address, netmask).getInfo();
			rtn = subnetInfo.getCidrSignature();
		} catch(Exception e) {
			log.warn("error parsing network to cidr: {}",e.getMessage(), e);
		}
		return rtn;
	}

	//gets a cidr string for the first address and mask
	static public String networkToDisplayCidr(String address, String netmask) {
		String rtn = null;
		try {
			SubnetUtils.SubnetInfo subnetInfo = new SubnetUtils(address, netmask).getInfo();
			String addressCidr = subnetInfo.getCidrSignature();
			String networkAddress = subnetInfo.getNetworkAddress();
			String cidrRange = addressCidr.substring(addressCidr.indexOf('/'));
			rtn = networkAddress + cidrRange;
		} catch(Exception e) {
			log.warn("error parsing network to cidr - {} {}: {}", address,netmask,e.getMessage(),e);
		}
		return rtn;
	}

	//remove the cidr mask if there is one
	static public String cidrToAddress(String address) {
		String rtn = null;
		if(address != null && address.indexOf('/') > -1)
			rtn = address.substring(0, address.indexOf('/'));
		else {
			rtn = address;
		}

		return rtn;
	}

	//return the cidr if the address is in cidr form
	static public String addressToCidr(String address) {
		String rtn = null;
		if(address != null && address.indexOf('/') > -1) {
			SubnetUtils.SubnetInfo subnetInfo = new SubnetUtils(address).getInfo();
			rtn = subnetInfo.getLowAddress() + address.substring(address.indexOf('/'));
		}
		return rtn;
	}

	static public String addressToSubnet(String address) {
		String rtn = null;
		if(address != null && address.indexOf('/') > -1) {
			rtn = new SubnetUtils(address).getInfo().getNetmask();
		}
		return rtn;
	}

	static public String extractCidr(String address, String cidr, String netmask) {
		String rtn = null;
		if(cidr != null) {
			//if cidr is there - use it
			rtn = cidr;
		} else if(address != null) {
			//if the address has a cidr component
			if(address.contains("/")) {
				rtn = addressToCidr(address);
			} else if(netmask != null) {
				rtn = networkToDisplayCidr(address, netmask);
			} else {
				rtn = networkToDisplayCidr(address, "255.255.255.0");
			}
		}
		return rtn;
	}

	static public String extractSubnet(String address, String cidr, String netmask) {
		String rtn = "255.255.255.0";
		String newCidr = extractCidr(address, cidr, netmask);
		if(newCidr != null)
			rtn = addressToSubnet(newCidr);
		return rtn;
	}

	static public String hexToSubnetMask(String hexAddress) {
		String rtn = null;
		if(hexAddress != null && hexAddress.length() > 7) {
			String firstOct = hexAddress.substring(0, 2);
			rtn = Integer.toString(Integer.parseInt(firstOct, 16));
			String secondOct = hexAddress.substring(2, 4);
			rtn = rtn + "." + Integer.parseInt(secondOct, 16);
			String thirdOct = hexAddress.substring(4, 6);
			rtn = rtn + "." + Integer.parseInt(thirdOct, 16);
			String fourthOct = hexAddress.substring(6);
			rtn = rtn + "." + Integer.parseInt(fourthOct, 16);
		}
		return rtn;
	}

	static public String networkToPrefixLength(String address, String netmask) {
		String rtn = null;
		String cidr = networkToCidr(address, netmask);
		if(cidr != null && cidr.length() > 0) {
			int lastSlash = cidr.indexOf('/');
			if(lastSlash > -1)
				rtn = cidr.substring(lastSlash + 1);
		}
		return rtn;
	}

	static public String getNetworkSubnetMask(NetworkPool networkPool, Network network, NetworkSubnet subnet) {
		String rtn = null;
		try {
			if(subnet != null) {
				rtn = subnet.getNetmask();
			}
			if(rtn == null && subnet != null && subnet.getCidr() != null) {
				SubnetUtils.SubnetInfo subnetInfo = new SubnetUtils(subnet.getCidr()).getInfo();
				rtn = subnetInfo.getNetmask();
			}
			if(rtn == null && network != null && network.getNetmask() != null) {
				rtn = network.getNetmask();
			}
			if(rtn == null && network != null && network.getCidr() != null) {
				SubnetUtils.SubnetInfo subnetInfo = new SubnetUtils(network.getCidr()).getInfo();
				rtn = subnetInfo.getNetmask();
			}
			if(rtn == null && networkPool != null) {
				rtn = networkPool.getNetmask();
			}
		} catch(Exception e) {
			log.warn("error parsing network cidr: {}",e.getMessage(), e);
		}
		return rtn;
	}

	static public String getServiceUrlHost(String serviceUrl) throws MalformedURLException {
		return new URL(serviceUrl).getHost();
	}

	static public Integer getServiceUrlPort(String serviceUrl) throws MalformedURLException {
		URL urlObj = new URL(serviceUrl);
		return urlObj.getPort() > 0 ? urlObj.getPort() : (urlObj.getProtocol().toLowerCase() == "https" ? 443 : 80);
	}

	static public String getDomainRecordFqdn(String name, String domain) {
		String rtn = null;
		if(name.contains(domain)) {
			rtn = getFqdnDomainName(name);
		} else {
			if(domain.endsWith(".")) {
				String shortDomain = getFriendlyDomainName(domain);
				if(name.contains(shortDomain)) {
					rtn = getFqdnDomainName(name);
				}
			}
			if(rtn == null) {
				rtn = getFqdnDomainName(name + "." + domain);
			}
		}
		return rtn;
	}

	static public String getDomainRecordName(String name, String domain) {
		String rtn = null;
		if(domain != null && name != null) {
			domain = getFqdnDomainName(domain);
			name = getFqdnDomainName(name);
			if(name.length() > domain.length()) {
				log.debug("name: {} domain: {}",name,domain);
				rtn = name.substring(0, (name.length() - domain.length() - 1));
			} else {
				rtn = name;
			}
		}
		return rtn;
	}

	static public String getFriendlyDomainName(String name) {
		if(name != null) {
			name = name.toLowerCase();
			if(name.endsWith(".")) {
				name = name.substring(0, name.length() - 1);
			}
		}
		return name;
	}

	public static String getFqdnDomainName(String name) {
		if(name != null) {
			name = name.toLowerCase();
			if(!name.endsWith("."))
				name = name + ".";
		}
		return name;
	}

	static public String cleanHostname(String name) {
		return name.replaceAll(" ", "-");
	}

	static public String getNextIpAddress(String ipAddress) throws UnknownHostException {
		return getNextIpAddress(ipAddress,1);
	}

	static public String getNextIpAddress(String ipAddress, Integer increment) throws UnknownHostException {
		InetAddress address = InetAddress.getByName(ipAddress);
		byte[] newAddress = address.getAddress();
		for(int x =0 ;x<increment;x++) {
			for (int i = newAddress.length - 1; i >= 0; i--) {
				if (++newAddress[i] != 0x00)
					break;
			}
		}
		return InetAddress.getByAddress(newAddress).toString();
	}


	static public Integer getMinPort(String portRange) {
		if(portRange.contains("-")) {
			String[] ports = portRange.split("-");
			return Integer.parseInt(ports[0]);
		} else {
			return Integer.parseInt(portRange);
		}
	}

	static public Integer getMaxPort(String portRange) {
		if(portRange.contains("-")) {
			String[] ports = portRange.split("-");
			return Integer.parseInt(ports[1]);
		} else {
			return Integer.parseInt(portRange);
		}
	}

	static public List<String> getNetworksFromRange(String ipStart, String ipEnd) {
		long start = ipToLong(ipStart);
		long end = ipToLong(ipEnd);
		ArrayList<String> result = new ArrayList<>();
		while (end >= start) {
			byte maxSize = 32;
			while (maxSize > 0) {
				long mask = 0;
				try {
					mask = iMask(maxSize - 1);
					long maskBase = start & mask;

					if (maskBase != start) {
						break;
					}
					maxSize--;
				}
				catch (Throwable t) {
					log.error(t.getMessage(), t);
					maxSize--;
				}
			}
			double x = Math.log( end - start + 1) / Math.log( 2 );
			byte maxDiff = (byte)( 32 - Math.floor( x ) );
			if ( maxSize < maxDiff) {
				maxSize = maxDiff;
			}
			String ip = longToIp(start);
			result.add(ip + "/" + maxSize);

			start = start + (long)Math.pow( 2, (32 - maxSize) );
		}
		return result;
	}

	static public long ipToLong(String ipstr) {
		String[] ipAddressInArray = ipstr.split("\\.");
		long num = 0L;
		long ip = 0L;
		for (int x = 3; x >= 0; x--) {
			ip = Long.parseLong(ipAddressInArray[3 - x]);
			num |= ip << (x << 3);
		}
		return num;
	}

	static public String longToIp(Long longIP) {
		return String.valueOf(longIP >>> 24) + "." + String.valueOf((longIP & 0x00FFFFFF) >>> 16) + "." + String.valueOf((longIP & 0x0000FFFF) >>> 8) + "." + String.valueOf(longIP & 0x000000FF);
	}

	static public Long iMask(Integer s) {
		return Math.round(Math.pow(2, 32) - Math.pow(2, (32 - s)));
	}

	static public Boolean checkIpv4Ip(String ipAddress) {
		Boolean rtn = false;
		if(ipAddress != null) {
			if(ipAddress.indexOf('.') > 0 && !ipAddress.startsWith("169"))
				rtn = true;
		}
		return rtn;
	}

	static public String getIpAddressType(String ipAddress) throws UnknownHostException {
		InetAddress address = InetAddress.getByName(ipAddress);
		if (address instanceof Inet6Address) {
			return "ipv6";
		} else if (address instanceof Inet4Address) {
			return "ipv4";
		} else {
			return null;
		}
	}

	static public String getIpAddressIndex(String name) {
		String rtn = null;
		if(name != null) {
			if(name.startsWith("eth"))
				rtn = name.substring(3);
			else if(name.startsWith("en"))
				rtn = name.substring(name.length() - 1);
			else
				rtn = name.substring(name.length() - 1);
		}
		return rtn;
	}



}
