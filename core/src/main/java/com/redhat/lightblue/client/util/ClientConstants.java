package com.redhat.lightblue.client.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public final class ClientConstants {
    private static final DateFormat DATE_FORMAT;
    private static final String DATE_FORMAT_STR = "yyyyMMdd'T'HH:mm:ss.SSSZ";

    static {
        DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STR);
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * Returns a DateFormat instance using the DATE_FORMAT_STR in GMT. Clone of
     * the static internal variable, because SimpleDateFormat is not thread safe
     */
    public static DateFormat getDateFormat() {
        return (DateFormat) DATE_FORMAT.clone();
    }

    private ClientConstants() {}

}
