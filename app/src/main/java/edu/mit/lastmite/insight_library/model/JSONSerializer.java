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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import edu.mit.lastmite.insight_library.R;
import edu.mit.lastmite.insight_library.security.AesCbcWithIntegrity;


public class JSONSerializer {

    private Context mContext;
    private String mFilename;
    private AesCbcWithIntegrity.SecretKeys mSecretKeys;

    public JSONSerializer(Context context, String filename) {
        mContext = context;
        mFilename = filename;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String keys = sharedPreferences.getString(context.getString(R.string.secret_keys), "");
        if (keys.isEmpty()) {
            try {
                mSecretKeys = AesCbcWithIntegrity.generateKey();
                sharedPreferences.edit().putString(context.getString(R.string.secret_keys), mSecretKeys.toString()).apply();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mSecretKeys = AesCbcWithIntegrity.keys(keys);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public Object loadObject(String className) throws Exception {
        Class cls = Class.forName(className);
        Constructor<?> constructor = cls.getConstructor(JSONObject.class);

        JSONObject object = new JSONObject(AesCbcWithIntegrity.decryptString(new AesCbcWithIntegrity.CipherTextIvMac(readJSONString()), mSecretKeys));

        return constructor.newInstance(object);
    }

    public ArrayList<?> loadArray(String className) throws Exception {
        ArrayList<Object> objects = new ArrayList<>();

        Class cls = Class.forName(className);
        Constructor<?> constructor = cls.getConstructor(JSONObject.class);

        JSONArray array = (JSONArray) new JSONTokener(AesCbcWithIntegrity.decryptString(new AesCbcWithIntegrity.CipherTextIvMac(readJSONString()), mSecretKeys)).nextValue();

        for (int i = 0; i < array.length(); ++i) {
            objects.add(constructor.newInstance(array.getJSONObject(i)));
        }

        return objects;
    }

    public void saveArray(ArrayList<JSONable> objects) throws JSONException, IOException, GeneralSecurityException {
        JSONArray array = new JSONArray();
        for (JSONable o : objects) {
            array.put(o.toJSON());
        }
        saveJSONString(AesCbcWithIntegrity.encrypt(array.toString(), mSecretKeys).toString());
    }

    public void saveObject(JSONable object) throws JSONException, IOException, GeneralSecurityException {
        saveJSONString(AesCbcWithIntegrity.encrypt(object.toJSON().toString(), mSecretKeys).toString());
    }

    public void deleteObject() throws IOException {
        mContext.deleteFile(mFilename);
    }

    private void saveJSONString(String jsonString) throws IOException {
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonString);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private String readJSONString() throws Exception {
        BufferedReader reader = null;
        StringBuilder jsonString = new StringBuilder();
        try {
            InputStream inputStream = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return jsonString.toString();
    }

}
