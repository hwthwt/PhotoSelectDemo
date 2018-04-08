package com.zhaoshuang.photoselectlib.util;

import com.zhaoshuang.photoselectlib.bean.FileModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhaoshuang on 2017/11/14.
 */

public class PhotoWeakReference {

    private static WeakReference<ArrayList<FileModel>> photoList;
    private static WeakReference<ArrayList<FileModel>> checkList;

    public static void setPhotoList(ArrayList<FileModel> list){

        photoList = new WeakReference<>(list);
    }

    public static ArrayList<FileModel> getPhotoList(){

        if(photoList != null){
            return photoList.get();
        }
        return new ArrayList<>();
    }

    public static void setCheckList(ArrayList<FileModel> list){

        checkList = new WeakReference<>(list);
    }

    public static ArrayList<FileModel> getCheckList(){

        if(checkList != null){
            return checkList.get();
        }
        return new ArrayList<>();
    }

    public static void cancel(){
        if(photoList != null){
            photoList.clear();
            photoList = null;
        }
        if(checkList != null){
            checkList.clear();
            checkList = null;
        }
    }

}
