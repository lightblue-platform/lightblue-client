package com.redhat.lightblue.client.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public final class ClientConstants {
    private static final ThreadLocal<DateFormat> DATE_FORMATS = new ThreadLocal<>();
    public static final String DATE_FORMAT_STR = "yyyyMMdd'T'HH:mm:ss.SSSZ";

    /**
     * Returns a DateFormat instance using the DATE_FORMAT_STR. Uses ThreadLocal
     * under the hood, because SimpleDateFormat is not thread safe
     */
    public static DateFormat getDateFormat() {
        if (DATE_FORMATS.get() == null) {
            DATE_FORMATS.set(new SimpleDateFormat(DATE_FORMAT_STR));
        }
        return DATE_FORMATS.get();
    }

    private ClientConstants() {}

}
