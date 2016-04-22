package edu.mit.lastmite.insight_library.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.clans.fab.Label;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;

import javax.inject.Inject;

import edu.mit.lastmite.insight_library.R;
import edu.mit.lastmite.insight_library.event.ClearMapEvent;
import edu.mit.lastmite.insight_library.event.ClearSectionTimerEvent;
import edu.mit.lastmite.insight_library.event.ClearTimerEvent;
import edu.mit.lastmite.insight_library.event.TimerEvent;
import edu.mit.lastmite.insight_library.model.Location;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;
import edu.mit.lastmite.insight_library.util.ColorTransformation;
import edu.mit.lastmite.insight_library.util.Helper;
import edu.mit.lastmite.insight_library.util.StringUtils;
import edu.mit.lastmite.insight_library.util.SysUtils;
import icepick.Icepick;

public class TrackFragment extends FragmentResponder {
    @SuppressWarnings("UnusedDeclaration")
    protected static final float OVERLAY_OPACITY = 0.35f;

    @SuppressWarnings("UnusedDeclaration")
    public static final int PANEL_DELAY = 30;

    @SuppressWarnings("UnusedDeclaration")
    public static final int PANEL_STATUS_HEIGHT = 48;

    @SuppressWarnings("UnusedDeclaration")
    public static final int PANEL_ACTION_HEIGHT = 98;

    @SuppressWarnings("UnusedDeclaration")
    public static final int PANEL_ACTION_WIDTH = 224;

    protected static final String KEY_STATE = "state";
    protected static final String KEY_LAST_STATE = "last_state";
    protected static final String KEY_WAITING_CALLBACK = "waiting_callback";

    public interface State {
    }

    @Inject
    protected Bus mBus;

    @Inject
    protected Helper mHelper;

    @icepick.State
    protected float mAcumDistance = 0.0f;

    @icepick.State
    protected Location mLastLocation;

    @icepick.State
    protected BigDecimal mAcumSpeed = new BigDecimal(0);

    @icepick.State
    protected BigDecimal mSpeedCount = new BigDecimal(0);

    protected State mLastState;
    protected State mState;

    protected long mSectionSeconds = 0;
    protected long mTotalSeconds = 0;

    protected WaitForLocationCallback mWaitForLocationCallback;

    protected TextView mTimeTextView;
    protected TextView mCompleteTimeTextView;
    protected TextView mDistanceTextView;
    protected TextView mSpeedTextView;
    protected TextView mAverageSpeedTextView;
    protected TextView mStateTextView;
    protected TextView mWaitingLocationTextView;
    protected TextView mPausedTextView;
    protected Button mVehicleButton;

    protected Button mStartButton;

    protected TableLayout mStatsLayout;
    protected FrameLayout mStatusLayout;
    protected FrameLayout mOverlayLayout;
    protected View mSpeedLayout;
    protected SlidingUpPanelLayout mSlidingUpPanel;

    protected AlertDialog mAlertDialog;

