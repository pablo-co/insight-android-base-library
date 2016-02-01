package edu.mit.lastmite.insight_library.util;

import android.content.Context;
import android.location.LocationManager;
import android.os.Looper;

public class SysUtils {
    private SysUtils() {
    }

    public static boolean isMainThread() {
        return (Thread.currentThread() == Looper.getMainLooper().getThread());
    }

    public static boolean isGPSEnabled(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}