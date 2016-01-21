package edu.mit.lastmite.insight_library.preferences;

import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import edu.mit.lastmite.insight_library.BaseLibrary;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;

/**
 * The OptionDialogPreference will display a dialog, and will persist the
 * <code>true</code> when pressing the positive button and <code>false</code>
 * otherwise. It will persist to the android:key specified in xml-preference.
 */
public abstract class DaggerDialogPreference extends DialogPreference {

    public DaggerDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        ApplicationComponent component = getComponent(context);
        injectPreference(component);
    }

    protected ApplicationComponent getComponent(Context context) {
        return ((BaseLibrary) context.getApplicationContext()).getComponent();
    }

    public abstract void injectPreference(ApplicationComponent component);
}