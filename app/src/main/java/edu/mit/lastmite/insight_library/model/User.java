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

import java.util.HashMap;

public class User implements JSONable, Parcelable {
    public static final String JSON_WRAPPER = "crew";
    public static final String JSON_ID = "id_crew";
    public static final String JSON_ACCESS_TOKEN = "access_token";
    public static final String JSON_COMPANY_ID = "company_id";
    public static final String JSON_EMAIL = "email";
    public static final String JSON_PASSWORD = "password";

    protected Long mId;
    protected String mAccessToken;
    protected String mEmail;
    protected Long mCompanyId;
    protected String mPassword;

    public User() {
        mId = null;
        mAccessToken = null;
        mEmail = null;
        mCompanyId = null;
        mPassword = null;
    }

    public User(Parcel in) {
        mId = in.readLong();
        mAccessToken = in.readString();
        mEmail = in.readString();
        mCompanyId = in.readLong();
        mPassword = in.readString();
    }

    public User(JSONObject json) throws JSONException {
        JSONObject object = json;
        if (json.has(JSON_WRAPPER)) {
            object = json.getJSONObject(JSON_WRAPPER);
        }

        if (object.has(JSON_EMAIL) && !object.isNull(JSON_EMAIL)) {
            mEmail = object.getString(JSON_EMAIL);
        }

        if (object.has(JSON_ID) && !object.isNull(JSON_ID)) {
            mId = object.getLong(JSON_ID);
        }

        if (object.has(JSON_COMPANY_ID) && !object.isNull(JSON_COMPANY_ID)) {
            mCompanyId = object.getLong(JSON_COMPANY_ID);
        }

        if (object.has(JSON_ACCESS_TOKEN) && !object.isNull(JSON_ACCESS_TOKEN)) {
            mAccessToken = object.getString(JSON_ACCESS_TOKEN);
        }

        if (object.has(JSON_PASSWORD)  && !object.isNull(JSON_PASSWORD)) {
            mPassword = object.getString(JSON_PASSWORD);
        }
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    public Long getCompanyId() {
        return mCompanyId;
    }

    public void setCompanyId(Long companyId) {
        mCompanyId = companyId;
    }

    public Long getId() {
        return mId;
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

    public JSONObject toJSON() throws JSONException {
        JSONObject wrapper = new JSONObject();
        wrapper.put(JSON_WRAPPER, toJSONWithoutWrapper());
        return wrapper;
    }

    public JSONObject toJSONWithoutWrapper() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(JSON_ID, mId);
        object.put(JSON_ACCESS_TOKEN, mAccessToken);
        object.put(JSON_EMAIL, mEmail);
        object.put(JSON_COMPANY_ID, mCompanyId);
        object.put(JSON_PASSWORD, mPassword);

        return object;
    }

    public RequestParams buildParams() {
        HashMap<String, Object> object = new HashMap<>();
        object.put(JSON_ID, mId);
        object.put(JSON_ACCESS_TOKEN, mAccessToken);
        object.put(JSON_EMAIL, mEmail);
        object.put(JSON_COMPANY_ID, mCompanyId);
        object.put(JSON_PASSWORD, mPassword);

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
        out.writeLong(mId);
        out.writeString(mAccessToken);
        out.writeString(mEmail);
        out.writeLong(mCompanyId);
        out.writeString(mPassword);
    }

    public static final Creator CREATOR = new Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
