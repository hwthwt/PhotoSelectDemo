package com.zhaoshuang.photoselectlib.viewModle;

import android.app.Activity;
import android.content.Intent;

import com.zhaoshuang.photoselectlib.R;
import com.zhaoshuang.photoselectlib.adapter.PreviewAdapter;
import com.zhaoshuang.photoselectlib.bean.FileModel;
import com.zhaoshuang.photoselectlib.modle.IPhotoPreviewModle;
import com.zhaoshuang.photoselectlib.modle.PhotoPreviewModle;
import com.zhaoshuang.photoselectlib.util.Constant;
import com.zhaoshuang.photoselectlib.util.PhotoWeakReference;
import com.zhaoshuang.photoselectlib.view.IPhotoPreViewActivityView;

import java.util.ArrayList;

/**
 * Created by zhaoshuang on 2018/2/28.
 * MVVM: ViewModle
 */

public class PhotoPreviewVM implements IPhotoPreviewModle {

    private Activity mActivity;
    private PhotoPreviewModle photoPreviewModle;
    private IPhotoPreViewActivityView photoPreViewActivityView;
    private PreviewAdapter previewAdapter;
    private int currentIndex;
    private int maxPhoto;

    public PhotoPreviewVM(Activity activity, IPhotoPreViewActivityView photoPreViewActivityView, PreviewAdapter previewAdapter){

        this.mActivity = activity;
        this.photoPreViewActivityView = photoPreViewActivityView;
        this.previewAdapter = previewAdapter;

        Intent intent = mActivity.getIntent();
        currentIndex = intent.getIntExtra(Constant.INTENT_INDEX, 0);
        maxPhoto = intent.getIntExtra(Constant.INTENT_MAX_PHOTO, 0);

        photoPreviewModle = new PhotoPreviewModle();
        photoPreviewModle.loadData(this);
    }

    public void onCheckClick(int clickPosition){

        boolean checkState = false;
        boolean isMaxCount = false;

        FileModel fileModel = photoPreviewModle.getPhotoList().get(clickPosition);
        if(photoPreviewModle.getNewCheckList().contains(fileModel)){
            photoPreviewModle.getNewCheckList().remove(fileModel);
            checkState = false;
            isMaxCount = false;
        }else if(photoPreviewModle.getNewCheckList().size() < maxPhoto){
            photoPreviewModle.getNewCheckList().add(fileModel);
            checkState = true;
            isMaxCount = false;
        }else{
            isMaxCount = true;
        }
        photoPreViewActivityView.onClickCheckButton(isMaxCount, checkState, photoPreviewModle.getNewCheckList().size());
    }

    public void finishActivity(boolean isFinish){

        PhotoWeakReference.setCheckList(photoPreviewModle.getNewCheckList());

        if(isFinish && photoPreviewModle.getNewCheckList().size() == 0){
            photoPreViewActivityView.onShowToast("", R.string.PhotoPreviewActivity_not_photo);
        }else{
            photoPreViewActivityView.onFinishActivity(isFinish);
        }
    }

    public void scrollPagerChangeState(int currentIndex) {

        if(photoPreviewModle.getPhotoList().size() > currentIndex) {
            String currentIndexStr = (currentIndex + 1) + "/" + photoPreviewModle.getPhotoList().size();
            if (photoPreviewModle.getNewCheckList().contains(photoPreviewModle.getPhotoList().get(currentIndex))) {
                photoPreViewActivityView.onPageScrollCheckChange(true, currentIndexStr);
            } else {
                photoPreViewActivityView.onPageScrollCheckChange(false, currentIndexStr);
            }
        }
    }

    @Override
    public void onLoadPhotoDataSucc(ArrayList<FileModel> photoList, ArrayList<FileModel> newCheckList) {

        scrollPagerChangeState(currentIndex);
        previewAdapter.notifyDataSetChanged(photoList);
        photoPreViewActivityView.onPhotoUISucc(newCheckList.size());
    }

    @Override
    public void onLoadPhotoDataFail() {

        photoPreViewActivityView.onPhotoUIFail();
    }
}
