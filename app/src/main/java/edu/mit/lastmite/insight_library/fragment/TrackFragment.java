package edu.mit.lastmite.insight_library.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.inject.Inject;

import edu.mit.lastmite.insight_library.R;
import edu.mit.lastmite.insight_library.http.APIFetch;
import edu.mit.lastmite.insight_library.model.Location;
import edu.mit.lastmite.insight_library.queue.NetworkTaskQueue;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;
import edu.mit.lastmite.insight_library.util.Helper;

public class TrackFragment extends FragmentResponder {

    @SuppressWarnings("UnusedDeclaration")
    protected static final int TIMER_LENGTH = 1000;

    @SuppressWarnings("UnusedDeclaration")
    protected static final float OVERLAY_OPACITY = 0.35f;

    public interface State {
    }

    @Inject
    protected Bus mBus;

    @Inject
    protected Helper mHelper;

    protected CountDownTimer mCountDownTimer;
    protected int times = 0;
    protected float mAcumDistance = 0.0f;

    protected State mState;
    protected Location mLastLocation;
    protected BigDecimal mAcumSpeed = new BigDecimal(0);
    protected BigDecimal mSpeedCount = new BigDecimal(0);

    protected TextView mTimeTextView;
    protected TextView mDistanceTextView;
    protected TextView mSpeedTextView;
    protected TextView mAverageSpeedTextView;
    protected TextView mStateTextView;
    protected TextView mWaitingLocationTextView;

    protected Button mStartButton;

    protected TableLayout mStatsLayout;
    protected FrameLayout mOverlayLayout;

    @Override
    public void injectFragment(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBus.register(this);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void findTrackViews(View view) {
        findTextViews(view);
        findLayoutViews(view);
        findButtonViews(view);
    }

    protected void findTextViews(View view) {
        mDistanceTextView = (TextView) view.findViewById(R.id.track_distanceTextView);
        mTimeTextView = (TextView) view.findViewById(R.id.track_timeTextView);
        mSpeedTextView = (TextView) view.findViewById(R.id.track_speedTextView);
        mAverageSpeedTextView = (TextView) view.findViewById(R.id.track_averageSpeedTextView);
        mStateTextView = (TextView) view.findViewById(R.id.track_stateTextView);
        mWaitingLocationTextView = (TextView) view.findViewById(R.id.track_waitingLocationTextView);
    }

    protected void findLayoutViews(View view) {
        mStatsLayout = (TableLayout) view.findViewById(R.id.track_statsLayout);
        mOverlayLayout = (FrameLayout) view.findViewById(R.id.track_overlayLayout);
    }

    protected void findButtonViews(View view) {
        mStartButton = (Button) view.findViewById(R.id.track_startButton);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void inflateMapsFragment(int layout) {
        mHelper.inflateFragment(getChildFragmentManager(), layout, new Helper.FragmentCreator() {
            @Override
            public Fragment createFragment() {
                return InsightMapsFragment.newInstance(InsightMapsFragment.Flags.DRAW_MARKER |
                        InsightMapsFragment.Flags.DRAW_PATH |
                        InsightMapsFragment.Flags.ROTATE_WITH_DEVICE);
            }
        }, R.animator.no_animation, R.animator.no_animation);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void hideAllViews() {
        mStatsLayout.setVisibility(View.GONE);
        mStartButton.setVisibility(View.GONE);
        mTimeTextView.setVisibility(View.GONE);
        mWaitingLocationTextView.setVisibility(View.GONE);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void showIdleView() {
        mStartButton.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void showWaitingLocationView() {
        mWaitingLocationTextView.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected Animation createFadeInAnimation(int duration) {
        Animation fadeIn = new AlphaAnimation(OVERLAY_OPACITY, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(duration);

        return fadeIn;
    }

    @SuppressWarnings("UnusedDeclaration")
    protected Animation createFadeOutAnimation(int duration) {
        Animation fadeOut = new AlphaAnimation(1, OVERLAY_OPACITY);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(duration);

        return fadeOut;
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void goToState(State state) {
        mState = state;
        updateStateView();
    }

    protected void updateStateView() {
        String label = "";
        mStateTextView.setText(label);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void updateStats(Location location) {
        if (mLastLocation != null) {
            try {
                android.location.Location lastLocation = new android.location.Location("");
                lastLocation.setLatitude(mLastLocation.getLatitude());
                lastLocation.setLongitude(mLastLocation.getLongitude());

                android.location.Location newLocation = new android.location.Location("");
                newLocation.setLatitude(location.getLatitude());
                newLocation.setLongitude(location.getLongitude());

                /* Calculate distance */
                float distanceInMeters = lastLocation.distanceTo(newLocation);
                mAcumDistance += distanceInMeters / 1000.0f;
                String distance = mHelper.formatDouble(mAcumDistance);
                mDistanceTextView.setText(distance + " km");

                /* Calculate speed */
                String speed = mHelper.formatDouble(location.getSpeed());
                mSpeedTextView.setText(speed + " kph");

                /* Averge speed */
                mAcumSpeed = mAcumSpeed.add(new BigDecimal(location.getSpeed()));
                mSpeedCount = mSpeedCount.add(new BigDecimal(1));
                String averageSpeed = mHelper.formatDouble(mAcumSpeed.divide(mSpeedCount, 2, RoundingMode.HALF_UP).doubleValue());
                mAverageSpeedTextView.setText(averageSpeed + " kph");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void updateTime() {
        mTimeTextView.setText(secondsToString(times));
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void resetStats() {
        mAcumSpeed = new BigDecimal(0);
        mSpeedCount = new BigDecimal(0);
        mAcumDistance = 0;
        times = 0;
    }

    private String secondsToString(int time) {
        int mins = time / 60;
        int secs = time % 60;

        String strMin = String.format("%02d", mins);
        String strSec = String.format("%02d", secs);
        return String.format("%s:%s", strMin, strSec);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void startTimer(final int time) {
        stopTimer();
        mCountDownTimer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                times++;
                updateTime();
                startTimer(time);
            }
        };
        mCountDownTimer.start();
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void stopTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
}