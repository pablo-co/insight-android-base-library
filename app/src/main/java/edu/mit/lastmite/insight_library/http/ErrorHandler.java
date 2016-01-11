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

import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.mit.lastmite.insight_library.fragment.ErrorFragment;

public class ErrorHandler {

    private static final String TAG = "ErrorHandler";

    public static String buildErrorString(JSONArray array) {
        String errors = "";
        for (int i = 0; i < array.length(); ++i) {
            try {
                errors += array.getString(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i < array.length() - 1) {
                errors += ", ";
            }
        }
        return errors;
    }

    public static void handleError(FragmentManager fragmentManager, int statusCode, String message) {
        if (message == null) {
            message = "Unknown error";
        }

        Log.e(TAG, message);

        if (fragmentManager == null) return;

        ErrorFragment fragment = ErrorFragment.newInstance(message);
        try {
            fragment.show(fragmentManager, ErrorFragment.DIALOG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleError(FragmentManager fragmentManager, int statusCode, JSONObject errorResponse) {
        if (statusCode >= 400 && statusCode < 500) {
            JSONObject error = null;
            try {
                error = errorResponse.getJSONObject("error");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (error != null) {
                try {
                    handleError(fragmentManager, statusCode, error.getString("message"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (statusCode != 0) {
            handleError(fragmentManager, statusCode, "Hubo un error desconocido en el servidor.");
        }
        Log.e(TAG, "Error doing request: status: " + statusCode + ", response: " + errorResponse);
    }

    public static void addErrorsToEditText(Object object, JSONObject errors, String key) throws Exception {
        if (errors.has(key)) {
            object.getClass().getMethod("setError").invoke(buildErrorString(errors.getJSONArray(key)));
        }
    }

    public static void addErrorsToRadioButton(RadioButton field, JSONObject errors, String key) throws JSONException {
        if (errors.has(key)) {
            field.setError(buildErrorString(errors.getJSONArray(key)));
        }
    }
}
