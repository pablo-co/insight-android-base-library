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


public class Shop implements JSONable {

    public static final String JSON_WRAPPER = "shop";
    public static final String JSON_NAME = "shop_name";
    public static final String JSON_LATITUDE = "lat";
    public static final String JSON_LONGITUDE = "lng";
    public static final String JSON_STORE_FRONT_LENGTH = "store_front_length";
    public static final String JSON_STARTING_FLOOR = "starting_floor";
    public static final String JSON_TOTAL_FLOORS = "total_floors";
    public static final String JSON_PRIVATE_LOADING_AREA = "private_loading_area";
    public static final String JSON_STREET_LOADING_AREA = "street_loading_area";
    public static final String JSON_NOTES = "notes";
    public static final String JSON_DISTANCE = "distance";

    protected String mName;
    protected Double mLatitude;
    protected Double mLongitude;
    protected Integer mStoreFrontLength;
    protected Integer mStartingFloor;
    protected Integer mTotalFloors;
    protected Boolean mPrivateLoadingArea;
    protected Boolean mStreetLoadingArea;
    protected String mNotes;
    protected Double mDistance;

    public Shop() {
        mName = null;
        mLatitude = null;
        mLongitude = null;
        mStoreFrontLength = null;
        mStartingFloor = null;
        mTotalFloors = null;
        mPrivateLoadingArea = null;
        mStreetLoadingArea = null;
        mNotes = null;
        mDistance = null;
    }

    public Shop(JSONObject json) throws JSONException {
        mName = json.getString(JSON_NAME);
        //mLatitude = json.getDouble(JSON_LATITUDE);
        //mLongitude = json.getDouble(JSON_LONGITUDE);
        //mStoreFrontLength = json.getInt(JSON_STORE_FRONT_LENGTH);
        //mStartingFloor = json.getInt(JSON_STARTING_FLOOR);
        //mTotalFloors = json.getInt(JSON_TOTAL_FLOORS);
        //mPrivateLoadingArea = json.getBoolean(JSON_PRIVATE_LOADING_AREA);
        //mStreetLoadingArea = json.getBoolean(JSON_STREET_LOADING_AREA);
        //mNotes = json.getString(JSON_NOTES);

        if (json.has(JSON_NOTES) && !json.isNull(JSON_NOTES)) {
            mNotes = json.getString(JSON_NOTES);
        }

        if (json.has(JSON_DISTANCE) && !json.isNull(JSON_DISTANCE)) {
            mDistance = json.getDouble(JSON_DISTANCE);
        }
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
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

    public Integer getStoreFrontLength() {
        return mStoreFrontLength;
    }

    public void setStoreFrontLength(Integer storeFrontLength) {
        mStoreFrontLength = storeFrontLength;
    }

    public Integer getStartingFloor() {
        return mStartingFloor;
    }

    public void setStartingFloor(Integer startingFloor) {
        mStartingFloor = startingFloor;
    }

    public Integer getTotalFloors() {
        return mTotalFloors;
    }

    public void setTotalFloors(Integer totalFloors) {
        mTotalFloors = totalFloors;
    }

    public Boolean getPrivateLoadingArea() {
        return mPrivateLoadingArea;
    }

    public void setPrivateLoadingArea(Boolean privateLoadingArea) {
        mPrivateLoadingArea = privateLoadingArea;
    }

    public Boolean getStreetLoadingArea() {
        return mStreetLoadingArea;
    }

    public void setStreetLoadingArea(Boolean streetLoadingArea) {
        mStreetLoadingArea = streetLoadingArea;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public Double getDistance() {
        return mDistance;
    }

    public void setDistance(Double distance) {
        mDistance = distance;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(JSON_LATITUDE, mLatitude);
        object.put(JSON_LONGITUDE, mLongitude);
        return object;
    }

    public RequestParams buildParams() {
        HashMap<String, Object> object = new HashMap<>();
        object.put(JSON_LATITUDE, mLatitude);
        object.put(JSON_LONGITUDE, mLongitude);

        RequestParams params = new RequestParams();
        params.put(JSON_WRAPPER, object);
        return params;
    }

}
