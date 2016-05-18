package com.redhat.lightblue.client.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public final class ClientConstants {

    /**
     * String representation of the {@link DateFormat} used by lightblue.
     */
    public static final String LIGHTBLUE_DATE_FORMAT_STR = "yyyyMMdd'T'HH:mm:ss.SSSZ";

    /**
     * Contains the lightblue {@link DateFormat} for each Thread.
     */
    private static final ThreadLocal<DateFormat> DATE_FORMATS = new ThreadLocal<>();
    /**
     * It is faster to clone than to create new {@link DateFormat} instances.
     * This is the base instance from which others are cloned.
     */
    private static final DateFormat BASE_DATE_FORMAT = new SimpleDateFormat(LIGHTBLUE_DATE_FORMAT_STR);

    /**
     * Returns a {@link DateFormat} instance using the
     * {@link #LIGHTBLUE_DATE_FORMAT_STR}. Uses {@link ThreadLocal} under the
     * hood, because {@link SimpleDateFormat} is not thread safe
     */
    public static DateFormat getDateFormat() {
        if (DATE_FORMATS.get() == null) {
            DATE_FORMATS.set((DateFormat) BASE_DATE_FORMAT.clone());
        }
        return DATE_FORMATS.get();
    }

    private ClientConstants() {
    }

}
