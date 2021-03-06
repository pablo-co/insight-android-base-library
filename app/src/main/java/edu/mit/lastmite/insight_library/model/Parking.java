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

public class Parking implements JSONable {
    public static final String JSON_WRAPPER = "parking";
    public static final String JSON_ID = "id_parking";
    public static final String JSON_START_TIME = "start_time";
    public static final String JSON_END_TIME = "end_time";
    public static final String JSON_LATITUDE = "lat";
    public static final String JSON_LONGITUDE = "lng";
    public static final String JSON_ROUTE_ID = "route_id";

    protected Long mId;
    protected Long mStartTime;
    protected Long mEndTime;
    protected Double mLatitude;
    protected Double mLongitude;
    protected Long mRouteId;

    public Parking() {
        mId = null;
        mStartTime = null;
        mEndTime = null;
        mLatitude = null;
        mLongitude = null;
        mRouteId = null;
        measureTime();
    }

    public Parking(JSONObject json) throws JSONException {
        JSONObject object = json.getJSONObject(JSON_WRAPPER);
        mId = object.getLong(JSON_ID);
        mLatitude = object.getDouble(JSON_LATITUDE);
        mLongitude = object.getDouble(JSON_LONGITUDE);
        mRouteId =  object.getLong(JSON_ROUTE_ID);
        measureTime();
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Long startTime) {
        mStartTime = startTime;
    }

    public Long getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Long endTime) {
        mEndTime = endTime;
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

    public Long getRouteId() {
        return mRouteId;
    }

    public void setRouteId(Long routeId) {
        mRouteId = routeId;
    }

    public boolean isEmpty() {
        return mId == null;
    }


    public void measureTime() {
        if (mStartTime == null) {
            mStartTime = System.currentTimeMillis();
        }

        if (mEndTime == null) {
            mEndTime = System.currentTimeMillis();
        }
    }

    public JSONObject toJSONWithoutWrapper() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(JSON_ID, mId);
        object.put(JSON_START_TIME, mStartTime);
        object.put(JSON_END_TIME, mEndTime);
        object.put(JSON_ROUTE_ID, mRouteId);
        return object;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject wrapper = new JSONObject();
        wrapper.put(JSON_WRAPPER, toJSONWithoutWrapper());
        return wrapper;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> object = new HashMap<>();
        object.put(JSON_ID, mId);
        object.put(JSON_START_TIME, mStartTime);
        object.put(JSON_END_TIME, mEndTime);
        object.put(JSON_LATITUDE, mLatitude);
        object.put(JSON_LONGITUDE, mLongitude);
        object.put(JSON_ROUTE_ID, mRouteId);
        return object;
    }

    public RequestParams buildParams() {
        HashMap<String, Object> object = toHashMap();
        object.put(JSON_ID, mId);
        object.put(JSON_START_TIME, mStartTime);
        object.put(JSON_END_TIME, mEndTime);
        object.put(JSON_LATITUDE, mLatitude);
        object.put(JSON_LONGITUDE, mLongitude);
        object.put(JSON_ROUTE_ID, mRouteId);

        RequestParams params = new RequestParams();
        params.put(JSON_WRAPPER, object);
        return params;
    }

}
