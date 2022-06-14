package com.morpheusdata.core.util;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.PlatformType;
import com.morpheusdata.model.projection.ComputeServerIdentityProjection;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for compute operations.
 *
 * @author David Estes
 * @since 0.8.0
 */
public class ComputeUtility {

	static public Long ONE_BYTE = 1L;
	static public Long ONE_KILOBYTE = 1024L;
	static public Long ONE_KILOBYTE_REAL = 1000L;
	static public Long ONE_MEGABYTE = 1024L * 1024L;
	static public Long ONE_MEGABYTE_REAL = 1000L * 1000L;
	static public Long ONE_GIGABYTE = 1024L * 1024L * 1024L;
	static public Long ONE_GIGABYTE_REAL = 1000L * 1000L * 1000L;
	static public Long ONE_TERABYTE = 1024L * 1024L * 1024L * 1024L;

	static String formatHostname(String name) {
		return formatHostname(name,PlatformType.linux);
	}

	static String formatHostname(String name, PlatformType platform) {
		String rtn = name;
		if(rtn != null) {
			rtn = rtn.replaceAll("\\W+","-");
			rtn = rtn.toLowerCase();
			if(platform == PlatformType.windows) {
				rtn = rtn.substring(0, Math.min(rtn.length(), 15));

				while(rtn.endsWith("-")) {
					rtn = rtn.substring(0,rtn.length() - 1);
				}
			}
		}
		return rtn;
	}

	static String formatHostname(String name, PlatformType platform, Long serverId, List<ComputeServerIdentityProjection> existingServers) {
		String rtn = name;
		if(rtn != null) {
			rtn = rtn.replaceAll("\\W+", "-");
			rtn = rtn.replaceAll("\'", "");
			rtn = rtn.replaceAll(",", "");
			rtn = rtn.toLowerCase();
		}

		if(rtn == null || rtn =="") {
			return rtn;
		}
		if(platform == PlatformType.windows) {
			rtn = rtn.substring(0, Math.min(rtn.length(), 15));
			while(rtn.endsWith("-")) {
				rtn = rtn.substring(0,rtn.length() - 1);
			}
			String originalInternalName = rtn;
			Integer seq=1;
			ArrayList<String> existingNames = new ArrayList<>();
			for(int i=0; i < existingServers.size(); i++) {
				ComputeServerIdentityProjection server = existingServers.get(i);
				String hostName = server.getHostname();
				if(server.getId() != serverId && hostName.startsWith(originalInternalName.substring(0,3))) {
					existingNames.add(hostName);
				}
			}
			Boolean nameExists = existingNames.contains(rtn);
			while(nameExists) {
				seq++;
				String sequence = seq.toString();
				Boolean originalEndsWithDash = originalInternalName.endsWith("-");
				String suffix = originalEndsWithDash ? sequence : "-" + sequence;
				int suffixLength = suffix.length();

				String namePrefix = originalInternalName.substring(0, 15-suffixLength);
				rtn = namePrefix + suffix;
				nameExists = existingNames.contains(rtn);
			}
		}
		return rtn;
	}

	static String formatProvisionHostname(String name) {
		String rtn = name;
		try {
			if(rtn.indexOf('$') < 0) {
				rtn = rtn.replaceAll(" ", "-");
				rtn = rtn.replaceAll("'", "");
				rtn = rtn.replaceAll(",", "");
				rtn = rtn.toLowerCase();
			}
		} catch(Exception ignored) {
			//ok
		}
		return rtn;
	}

	static Long parseGigabytesToBytes(Number value) {
		Long rtn = 0L;
		try {
			rtn = (long)(value.doubleValue() * ONE_GIGABYTE);
		} catch(Exception ex) {
			rtn = 0L;
		}
		return rtn;
	}

	static String bytesToHuman(Float bytes) {
		float kbMultiplier = 1024f;
		float mbMultiplier = 1024f*1024f;
		float gbMultiplier = mbMultiplier*1024f;
		float tbMultiplier = gbMultiplier*1024f;
		if(bytes == null || bytes == 0) {
			return "0MB";
		}
		DecimalFormat df = new DecimalFormat("#.#");
		df.setRoundingMode(RoundingMode.DOWN);

		if(bytes >= tbMultiplier) {
			bytes = bytes / tbMultiplier;
			return (df.format(bytes) + "TB");
		} else if (bytes >= gbMultiplier) {
			bytes = bytes / gbMultiplier;
			return (df.format(bytes) + "GB");
		} else if (bytes >= mbMultiplier) {
			bytes = bytes / mbMultiplier;
			return (df.format(bytes) + "MB");
		} else if (bytes >= 1024) {
			bytes = bytes / kbMultiplier;
			return (df.format(bytes) + "KB");
		} else {
			return df.format(bytes) + 'B';
		}
	}

	// convert bytes to GB
	// returns as Long or Double if not a whole number, for nicer serialization
	static Number bytesAsGB(Long numBytes) {
		if(numBytes == null || numBytes == 0L) {
			return numBytes;
		}
		double value = new Double(numBytes) / ONE_GIGABYTE;
		if(value % 1 == 0) {
			return (long) (double) value;
		} else {
			return value;
		}
	}

}
