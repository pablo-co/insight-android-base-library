package edu.mit.lastmite.insight_library.fragment;


import android.os.Bundle;

import com.rey.material.app.DialogFragment;

import edu.mit.lastmite.insight_library.BaseLibrary;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;

public abstract class DaggerDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationComponent component = getComponent();
        injectFragment(component);
    }

    protected ApplicationComponent getComponent() {
        return ((BaseLibrary) getActivity().getApplicationContext()).getComponent();
    }

    public abstract void injectFragment(ApplicationComponent component);
}