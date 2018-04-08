package com.zhaoshuang.photoselectlib.util;

import android.app.Activity;
import android.content.Intent;

import com.zhaoshuang.photoselectlib.view.PhotoSelectActivity;

public class PhotoSelectIntent {

    public static final int REQUEST_PHOTO_LIST = 100;

    /**
     *
     * @param activity
     * @param modeMedia  1.全部媒体文件, 2.只有图片, 3.只有视频
     * @param modeLocal  拍照片还是拍视频
     * @param maxSize
     */
    public static void gotoPhotoSelectActivity(Activity activity, int modeMedia, int modeLocal, int maxSize) {
        Intent intent = new Intent(activity, PhotoSelectActivity.class);
        intent.putExtra(Constant.INTENT_MAX_PHOTO, maxSize);
        intent.putExtra(Constant.INTENT_MODE_MEDIA, modeMedia);
        intent.putExtra(Constant.INTENT_MODE_LOCAL, modeLocal);
        activity.startActivityForResult(intent, REQUEST_PHOTO_LIST);
    }
}

