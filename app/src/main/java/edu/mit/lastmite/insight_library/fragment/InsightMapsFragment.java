/*
 * [2015] - [2015] Grupo Raido SAPI de CV.
 * All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * Created by Pablo Cárdenas on 25/10/15.
 */

package edu.mit.lastmite.insight_library.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import edu.mit.lastmite.insight_library.R;
import edu.mit.lastmite.insight_library.annotation.ServiceConstant;
import edu.mit.lastmite.insight_library.event.ClearMapEvent;
import edu.mit.lastmite.insight_library.event.TrackEvent;
import edu.mit.lastmite.insight_library.model.Location;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;
import edu.mit.lastmite.insight_library.util.Helper;
import edu.mit.lastmite.insight_library.util.ServiceUtils;
import edu.mit.lastmite.insight_library.util.ViewUtils;
import edu.mit.lastmite.insight_library.view.TouchFrameLayout;

public class InsightMapsFragment extends BaseFragment implements SensorEventListener, GoogleMap.OnCameraChangeListener {
    @ServiceConstant
    public static String EXTRA_FLAGS;

    public static final float MAP_TILT = 45.0f;
    public static final int DISABLED_COLOR = Color.rgb(100, 100, 100);
    public static final int HIGHLIGHT_COLOR = Color.rgb(40, 111, 181);

    static {
        ServiceUtils.populateConstants(InsightMapsFragment.class);
    }

    @Inject
    protected Helper mHelper;

    protected int mFlags = 0;
    protected float mAngleOffset = 0.0f;
    protected float mCurrentAngle = 0.0f;
    protected boolean mSensorRotate = false;

    protected GoogleMap mGoogleMap;

    protected Location mLastLocation;
    protected LatLng mMapFocus;
    protected boolean mAutomaticFocus = true;
    protected State mState = State.CURRENT_LOCATION;

    protected ArrayList<LatLng> mTrackingPoints;
    protected SensorManager mSensorManager;

    protected MapView mMapView;
    protected ArrayList<Marker> mMarkers;
    protected FloatingActionButton mActionButton;
    protected TouchFrameLayout mTouchLayout;

    public class Flags {
        public static final int DRAW_PATH = 1;
        public static final int DRAW_MARKER = 2;
        public static final int DRAW_TRACKS = 4;
        public static final int ROTATE_WITH_DEVICE = 8;
    }

    public enum State {
        CURRENT_LOCATION,
        ROTATION,
        FREE
    }

    public static InsightMapsFragment newInstance(int flags) {
        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_FLAGS, flags);

        InsightMapsFragment fragment = new InsightMapsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void injectFragment(ApplicationComponent component) {
        super.injectFragment(component);
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mFlags = getArguments().getInt(EXTRA_FLAGS);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mTrackingPoints = new ArrayList<>();
        mMarkers = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insight_maps, container, false);

        mMapView = (MapView) view.findViewById(R.id.maps_mapView);
        mMapView.onCreate(savedInstanceState);
        setUpMap();

        mTouchLayout = (TouchFrameLayout) view.findViewById(R.id.touchLayout);

        mTouchLayout.setOnDragListener(new TouchFrameLayout.OnDragListener() {
            @Override
            public void onDrag(MotionEvent motionEvent) {
                mState = State.FREE;
                applyLocationPolicy();
            }
        });

