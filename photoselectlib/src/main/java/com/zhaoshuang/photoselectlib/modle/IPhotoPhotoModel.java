package com.zhaoshuang.photoselectlib.modle;


import com.zhaoshuang.photoselectlib.bean.DirPhotoModel;
import com.zhaoshuang.photoselectlib.bean.FileModel;

import java.util.ArrayList;

/**
 * Created by zhaoshuang on 2018/2/28.
 */

public interface IPhotoPhotoModel {

    //相册数据成功
    void onLoadPhotoDataSucc(int mediaType, ArrayList<FileModel> photoList);

    //文件夹数据成功
    void onLoadDirDataSucc(int mediaType, ArrayList<DirPhotoModel> dirList);

    //相册数据加载失败
    void onLoadPhotoDataFail();
}
