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

import android.os.Parcel;
import android.os.Parcelable;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Visit implements JSONable, Parcelable {
    public static final String JSON_WRAPPER = "visit";
    public static final String JSON_ID = "id_visit";
    public static final String JSON_START_TIME = "start_time";
    public static final String JSON_END_TIME = "end_time";
    public static final String JSON_LATITUDE_START = "lat1";
    public static final String JSON_LONGITUDE_START = "lng1";
    public static final String JSON_LATITUDE_END = "lat2";
    public static final String JSON_LONGITUDE_END = "lng2";
    public static final String JSON_CSTOP_ID = "cstop_id";

    protected Long mId;
    protected Long mStartTime;
    protected Long mEndTime;
    protected Double mLatitudeStart;
    protected Double mLongitudeStart;
    protected Double mLatitudeEnd;
    protected Double mLongitudeEnd;
    protected Long mCStopId;
    protected transient ArrayList<Location> mLocations;

    public Visit() {
        mId = null;
        mStartTime = null;
        mEndTime = null;
        mLatitudeStart = null;
        mLongitudeStart = null;
        mLatitudeEnd = null;
        mLongitudeEnd = null;
        mCStopId = null;
        measureTime();
    }

    public Visit(Parcel in) {
        mId = (Long) in.readValue(Long.class.getClassLoader());
        mStartTime = in.readLong();
        mEndTime = in.readLong();
        mLatitudeStart = in.readDouble();
        mLongitudeStart = in.readDouble();
        mLatitudeEnd = in.readDouble();
        mLongitudeEnd = in.readDouble();
        mCStopId = in.readLong();
        mLocations = in.readArrayList(Location.class.getClassLoader());
    }

    public Visit(JSONObject json) throws JSONException {
        JSONObject object = json.getJSONObject(JSON_WRAPPER);
        mId = object.getLong(JSON_ID);
        mLatitudeStart = object.getDouble(JSON_LATITUDE_START);
        mLongitudeStart = object.getDouble(JSON_LONGITUDE_END);
        mLatitudeEnd = object.getDouble(JSON_LATITUDE_END);
        mLongitudeEnd = object.getDouble(JSON_LONGITUDE_END);
        mCStopId = object.getLong(JSON_CSTOP_ID);
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

    public Double getLatitudeStart() {
        return mLatitudeStart;
    }

    public void setLatitudeStart(Double latitudeStart) {
        mLatitudeStart = latitudeStart;
    }

    public Double getLongitudeStart() {
        return mLongitudeStart;
    }

    public void setLongitudeStart(Double longitudeStart) {
        mLongitudeStart = longitudeStart;
    }

    public Double getLatitudeEnd() {
        return mLatitudeEnd;
    }

    public void setLatitudeEnd(Double latitudeEnd) {
        mLatitudeEnd = latitudeEnd;
    }

    public Double getLongitudeEnd() {
        return mLongitudeEnd;
    }

    public void setLongitudeEnd(Double longitudeEnd) {
        mLongitudeEnd = longitudeEnd;
    }

    public Long getCStopId() {
        return mCStopId;
    }

    public void setCStopId(Long CStopId) {
        mCStopId = CStopId;
    }

    public boolean isEmpty() {
        return mId == null;
    }

    public ArrayList<Location> getLocations() {
        return mLocations;
    }

    public void setLocations(ArrayList<Location> locations) {
        mLocations = locations;
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
        object.put(JSON_LATITUDE_START, mLatitudeStart);
        object.put(JSON_LONGITUDE_START, mLongitudeEnd);
        object.put(JSON_LATITUDE_END, mLatitudeEnd);
        object.put(JSON_LONGITUDE_END, mLongitudeEnd);
        object.put(JSON_CSTOP_ID, mCStopId);
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
        object.put(JSON_LATITUDE_START, mLatitudeStart);
        object.put(JSON_LONGITUDE_START, mLongitudeEnd);
        object.put(JSON_LATITUDE_END, mLatitudeEnd);
        object.put(JSON_LONGITUDE_END, mLongitudeEnd);
        object.put(JSON_CSTOP_ID, mCStopId);
        return object;
    }

    public RequestParams buildParams() {
        HashMap<String, Object> object = toHashMap();
        object.put(JSON_ID, mId);
        object.put(JSON_START_TIME, mStartTime);
        object.put(JSON_END_TIME, mEndTime);
        object.put(JSON_LATITUDE_START, mLatitudeStart);
        object.put(JSON_LONGITUDE_START, mLongitudeEnd);
        object.put(JSON_LATITUDE_END, mLatitudeEnd);
        object.put(JSON_LONGITUDE_END, mLongitudeEnd);
        object.put(JSON_CSTOP_ID, mCStopId);
        RequestParams params = new RequestParams();
        params.put(JSON_WRAPPER, object);
        return params;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeValue(mId);
        out.writeValue(mStartTime);
        out.writeValue(mEndTime);
        out.writeValue(mLatitudeStart);
        out.writeValue(mLongitudeStart);
        out.writeValue(mLatitudeEnd);
        out.writeValue(mLongitudeEnd);
        out.writeValue(mCStopId);
        out.writeValue(mLocations);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
