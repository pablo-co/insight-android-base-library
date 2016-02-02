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

import android.app.Application;
import android.content.Context;

import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.slf4j.Logger;

/**
 * Base class for reporters using ACRA. Takes care of all the ACRA stuff.
 * All the subclasses have to do is override \c finishConfig to set up
 * their specific configuration (e.g., set their sender or formUri).
 * <p>
 * Notes:
 * - Your application class requires the @ReportsCrashes annotation
 * in order for ACRA to initialize properly.
 */
public abstract class ACRAReporter extends Reporter {
    private static final Logger LOG = Logging.getLogger(ACRAReporter.class);

    public void init(Application app) {
        try {
            LOG.info("Initializing ACRA reporter");
            ACRA.init(app);
            ACRAConfiguration config = ACRA.getConfig();
            config.setApplicationLogFile(Logging.getLogFilePath());
            config.setApplicationLogFileLines(300);
            finishConfig(app, config);
        } catch (IllegalStateException e) {
            LOG.error("ACRA error: " + e.toString() + e.getStackTrace().toString());
        }
    }

    protected abstract void finishConfig(Application app, ACRAConfiguration config);

    @Override
    public void handleException(Throwable e) {
        ACRA.getErrorReporter().handleException(e);
    }

    @Override
    public void handleSilentException(Throwable e) {
        ACRA.getErrorReporter().handleSilentException(e);
    }

    @Override
    public void putCustomData(String key, String value) {
        ACRA.getErrorReporter().putCustomData(key, value);
    }

    @Override
    public void toggleReports(Context context, boolean state) {
        // Nothing do do, implemented implicitly by acra.enable preference.
    }
}