package com.morpheusdata.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
public class InvoiceUtility {
	static String getCurrentPeriodString() {
		return getPeriodString(new Date());
	}

	static String getPeriodString(Date costDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		return dateFormat.format(costDate);
	}

	static Date getPeriodStartDate(String periodString) {
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
}
