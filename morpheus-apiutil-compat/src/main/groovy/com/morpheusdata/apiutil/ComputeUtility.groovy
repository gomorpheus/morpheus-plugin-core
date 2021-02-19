package com.morpheusdata.apiutil

import com.morpheusdata.model.PlatformType

class ComputeUtility {

	static ONE_BYTE = 1l
	static ONE_KILOBYTE = 1024l
	static ONE_KILOBYTE_REAL = 1000l
	static ONE_MEGABYTE = 1024l * 1024l
	static ONE_MEGABYTE_REAL = 1000l * 1000l
	static ONE_GIGABYTE = 1024l * 1024l * 1024l
	static ONE_GIGABYTE_REAL = 1000l * 1000l * 1000l
	static ONE_TERABYTE = 1024l * 1024l * 1024l * 1024l

	static String formatHostname(String name, PlatformType platform = PlatformType.linux) {
		String rtn = name
		rtn = rtn?.replaceAll(/\W+/, '-')
		rtn = rtn?.toLowerCase()
		if(!rtn) {
			return rtn
		}
		if(platform == PlatformType.windows) {
			rtn = rtn?.take(15)
			while(rtn.endsWith('-')) {
				rtn = rtn[0..-2] //lop off ending hyphens as these conflict in azure
			}
		}
		return rtn
	}

	static String formatProvisionHostname(String name) {
		def rtn = name
		try {
			if(rtn.indexOf('\$') < 0) {
				rtn = rtn.replaceAll(' ', '-')
				rtn = rtn.replaceAll('\'', '')
				rtn = rtn.replaceAll(',', '')
				rtn = rtn.toLowerCase()
			}
		} catch(e) {
			//ok
		}
		return rtn
	}

	static parseGigabytesToBytes(Number value) {
		def rtn = 0
		try {
			rtn = value.toDouble()
			rtn = (rtn * ONE_GIGABYTE).toLong()
		} catch(e) {
			rtn = 0
		}
		return rtn
	}

	static String bytesToHuman(Float bytes) {
		float kbMultiplier = 1024f
		float mbMultiplier = 1024f*1024f
		float gbMultiplier = mbMultiplier*1024f
		float tbMultiplier = gbMultiplier*1024f
		if(!bytes) {
			return '0MB'
		}
		if(bytes >= tbMultiplier) {
			bytes = bytes / tbMultiplier
			return (bytes.trunc(1) + 'TB').replace('.0','')
		} else if (bytes >= gbMultiplier) {
			bytes = bytes / gbMultiplier
			return (bytes.trunc(1) + 'GB').replace('.0','')
		} else if (bytes >= mbMultiplier) {
			bytes = bytes / mbMultiplier
			return (bytes.trunc(1) + 'MB').replace('.0','')
		} else if (bytes >= 1024) {
			bytes = bytes / kbMultiplier
			return (bytes.trunc(1) + 'KB').replace('.0','')
		} else {
			return bytes.trunc(1) + 'B'
		}

	}

	// convert bytes to GB
	// returns as Long or Double if not a whole number, for nicer serialization
	static Number bytesAsGB(numBytes) {
		if(!numBytes) {
			return numBytes
		}
		Double value = ((Double)numBytes) / ONE_GIGABYTE
		if(value % 1 == 0) {
			return (Integer)value
		} else {
			return (Double)value
		}
	}

}
