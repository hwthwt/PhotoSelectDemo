package com.zhaoshuang.photoselectlib.modle;

import com.zhaoshuang.photoselectlib.bean.FileModel;
import com.zhaoshuang.photoselectlib.util.PhotoWeakReference;

import java.util.ArrayList;

/**
 * Created by zhaoshuang on 2018/2/27.
 * MVVM: Modler
 */

public class PhotoPreviewModle {

    private ArrayList<FileModel> photoList;
    private ArrayList<FileModel> newCheckList;

    public ArrayList<FileModel> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(ArrayList<FileModel> photoList) {
        this.photoList = photoList;
    }

    public ArrayList<FileModel> getNewCheckList() {
        return newCheckList;
    }

    public void setNewCheckList(ArrayList<FileModel> newCheckList) {
        this.newCheckList = newCheckList;
    }

    public void loadData(IPhotoPreviewModle iPhotoPreviewModle){

        photoList = PhotoWeakReference.getPhotoList();
        ArrayList<FileModel> checkList = PhotoWeakReference.getCheckList();

        newCheckList = new ArrayList<>();
        newCheckList.clear();
        newCheckList.addAll(checkList);

        if(photoList ==null || photoList.size()==0){
            iPhotoPreviewModle.onLoadPhotoDataFail();
        }else{
            iPhotoPreviewModle.onLoadPhotoDataSucc(photoList, newCheckList);
        }
    }
}
