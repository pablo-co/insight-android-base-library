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

package edu.mit.lastmite.insight_library.model;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class Location implements JSONable {
    public static final String JSON_WRAPPER = "trace";
    public static final String JSON_LATITUDE = "lat";
    public static final String JSON_LONGITUDE = "lng";
    public static final String JSON_SPEED = "speed";
    public static final String JSON_TIME = "time";
    public static final String JSON_ROUTE_ID = "route_id";
    public static final String JSON_CSTOP_ID = "cstop_id";

    protected Double mLatitude;
    protected Double mLongitude;
    protected Float mSpeed;
    protected Long mTime;
    protected Long mRouteId;
    protected Long mCStopId;

    public Location() {
        mLatitude = null;
        mLongitude = null;
        mRouteId = null;
        mSpeed = null;
        mCStopId = null;
        measureTime();
    }

    public Location(android.location.Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        mSpeed = null;
        mRouteId = null;
        mCStopId = null;
        measureTime();
    }

    public Location(android.location.Location location, float speed) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        mSpeed = speed;
        mRouteId = null;
        mCStopId = null;
        measureTime();
    }

    public Location(JSONObject json) throws JSONException {
        mLatitude = json.getDouble(JSON_LATITUDE);
        mLongitude = json.getDouble(JSON_LONGITUDE);

        if (json.has(JSON_ROUTE_ID) && !json.isNull(JSON_ROUTE_ID)) {
            mRouteId = json.getLong(JSON_ROUTE_ID);
        }
    }

    public Location(Double latitude, Double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
        mSpeed = 10.0f;
        mRouteId = Long.valueOf(419);
        measureTime();
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

    public Float getSpeed() {
        return mSpeed;
    }

    public Long getTime() {
        return mTime;
    }

    public void setTime(Long time) {
        mTime = time;
    }

    public void setSpeed(Float speed) {
        mSpeed = speed;
    }

    public Long getRouteId() {
        return mRouteId;
    }

    public void setRouteId(Long driveTime) {
        mRouteId = driveTime;
    }

    public Long getCStopId() {
        return mCStopId;
    }

    public void setCStopId(Long CStopId) {
        mCStopId = CStopId;
    }

    public void measureTime() {
        mTime = System.currentTimeMillis();
    }

    public boolean isEmpty() {
        return mLatitude == null || mLongitude == null;
    }

    public JSONObject toJSONWithoutWrapper() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(JSON_LATITUDE, mLatitude);
        object.put(JSON_LONGITUDE, mLongitude);
        object.put(JSON_SPEED, mSpeed);
        object.put(JSON_TIME, mTime);
        object.put(JSON_ROUTE_ID, mRouteId);
        object.put(JSON_CSTOP_ID, mCStopId);
        return object;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject wrapper = new JSONObject();
        wrapper.put(JSON_WRAPPER, toJSONWithoutWrapper());
        return wrapper;
    }

    public RequestParams buildParams() {
        HashMap<String, Object> object = new HashMap<>();
        object.put(JSON_LATITUDE, mLatitude);
        object.put(JSON_LONGITUDE, mLongitude);
        object.put(JSON_SPEED, mSpeed);
        object.put(JSON_TIME, mTime);
        object.put(JSON_ROUTE_ID, mRouteId);
        object.put(JSON_CSTOP_ID, mCStopId);

        RequestParams params = new RequestParams();
        params.put(JSON_WRAPPER, object);
        return params;
    }
}