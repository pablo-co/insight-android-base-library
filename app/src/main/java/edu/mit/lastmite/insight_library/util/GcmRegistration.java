/**
 * GRUPO RAIDO CONFIDENTIAL
 * __________________
 *
 * [2015] - [2015] Grupo Raido SAPI de CV
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains
 * the property of Grupo Raido SAPI de CV and its suppliers,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to Grupo Raido SAPI de CV and its
 * suppliers and may be covered by México and Foreign Patents,
 * patents in process, and are protected by trade secret or
 * copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless
 * prior written permission is obtained from Grupo Raido SAPI
 * de CV.
 */

package edu.mit.lastmite.insight_library.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import edu.mit.lastmite.insight_library.model.Session;


public class GcmRegistration {

    private static final String TAG = "GcmRegistration";

    private static GcmRegistration sGcmRegistration;

    protected static final String PROPERTY_REG_ID = "registration_id";
    protected static final String PROPERTY_APP_VERSION = "app_version";
    protected static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    protected static final String SENDER_ID = "57776734450";

    protected GoogleCloudMessaging mGcm;
    protected Context mContext;
    protected Session mSession;

    public interface Callbacks {
        void onRegister(Session session);
    }

    public static GcmRegistration get(Context context) {
        if (sGcmRegistration == null) {
            sGcmRegistration = new GcmRegistration(context);
        }
        return sGcmRegistration;
    }

    GcmRegistration(Context context) {
        mContext = context.getApplicationContext();
        mSession = new Session();
    }

    /**
     * Register device id to back-end for GCM service
     */
    public void register(Callbacks callbacks, Activity activity) {
        if (checkPlayServices(activity)) {
            mGcm = GoogleCloudMessaging.getInstance(mContext);
            mSession.setPushId(getRegistrationId(mContext));

            if (mSession.getPushId() == null || mSession.getPushId().isEmpty()) {
                registerInBackground(callbacks);
            } else {
                callbacks.onRegister(mSession);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    /**
     * Check if device has a valid version of GooglePlayServices.
     *
     * @return
     */
    private boolean checkPlayServices(Activity activity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    /**
     * Register this device to the GCM service of our platform
     */
    private void registerInBackground(final Callbacks callbacks) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (mGcm == null) {
                        mGcm = GoogleCloudMessaging.getInstance(mContext);
                    }
                    mSession.setPushId(mGcm.register(SENDER_ID));
                    storeRegistrationId(mContext, mSession.getPushId());

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String params) {
                callbacks.onRegister(mSession);
            }
        }.execute(null, null, null);
    }

    /**
     * Get Session ID as long as the app has not been updated
     *
     * @param context
     * @return
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }


    /**
     * Save in the SharedPreferences the Session Registration Id.
     *
     * @param context
     * @param regId
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }


    /**
     * Get the App version
     *
     * @param context
     * @return
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @param context
     * @return
     */
    private SharedPreferences getGcmPreferences(Context context) {
        return context.getSharedPreferences(GcmRegistration.class.getSimpleName(), Context.MODE_PRIVATE);
    }


}
