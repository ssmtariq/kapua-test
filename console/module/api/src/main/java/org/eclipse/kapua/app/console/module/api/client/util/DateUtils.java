/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;

import java.util.Date;

public class DateUtils {

    private static final String EEE_DD_MMM_YYYY_HH_MM_SS_ZZZZ = "EEE dd MMM yyyy HH:mm:ss ZZZZ";
    private static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy.MM.dd.HH.mm.ss.SSS";
    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private DateUtils() {
    }

    /**
     * formatDate takes a date an return its string representation with date and time
     */
    public static String formatDateTime(Date d) {
        return formatDateTime(d, TimeZone.createTimeZone(0));
    }

    public static String formatDateTime(Date d, TimeZone tz) {
        if (d == null) {
            return MSGS.dateTimeNone();
        }

        return DateTimeFormat.getFormat(EEE_DD_MMM_YYYY_HH_MM_SS_ZZZZ).format(d);
    }

    public static int getYear(Date date) {
        return Integer.parseInt(DateTimeFormat.getFormat("yyyy").format(date));
    }

    public static int getMonth(Date date) {
        // NB: The month in DateTimeFormat is not zero based [unlike Date() & Calendar()] so we need to subtract one when getting !!!
        return Integer.parseInt(DateTimeFormat.getFormat("MM").format(date)) - 1;
    }

    public static Date setYear(Date date, int year) {
        String dateString = DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(pad(Integer.toString(year), 4));
        sb.append(dateString.substring(4));

        return DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).parse(sb.toString());
    }

    public static Date setMonth(Date date, int month) {
        String dateString = DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(dateString.substring(0, 5));
        // NB: The month in DateTimeFormat is not zero based [unlike Date() & Calendar()] so we need to add one when setting !!!
        sb.append(pad(Integer.toString(month + 1), 2));
        sb.append(dateString.substring(7));

        return DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).parse(sb.toString());
    }

    public static Date setDayOfMonth(Date date, int dayOfMonth) {
        String dateString = DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(dateString.substring(0, 8));
        sb.append(pad(Integer.toString(dayOfMonth), 2));
        sb.append(dateString.substring(10));

        return DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).parse(sb.toString());
    }

    public static Date setToLastDayOfMonth(Date date) {
        String dateString = DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(dateString.substring(0, 8));

        switch (getMonth(date)) {
        case 0: // Jan
            sb.append("31");
            break;
        case 1: // Feb
            if (isLeapYear(getYear(date))) {
                sb.append("29");
            } else {
                sb.append("28");
            }
            break;
        case 2: // Mar
            sb.append("31");
            break;
        case 3: // Apr
            sb.append("30");
            break;
        case 4: // May
            sb.append("31");
            break;
        case 5: // June
            sb.append("30");
            break;
        case 6: // July
            sb.append("31");
            break;
        case 7: // August
            sb.append("31");
            break;
        case 8: // Sept
            sb.append("30");
            break;
        case 9: // Oct
            sb.append("31");
            break;
        case 10: // Nov
            sb.append("30");
            break;
        case 11: // Dec
            sb.append("31");
            break;
        default:
            break;
        }

        sb.append(dateString.substring(10));

        return DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).parse(sb.toString());
    }

    public static Date setHour(Date date, int hour) {
        String dateString = DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(dateString.substring(0, 11));
        sb.append(pad(Integer.toString(hour), 2));
        sb.append(dateString.substring(13));

        return DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).parse(sb.toString());
    }

    public static Date setMinute(Date date, int minute) {
        String dateString = DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(dateString.substring(0, 14));
        sb.append(pad(Integer.toString(minute), 2));
        sb.append(dateString.substring(16));

        return DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).parse(sb.toString());
    }

    public static Date setSecond(Date date, int second) {
        String dateString = DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(dateString.substring(0, 17));
        sb.append(pad(Integer.toString(second), 2));
        sb.append(dateString.substring(19));

        return DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).parse(sb.toString());
    }

    public static Date setMillisecond(Date date, int millisecond) {
        String dateString = DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(dateString.substring(0, 20));
        sb.append(pad(Integer.toString(millisecond), 3));

        return DateTimeFormat.getFormat(YYYY_MM_DD_HH_MM_SS_SSS).parse(sb.toString());
    }

    private static String pad(String data, int size) {
        if (data.length() < size) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < (size - data.length()); i++) {
                sb.append("0");
            }
            sb.append(data);
            return sb.toString();
        }

        return data;
    }

    private static boolean isLeapYear(int year) {
        return year % 4 == 0;
    }
}
