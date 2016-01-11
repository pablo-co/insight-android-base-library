package edu.mit.lastmite.insight_library.service;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.otto.Bus;

import java.text.ParseException;

import javax.inject.Inject;

import edu.mit.lastmite.insight_library.BaseLibrary;
import edu.mit.lastmite.insight_library.error.NotImplementedError;
import edu.mit.lastmite.insight_library.error.ParseError;
import edu.mit.lastmite.insight_library.event.FinishProgressEvent;
import edu.mit.lastmite.insight_library.event.ProgressEvent;
import edu.mit.lastmite.insight_library.queue.GsonConverter;
import edu.mit.lastmite.insight_library.queue.NetworkSuccessEvent;
import edu.mit.lastmite.insight_library.queue.NetworkTaskQueue;
import edu.mit.lastmite.insight_library.task.NetworkTask;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;


public class NetworkQueueService extends DaggerIntentService implements NetworkTask.Callback {
    private static final String TAG = "NetworkQueueService";

    public static final String EXTRA_QUEUE_NAME = "edu.mit.lastmite.insight_library.queue_name";

    protected static final int SLEEP_TIME = 5000;

    @Inject
    protected Gson mGson;

    @Inject
    protected Bus mBus;

    protected NetworkTaskQueue mQueue;
    protected int mStartingSize;
    protected boolean mRunning;

    @Override
    public void injectService(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Service starting!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String queueName = getQueueName(intent);
        mQueue = createQueue(queueName);
        init();
        return START_STICKY;
    }

    protected void init() {
        mStartingSize = mQueue.size();
        executeNext();
    }

    /**
     * Create a new queue for processing.
     * <b>Warning</b> You should obtain a reference to an active queue through
     * dependency injection or other equivalent method. Otherwise there will
     * be multiple instances of the queue in memory. This will cause file corruption.
     */
    protected NetworkTaskQueue createQueue(String queueName) {
        throw new NotImplementedError("You should obtain a reference to the processing queue." +
                "(Warning: Don't create a new queue, this would cause file corruption.)");
    }

    protected String getQueueName(Intent intent) {
        String queueName = intent.getStringExtra(EXTRA_QUEUE_NAME);
        if (queueName == null) {
            throw new RuntimeException("No queue name was specified.");
        }
        return queueName;
    }

    protected void executeNext() {
        if (mRunning) return;

        NetworkTask task = null;
        try {
            task = mQueue.peek();
        } catch (ParseError e) {
            e.printStackTrace();
            mQueue.remove();
        }
        if (task != null) {
            mRunning = true;
            task.setQueueName(mQueue.getFileName());
            ((BaseLibrary) getApplication()).getComponent().inject(task);
            task.execute(this);
        } else {
            publishFinishProgress();
            stopSelf();
        }
    }

    @Override
    public void onSuccess() {
        mQueue.remove();
        publishNetworkSuccess();
        publishProgress();
        mRunning = false;
        executeNext();
    }

    @Override
    public void onFailure() {
        mRunning = false;
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                executeNext();
            }
        };
        handler.postDelayed(runnable, SLEEP_TIME);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void publishNetworkSuccess() {
        mBus.post(new NetworkSuccessEvent(1));
    }

    protected float getProgress() {
        float itemsLeft = (float) mStartingSize - mQueue.size();
        return itemsLeft / mStartingSize;
    }

    protected void publishProgress() {
        mBus.post(new ProgressEvent(mQueue.getFileName(), getProgress()));
    }

    protected void publishFinishProgress() {
        mBus.post(new FinishProgressEvent(mQueue.getFileName()));
    }
}