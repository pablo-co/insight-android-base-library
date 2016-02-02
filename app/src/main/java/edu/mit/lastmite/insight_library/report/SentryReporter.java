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

import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.slf4j.Logger;

/**
 * Crash reporter using ACRA with the Sentry backend.
 * See https://www.getsentry.com/
 * <p/>
 * Note: the SentrySender used in this package is not the exact
 * SentrySender implementation submitted, in order to be able to
 * use the unmodified ACRA library. See https://github.com/dz0ny/acra
 * for the original SentrySender.
 */
public class SentryReporter extends ACRAReporter {

    static final Logger LOG = Logging.getLogger(SentryReporter.class);
    private String mURL = "";

    public SentryReporter(String url) {
        mURL = url;
    }

    @Override
    protected void finishConfig(Application app, ACRAConfiguration config) {
        LOG.info("Using Sentry backend");
        ACRA.getErrorReporter().setReportSender(new SentrySender(mURL));
    }

}
