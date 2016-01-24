package edu.mit.lastmite.insight_library.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public final class IntentUtils {
  private IntentUtils() {}

  public static void openActivity(Context context, Class klass) {
    Intent intent = new Intent(context, klass);
    context.startActivity(intent);
  }

  public static void openActivity(Context context, Class klass, Bundle extras) {
    Intent intent = new Intent(context, klass);
    intent.putExtras(extras);
    context.startActivity(intent);
  }

  public static void openActivityForResult(Activity activity, Class klass, int requestCode) {
    Intent intent = new Intent(activity, klass);
    activity.startActivityForResult(intent, requestCode);
  }


  public static void openActivityForResult(Fragment fragment, Class klass, int requestCode) {
    Intent intent = new Intent(fragment.getContext(), klass);
    fragment.startActivityForResult(intent, requestCode);
  }

  public static void openExternalUrl(Context context, String url) {
    Uri uri = Uri.parse(url);
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  public static void share(final Context context, final String url, final String title) {
    Intent intent = shareIntent(url, title);
    context.startActivity(intent);
  }

  public static Intent shareIntent(final String url, final String title) {
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_TEXT, url);
    intent.putExtra(Intent.EXTRA_SUBJECT, title);
    return intent;
  }
}