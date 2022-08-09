package com.morpheusdata.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MorpheusUtils {

	static Logger log = LoggerFactory.getLogger(MorpheusUtils.class);

	static Date parseDate(Object date) {
		Date rtn = null;
		try {
			//handle multiple formats - 2019-02-08T19:15:39.259
			if(date != null) {
				if(date instanceof Date) {
					rtn = (Date)date;
				} else if(date instanceof CharSequence) {
					TimeZone timezone = TimeZone.getTimeZone("GMT");
					CharSequence dateCharSequence = (CharSequence)date;

					if(dateCharSequence.length() == 28) { //yyyy-MM-dd'T'HH:mm:ss.SSSZ
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
						df.setTimeZone(timezone);
						rtn = df.parse(dateCharSequence.toString());
					} else if(dateCharSequence.length() == 24) { //2018-03-23T16:53:27+0000 or 2018-03-23T16:53:27.432Z
						if(dateCharSequence.toString().indexOf('+') > -1 || dateCharSequence.toString().indexOf('-') > 16) {    //2018-03-23T19:00:01Z
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
							df.setTimeZone(timezone);
							rtn = df.parse(dateCharSequence.toString());
						} else {
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
							df.setTimeZone(timezone);
							rtn = df.parse(dateCharSequence.toString());
						}
					} else if(dateCharSequence.length() == 22) { //2018-03-23T16:53:27Z
						try {
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'UTC'");
							df.setTimeZone(timezone);
							rtn = df.parse(dateCharSequence.toString());
						} catch(Exception e2) {
							try {
								DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH.mm.ss'UTC'");
								df.setTimeZone(timezone);
								rtn = df.parse(dateCharSequence.toString());
							} catch(Exception e3) {
								try {
									DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH.mm.s'Z'");
									df.setTimeZone(timezone);
									rtn = df.parse(dateCharSequence.toString());
								} catch(Exception e4) {
									DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
									df.setTimeZone(timezone);
									rtn = df.parse(dateCharSequence.toString());
								}
							}
						}
					} else if(dateCharSequence.length() == 20) { //2018-03-23T16:53:27Z
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
						df.setTimeZone(timezone);
						rtn = df.parse(dateCharSequence.toString());
					} else if(dateCharSequence.length() == 10) { ////2018-03-23
						if (dateCharSequence.toString().indexOf('-') > -1) {
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							df.setTimeZone(timezone);
							rtn = df.parse(dateCharSequence.toString());
						} else {
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
							df.setTimeZone(timezone);
							rtn = df.parse(dateCharSequence.toString());
						}
					} else if(dateCharSequence.length() == 27) { //2018-03-23T16:53:27.000432Z
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
						df.setTimeZone(timezone);
						rtn = df.parse(dateCharSequence.toString());
					} else if(dateCharSequence.length() == 29) { //Mon, 19 Aug 2019 19:09:41 GMT
						DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
						df.setTimeZone(timezone);
						rtn = df.parse(dateCharSequence.toString());
					} else {
						// attempt default formats
						try {
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
							rtn = df.parse(dateCharSequence.toString());
						} catch (Exception e) {}
						try {
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							rtn = df.parse(dateCharSequence.toString());
						} catch (Exception e) {}
						try {
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
							rtn = df.parse(dateCharSequence.toString());
						} catch (Exception e) {}
						try {
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
							rtn = df.parse(dateCharSequence.toString());
						} catch (Exception e) {}
						if(rtn == null) {
							try {
								DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
								rtn = df.parse(dateCharSequence.toString());
							} catch(Exception e) {}
						}
						if(rtn == null) {
							try {
								DateFormat df = new SimpleDateFormat("MM/yyyy");
								rtn = df.parse(dateCharSequence.toString());
							} catch(Exception e) {}
						}
						if(rtn == null) {
							try {
								DateFormat df = new SimpleDateFormat("MM/yyyy HH:mm:ss");
								rtn = df.parse(dateCharSequence.toString());
							} catch(Exception e) {}
						}
					}
				}
			}
		} catch(Exception e) {
			log.error("error parsing date: ${date}", e);
		}
		return rtn;
	}

}
