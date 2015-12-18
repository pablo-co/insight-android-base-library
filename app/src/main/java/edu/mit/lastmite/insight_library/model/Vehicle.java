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

public class Vehicle implements JSONable {

    public static final String JSON_WRAPPER = "vehicle";
    public static final String JSON_ID = "id_vehicle";
    public static final String JSON_PLATES = "plates";
    public static final String JSON_PASSWORD = "password";

    protected Long mId;
    protected String mPlates;
    protected String mPassword;

    public Vehicle() {
        mId = null;
        mPlates = null;
        mPassword = null;
    }

    public Vehicle(JSONObject json) throws JSONException {
        JSONObject object = json;
        if (json.has(JSON_WRAPPER)) {
            object = json.getJSONObject(JSON_WRAPPER);
        }

        mId = object.getLong(JSON_ID);
        mPlates = object.getString(JSON_PLATES);
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getPlates() {
        return mPlates;
    }

    public void setPlates(String plates) {
        mPlates = plates;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public boolean isEmpty() {
        return mId == null;
    }

    public JSONObject toJSONWithoutWrapper() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(JSON_ID, mId);
        object.put(JSON_PLATES, mPlates);
        object.put(JSON_PASSWORD, mPassword);
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
        object.put(JSON_PLATES, mPlates);
        object.put(JSON_PASSWORD, mPassword);
        return object;
    }

    public RequestParams buildParams() {
        HashMap<String, Object> object = toHashMap();
        object.put(JSON_PLATES, mPlates);
        object.put(JSON_PASSWORD, mPassword);

        RequestParams params = new RequestParams();
        params.put(JSON_WRAPPER, object);
        return params;
    }

}