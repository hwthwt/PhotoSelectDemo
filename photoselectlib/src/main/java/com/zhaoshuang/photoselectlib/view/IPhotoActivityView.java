package com.zhaoshuang.photoselectlib.view;

import com.zhaoshuang.photoselectlib.bean.FileModel;

import java.util.ArrayList;

/**
 * Created by zhaoshuang on 2018/2/27.
 */

public interface IPhotoActivityView {

    //相册UI成功
    void onPhotoUISucc(int mediaType);

    //相册UI失败
    void onPhotoUIFail();

    //关闭activity
    void onFinishActivityByPhoto(ArrayList<String> resultList);

    //关闭activity
    void onFinishActivityByVideo(String path);

    //刷新UI
    void onNotifyUI(String dirName);

    //刷新选中相片UI
    void onNotifyCheckPhotoUI(ArrayList<FileModel> newCheckList);

    //显示toast
    void onShowToast(String text, int id);
}
