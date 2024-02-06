package com.morpheusdata.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtility {

	static Logger log = LoggerFactory.getLogger(DateUtility.class);

	public static final TimeZone gmtTimezone = TimeZone.getTimeZone("GMT");
	public static final TimeZone serverTimezone = TimeZone.getDefault();

	public static String formatDate(Date date) {
		return formatDate(date,"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	}
	public static String formatDate(Date date, String outputFormat) {
		String rtn = null;
		try {
			if(date != null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(outputFormat);

				rtn = dateFormat.format(date);
			}
		} catch(Exception ignored) {
			log.warn("error formatting date: ${date}");
		}
		return rtn;
	}

	public static Date parseDate(Date date) {
		return date;
	}

	public static Date parseDate(CharSequence date) {
		Date rtn = null;
		try {
			if (date != null) {
				String dateStr = date.toString();
				TimeZone timezone = TimeZone.getTimeZone("GMT");
				if (dateStr.length() == 28) { //yyyy-MM-dd'T'HH:mm:ss.SSSZ
					rtn = parseDateWithFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone);
				} else if (dateStr.length() == 24) { //2018-03-23T16:53:27+0000 or 2018-03-23T16:53:27.432Z
					if (dateStr.indexOf('+') > -1 || dateStr.indexOf('-') > 16) {   //2018-03-23T19:00:01Z
						rtn = parseDateWithFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ssZ", timezone);
					} else {
						rtn = parseDateWithFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone);
					}
				} else if (dateStr.length() == 22) { //2018-03-23T16:53:27Z
					try {
						rtn = parseDateWithFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ss'UTC'", timezone);
					} catch (Exception e2) {
						try {
							rtn = parseDateWithFormat(dateStr, "yyyy-MM-dd'T'HH.mm.ss'UTC'", timezone);
						} catch (Exception e3) {
							try {
								rtn = parseDateWithFormat(dateStr, "yyyy-MM-dd'T'HH.mm.s'Z'", timezone);
							} catch (Exception e4) {
								rtn = parseDateWithFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ss.S'Z'", timezone);
							}
						}
					}
				} else if (dateStr.length() == 20) { //2018-03-23T16:53:27Z
					rtn = parseDateWithFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone);
				} else if (dateStr.length() == 10) { ////2018-03-23
					if (dateStr.indexOf('-') > -1) {
						rtn = parseDateWithFormat(dateStr, "yyyy-MM-dd", timezone);
					} else {
						rtn = parseDateWithFormat(dateStr, "MM/dd/yyyy", timezone);
					}
				} else if (dateStr.length() == 27) { //2018-03-23T16:53:27.000432Z
					rtn = parseDateWithFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone);
				} else if (dateStr.length() == 29) { //Mon, 19 Aug 2019 19:09:41 GMT
					rtn = parseDateWithFormat(dateStr, "EEE, dd MMM yyyy HH:mm:ss zzz", timezone);
				} else {
					// attempt default formats
					try {
						rtn = parseDateWithFormat(dateStr, "MM/dd/yyyy HH:mm:ss");
					} catch (Exception e) {
					}
					try {
						rtn = parseDateWithFormat(dateStr, "yyyy-MM-dd HH:mm:ss");
					} catch (Exception e) {
					}
					try {
						rtn = parseDateWithFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ss");
					} catch (Exception e) {
					}
					try {
						rtn = parseDateWithFormat(dateStr, "MM/dd/yyyy HH:mm");
					} catch (Exception e) {
					}
					if (rtn == null) {
						try {
							rtn = parseDateWithFormat(dateStr, "MM/dd/yyyy");
						} catch (Exception e) {
						}
					}
					if (rtn == null) {
						try {
							rtn = parseDateWithFormat(dateStr, "MM/yyyy");
						} catch (Exception e) {
						}
					}
					if (rtn == null) {
						try {
							rtn = parseDateWithFormat(dateStr, "MM/yyyy HH:mm:ss");
						} catch (Exception e) {
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("error parsing date: ${date}", e);
		}

		return rtn;
	}

	public static Date parseDateWithFormat(String dateStr, String format) throws ParseException {
		SimpleDateFormat dfInstance = new SimpleDateFormat(format);
		return dfInstance.parse(dateStr);
	}

	public static Date parseDateWithFormat(String dateStr, String format, TimeZone timezone) throws ParseException {
		SimpleDateFormat dfInstance = new SimpleDateFormat(format);
		dfInstance.setTimeZone(timezone);
		return dfInstance.parse(dateStr);
	}

	public static Date getStartOfNextGmtMonth(Date date) {
		Calendar rtn = getGregorianCalendarForDate(date, gmtTimezone);


		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);
		rtn.set(Calendar.DAY_OF_MONTH, 1);

		rtn.add(Calendar.MONTH, 1);
		return rtn.getTime();
	}

	public static Date getEndOfGmtDay(Date date) {
		Calendar rtn = getGregorianCalendarForDate(date, gmtTimezone);

		rtn.set(Calendar.HOUR_OF_DAY, 23);
		rtn.set(Calendar.MINUTE, 59);
		rtn.set(Calendar.SECOND, 59);
		rtn.set(Calendar.MILLISECOND, 999);

		return rtn.getTime();
	}

	public static Date getEndOfGmtHour(Date date) {
		Calendar rtn = getGregorianCalendarForDate(date, gmtTimezone);

		rtn.set(Calendar.MINUTE, 59);
		rtn.set(Calendar.SECOND, 59);
		rtn.set(Calendar.MILLISECOND, 999);

		return rtn.getTime();
	}

	public static Date getStartOfDay() {
		return getStartOfDay(serverTimezone);
	}

	public static Date getStartOfDay(TimeZone timezone) {
		Calendar rtn = getCalendarForDate(null, timezone);


		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);

		return rtn.getTime();
	}

	public static Date getStartOfMonth() {
		return getStartOfMonth(serverTimezone);
	}

	public static Date getStartOfMonth(TimeZone timezone) {
		Calendar rtn = getCalendarForDate(null, timezone);

		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);
		rtn.set(Calendar.DAY_OF_MONTH, 1);

		return rtn.getTime();
	}

	public static Date getStartOfYear() {
		return getStartOfYear(serverTimezone);
	}

	public static Date getStartOfYear(TimeZone timezone) {
		Calendar rtn = getCalendarForDate(null, timezone);

		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);
		rtn.set(Calendar.DAY_OF_MONTH, 1);
		rtn.set(Calendar.MONTH, 0);

		return rtn.getTime();
	}

	public static Date getStartOfYear(Integer year) {
		return getStartOfYear(year, null);
	}

	public static Date getStartOfYear(Integer year, TimeZone timezone) {
		Calendar rtn = getCalendarForDate(null, timezone);

		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);
		rtn.set(Calendar.DAY_OF_MONTH, 1);
		rtn.set(Calendar.MONTH, 0);
		rtn.set(Calendar.YEAR, year);

		return rtn.getTime();
	}

	public static Date getStartOfDateMonth(Date date) {
		return getStartOfDateMonth(date, null);
	}

	public static Date getStartOfDateMonth(Date date, TimeZone timezone) {
		Calendar rtn = getGregorianCalendarForDate(date, timezone);


		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);
		rtn.set(Calendar.DAY_OF_MONTH, 1);

		return rtn.getTime();
	}

	public static Date getEndOfDateMonth(Date date) {
		return getEndOfDateMonth(date, null);
	}

	public static Date getEndOfDateMonth(Date date, TimeZone timezone) {
		Calendar rtn = getGregorianCalendarForDate(date, timezone);


		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);
		rtn.set(Calendar.DAY_OF_MONTH, 1);

		rtn.add(Calendar.MONTH, 1);
		rtn.add(Calendar.DAY_OF_YEAR, -1);
		return rtn.getTime();
	}

	public static Date getStartOfDateDay(Date date) {
		return getStartOfDateDay(date, null);
	}

	public static Date getStartOfDateDay(Date date, TimeZone timezone) {
		Calendar rtn = getGregorianCalendarForDate(date, timezone);


		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);

		return rtn.getTime();
	}

	public static Date getEndOfDateDay(Date date) {
		return getEndOfDateDay(date, null);
	}

	public static Date getEndOfDateDay(Date date, TimeZone timezone) {
		Calendar rtn = GregorianCalendar.getInstance();
		if (timezone != null) {
			rtn.setTimeZone(timezone);
		}
		rtn.setTime(date);

		rtn.set(Calendar.HOUR_OF_DAY, 23);
		rtn.set(Calendar.MINUTE, 59);
		rtn.set(Calendar.SECOND, 59);
		rtn.set(Calendar.MILLISECOND, 999);

		return rtn.getTime();
	}

	public static Date getGmtDate(Date date) {
		Calendar rtn = getGregorianCalendarForDate(date, gmtTimezone);
		return rtn.getTime();
	}

	public static Date getStartOfHour(Date date) {
		Calendar rtn = GregorianCalendar.getInstance();
		rtn.setTime(date);

		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);

		return rtn.getTime();
	}

	public static Date getStartOfDay(Date date) {
		Calendar rtn = GregorianCalendar.getInstance();
		rtn.setTime(date);

		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);

		return rtn.getTime();
	}

	public static Date getStartOfMonth(Date date) {
		Calendar rtn = GregorianCalendar.getInstance();
		rtn.setTime(date);

		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);
		rtn.set(Calendar.DAY_OF_MONTH, 1);

		return rtn.getTime();
	}

	public static Date getEndOfMonth(Date date) {
		return getEndOfMonth(date, false);
	}

	public static Date getEndOfMonth(Date date, Boolean doMillis) {
		Calendar rtn = GregorianCalendar.getInstance();
		rtn.setTime(date);

		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);
		rtn.set(Calendar.DAY_OF_MONTH, 1);

		rtn.add(Calendar.MONTH, 1);
		if (doMillis == true)
			rtn.add(Calendar.MILLISECOND, -1);
		else
			rtn.add(Calendar.SECOND, -1);
		return rtn.getTime();
	}

	public static Date getEndOfYear(Date date) {
		return getEndOfYear(date, false);
	}

	public static Date getEndOfYear(Date date, Boolean doMillis) {
		Calendar rtn = GregorianCalendar.getInstance();
		rtn.setTime(date);

		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);
		rtn.set(Calendar.DAY_OF_MONTH, 1);
		rtn.set(Calendar.MONTH, 11);

		rtn.add(Calendar.MONTH, 1);
		if (doMillis == true)
			rtn.add(Calendar.MILLISECOND, -1);
		else
			rtn.add(Calendar.SECOND, -1);
		return rtn.getTime();
	}

	public static Date getEndOfDay(Date date) {
		return getEndOfDay(date, false);
	}

	public static Date getEndOfDay(Date date, Boolean doMillis) {
		Calendar rtn = GregorianCalendar.getInstance();
		rtn.setTime(date);

		rtn.set(Calendar.HOUR_OF_DAY, 23);
		rtn.set(Calendar.MINUTE, 59);
		rtn.set(Calendar.SECOND, 59);
		if (doMillis == true)
			rtn.set(Calendar.MILLISECOND, 999);
		else
			rtn.set(Calendar.MILLISECOND, 0);

		return rtn.getTime();
	}

	public static Date getEndOfHour(Date date) {
		return getEndOfHour(date, false);
	}

	public static Date getEndOfHour(Date date, Boolean doMillis) {
		Calendar rtn = GregorianCalendar.getInstance();
		rtn.setTime(date);

		rtn.set(Calendar.MINUTE, 59);
		rtn.set(Calendar.SECOND, 59);
		if (doMillis)
			rtn.set(Calendar.MILLISECOND, 999);
		else
			rtn.set(Calendar.MILLISECOND, 0);

		return rtn.getTime();
	}

	public static Date getStartOfGmtHour(Date date) {
		Calendar rtn = getGregorianCalendarForDate(date, gmtTimezone);

		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);

		return rtn.getTime();
	}

	public static Date getStartOfGmtDay(Date date) {
		Calendar rtn = getGregorianCalendarForDate(date, gmtTimezone);

		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);

		return rtn.getTime();
	}

	public static Date getStartOfGmtMonth(Date date) {
		Calendar rtn = getGregorianCalendarForDate(date, gmtTimezone);

		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);
		rtn.set(Calendar.DAY_OF_MONTH, 1);

		return rtn.getTime();
	}

	public static Date getStartOfGmtYear(Date date) {
		Calendar rtn = getGregorianCalendarForDate(date, gmtTimezone);

		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);
		rtn.set(Calendar.DAY_OF_MONTH, 1);
		rtn.set(Calendar.MONTH, 0);

		return rtn.getTime();
	}

	public static Date getEndOfGmtMonth(Date date) {
		Calendar rtn = getGregorianCalendarForDate(date, gmtTimezone);

		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);
		rtn.set(Calendar.DAY_OF_MONTH, 1);

		rtn.add(Calendar.MONTH, 1);
		rtn.add(Calendar.SECOND, -1);
		return rtn.getTime();
	}

	public static Date getEndOfGmtYear(Date date) {
		Calendar rtn = getGregorianCalendarForDate(date, gmtTimezone);


		rtn.set(Calendar.HOUR_OF_DAY, 0);
		rtn.set(Calendar.MINUTE, 0);
		rtn.set(Calendar.SECOND, 0);
		rtn.set(Calendar.MILLISECOND, 0);
		rtn.set(Calendar.DAY_OF_MONTH, 1);
		rtn.set(Calendar.MONTH, 11);

		rtn.add(Calendar.MONTH, 1);
		rtn.add(Calendar.DAY_OF_YEAR, -1);
		return rtn.getTime();
	}

	public static Calendar getGregorianCalendarForDate(Date date, TimeZone timezone) {
		Calendar rtn = GregorianCalendar.getInstance();
		if (date != null) {
			rtn.setTime(date);
		}
		if (timezone != null) {
			rtn.setTimeZone(timezone);
		}
		return rtn;
	}

	public static Calendar getCalendarForDate(Date date, TimeZone timezone) {
		Calendar rtn = Calendar.getInstance();
		if (date != null) {
			rtn.setTime(date);
		}
		if (timezone != null) {
			rtn.setTimeZone(timezone);
		}
		return rtn;
	}

	public static Date addToDate(Date date, int field, int amount) {
		Calendar rtn = Calendar.getInstance();
		rtn.setTimeZone(gmtTimezone);
		rtn.setTime(date);
		rtn.add(field, amount);
		return rtn.getTime();
	}
}
