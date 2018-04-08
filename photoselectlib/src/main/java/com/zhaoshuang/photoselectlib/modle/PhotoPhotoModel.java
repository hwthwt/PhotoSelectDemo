package com.zhaoshuang.photoselectlib.modle;

import android.content.Context;

import com.zhaoshuang.photoselectlib.R;
import com.zhaoshuang.photoselectlib.bean.DirPhotoModel;
import com.zhaoshuang.photoselectlib.bean.FileModel;
import com.zhaoshuang.photoselectlib.util.Constant;
import com.zhaoshuang.photoselectlib.util.QueryLocalMediaUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhaoshuang on 2018/2/27.
 * MVVM: Modler
 */

public class PhotoPhotoModel {

    private ArrayList<FileModel> dataList = new ArrayList<>();
    private ArrayList<FileModel> allList = new ArrayList<>();
    private ArrayList<FileModel> imageList = new ArrayList<>();
    private ArrayList<FileModel> videoList = new ArrayList<>();
    private ArrayList<DirPhotoModel> mediaDirList = new ArrayList<>();

    public ArrayList<FileModel> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<FileModel> dataList) {
        this.dataList = dataList;
    }

    public ArrayList<FileModel> getAllList() {
        return allList;
    }

    public void setAllList(ArrayList<FileModel> allList) {
        this.allList = allList;
    }

    public ArrayList<FileModel> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<FileModel> imageList) {
        this.imageList = imageList;
    }

    public ArrayList<FileModel> getVideoList() {
        return videoList;
    }

    public void setVideoList(ArrayList<FileModel> videoList) {
        this.videoList = videoList;
    }

    public ArrayList<DirPhotoModel> getMediaDirList() {
        return mediaDirList;
    }

    public void setMediaDirList(ArrayList<DirPhotoModel> mediaDirList) {
        this.mediaDirList = mediaDirList;
    }

    public void loadData(final Context context, final IPhotoPhotoModel photoModel, final int modeMedia){

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                switch (modeMedia) {
                    case Constant.MODE_MEDIA_ALL:
                        imageList = QueryLocalMediaUtil.queryImage(context);
                        videoList = QueryLocalMediaUtil.queryVideo(context);

                        allList.addAll(imageList);
                        for (int i = 0; i < videoList.size(); i++) {
                            FileModel fileModel = videoList.get(i);
                            int insertPosition = getInsertPosition(allList, fileModel.getModifiedTime(), 0, allList.size()-1);
                            allList.add(insertPosition, fileModel);
                        }

                        mediaDirList.add(new DirPhotoModel(context.getString(R.string.string_id_all_file), allList));
                        mediaDirList.add(new DirPhotoModel(context.getString(R.string.string_id_all_picture), imageList));
                        mediaDirList.add(new DirPhotoModel(context.getString(R.string.string_id_all_video), videoList));
                        break;
                    case Constant.MODE_MEDIA_IMAGE:
                        imageList = QueryLocalMediaUtil.queryImage(context);

                        allList.addAll(imageList);
                        mediaDirList.add(new DirPhotoModel(context.getString(R.string.string_id_all_picture), imageList));
                        break;
                    case Constant.MODE_MEDIA_VIDEO:
                        videoList = QueryLocalMediaUtil.queryVideo(context);

                        allList.addAll(videoList);
                        mediaDirList.add(new DirPhotoModel(context.getString(R.string.string_id_all_video), videoList));
                        break;
                }
                dataList.clear();
                dataList.addAll(allList);

                //更新相册列表
                e.onNext(0);

                classDir();

                ArrayList<DirPhotoModel> deleteList = new ArrayList<>();
                for (DirPhotoModel bean : mediaDirList){
                    if(bean.getPhotoList().size()==0){
                        deleteList.add(bean);
                    }
                }
                for (DirPhotoModel bean : deleteList){
                    mediaDirList.remove(bean);
                }

                //更新文件夹列表
                e.onNext(1);
            }
        })
        .subscribeOn(Schedulers.computation())//耗费cpu的线程
        .observeOn(AndroidSchedulers.mainThread())
        .safeSubscribe(new Observer<Integer>(){
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            @Override
            public void onNext(@NonNull Integer result) {
                switch (result){
                    case 0:
                        photoModel.onLoadPhotoDataSucc(result, dataList);
                        break;
                    case 1:
                        photoModel.onLoadDirDataSucc(result, mediaDirList);
                        break;
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }
            @Override
            public void onComplete() {
                photoModel.onLoadPhotoDataFail();
            }
        });
    }

    //初始化文件夹列表
    private void classDir(){

        for (FileModel fileModel : allList){

            String name = fileModel.getFile().getParentFile().getName();

            boolean isExist = false;
            for (DirPhotoModel m : mediaDirList){
                if(m.equals(name)){
                    isExist = true;
                    m.getPhotoList().add(fileModel);
                }
            }

            if(!isExist){
                ArrayList<FileModel> list = new ArrayList<>();
                list.add(fileModel);
                DirPhotoModel dirPhotoModel = new DirPhotoModel(name, list);
                mediaDirList.add(dirPhotoModel);
            }
        }
    }

    //二分查找
    private int getInsertPosition(List<FileModel> list, long date, int start, int end) {

        int mid = (start + end) / 2;
        if (start == end) {
            return mid;
        }
        FileModel fileModel = list.get(mid);
        if (date > fileModel.getModifiedTime()) {
            return getInsertPosition(list, date, start, mid);
        } else if (date < fileModel.getModifiedTime()) {
            return getInsertPosition(list, date, mid + 1, end);
        } else {
            return mid;
        }
    }
}
