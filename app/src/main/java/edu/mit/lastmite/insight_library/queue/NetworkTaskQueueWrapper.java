package edu.mit.lastmite.insight_library.queue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.squareup.otto.Bus;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import edu.mit.lastmite.insight_library.service.NetworkQueueService;
import edu.mit.lastmite.insight_library.task.NetworkTask;

public class NetworkTaskQueueWrapper {

    private static final String PREFERENCES_QUEUE_NAMES = "queue_names";

    protected final Context mContext;
    protected final Bus mBus;
    protected final Gson mGson;
    protected ArrayList<String> mQueueNames;
    protected NetworkTaskQueue mQueue;

    public NetworkTaskQueueWrapper(Context context, Gson gson, Bus bus) {
        mContext = context;
        mBus = bus;
        mGson = gson;
        mQueueNames = loadQueueNames();
    }

    public void addTask(NetworkTask entry) {
        if (mQueue == null) {
            throw new IllegalStateException("You need to specify a queue before adding a task.");
        }
        mQueue.add(entry);
    }

    public void changeToNewQueue() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String fileName = formatter.format(new Date());
        changeToQueue(fileName);
        addQueueName(fileName);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void changeOrCreateIfNoQueue() {
        if (mQueue == null) {
            changeToLastOrNewQueue();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void changeToLastOrNewQueue() {
        String queueName = getLastQueueName();
        if (queueName != null) {
            changeToQueue(queueName);
        } else {
            changeToNewQueue();
        }
    }

    public void changeToQueue(String queueName) {
        mQueue = retrieveQueue(queueName);
    }

    @SuppressWarnings("UnusedDeclaration")
    public ArrayList<String> getQueueNames() {
        return mQueueNames;
    }

    @SuppressWarnings("UnusedDeclaration")
    public NetworkTaskQueue getQueue() {
        return mQueue;
    }

    public void executeQueue(String queueName) {
        launchQueueService(queueName);
    }

    @SuppressWarnings("UnusedDeclaration")
    public NetworkTaskQueue destroyQueue(String queueName) {
        NetworkTaskQueue queue = retrieveQueue(queueName);
        queue.remove();
        removeQueueName(queueName);
        return queue;
    }

    protected void launchQueueService(String queueName) {
        Intent intent = new Intent(mContext, NetworkQueueService.class);
        intent.putExtra(NetworkQueueService.EXTRA_QUEUE_NAME, queueName);
        mContext.startService(intent);
    }

    protected String getLastQueueName() {
        if (mQueueNames.isEmpty()) {
            return null;
        }

        Iterator<String> iterator = mQueueNames.iterator();
        String lastElement = iterator.next();
        while (iterator.hasNext()) {
            lastElement = iterator.next();
        }
        return lastElement;
    }

    protected NetworkTaskQueue retrieveQueue(String queueName) {
        return NetworkTaskQueue.createWithoutProcessing(mContext, mGson, mBus, queueName);
    }

    protected void addQueueName(String queueName) {
        mQueueNames.add(queueName);
        saveQueueNames();
    }

    protected void removeQueueName(String queueName) {
        mQueueNames.remove(queueName);
        saveQueueNames();
    }

    protected ArrayList<String> loadQueueNames() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String json = sharedPreferences.getString(PREFERENCES_QUEUE_NAMES, null);
        ArrayList<String> queueNames = new ArrayList<>();
        if (json != null) {
            Type type = $Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, String.class);
            queueNames = mGson.fromJson(json, type);
        }
        return queueNames;
    }

    protected void saveQueueNames() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        sharedPreferences.edit().putString(PREFERENCES_QUEUE_NAMES, mGson.toJson(mQueueNames)).apply();
    }
}
