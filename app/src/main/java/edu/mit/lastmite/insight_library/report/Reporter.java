/**
 *
 * GRUPO RAIDO CONFIDENTIAL
 * __________________
 *
 *  [2015] - [2015] Grupo Raido SAPI de CV
 *  All Rights Reserved.
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

package edu.mit.lastmite.insight_library.report;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * Base crash reporter interface.
 * <p/>
 * Notes:
 * - Not all reporters support the possibility to handle exceptions
 * either silently or not. ACRA does. Other reporters may implement
 * handleException and handleSilentException in exactly the same way.
 */
public abstract class Reporter {

    /** Mandatory methods */

    /**
     * Initialise the reporter and install the exception handler
     */
    public abstract void init(Application app);

    /**
     * Report a handled exception (same as ACRA)
     */
    public abstract void handleException(Throwable e);

    /**
     * Silently report a handled exception (same as ACRA)
     */
    public abstract void handleSilentException(Throwable e);

    /**
     * Insert/replace custom metadata that will be sent in the next report
     */
    public abstract void putCustomData(String key, String value);

    /**
     * Turn reporting on/off
     */
    public abstract void toggleReports(Context context, boolean state);

    /**
     * Optional methods.
     * <p/>
     * Some reporters add extra information in the crash report (e.g.,
     * activity stack) by having your apps call these methods.
     */

    public void onCreateActivity(Activity a) {
    }

    public void onStartActivity(Activity a) {
    }

    public void onStopActivity(Activity a) {
    }

    public void onResumeActivity(Activity a) {
    }

    public void onPauseActivity(Activity a) {
    }
}