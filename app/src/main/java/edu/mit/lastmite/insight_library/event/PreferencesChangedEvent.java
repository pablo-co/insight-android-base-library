package edu.mit.lastmite.insight_library.event;

public class PreferencesChangedEvent {
    protected String mPreference;

    PreferencesChangedEvent(String preference) {
        mPreference = preference;
    }

    public String getPreference() {
        return mPreference;
    }
}
