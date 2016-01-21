package edu.mit.lastmite.insight_library.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public final class ViewUtils {
    public static final float STATUS_BAR_COLOR_RATE = 0.75f;

    private ViewUtils() {
    }

    public static <T extends View> T findById(Activity activity, int id) {
        return Objects.<T>cast(activity.findViewById(id));
    }

    public static <T extends View> T findById(View view, int id) {
        return Objects.<T>cast(view.findViewById(id));
    }

    public static <T extends View> T inflate(ViewGroup root, @LayoutRes int resource) {
        return inflate(root, resource, false);
    }

    public static <T extends View> T inflate(ViewGroup root, @LayoutRes int resource,
                                             boolean attachToRoot) {
        return Objects.<T>cast(LayoutInflater.from(root.getContext())
                .inflate(resource, root, attachToRoot));
    }

    public static float getCenterX(View v) {
        return (v.getLeft() + v.getRight()) / 2f;
    }

    public static float getCenterY(View v) {
        return (v.getTop() + v.getBottom()) / 2f;
    }

    public static void setText(TextView v, int id, Object... formatArgs) {
        v.setText(ResUtils.getString(v.getContext(), id, formatArgs));
    }

    public static void setHtmlText(TextView v, int id, Object... formatArgs) {
        v.setText(Html.fromHtml(ResUtils.getString(v.getContext(), id, formatArgs)));
    }

    public static void setQuantityText(TextView v, int id, int quantity) {
        v.setText(ResUtils.getQuantityString(v.getContext(), id, quantity));
    }

    public static void setQuantityHtmlText(TextView v, int id, int quantity) {
        v.setText(Html.fromHtml(ResUtils.getQuantityString(v.getContext(), id, quantity)));
    }

    public static void setupWebView(WebView webview) {
        webview.setVerticalScrollbarOverlay(true);
        WebSettings settings = webview.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
    }

    public static boolean isVisible(View v) {
        return (v.getVisibility() == View.VISIBLE);
    }

    public static void setVisible(View v) {
        setVisibility(v, View.VISIBLE);
    }

    public static void setInvisible(View v) {
        setVisibility(v, View.INVISIBLE);
    }

    public static void setGone(View v) {
        setVisibility(v, View.GONE);
    }

    public static int getDarkenedColor(int color) {
        int r = Color.red(color);
        int b = Color.blue(color);
        int g = Color.green(color);
        int darkerColor = Color.rgb((int) (r * STATUS_BAR_COLOR_RATE), (int) (g * STATUS_BAR_COLOR_RATE), (int) (b * STATUS_BAR_COLOR_RATE));
        return darkerColor;
    }

    public static View getActionBarView(Activity activity) {
        Window window = activity.getWindow();
        View view = window.getDecorView();
        int resId = activity.getResources().getIdentifier("action_bar_container", "id", activity.getPackageName());
        return view.findViewById(resId);
    }

    private static void setVisibility(View v, int visibility) {
        if (v.getVisibility() != visibility) {
            v.setVisibility(visibility);
        }
    }
}