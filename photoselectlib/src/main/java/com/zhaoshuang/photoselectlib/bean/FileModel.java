package com.zhaoshuang.photoselectlib.bean;

import java.io.File;

/**
 * Created by zhaoshuang on 17/8/25.
 */

public class FileModel {

    public static final int TYPE_IMAGE = 100;
    public static final int TYPE_VIDEO = 101;

    public static final int TYPE_CLICK_CAMERA = 102;
    public static final int TYPE_CLICK_VIDEO = 103;

    private File file;
    //文件类型
    private int type;
    //文件类型字符串
    private String typeStr;
    //秒
    private long modifiedTime;

    //视频时长
    private long duration;

    public FileModel() {
    }

    public FileModel(File file, int type, long createTime) {
        this.file = file;
        this.type = type;
        this.modifiedTime = createTime;
    }

    public FileModel(File file, int type, String typeStr, long modifiedTime, long duration) {
        this.file = file;
        this.type = type;
        this.typeStr = typeStr;
        this.modifiedTime = modifiedTime;
        this.duration = duration;
    }

    public FileModel(File file, int type, String typeStr, long modifiedTime) {
        this.file = file;
        this.type = type;
        this.typeStr = typeStr;
        this.modifiedTime = modifiedTime;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
