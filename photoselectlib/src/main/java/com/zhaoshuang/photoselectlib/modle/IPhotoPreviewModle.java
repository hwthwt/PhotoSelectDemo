package com.zhaoshuang.photoselectlib.modle;

import com.zhaoshuang.photoselectlib.bean.FileModel;

import java.util.ArrayList;

/**
 * Created by zhaoshuang on 2018/2/27.
 * MVVM: Modler
 */

public interface IPhotoPreviewModle {

    //相册数据成功
    void onLoadPhotoDataSucc(ArrayList<FileModel> photoList, ArrayList<FileModel> newCheckList);

    //相册数据加载失败
    void onLoadPhotoDataFail();
}
