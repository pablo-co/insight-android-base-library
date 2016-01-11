package edu.mit.lastmite.insight_library.task;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import com.squareup.tape.Task;

import javax.inject.Inject;

import edu.mit.lastmite.insight_library.http.APIFetch;
import edu.mit.lastmite.insight_library.util.Storage;

public abstract class NetworkTask implements Task<NetworkTask.Callback> {

    private static final Handler MAIN_THREAD = new Handler(Looper.getMainLooper());

    @Inject
    protected transient APIFetch mAPIFetch;

    @Inject
    protected transient Application mApplication;

    @Inject
    protected transient Storage mStorage;

    protected transient Callback mCallback;

    protected transient String mQueueName;

    @SuppressWarnings("UnusedDeclaration")
    protected void activateCallback(final boolean success) {
        if (success) {
            mCallback.onSuccess();
        } else {
            mCallback.onFailure();
        }
    }

    public void setQueueName(String queueName) {
        mQueueName = queueName;
    }

    public abstract Object getModel();

    public interface Callback {
        void onSuccess();

        void onFailure();
    }

    @SuppressWarnings("UnusedDeclaration")
    protected float getGlobalFloat(String key) {
        return mStorage.getGlobalFloat(key);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected float getLocalFloat(String key) {
        return mStorage.getLocalFloat(mQueueName, key);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void putGlobalFloat(String key, float value) {
        mStorage.putGlobalFloat(key, value);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void putLocalFloat(String key, float value) {
        mStorage.putLocalFloat(mQueueName, key, value);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected long getGlobalLong(String key) {
        return mStorage.getGlobalLong(key);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected long getLocalLong(String key) {
        return mStorage.getLocalLong(mQueueName, key);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void putGlobalLong(String key, long value) {
        mStorage.putGlobalLong(key, value);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void putLocalLong(String key, long value) {
        mStorage.putLocalLong(mQueueName, key, value);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected String getGlobalString(String key) {
        return mStorage.getGlobalString(key);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected String getLocalString(String key) {
        return mStorage.getLocalString(mQueueName, key);
    }
}