        mActionButton = (FloatingActionButton) view.findViewById(R.id.actionButton);
        if (shouldShowActionButton()) {
            mActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchLocationPolicy();
                }
            });
        } else {
            mActionButton.setVisibility(View.GONE);
        }

        if (shouldRotateWithDevice()) {
            applySensorRotation();
        }

        changeGestures(!mAutomaticFocus);
        applyLocationPolicy();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onCameraChange(CameraPosition position) {
    }

    protected void registerSensor() {
        mSensorManager.registerListener(
                this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    protected void unregisterSensor() {
        mSensorManager.unregisterListener(this);
    }

    protected void switchSensorRotation(boolean shouldRotate) {
        mSensorRotate = shouldRotate;
        applySensorRotation();
    }

    protected void applySensorRotation() {
        if (mSensorRotate) {
            registerSensor();
            saveMapFocus();
        } else {
            unregisterSensor();
        }
    }

    protected void switchLocationPolicy() {
        switch (mState) {
            case CURRENT_LOCATION:
                mState = State.ROTATION;
                break;
            case ROTATION:
                mState = State.CURRENT_LOCATION;
                break;
            case FREE:
                mState = State.CURRENT_LOCATION;
                break;
        }
        applyLocationPolicy();
    }

    protected void applyLocationPolicy() {
        switch (mState) {
            case CURRENT_LOCATION:
                stopRotation();
                startCurrentLocation();
                break;
            case ROTATION:
                startRotation();
                break;
            case FREE:
                stopRotation();
                startFree();
                break;
        }
    }

    protected void startCurrentLocation() {
        ViewUtils.changeDrawableColor(getContext(), R.mipmap.ic_location_target, HIGHLIGHT_COLOR, mActionButton);
        centerToLastLocation();
        mAutomaticFocus = true;
    }

    protected void startRotation() {
        ViewUtils.changeDrawableColor(getContext(), R.mipmap.ic_compass, HIGHLIGHT_COLOR, mActionButton);
        switchSensorRotation(true);
        mAutomaticFocus = true;
    }

    protected void stopRotation() {
        switchSensorRotation(false);
    }

    protected void startFree() {
        mAutomaticFocus = false;
        ViewUtils.changeDrawableColor(getContext(), R.mipmap.ic_location_target, DISABLED_COLOR, mActionButton);
    }

    protected void centerToLastLocation() {
        if (mLastLocation != null) {
            centerZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 18);
        }
    }

    protected void changeGestures(boolean enabled) {
        //mGoogleMap.getUiSettings().setAllGesturesEnabled(enabled);
    }

    protected void saveMapFocus() {
        CameraPosition pos = mGoogleMap.getCameraPosition();
        mMapFocus = pos.target;
    }

    protected void setUpMap() {
        if (mGoogleMap == null) {
            mGoogleMap = mMapView.getMap();
            mGoogleMap.setOnCameraChangeListener(this);
        }
    }

    /**
     * Flags
     */

    protected boolean shouldShowActionButton() {
        return shouldDrawMarker() | shouldDrawPath() | shouldRotateWithDevice();
    }

    protected boolean shouldRotateWithDevice() {
        return mHelper.isFlagActivated(mFlags, Flags.ROTATE_WITH_DEVICE);
    }

    protected boolean shouldDrawMarker() {
        return mHelper.isFlagActivated(mFlags, Flags.DRAW_MARKER);
    }

    protected boolean shouldDrawPath() {
        return mHelper.isFlagActivated(mFlags, Flags.DRAW_PATH);
    }

    protected boolean shouldDrawTracks() {
        return mHelper.isFlagActivated(mFlags, Flags.DRAW_TRACKS);
    }


    /**
     * Sensor manager
     */

    @Override
    public void onSensorChanged(SensorEvent event) {
        CameraPosition position = mGoogleMap.getCameraPosition();
        mAngleOffset = position.bearing - mCurrentAngle;
        mCurrentAngle = Math.round(event.values[0]);
        updateCamera(mCurrentAngle + mAngleOffset);
    }

    protected void updateCamera(float bearing) {
        if (mLastLocation != null) {
            CameraPosition oldPos = mGoogleMap.getCameraPosition();
            CameraPosition pos = CameraPosition.builder(oldPos)
                    .tilt(MAP_TILT)
                    .bearing(bearing)
                    .target(mMapFocus)
                    .build();
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe
    public void onLocationEvent(Location location) {
        mLastLocation = location;

        if (shouldDrawMarker() || shouldDrawPath()) {
            clearMap();
        }

        if (shouldDrawMarker()) {
            addMarkerToMap(location.getLatitude(), location.getLongitude());
            if (mAutomaticFocus) {
                centerToLastLocation();
            }
        }

        if (shouldDrawPath()) {
            drawPath();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe
    public void onTrackEvent(TrackEvent trackEvent) {
        if (shouldDrawTracks()) {
            clearMap();
            List<LatLng> locations = trackEvent.getLocations();
            if (locations.size() > 0) {
                addMarkerToMap(locations.get(0));
                addMarkerToMap(locations.get(locations.size() - 1));
                resizeToMarkers(mMarkers);
                drawPathFromLatLngs(trackEvent.getLocations());
            }
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe
    public void onClearMapEvent(ClearMapEvent event) {
        clearMap();
    }

    protected void removeMarker() {
        mGoogleMap.clear();
    }

    protected void addMarkerToMap(double latitude, double longitude) {
        addMarkerToMap(new LatLng(latitude, longitude));
    }

    protected void addMarkerToMap(LatLng latLng) {
        mTrackingPoints.add(latLng);
        removeMarker();

        MarkerOptions options = new MarkerOptions().position(latLng);
        mMarkers.add(mGoogleMap.addMarker(options));
    }

    protected void resizeToMarkers(ArrayList<Marker> markers) {
        if (markers.size() == 0) return;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), mHelper.dpToPx(10));
        mGoogleMap.animateCamera(cu);
    }

    protected void centerZoom(LatLng latLng, int zoom) {
        CameraUpdate zoomUpdate = CameraUpdateFactory.zoomTo(zoom);
        mGoogleMap.moveCamera(zoomUpdate);
        CameraUpdate cu = CameraUpdateFactory.newLatLng(latLng);
        mGoogleMap.moveCamera(cu);
    }

    protected void drawPath() {
        drawPathFromLatLngs(mTrackingPoints);
    }

    protected void drawPathFromLatLngs(ArrayList<LatLng> latLngs) {
        mGoogleMap.addPolyline(new PolylineOptions()
                .addAll(latLngs)
                .width(5)
                .color(getResources().getColor(R.color.colorAccent)));
    }

    protected void clearMap() {
        mTrackingPoints = new ArrayList<>();
        mMarkers = new ArrayList<>();
        mGoogleMap.clear();
    }
}
