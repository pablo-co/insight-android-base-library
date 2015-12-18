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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import edu.mit.lastmite.insight_library.R;
import edu.mit.lastmite.insight_library.model.Location;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;
import edu.mit.lastmite.insight_library.util.Helper;

public class InsightMapsFragment extends DaggerFragment implements SensorEventListener {

    public static final String EXTRA_FLAGS = "edu.mit.lastmite.insight_library.extra_flags";

    protected int mFlags = 0;
    protected float mAngleOffset = 0.0f;
    protected float mCurrentAngle = 0.0f;

    protected GoogleMap mGoogleMap;

    protected Location mLastLocation;

    protected List<LatLng> mTrackingPoints;
    protected SensorManager mSensorManager;

    @Inject
    protected Bus mBus;

    protected MapView mMapView;

    public class Flags {
        public static final int DRAW_PATH = 1;
        public static final int DRAW_MARKER = 2;
        public static final int ROTATE_WITH_DEVICE = 4;
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
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mBus.register(this);
        mFlags = getArguments().getInt(EXTRA_FLAGS);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mTrackingPoints = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = (MapView) view.findViewById(R.id.maps_mapView);
        mMapView.onCreate(savedInstanceState);
        setUpMap();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (shouldRotateWithDevice() && mSensorManager != null) {
            mSensorManager.registerListener(
                    this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        if (shouldRotateWithDevice()) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onDestroy() {
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        super.onDestroy();
    }

    protected void setUpMap() {
        if (mGoogleMap == null) {
            mGoogleMap = mMapView.getMap();
        }
    }

    /**
     * Flags
     */

    protected boolean shouldRotateWithDevice() {
        return Helper.get(getContext()).isFlagActivated(mFlags, Flags.ROTATE_WITH_DEVICE);
    }

    protected boolean shouldDrawMarker() {
        return Helper.get(getContext()).isFlagActivated(mFlags, Flags.DRAW_MARKER);
    }

    protected boolean shouldDrawPath() {
        return Helper.get(getContext()).isFlagActivated(mFlags, Flags.DRAW_PATH);
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
            CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing).target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).build();
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Subscribe
    public void onLocationEvent(Location location) {
        mLastLocation = location;

        if (shouldDrawMarker()) {
            addMarkerToMap(location.getLatitude(), location.getLongitude());
        }

        if (shouldDrawPath()) {
            drawPath();
        }
    }

    protected void removeMarker() {
        mGoogleMap.clear();
    }

    protected void addMarkerToMap(double latitude, double longitude) {
        mTrackingPoints.add(new LatLng(latitude, longitude));
        removeMarker();

        MarkerOptions options = new MarkerOptions().position(new LatLng(latitude, longitude));
        mGoogleMap.addMarker(options);
        centerZoom(new LatLng(latitude, longitude), 18);
    }

    protected void centerZoom(LatLng latLng, int zoom) {
        CameraUpdate zoomUpdate = CameraUpdateFactory.zoomTo(zoom);
        mGoogleMap.moveCamera(zoomUpdate);
        CameraUpdate cu = CameraUpdateFactory.newLatLng(latLng);
        mGoogleMap.moveCamera(cu);
    }

    protected void drawPath() {
        mGoogleMap.addPolyline(new PolylineOptions()
                .addAll(mTrackingPoints)
                .width(4)
                .color(getResources().getColor(R.color.colorAccent)));
    }
}
