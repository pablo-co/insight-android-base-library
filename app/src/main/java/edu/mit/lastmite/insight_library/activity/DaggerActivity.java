package edu.mit.lastmite.insight_library.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.mit.lastmite.insight_library.BaseLibrary;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;


public abstract class DaggerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationComponent component = getComponent();
        injectActivity(component);
    }

    protected ApplicationComponent getComponent() {
        return ((BaseLibrary) getApplication()).getComponent();
    }

    public abstract void injectActivity(ApplicationComponent component);
}