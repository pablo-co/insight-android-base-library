package edu.mit.lastmite.insight_library;

import android.app.Application;

import edu.mit.lastmite.insight_library.util.AppModule;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;
import edu.mit.lastmite.insight_library.util.DaggerApplicationComponent;

public abstract class BaseLibrary extends Application {

    protected ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        createComponent();
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
}