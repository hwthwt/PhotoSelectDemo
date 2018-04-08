package com.zhaoshuang.photoselectlib.bean;

import java.util.ArrayList;

/**
 * Created by zhaoshuang on 17/9/1.
 */

public class DirPhotoModel {

    private String dirName;
    private ArrayList<FileModel> photoList;

    public DirPhotoModel(String dirName, ArrayList<FileModel> photoList) {
        this.dirName = dirName;
        this.photoList = photoList;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public ArrayList<FileModel> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(ArrayList<FileModel> photoList) {
        this.photoList = photoList;
    }

    @Override
    public boolean equals(Object o) {

        return dirName.equals(o);
    }

}