    @Override
    public void injectFragment(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkIfGpsEnabled();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void findTrackViews(View view) {
        findTextViews(view);
        findLayoutViews(view);
        findButtonViews(view);
    }

    protected void findTextViews(View view) {
        mTimeTextView = (TextView) view.findViewById(R.id.track_timeTextView);
        mCompleteTimeTextView = (TextView) view.findViewById(R.id.track_completeTimeTextView);
        mDistanceTextView = (TextView) view.findViewById(R.id.track_distanceTextView);
        mSpeedTextView = (TextView) view.findViewById(R.id.track_speedTextView);
        mAverageSpeedTextView = (TextView) view.findViewById(R.id.track_averageSpeedTextView);
        mStateTextView = (TextView) view.findViewById(R.id.track_stateTextView);
        mWaitingLocationTextView = (TextView) view.findViewById(R.id.track_waitingLocationTextView);
        mPausedTextView = (TextView) view.findViewById(R.id.track_pausedTextView);
    }

    protected void findLayoutViews(View view) {
        mStatsLayout = (TableLayout) view.findViewById(R.id.track_statsLayout);
        mStatusLayout = (FrameLayout) view.findViewById(R.id.track_statusLayout);
        mOverlayLayout = (FrameLayout) view.findViewById(R.id.track_overlayLayout);
        mSpeedLayout = view.findViewById(R.id.track_speedLayout);
    }

    protected void findButtonViews(View view) {
        mStartButton = (Button) view.findViewById(R.id.track_startButton);
        mVehicleButton = (Button) view.findViewById(R.id.track_vehicleButton);
    }

    protected void findPanelLayout(View view, int resourceId) {
        mSlidingUpPanel = (SlidingUpPanelLayout) view.findViewById(resourceId);
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

    /**
     * Views
     */

    @SuppressWarnings("UnusedDeclaration")
    protected void hideAllViews() {
        mStatsLayout.setVisibility(View.GONE);
        mStartButton.setVisibility(View.GONE);
        mVehicleButton.setVisibility(View.GONE);
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

    protected ArrayList<Label> getLabels(ViewGroup viewGroup) {
        ArrayList<Label> labels = new ArrayList<>();

        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                labels.addAll(getLabels((ViewGroup) child));
            }

            if (child.getClass() == Label.class) {
                labels.add((Label) child);
            }
        }

        return labels;
    }

    protected void showLabels() {
        ArrayList<Label> labels = getLabels((ViewGroup) getView());
        Iterator<Label> iterator = labels.iterator();
        while (iterator.hasNext()) {
            View view = iterator.next();
            view.setVisibility(View.VISIBLE);
        }
    }

    protected void hideLabels() {
        ArrayList<Label> labels = getLabels((ViewGroup) getView());
        Iterator<Label> iterator = labels.iterator();
        while (iterator.hasNext()) {
            View view = iterator.next();
            view.setVisibility(View.GONE);
        }
    }

    protected void hideSpeedKPIs() {
        mSpeedLayout.setVisibility(View.GONE);
    }

    /**
     * Animation
     */

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

    protected ValueAnimator createColorValueAnimator(int fromColor, int toColor) {
        return ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void animateViewColor(final View view, int fromColor, int toColor, long duration) {
        ValueAnimator colorAnimation = createColorAnimation(fromColor, toColor, duration);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }

    protected ValueAnimator createColorAnimation(int fromColor, int toColor, long duration) {
        ValueAnimator colorAnimation = createColorValueAnimator(fromColor, toColor);
        colorAnimation.setDuration(duration);
        return colorAnimation;
    }

    /**
     * Panel
     */

    @SuppressWarnings("UnusedDeclaration")
    @SuppressLint("NewApi")
    protected void showPanel(int delay) {
        mSlidingUpPanel.setTouchEnabled(true);
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN) {
            Choreographer.getInstance().postFrameCallbackDelayed(new Choreographer.FrameCallback() {
                @Override
                public void doFrame(long frameTimeNanos) {
                    mSlidingUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
            }, delay);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @SuppressLint("NewApi")
    protected void hidePanel(int delay) {
        mSlidingUpPanel.setTouchEnabled(false);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Choreographer.getInstance().postFrameCallbackDelayed(new Choreographer.FrameCallback() {
                @Override
                public void doFrame(long frameTimeNanos) {
                    mSlidingUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            }, delay);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void setPanelHeight(int value) {
        mSlidingUpPanel.setPanelHeight(value);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void setBottomPadding(View view, int space) {
        view.setPadding(
                view.getPaddingLeft(),
                view.getPaddingTop(),
                view.getPaddingRight(),
                space
        );
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void setLeftPadding(View view, int space) {
        view.setPadding(
                space,
                view.getPaddingTop(),
                view.getPaddingRight(),
                view.getPaddingBottom()
        );
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void setRightPadding(View view, int space) {
        view.setPadding(
                view.getPaddingLeft(),
                view.getPaddingTop(),
                space,
                view.getPaddingBottom()
        );
    }

    /**
     * Util
     */

    @SuppressWarnings("UnusedDeclaration")
    protected boolean isInPortrait() {
        int orientation = getResources().getConfiguration().orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                return true;
            case Configuration.ORIENTATION_LANDSCAPE:
                return false;
            default:
                return false;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void goToState(State state) {
        mLastState = mState;
        mState = state;
        updateStateView();
    }

    protected void updateStateView() {
        String label = "";
        mStateTextView.setText(label);
    }

    protected void checkIfGpsEnabled() {
        if (!SysUtils.isGPSEnabled(getContext())) {
            buildAlertMessageNoGps();
        }
    }

    protected void buildAlertMessageNoGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.dialog_gps_title))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.action_ok), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    /**
     * Stats
     */

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe
    public void onTimerEvent(TimerEvent event) {
        mTotalSeconds = event.getTotalSeconds();
        mSectionSeconds = event.getSectionSeconds();
        updateTime();
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

                /* Average speed */
                mAcumSpeed = mAcumSpeed.add(new BigDecimal(location.getSpeed()));
                mSpeedCount = mSpeedCount.add(new BigDecimal(1));

                updateStatsView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void updateStatsView() {
        String distance = mHelper.formatDouble(mAcumDistance);
        mDistanceTextView.setText(distance + " km");

        if (mLastLocation != null) {
            String speed = mHelper.formatDouble(mLastLocation.getSpeed());
            mSpeedTextView.setText(speed + " kph");
        }

        String averageSpeed = mHelper.formatDouble(mAcumSpeed.divide(mSpeedCount.max(new BigDecimal(1)), 2, RoundingMode.HALF_UP).doubleValue());
        mAverageSpeedTextView.setText(averageSpeed + " kph");
    }

    protected void updateTime() {
        mCompleteTimeTextView.setText(StringUtils.secondsToString(mTotalSeconds));
        mTimeTextView.setText(StringUtils.secondsToString(mSectionSeconds));
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void resetStats() {
        mAcumSpeed = new BigDecimal(0);
        mSpeedCount = new BigDecimal(0);
        mAcumDistance = 0;
        mSectionSeconds = 0;
        mTotalSeconds = 0;
        mBus.post(new ClearTimerEvent());
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void resetMap() {
        mBus.post(new ClearMapEvent());
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void resetSectionStats() {
        mSectionSeconds = 0;
        mBus.post(new ClearSectionTimerEvent());
    }

    @SuppressWarnings("UnusedDeclaration")
    public interface WaitForLocationCallback extends Serializable {
        void onReceivedLocation(Location location);
    }
}