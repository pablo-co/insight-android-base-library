package edu.mit.lastmite.insight_library.service;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import edu.mit.lastmite.insight_library.event.ClearSectionTimerEvent;
import edu.mit.lastmite.insight_library.event.ClearTimerEvent;
import edu.mit.lastmite.insight_library.event.PauseTimerEvent;
import edu.mit.lastmite.insight_library.event.StartTimerEvent;
import edu.mit.lastmite.insight_library.event.StopTimerEvent;
import edu.mit.lastmite.insight_library.event.TimerEvent;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;

public class TimerService extends DaggerService {
    public static final String TAG = "TimerService";

    public static final int TIMER_LENGTH = 1000;

    @Inject
    protected Bus mBus;

    protected long mSectionSeconds = 0;
    protected long mTotalSeconds = 0;
    protected boolean mPaused = false;

    protected CountDownTimer mCountDownTimer;

    @Override
    public void injectService(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBus.register(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Service starting!");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
        mBus.unregister(this);
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe
    public void onClearSectionEvent(ClearSectionTimerEvent event) {
        mSectionSeconds = 0;
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe
    public void onClearEvent(ClearTimerEvent event) {
        mSectionSeconds = 0;
        mTotalSeconds = 0;
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe
    public void onStartEvent(StartTimerEvent event) {
        if (mPaused) {
            mPaused = false;
            startTimer();
            publishTimerEvent();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe
    public void onPauseEvent(PauseTimerEvent event) {
        mPaused = true;
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe
    public void onStopEvent(StopTimerEvent event) {
        stopSelf();
    }

    protected void startTimer() {
        stopTimer();
        mCountDownTimer = new CountDownTimer(TIMER_LENGTH, TIMER_LENGTH) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                mTotalSeconds++;
                mSectionSeconds++;
                if (!mPaused) {
                    startTimer();
                    publishTimerEvent();
                }
            }
        };
        mCountDownTimer.start();
    }

    protected void stopTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    protected void publishTimerEvent() {
        TimerEvent event = new TimerEvent(mTotalSeconds, mSectionSeconds);
        mBus.post(event);
    }
}