package edu.mit.lastmite.insight_library.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;

public class Storage {

    @Inject
    protected transient Application mApplication;

    public Storage(Application application) {
        mApplication = application;
    }

    public String getLocalKey(String queueName, String key) {
        return String.format("%s_%s", queueName, key);
    }

    public SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mApplication);
    }

    @SuppressWarnings("UnusedDeclaration")
    public float getGlobalFloat(String key) {
        return getSharedPreferences().getFloat(key, -1.0f);
    }

    @SuppressWarnings("UnusedDeclaration")
    public float getLocalFloat(String queueName, String key) {
        return getSharedPreferences().getFloat(getLocalKey(queueName, key), -1.0f);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void putGlobalFloat(String key, float value) {
        getSharedPreferences().edit().putFloat(key, value).apply();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void putLocalFloat(String queueName, String key, float value) {
        getSharedPreferences().edit().putFloat(getLocalKey(queueName, key), value).apply();
    }

    @SuppressWarnings("UnusedDeclaration")
    public Integer getGlobalInteger(String key) {
        return getSharedPreferences().getInt(key, -1);
    }

    @SuppressWarnings("UnusedDeclaration")
    public Integer getLocalInteger(String queueName, String key) {
        return getSharedPreferences().getInt(getLocalKey(queueName, key), -1);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void putGlobalInteger(String key, Integer value) {
        getSharedPreferences().edit().putInt(key, value).apply();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void putLocalInteger(String queueName, String key, Integer value) {
        getSharedPreferences().edit().putInt(getLocalKey(queueName, key), value).apply();
    }

    @SuppressWarnings("UnusedDeclaration")
    public long getGlobalLong(String key) {
        return getSharedPreferences().getLong(key, -1);
    }

    @SuppressWarnings("UnusedDeclaration")
    public long getLocalLong(String queueName, String key) {
        return getSharedPreferences().getLong(getLocalKey(queueName, key), -1);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void putGlobalLong(String key, long value) {
        getSharedPreferences().edit().putLong(key, value).apply();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void putLocalLong(String queueName, String key, long value) {
        getSharedPreferences().edit().putLong(getLocalKey(queueName, key), value).apply();
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getGlobalString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getLocalString(String queueName, String key) {
        return getSharedPreferences().getString(getLocalKey(queueName, key), null);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void clear() {
        getSharedPreferences().edit().clear().apply();
    }
}
