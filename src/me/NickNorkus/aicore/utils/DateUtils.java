package me.NickNorkus.aicore.utils;

import java.util.*;
import java.text.*;

public class DateUtils
{
    public static String toString(final Date date, final String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static float nanosToSeconds(final float nanos) {
        return (float)(Math.round(nanos / 1.0E9f * 100.0) / 100.0);
    }

    public static float millisToSeconds(final float millis) {
        return (float)(Math.round(millis / 1000.0f * 100.0) / 100.0);
    }

    public static String toString(final long seconds, final String format) {
        final Date date = new Date(seconds * 1000L);
        return toString(date, format);
    }
}
