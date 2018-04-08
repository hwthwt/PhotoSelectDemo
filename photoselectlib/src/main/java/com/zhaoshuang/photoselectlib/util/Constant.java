package com.zhaoshuang.photoselectlib.util;

/**
 * Created by zhaoshuang on 2018/2/27.
 */

public class Constant {

    //最大选择照片数量
    public static final int MAX_SELECT_PHOTO_COUNT = 9;

    //PreviewActivity
    public static final int REQUEST_PREVIEW_ACTIVITY = 303;

    //返回文件类型
    public static final String RESULT_TYPE = "result_type";
    //0为失败, 1是视频, 2是照片
    public static final int RESULT_TYPE_VIDEO = 1;
    public static final int RESULT_TYPE_PHOTO = 2;
    //返回上个界面视频url
    public static final String RESULT_VIDEO_PATH = "result_video_path";

    //-----------------------------------------------------------------------------------------

    //最大可选相片数量
    public static final String INTENT_MAX_PHOTO = "intent_max_photo";

    //媒体筛选媒体类型
    public static final String INTENT_MODE_MEDIA = "intent_mode_media";

    //本地拍摄或录制
    public static final String INTENT_MODE_LOCAL = "intent_mode_local";
    //本地拍照
    public static final int MODE_LOCAL_CAMERA = 500;
    //本地录制
    public static final int MODE_LOCAL_VIDEO = 501;
    //不可本地拍照和录制
    public static final int MODE_LOCAL_NOT = 502;
    //所有类型
    public static final int MODE_LOCAL_ALL = 503;

    //返回的图片集合
    public static final String RESULT_PHOTOS = "result_photos";

    //所有类型
    public static final int MODE_MEDIA_ALL = 400;
    //图片
    public static final int MODE_MEDIA_IMAGE = 401;
    //视频
    public static final int MODE_MEDIA_VIDEO = 402;

    //PreviewActivity
    public static String INTENT_INDEX = "intent_index";
    public static String INTENT_FINISH = "intent_finish";

    public static final int REQUEST_CAMERA_PHOTO = 600;
    public static final int REQUEST_CAMERA_VIDEO = 601;
}
