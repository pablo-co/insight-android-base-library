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
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
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
import edu.mit.lastmite.insight_library.event.ClearMapEvent;
import edu.mit.lastmite.insight_library.event.TrackEvent;
import edu.mit.lastmite.insight_library.model.Location;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;
import edu.mit.lastmite.insight_library.util.Helper;

public class InsightMapsFragment extends BaseFragment implements SensorEventListener, GoogleMap.OnCameraChangeListener {

    public static final String EXTRA_FLAGS = "edu.mit.lastmite.insight_library.extra_flags";

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

    protected ArrayList<LatLng> mTrackingPoints;
    protected SensorManager mSensorManager;

    protected MapView mMapView;
    protected ArrayList<Marker> mMarkers;
    protected FloatingActionButton mLocationButton;
    protected FloatingActionButton mRotationButton;

    public class Flags {
        public static final int DRAW_PATH = 1;
        public static final int DRAW_MARKER = 2;
        public static final int DRAW_TRACKS = 4;
        public static final int ROTATE_WITH_DEVICE = 8;
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

        mLocationButton = (FloatingActionButton) view.findViewById(R.id.locationButton);
        if (shouldDrawPath() || shouldDrawMarker()) {
            mLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchLocationPolicy();
                }
            });
        } else {
            mLocationButton.setVisibility(View.GONE);
        }

        mRotationButton = (FloatingActionButton) view.findViewById(R.id.rotationButton);
        if (shouldRotateWithDevice()) {
            applySensorRotation();
            mRotationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchSensorRotation();
                }
            });
        } else {
            mRotationButton.setVisibility(View.GONE);
        }

        changeGestures(!mAutomaticFocus);

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
        //mAutomaticFocus = false;
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

    protected void switchSensorRotation() {
        mSensorRotate = !mSensorRotate;
        applySensorRotation();
    }

    protected void applySensorRotation() {
        if (mSensorRotate) {
            setFabDrawable();
            registerSensor();
            saveMapFocus();
        } else {
            setFabDarkenedDrawable();
            unregisterSensor();
        }
    }

    protected void switchLocationPolicy() {
        //mAutomaticFocus = true;
        mAutomaticFocus = !mAutomaticFocus;
        changeGestures(!mAutomaticFocus);
        centerToLastLocation();
    }

    protected void centerToLastLocation() {
        if (mLastLocation != null) {
            centerZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 18);
        }
    }

    protected void changeGestures(boolean enabled) {
        mGoogleMap.getUiSettings().setAllGesturesEnabled(enabled);
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

    /* Views */
    protected void setFabDrawable() {
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_compass);
        drawable.setAlpha(255);
        mRotationButton.setImageDrawable(drawable);
    }

    protected void setFabDarkenedDrawable() {
        mRotationButton.setImageDrawable(getDarkenedDrawable(R.mipmap.ic_compass));
    }

    protected Drawable getDarkenedDrawable(int id) {
        Drawable drawable = getResources().getDrawable(id);
        drawable.setAlpha(200);
        return drawable;
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
        mGoogleMap.clear();
    }
}
