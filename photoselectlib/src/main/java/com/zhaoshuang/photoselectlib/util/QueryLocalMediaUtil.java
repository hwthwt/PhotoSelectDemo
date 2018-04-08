package com.zhaoshuang.photoselectlib.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.zhaoshuang.photoselectlib.bean.FileModel;

import java.io.File;
import java.util.ArrayList;

/**
 * 查询本地媒体文件 如图片或视频
 * Created by zhaoshuang on 2017/10/30.
 */

public class QueryLocalMediaUtil {

    public static ArrayList<FileModel> queryImage(Context context){

        ArrayList<FileModel> dataList = new ArrayList<>();

        ContentResolver mContentResolver = context.getContentResolver();

        Cursor imageCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                "mime_type=? or mime_type=? or mime_type=?",
                new String[] {"image/jpeg", "image/png", "image/gif"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc");

        if(imageCursor==null || imageCursor.getCount()==0){
            return dataList;
        }

        while (imageCursor.moveToNext()) {
            String path = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            long date = imageCursor.getLong(imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
            String type = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
            int size = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE));

            if(TextUtils.isEmpty(path) || size==0){
                continue;
            }

            File file = new File(path);
            FileModel fileModel = new FileModel(file, FileModel.TYPE_IMAGE, type, date);
            dataList.add(fileModel);
        }
        imageCursor.close();

        return dataList;
    }

    public static ArrayList<FileModel> queryVideo(Context context){

        ArrayList<FileModel> dataList = new ArrayList<>();

        ContentResolver mContentResolver = context.getContentResolver();

        Cursor videoCursor = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
                "mime_type=?",
                new String[] {"video/mp4"},
                MediaStore.Video.Media.DATE_MODIFIED + " desc");

        if(videoCursor==null || videoCursor.getCount()==0){
            return dataList;
        }

        while (videoCursor.moveToNext()) {
            String path = videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Video.Media.DATA));
            long date = videoCursor.getLong(videoCursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED));
            long duration = videoCursor.getInt(videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            String type = videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
            int size = videoCursor.getInt(videoCursor.getColumnIndex(MediaStore.Images.Media.SIZE));

            if(TextUtils.isEmpty(path) || size==0 || duration==0){
                continue;
            }

            File file = new File(path);
            FileModel fileModel = new FileModel(file, FileModel.TYPE_VIDEO, type, date, duration);
            dataList.add(fileModel);
        }
        videoCursor.close();

        return dataList;
    }
}
