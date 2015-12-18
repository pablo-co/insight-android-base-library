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


package edu.mit.lastmite.insight_library.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class provides helper functions to generally repeatable
 * problems or functionality.
 */
public class Helper {

    private float mDensity;
    private Context mContext;
    private DecimalFormat mDecimalFormat;
    private DecimalFormat mIntegerFormat;
    private static Helper sHelper;

    public interface FragmentCreator {
        Fragment createFragment();
    }

    /**
     * Class interface for the Singleton Design Pattern.
     *
     * @param context
     * @return
     */
    public static Helper get(Context context) {
        if (sHelper == null) {
            sHelper = new Helper(context);
        }
        return sHelper;
    }

    public Helper(Context context) {
        mContext = context;
        mDensity = mContext.getResources().getDisplayMetrics().density;
        mDecimalFormat = new DecimalFormat("0.00");
        mIntegerFormat = new DecimalFormat("00");
    }

    /**
     * Inflate the fragment returned in {@link Helper.FragmentCreator} replacing the one before it.
     * To inflate with no animation use no_animation resource
     * <b><i>Note: Transaction done with possible state loss</i></b>
     *
     * @param fragmentManager
     * @param resourceId
     * @param creator
     * @param inAnimation
     * @param outAnimation
     */
    public void inflateFragment(FragmentManager fragmentManager, int resourceId, FragmentCreator creator, int inAnimation, int outAnimation) {
        inflateFragment(fragmentManager, resourceId, creator, inAnimation, outAnimation, false);
    }

    public void inflateFragment(FragmentManager fragmentManager, int resourceId, FragmentCreator creator, int inAnimation, int outAnimation, boolean addToBackStack) {
        try {
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(inAnimation, outAnimation).replace(resourceId, creator.createFragment());
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void inflateFragment(FragmentManager fragmentManager, int resourceId, FragmentCreator creator, int inAnimation, int outAnimation, int backInAnimation, int backOutAnimation, boolean addToBackStack) {
        try {
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(inAnimation, outAnimation, backInAnimation, backOutAnimation).replace(resourceId, creator.createFragment());
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the specified fragment with an animation.
     * <b><i>Note: Transaction done with possible state loss</i></b>
     *
     * @param fragmentManager
     * @param resourceId
     * @param inAnimation
     * @param outAnimation
     */
    public void removeFragment(FragmentManager fragmentManager, int resourceId, int inAnimation, int outAnimation) {
        Fragment fragment = fragmentManager.findFragmentById(resourceId);
        if (fragment != null) {
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction
                    .setCustomAnimations(inAnimation, outAnimation)
                    .remove(fragment)
                    .commitAllowingStateLoss();

        }
    }

    /**
     * Remove the specified fragment.
     * <b><i>Note: Transaction done with possible state loss</i></b>
     *
     * @param fragmentManager
     * @param resourceId
     */
    public void removeFragment(FragmentManager fragmentManager, int resourceId) {
        Fragment fragment = fragmentManager.findFragmentById(resourceId);
        if (fragment != null) {
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commitAllowingStateLoss();

        }
    }

    /**
     * Merge the specified JSONObjects into one.
     *
     * @param firstObject
     * @param secondObject
     * @return
     * @throws JSONException
     */
    public JSONObject mergeJSONObjects(JSONObject firstObject, JSONObject secondObject) throws JSONException {
        JSONObject result = new JSONObject();

        Iterator<String> iterator = firstObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            result.put(key, firstObject.get(key));
        }

        iterator = secondObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            result.put(key, secondObject.get(key));
        }

        return result;
    }

    /**
     * Builds a RequestParams object from a JSONObject.
     *
     * @param object
     * @return
     * @throws JSONException
     */
    public RequestParams JSONObjectToRequestParams(JSONObject object) throws JSONException {
        RequestParams requestParams = new RequestParams();

        Iterator<String> iterator = object.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            requestParams.put(key, object.get(key));
        }

        return requestParams;
    }

    /**
     * @param map
     * @return
     * @throws JSONException
     */
    public RequestParams HashMapToRequestParams(Map<String, Object> map) throws JSONException {
        RequestParams requestParams = new RequestParams();

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            requestParams.put(key, map.get(key));
        }

        return requestParams;
    }

    /**
     * @param json
     * @return
     * @throws JSONException
     */
    public Map<String, Object> JSONObjectToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    /**
     * @param object
     * @return
     * @throws JSONException
     */
    public Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    /**
     * @param array
     * @return
     * @throws JSONException
     */
    public List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    /**
     * @param dp
     * @return
     */
    public int dpToPx(int dp) {
        return (int) (dp * mDensity);
    }

    /**
     * @param value
     * @return
     */
    public String formatDouble(double value) {
        return mDecimalFormat.format(value);
    }

    public String formatInteger(int value) {
        return mIntegerFormat.format(value);
    }

    /**
     * @param receivedFlags
     * @param targetFlag
     * @return
     */
    public boolean isFlagActivated(int receivedFlags, int targetFlag) {
        return (receivedFlags & targetFlag) == targetFlag;
    }

    /**
     * @param drawableId
     * @param text
     * @param textSize
     * @param offsetX
     * @param offsetY
     * @return
     */
    public Bitmap drawTextOnDrawable(int drawableId, String text, int textSize, int offsetX, int offsetY) {

        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(dpToPx(textSize));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if (textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(dpToPx(textSize / 2));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2 + dpToPx(offsetX);     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) + dpToPx(offsetY);

        canvas.drawText(text, xPos, yPos, paint);

        return bm;
    }

    /**
     * Create a new JPEG file on the public external storage. Uses current timestamp
     * to name the file.
     * <b>Note: Files are accesible to other applications.</b>
     *
     * @return
     * @throws IOException
     */
    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }

    /**
     * Gets Navigation bar height if there is one, 0 otherwise
     *
     * @return
     */
    public int navigationBarSize() {
        Resources resources = mContext.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * Adds an extra bottom margin in case there is a Software Navigation Bar
     *
     * @param view
     */
    public void addBottomMarginforSoftNavbar(View view) {
        if (!ViewConfiguration.get(mContext).hasPermanentMenuKey()) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.bottomMargin += navigationBarSize();
            view.forceLayout();
        }
    }

    /**
     * Removes extra bottom margin in case there is a Software Navigation Bar
     *
     * @param view
     */
    public void removeBottomMarginforSoftNavbar(View view) {
        if (!ViewConfiguration.get(mContext).hasPermanentMenuKey()) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.bottomMargin -= navigationBarSize();
            view.forceLayout();
        }
    }

    /**
     * Adds an extra bottom padding in case there is a Software Navigation Bar
     *
     * @param view
     */
    public void addBottomPaddingforSoftNavbar(View view) {
        if (!ViewConfiguration.get(mContext).hasPermanentMenuKey()) {
            view.setPadding(
                    view.getPaddingLeft(),
                    view.getPaddingTop(),
                    view.getPaddingRight(),
                    view.getPaddingBottom() + navigationBarSize()
            );
        }
    }

    /**
     * @param listView
     * @return
     */
    public void setListViewHeightBasedOnChildren(ListViewCompat listView) {

        int height = listView.measureHeightOfChildrenCompat(
                View.MeasureSpec.EXACTLY,
                0,
                ListViewCompat.NO_POSITION,
                10000,
                100);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}