package edu.mit.lastmite.insight_library.queue;

/**
 * GRUPO RAIDO CONFIDENTIAL
 * __________________
 *
 * [2015] - [2015] Grupo Raido Incorporated
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains
 * the property of Grupo Raido SAPI de CV and its suppliers,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to Grupo Raido SAPI de CV and its
 * suppliers and may be covered by México and Foreign Patents,
 * patents in process, and are protected by trade secret or
 * copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless
 * prior written permission is obtained from Grupo Raido SAPI
 * de CV.
 */

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import edu.mit.lastmite.insight_library.BaseLibrary;
import edu.mit.lastmite.insight_library.service.DaggerService;
import edu.mit.lastmite.insight_library.task.NetworkTask;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;


public class NetworkQueueService extends DaggerService implements NetworkTask.Callback {
    private static final String TAG = "NetworkQueueService";

    @Inject
    NetworkTaskQueue queue;

    @Inject
    Bus bus;

    private boolean running;

    @Override
    public void injectService(ApplicationComponent component) {
        ((BaseLibrary) getApplicationContext()).getComponent().inject(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service starting!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "on Start!");
        //executeNext();
        return START_STICKY;
    }

    private void executeNext() {
        Log.i(TAG, "execute!");
        if (running) return; // Only one task at a time.

        NetworkTask task = queue.peek();
        if (task != null) {
            running = true;
            task.execute(this);
        } else {
            Log.i(TAG, "Service stopping!");
            stopSelf(); // No more tasks are present. Stop.
        }
    }

    @Override
    public void onSuccess() {
        running = false;
        queue.remove();
        bus.post(new NetworkSuccessEvent(1));
        executeNext();
    }

    @Override
    public void onFailure() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}