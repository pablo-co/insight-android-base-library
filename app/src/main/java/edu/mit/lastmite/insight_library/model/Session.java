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

public class Session implements JSONable {

    public static final String JSON_WRAPPER = "session";
    public static final String JSON_PLATFORM = "platform";
    public static final String JSON_PUSH_ID = "push_id";

    private int mPlatform;
    private String mPushId;

    public Session() {
        mPlatform = 1;
        mPushId = null;
    }

    public Session(JSONObject json) throws JSONException {
        mPlatform = json.getInt(JSON_PLATFORM);
        mPushId = json.getString(JSON_PUSH_ID);
    }

    public int getPlatform() {
        return mPlatform;
    }

    public void setPlatform(int platform) {
        mPlatform = platform;
    }

    public String getPushId() {
        return mPushId;
    }

    public void setPushId(String pushId) {
        mPushId = pushId;
    }

    public boolean isEmpty() {
        return mPushId == null || mPushId.isEmpty();
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(JSON_PLATFORM, mPlatform);
        object.put(JSON_PUSH_ID, mPushId);
        return object;
    }

    public JSONObject toJSONWithWrapper() throws JSONException {
        JSONObject wrapper = new JSONObject();
        wrapper.put(JSON_WRAPPER, toJSON());
        return wrapper;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> object = new HashMap<>();
        object.put(JSON_PLATFORM, mPlatform);
        object.put(JSON_PUSH_ID, mPushId);
        return object;
    }

    public RequestParams buildParams() {
        HashMap<String, Object> object = toHashMap();

        RequestParams params = new RequestParams();
        params.put(JSON_WRAPPER, object);
        return params;
    }

}
