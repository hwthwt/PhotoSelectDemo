package com.zhaoshuang.photoselectlib.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by zhaoshuang on 2018/3/16.
 */

public class Utils {

    public static int getWindowWidth(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getWindowHeight(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    public static String getImageType(String imgPath) {
        if (TextUtils.isEmpty(imgPath)) {
            return "";
        }
        File file = new File(imgPath);
        if (!file.exists() || !file.isFile()) {
            return "";
        }

        byte[] bytes = new byte[10];
        try {
            FileInputStream fis = new FileInputStream(file);
            int length = fis.read(bytes);
            fis.close();
            if (length == -1) {
                return "";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileHead = bytes2Hex(bytes).toLowerCase();
        if (fileHead.startsWith("ffd8ff")) {
            return "jpeg";
        } else if (fileHead.startsWith("89504e47")) {
            return "png";
        } else if (fileHead.startsWith("47494638")) {
            return "gif";
        } else {
            return "unknown";
        }
    }

    public static String bytes2Hex(byte[] bts) {
        StringBuilder sb = new StringBuilder();
        String des = "";
        String tmp;

        if (bts == null) {
            return "";
        }
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
//                des += "0";
                sb.append("0");
            }
//            des += tmp;
            sb.append(tmp);
        }
//        return des;
        return sb.toString();
    }
}
