package com.zhaoshuang.photoselectlib.util;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by zhaoshuang on 2018/3/16.
 */

public class ToastUtil {

    private static Toast toast;

    public static void show(Context context, final String text) {
        show(context, text, 0, false);
    }

    public static void show(Context context, final int id) {
        show(context, "", id, false);
    }

    private static void show(Context context, final String text, int resId, final boolean displayLong){

        final StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(text)) {
            sb.append(text);
        } else if (resId > 0) {
            String temp = context.getString(resId);
            sb.append(temp);
        }

        if (!TextUtils.isEmpty(sb)) {
            if (Looper.getMainLooper() == Looper.myLooper()) {   //子线程时
                if (toast == null) {
                    toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);  //生成
                }
                if (displayLong) {
                    toast.setDuration(Toast.LENGTH_LONG);
                } else {
                    toast.setDuration(Toast.LENGTH_SHORT);
                }
                toast.setText(sb);
                toast.show();
            }
        }
    }
}
