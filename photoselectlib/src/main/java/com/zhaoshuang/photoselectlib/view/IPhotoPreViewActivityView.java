package com.zhaoshuang.photoselectlib.view;

/**
 * Created by zhaoshuang on 2018/2/27.
 */

public interface IPhotoPreViewActivityView {

    //相册UI成功
    void onPhotoUISucc(int checkCount);

    //相册UI失败
    void onPhotoUIFail();

    //关闭activity
    void onFinishActivity(boolean isFinish);

    //点击选择button
    void onClickCheckButton(boolean isMaxCount, boolean checkState, int checkNum);

    //页面滑动时选择button的改变
    void onPageScrollCheckChange(boolean checkState, String currentIndexStr);

    //显示toast
    void onShowToast(String text, int id);
}
