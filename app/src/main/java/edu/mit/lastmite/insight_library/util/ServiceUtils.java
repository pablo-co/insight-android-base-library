package edu.mit.lastmite.insight_library.util;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import edu.mit.lastmite.insight_library.annotation.ServiceConstant;

@SuppressWarnings("UnusedDeclaration")
public class ServiceUtils {
    private static final String TAG = "ServiceUtils";

    @SuppressWarnings("UnusedDeclaration")
    public static void populateConstants(Class<?> klass) {
        String packageName = klass.getPackage().getName();
        for (Field field : klass.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) &&
                    field.isAnnotationPresent(ServiceConstant.class)) {
                String value = packageName + "." + field.getName();
                try {
                    field.set(null, value);
                    Log.i(TAG, "Setup service constant: " + value + "");
                } catch (IllegalAccessException iae) {
                    Log.e(TAG, "Unable to setup constant for field " +
                            field.getName() +
                            " in class " + klass.getName());
                }
            }
        }
    }
}
