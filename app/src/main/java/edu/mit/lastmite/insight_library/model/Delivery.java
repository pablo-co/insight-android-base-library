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

public class Delivery implements JSONable {

    public static final String JSON_WRAPPER = "delivery";
    public static final String JSON_ID = "id_delivery";
    public static final String JSON_TYPE = "type";
    public static final String JSON_TIME = "time";
    public static final String JSON_EQUIPMENT = "equipment";
    public static final String JSON_BEYOND_SEGMENT = "beyond_segment";
    public static final String JSON_LATITUDE = "lat";
    public static final String JSON_LONGITUDE = "lng";
    public static final String JSON_ORDER_ID = "order_id";
    public static final String JSON_VISIT_ID = "visit_id";
    public static final String JSON_ROUTE_ID = "route_id";

    protected Long mId;
    protected Integer mType;
    protected Boolean mEquipment;
    protected Boolean mBeyondSegment;
    protected Long mTime;
    protected Double mLatitude;
    protected Double mLongitude;
    protected Long mOrderId;
    protected Long mVisitId;
    protected Long mRouteId;

    public Delivery() {
        mId = null;
        mType = null;
        mEquipment = null;
        mBeyondSegment = null;
        mTime = null;
        mLatitude = null;
        mLongitude = null;
        mOrderId = null;
        mRouteId = null;
        mVisitId = null;
        measureTime();
    }

    public Delivery(JSONObject json) throws JSONException {
        JSONObject object = json.getJSONObject(JSON_WRAPPER);
        mId = object.getLong(JSON_ID);
        mLatitude = object.getDouble(JSON_LATITUDE);
        mLongitude = object.getDouble(JSON_LONGITUDE);
        mType = object.getInt(JSON_TYPE);
        mEquipment = object.getBoolean(JSON_EQUIPMENT);
        mBeyondSegment = object.getBoolean(JSON_BEYOND_SEGMENT);
        mTime = object.getLong(JSON_TIME);
        if (object.has(JSON_ORDER_ID)) {
            mOrderId = object.getLong(JSON_ORDER_ID);
        }
        mVisitId = object.getLong(JSON_VISIT_ID);
        mRouteId = object.getLong(JSON_ROUTE_ID);
        measureTime();
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Integer getType() {
        return mType;
    }

    public void setType(Integer type) {
        mType = type;
    }

    public Boolean getEquipment() {
        return mEquipment;
    }

    public void setEquipment(Boolean equipment) {
        mEquipment = equipment;
    }

    public Boolean getBeyondSegment() {
        return mBeyondSegment;
    }

    public void setBeyondSegment(Boolean beyondSegment) {
        mBeyondSegment = beyondSegment;
    }

    public Long getOrderId() {
        return mOrderId;
    }

    public void setOrderId(Long orderId) {
        mOrderId = orderId;
    }

    public Long getTime() {
        return mTime;
    }

    public void setTime(Long time) {
        mTime = time;
    }

    public Long getVisitId() {
        return mVisitId;
    }

    public void setVisitId(Long visitId) {
        mVisitId = visitId;
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
        if (mTime == null) {
            mTime = System.currentTimeMillis();
        }
    }

    public JSONObject toJSONWithoutWrapper() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(JSON_ID, mId);
        object.put(JSON_TIME, mTime);
        object.put(JSON_LATITUDE, mLatitude);
        object.put(JSON_LONGITUDE, mLongitude);
        object.put(JSON_BEYOND_SEGMENT, mBeyondSegment);
        object.put(JSON_EQUIPMENT, mEquipment);
        object.put(JSON_TYPE, mType);
        object.put(JSON_VISIT_ID, mVisitId);
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
        object.put(JSON_TIME, mTime);
        object.put(JSON_LATITUDE, mLatitude);
        object.put(JSON_LONGITUDE, mLongitude);
        object.put(JSON_BEYOND_SEGMENT, mBeyondSegment);
        object.put(JSON_EQUIPMENT, mEquipment);
        object.put(JSON_TYPE, mType);
        object.put(JSON_VISIT_ID, mVisitId);
        object.put(JSON_ROUTE_ID, mRouteId);
        return object;
    }

    public RequestParams buildParams() {
        HashMap<String, Object> object = toHashMap();
        object.put(JSON_ID, mId);
        object.put(JSON_TIME, mTime);
        object.put(JSON_LATITUDE, mLatitude);
        object.put(JSON_LONGITUDE, mLongitude);
        object.put(JSON_BEYOND_SEGMENT, mBeyondSegment);
        object.put(JSON_EQUIPMENT, mEquipment);
        object.put(JSON_TYPE, mType);
        object.put(JSON_VISIT_ID, mVisitId);
        object.put(JSON_ROUTE_ID, mRouteId);

        RequestParams params = new RequestParams();
        params.put(JSON_WRAPPER, object);
        return params;
    }
}
