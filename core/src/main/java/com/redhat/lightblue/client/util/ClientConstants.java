/*
 Copyright 2015 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.redhat.lightblue.client.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class ClientConstants {
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

}
