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

package edu.mit.lastmite.insight_library.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.mit.lastmite.insight_library.R;

public class APIResponseHandler extends JsonHttpResponseHandler {

    private static final String TAG = "APIResponseHandler";

    private Context mContext;
    private FragmentManager mFragmentManager;
    private ProgressDialog mProgressDialog;
    private boolean mShowProgress;

    public APIResponseHandler(Context context, FragmentManager fragmentManager, boolean showProgress) {
        super();
        mContext = context;
        mFragmentManager = fragmentManager;
        mShowProgress = showProgress;
        if (mShowProgress) {
            mProgressDialog = ProgressDialog.show(context, null, context.getString(R.string.loading_content));
        }
    }

    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Log.i(TAG, "State code: " + statusCode + ", response: " + response);
        onFinish(true);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        Log.i(TAG, "State code: " + statusCode + ", response: " + response);
        onFinish(true);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        ErrorHandler.handleError(mFragmentManager, statusCode, errorResponse);
        onFinish(false);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        ErrorHandler.handleError(mFragmentManager, statusCode, responseString);
        onFinish(false);
    }

    public void onFinish(boolean success) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
