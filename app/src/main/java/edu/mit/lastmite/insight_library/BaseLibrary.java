package edu.mit.lastmite.insight_library;

import android.app.Application;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import edu.mit.lastmite.insight_library.report.Debug;
import edu.mit.lastmite.insight_library.report.SentryReporter;
import edu.mit.lastmite.insight_library.util.AppModule;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;
import edu.mit.lastmite.insight_library.util.DaggerApplicationComponent;

@ReportsCrashes(
        formKey = "",
        mode = ReportingInteractionMode.SILENT,
        logcatArguments = {"-t", "500", "-v", "time"}

)
public abstract class BaseLibrary extends Application {
    protected Debug mDebug;

    protected ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        createComponent();
        createDebugger();
    }

    protected void createComponent() {
        mComponent = DaggerApplicationComponent.builder()
                .appModule(new AppModule(this))
                .build();
        mComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        return mComponent;
    }

    private void createDebugger() {
        mDebug = new Debug(
                new SentryReporter(getString(R.string.sentry_api))
        );
        mDebug.onCreateApp(this);
    }
}