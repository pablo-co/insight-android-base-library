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
    public static final String JSON_ID = "id_shop";
    public static final String JSON_NAME = "shop_name";
    public static final String JSON_LATITUDE = "lat";
    public static final String JSON_LONGITUDE = "lng";
    public static final String JSON_SIZE = "shop_size";
    public static final String JSON_STREET_NAME = "street_name";
    public static final String JSON_STREET_NUMBER = "street_number";
    public static final String JSON_STORE_FRONT_LENGTH = "front_length";
    public static final String JSON_FLOOR_LEVEL = "floor_level";
    public static final String JSON_PRIVATE_LOADING_AREA = "private_loading_area";
    public static final String JSON_NEARBY_LOADING_AREA = "nearby_loading_area";
    public static final String JSON_ON_STREET = "loading_off_on_street";
    public static final String JSON_NOTES = "notes";
    public static final String JSON_TYPE_ID = "stype_id";
    public static final String JSON_DISTANCE = "distance";

    protected Long mId;
    protected Long mTypeId;
    protected String mName;
    protected Double mLatitude;
    protected Double mLongitude;
    protected Integer mSize;
    protected String mStreetName;
    protected Integer mStreetNumber;
    protected Float mStoreFrontLength;
    protected Integer mStartingFloor;
    protected Integer mFloorLevel;
    protected Boolean mPrivateLoadingArea;
    protected Boolean mNearbyLoadingArea;
    protected Boolean mOnStreet;
    protected String mNotes;
    protected Double mDistance;

    public Shop() {
        mId = null;
        mName = null;
        mLatitude = null;
        mLongitude = null;
        mStreetNumber = null;
        mStreetName = null;
        mStoreFrontLength = null;
        mStartingFloor = null;
        mFloorLevel = null;
        mPrivateLoadingArea = null;
        mNearbyLoadingArea = null;
        mNotes = null;
        mSize = null;
        mDistance = null;
        mTypeId = null;
        mOnStreet = null;
    }

    public Shop(JSONObject json) throws JSONException {
        mName = json.getString(JSON_NAME);
        mLatitude = json.getDouble(JSON_LATITUDE);
        mLongitude = json.getDouble(JSON_LONGITUDE);

        if (json.has(JSON_TYPE_ID) && !json.isNull(JSON_TYPE_ID)) {
            mTypeId = json.getLong(JSON_TYPE_ID);
        }

        if (json.has(JSON_SIZE) && !json.isNull(JSON_SIZE)) {
            mSize = json.getInt(JSON_SIZE);
        }

        if (json.has(JSON_STORE_FRONT_LENGTH) && !json.isNull(JSON_STORE_FRONT_LENGTH)) {
            mStoreFrontLength = (float) json.getDouble(JSON_STORE_FRONT_LENGTH);
        }

        if (json.has(JSON_STREET_NAME) && !json.isNull(JSON_STREET_NAME)) {
            mStreetName = json.getString(JSON_STREET_NAME);
        }

        if (json.has(JSON_FLOOR_LEVEL) && !json.isNull(JSON_FLOOR_LEVEL)) {
            mFloorLevel = json.getInt(JSON_FLOOR_LEVEL);
        }

        if (json.has(JSON_PRIVATE_LOADING_AREA) && !json.isNull(JSON_PRIVATE_LOADING_AREA)) {
            mPrivateLoadingArea = json.getInt(JSON_PRIVATE_LOADING_AREA) == 1;
        }

        if (json.has(JSON_NEARBY_LOADING_AREA) && !json.isNull(JSON_NEARBY_LOADING_AREA)) {
            mNearbyLoadingArea = json.getInt(JSON_NEARBY_LOADING_AREA) == 1;
        }

        if (json.has(JSON_ON_STREET) && !json.isNull(JSON_ON_STREET)) {
            mOnStreet = json.getInt(JSON_ON_STREET) == 1;
        }

        if (json.has(JSON_NOTES) && !json.isNull(JSON_NOTES)) {
            mNotes = json.getString(JSON_NOTES);
        }

        if (json.has(JSON_DISTANCE) && !json.isNull(JSON_DISTANCE)) {
            mDistance = json.getDouble(JSON_DISTANCE);
        }
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Long getTypeId() {
        return mTypeId;
    }

    public void setTypeId(Long typeId) {
        mTypeId = typeId;
    }

    public Integer getSize() {
        return mSize;
    }

    public void setSize(Integer size) {
        mSize = size;
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

    public Integer getStreetNumber() {
        return mStreetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        mStreetNumber = streetNumber;
    }

    public String getStreetName() {
        return mStreetName;
    }

    public void setStreetName(String streetName) {
        mStreetName = streetName;
    }

    public Float getStoreFrontLength() {
        return mStoreFrontLength;
    }

    public void setStoreFrontLength(Float storeFrontLength) {
        mStoreFrontLength = storeFrontLength;
    }

    public Integer getStartingFloor() {
        return mStartingFloor;
    }

    public void setStartingFloor(Integer startingFloor) {
        mStartingFloor = startingFloor;
    }

    public Integer getFloorLevel() {
        return mFloorLevel;
    }

    public void setFloorLevel(Integer floorLevel) {
        mFloorLevel = floorLevel;
    }

    public Boolean getPrivateLoadingArea() {
        return mPrivateLoadingArea;
    }

    public void setPrivateLoadingArea(Boolean privateLoadingArea) {
        mPrivateLoadingArea = privateLoadingArea;
    }

    public Boolean getNearbyLoadingArea() {
        return mNearbyLoadingArea;
    }

    public void setNearbyLoadingArea(Boolean nearbyLoadingArea) {
        mNearbyLoadingArea = nearbyLoadingArea;
    }

    public Boolean getOnStreet() {
        return mOnStreet;
    }

    public void setOnStreet(Boolean onStreet) {
        mOnStreet = onStreet;
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

    public JSONObject toJSONWithoutWrapper() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(JSON_ID, mId);
        object.put(JSON_TYPE_ID, mTypeId);
        object.put(JSON_SIZE, mSize);
        object.put(JSON_LATITUDE, mLatitude);
        object.put(JSON_LONGITUDE, mLongitude);
        object.put(JSON_STREET_NUMBER, mStreetNumber);
        object.put(JSON_STREET_NAME, mStreetName);
        object.put(JSON_NAME, mName);
        object.put(JSON_STORE_FRONT_LENGTH, mStoreFrontLength);
        object.put(JSON_FLOOR_LEVEL, mFloorLevel);
        object.put(JSON_PRIVATE_LOADING_AREA, mPrivateLoadingArea);
        object.put(JSON_NEARBY_LOADING_AREA, mNearbyLoadingArea);
        object.put(JSON_ON_STREET, mOnStreet);
        object.put(JSON_NOTES, mNotes);
        return object;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject wrapper = new JSONObject();
        wrapper.put(JSON_WRAPPER, toJSONWithoutWrapper());
        return wrapper;
    }

    public RequestParams buildParams() {
        HashMap<String, Object> object = new HashMap<>();
        object.put(JSON_ID, mId);
        object.put(JSON_TYPE_ID, mTypeId);
        object.put(JSON_SIZE, mSize);
        object.put(JSON_LATITUDE, mLatitude);
        object.put(JSON_LONGITUDE, mLongitude);
        object.put(JSON_STREET_NUMBER, mStreetNumber);
        object.put(JSON_STREET_NAME, mStreetName);
        object.put(JSON_LATITUDE, mLatitude);
        object.put(JSON_LONGITUDE, mLongitude);
        object.put(JSON_NAME, mName);
        object.put(JSON_STORE_FRONT_LENGTH, mStoreFrontLength);
        object.put(JSON_FLOOR_LEVEL, mFloorLevel);
        object.put(JSON_PRIVATE_LOADING_AREA, mPrivateLoadingArea);
        object.put(JSON_NEARBY_LOADING_AREA, mNearbyLoadingArea);
        object.put(JSON_ON_STREET, mOnStreet);
        object.put(JSON_NOTES, mNotes);

        RequestParams params = new RequestParams();
        params.put(JSON_WRAPPER, object);
        return params;
    }

}
