package edu.mit.lastmite.insight_library.util;

import android.text.TextUtils;

public final class StringUtils {

    private StringUtils() {
    }

    public static String excerpt(String s, int len) {
        if (TextUtils.isEmpty(s)) {
            return s;
        }
        return s.substring(0, Math.min(len, s.length()));
    }

    public static String secondsToString(long time) {
        long hours = time / 3600;
        long mins = time / 60;
        long secs = time % 60;

        String strHour = String.format("%02d", hours);
        String strMin = String.format("%02d", mins);
        String strSec = String.format("%02d", secs);
        return String.format("%s:%s:%s", strHour, strMin, strSec);
    }

    public static String getLatLngString(double latitude, double longitude) {
        return String.format("%f,%f", latitude, longitude);
    }
}