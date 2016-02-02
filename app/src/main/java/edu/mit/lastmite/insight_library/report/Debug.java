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

import org.slf4j.Logger;

/**
 * Wrapper utility around logging, analytics and crash reporting facilities.
 * <p>
 * Create yourself a static instance of this class in your project and call
 * {@link #onCreateApp(Application)}) from your Application's {@code onCreate()} method.
 * <p>
 * For example:
 * <p>
 * <code><pre>
 *
 * @ReportsCrashes(    formKey = "",
 * resToastText = R.string.crash_toast_text,
 * mode = ReportingInteractionMode.TOAST,
 * )
 * public class MyApp extends Application {
 * static public Debug debug = new Debug(
 * new GoogleAnalyticsReporter(),
 * new AcralyzerReporter( "https://my.server/", "user", "password" )
 * );
 * <p>
 * @Override
 * public void onCreate() {
 * super.onCreate();
 * debug.onCreateApp(this);
 * }
 * }
 * </pre></code>
 */
public class Debug {
    static final Logger LOG = Logging.getLogger(Debug.class);
    static Reporter[] mReporters;

    public Debug(Reporter... reporters) {
        mReporters = reporters;
    }

    public void onCreateApp(Application app) {
        Logging.initLogger(app);
        for (Reporter r : mReporters) {
            r.init(app);
        }
    }

    public void handleException(Throwable e) {
        for (Reporter r : mReporters) {
            r.handleException(e);
        }
    }

    public void handleSilentException(Throwable e) {
        for (Reporter r : mReporters) {
            r.handleSilentException(e);
        }
    }

    public void putCustomData(String key, String value) {
        for (Reporter r : mReporters) {
            r.putCustomData(key, value);
        }
    }

    public void onCreateActivity(Activity a) {
        for (Reporter r : mReporters) {
            r.onCreateActivity(a);
        }
    }

    public void onStartActivity(Activity a) {
        for (Reporter r : mReporters) {
            r.onStartActivity(a);
        }
    }

    public void onStopActivity(Activity a) {
        for (Reporter r : mReporters) {
            r.onStopActivity(a);
        }
    }

    public void onResumeActivity(Activity a) {
        for (Reporter r : mReporters) {
            r.onResumeActivity(a);
        }
    }

    public void onPauseActivity(Activity a) {
        for (Reporter r : mReporters) {
            r.onPauseActivity(a);
        }
    }

    public void toggleReports(Context context, boolean state) {
        for (Reporter r : mReporters) {
            r.toggleReports(context, state);
        }
    }

}