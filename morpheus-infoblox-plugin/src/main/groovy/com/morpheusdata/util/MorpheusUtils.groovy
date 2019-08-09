package com.morpheusdata.util

// FIXME: should live in Core, need to convert to java
class MorpheusUtils {
    static formatDate(Object date, String outputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") {
        def rtn
        try {
            if(date) {
                if(date instanceof Date)
                    rtn = date.format(outputFormat, TimeZone.getTimeZone('GMT'))
                else if(date instanceof CharSequence)
                    rtn = date
            }
        } catch(ignored) { }
        return rtn
    }
}
