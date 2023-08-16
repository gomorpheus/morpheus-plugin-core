package com.morpheusdata.core.util;

import com.github.jknack.handlebars.internal.lang3.ArrayUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Calendar;
public class InvoiceUtility {
	public static String getCurrentPeriodString() {
		return getPeriodString(new Date());
	}

	public static String getPeriodString(Date costDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		return dateFormat.format(costDate);
	}

	public static Date getPeriodStartDate(String periodString) {
		int year = Integer.parseInt(periodString.substring(0,4));
		int month = Integer.parseInt(periodString.substring(4,6));
		Calendar rtn = Calendar.getInstance();
		rtn.set(Calendar.HOUR_OF_DAY,0);
		rtn.set(Calendar.MINUTE,0);
		rtn.set(Calendar.SECOND,0);
		rtn.set(Calendar.MILLISECOND,0);
		rtn.set(Calendar.DAY_OF_MONTH,1);
		rtn.set(Calendar.MONTH,month -1);
		rtn.set(Calendar.YEAR,year - 1);
		return rtn.getTime();
	}

	public static Date getPeriodStart(Date date) {
		return DateUtility.getStartOfMonth(date);
	}

	public static Date getPeriodEnd(Date date) {
		return DateUtility.getEndOfMonth(date);
	}

	public static Boolean checkDateCheckHash(Date billingStartDate, Date lineItemDate, String existingHash) throws DecoderException {

		byte[] hashArray;
		if(existingHash != null) {
			hashArray = Hex.decodeHex(existingHash);
		} else {
			return false;
		}

		long hourLong = ((lineItemDate.getTime() - billingStartDate.getTime()) / 3600000L );
		int currentHour = (int) hourLong;
		int hourByteIndex = currentHour / 8;
		int bitOffset = currentHour % 8;

		byte hourByte = hashArray[hourByteIndex];
		return (hourByte & (byte) ((byte) 0x01 << bitOffset)) != 0;
	}

	public static String updateDateCheckHash(Date billingStartDate, Date lineItemDate, String existingHash) throws DecoderException {

		Byte[] hashArray;
		if(existingHash != null) {
			hashArray = ArrayUtils.toObject(Hex.decodeHex(existingHash));
		} else {
			hashArray = new Byte[96];
			for(int x=0;x<96;x++) {
				hashArray[x] = 0x00;
			}
		}


		long hourLong = ((lineItemDate.getTime() - billingStartDate.getTime()) / 3600000L );
		int currentHour = (int) hourLong;
		int hourByteIndex = currentHour / 8;
		int bitOffset = currentHour % 8;
		if(hashArray[hourByteIndex] == null) {
			hashArray[hourByteIndex] = 0x00;
		}
		hashArray[hourByteIndex] = (byte) (hashArray[hourByteIndex] | (byte)((byte)(0x01) << bitOffset));
		return Arrays.toString(Hex.encodeHex(ArrayUtils.toPrimitive(hashArray)));

	}
}
