package com.morpheusdata.core.util;

import groovy.util.logging.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.text.DateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtility {

	static Logger log = LoggerFactory.getLogger(DateUtility.class);

	static Date parseDate(Date date) {
		return date;
	}

	static Date parseDate(CharSequence date) {
		Date rtn = null;
		try {
			if(date != null) {
				String dateStr = date.toString();
				TimeZone timezone = TimeZone.getTimeZone("GMT");
				if(dateStr.length() == 28) { //yyyy-MM-dd'T'HH:mm:ss.SSSZ
					rtn = parseDateWitFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone);
				} else if(dateStr.length() == 24) { //2018-03-23T16:53:27+0000 or 2018-03-23T16:53:27.432Z
					if(dateStr.indexOf('+') > -1 || dateStr.indexOf('-') > 16) {   //2018-03-23T19:00:01Z
						rtn = parseDateWitFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ssZ", timezone);
					} else {
						rtn = parseDateWitFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone);
					}
				} else if(dateStr.length() == 22) { //2018-03-23T16:53:27Z
					try {
						rtn = parseDateWitFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ss'UTC'", timezone);
					} catch(Exception e2) {
						try {
							rtn = parseDateWitFormat(dateStr, "yyyy-MM-dd'T'HH.mm.ss'UTC'", timezone);
						} catch(Exception e3) {
							try {
								rtn = parseDateWitFormat(dateStr, "yyyy-MM-dd'T'HH.mm.s'Z'", timezone);
							} catch(Exception e4) {
								rtn = parseDateWitFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ss.S'Z'", timezone);
							}
						}
					}
				} else if(dateStr.length() == 20) { //2018-03-23T16:53:27Z
					rtn = parseDateWitFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone);
				} else if(dateStr.length() == 10) { ////2018-03-23
					if (dateStr.indexOf('-') > -1) {
						rtn = parseDateWitFormat(dateStr, "yyyy-MM-dd", timezone);
					} else {
						rtn = parseDateWitFormat(dateStr, "MM/dd/yyyy", timezone);
					}
				} else if(dateStr.length() == 27) { //2018-03-23T16:53:27.000432Z
					rtn = parseDateWitFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone);
				} else if(dateStr.length() == 29) { //Mon, 19 Aug 2019 19:09:41 GMT
					rtn = parseDateWitFormat(dateStr, "EEE, dd MMM yyyy HH:mm:ss zzz", timezone);
				} else {
					// attempt default formats
					try {
						rtn = parseDateWitFormat(dateStr, "MM/dd/yyyy HH:mm:ss");
					} catch (Exception e) {}
					try {
						rtn = parseDateWitFormat(dateStr, "yyyy-MM-dd HH:mm:ss");
					} catch (Exception e) {}
					try {
						rtn = parseDateWitFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ss");
					} catch (Exception e) {}
					try {
						rtn = parseDateWitFormat(dateStr, "MM/dd/yyyy HH:mm");
					} catch (Exception e) {}
					if(rtn == null) {
						try {
							rtn = parseDateWitFormat(dateStr, "MM/dd/yyyy");
						} catch(Exception e) {}
					}
					if(rtn == null) {
						try {
							rtn = parseDateWitFormat(dateStr, "MM/yyyy");
						} catch(Exception e) {}
					}
					if(rtn == null) {
						try {
							rtn = parseDateWitFormat(dateStr, "MM/yyyy HH:mm:ss");
						} catch(Exception e) {}
					}
				}
			}
		} catch(Exception e) {
			log.error("error parsing date: ${date}", e);
		}

		return rtn;
	}

	static Date parseDateWitFormat(String dateStr, String format) throws ParseException {
		SimpleDateFormat dfInstance = new SimpleDateFormat(format);
		return dfInstance.parse(dateStr);
	}

	static Date parseDateWitFormat(String dateStr, String format, TimeZone timezone) throws ParseException {
		SimpleDateFormat dfInstance = new SimpleDateFormat(format);
		dfInstance.setTimeZone(timezone);
		return dfInstance.parse(dateStr);
	}
}
