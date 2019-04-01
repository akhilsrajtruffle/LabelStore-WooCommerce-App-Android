package com.mavsoft.label.Utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.widget.Toast;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

public final class DialogFactory {

  private static final @ColorInt int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");

  private static final @ColorInt int ERROR_COLOR = Color.parseColor("#D50000");
  private static final @ColorInt int INFO_COLOR = Color.parseColor("#3F51B5");
  private static final @ColorInt int SUCCESS_COLOR = Color.parseColor("#388E3C");
  private static final @ColorInt int WARNING_COLOR = Color.parseColor("#FFA900");

  public static StyleableToast success_toast(Context context, String message) {
    StyleableToast st = new StyleableToast(context, message, Toast.LENGTH_SHORT);
    st.setBackgroundColor(SUCCESS_COLOR);
    st.setTextColor(Color.WHITE);
    st.setMaxAlpha();
    return st;
  }

  public static StyleableToast simple_toast(Context context, String message) {
    StyleableToast st = new StyleableToast(context, message, Toast.LENGTH_SHORT);
    st.setBackgroundColor(DEFAULT_TEXT_COLOR);
    st.setTextColor(Color.WHITE);
    st.setMaxAlpha();
    return st;
  }

  public static StyleableToast loading_toast(Context context, String message) {
    StyleableToast st = new StyleableToast(context, message, Toast.LENGTH_SHORT);
    st.setBackgroundColor(INFO_COLOR);
    st.setTextColor(Color.WHITE);
    st.spinIcon();
    st.setMaxAlpha();
    return st;
  }

  public static StyleableToast warning_toast(Context context, String message) {
    StyleableToast st = new StyleableToast(context, message, Toast.LENGTH_SHORT);
    st.setBackgroundColor(WARNING_COLOR);
    st.setTextColor(Color.WHITE);
    st.setMaxAlpha();
    return st;
  }

  public static StyleableToast error_toast(Context context, String message) {
    StyleableToast st = new StyleableToast(context, message, Toast.LENGTH_SHORT);
    st.setBackgroundColor(ERROR_COLOR);
    st.setTextColor(Color.WHITE);
    st.setMaxAlpha();
    return st;
  }
}
