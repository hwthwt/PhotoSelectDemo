package com.zhaoshuang.photoselectlib.viewModle;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.View;

import com.zhaoshuang.photoselectlib.R;
import com.zhaoshuang.photoselectlib.adapter.DirListAdapter;
import com.zhaoshuang.photoselectlib.adapter.PhotoSelectAdapter;
import com.zhaoshuang.photoselectlib.bean.DirPhotoModel;
import com.zhaoshuang.photoselectlib.bean.FileModel;
import com.zhaoshuang.photoselectlib.modle.IPhotoPhotoModel;
import com.zhaoshuang.photoselectlib.modle.PhotoPhotoModel;
import com.zhaoshuang.photoselectlib.util.Constant;
import com.zhaoshuang.photoselectlib.util.PhotoWeakReference;
import com.zhaoshuang.photoselectlib.view.IPhotoActivityView;
import com.zhaoshuang.photoselectlib.view.PhotoPreviewActivity;

import java.util.ArrayList;

/**
 * Created by zhaoshuang on 2018/2/27.
 * MVVM: ViewModel
 */

public class PhotoPhotoSelectVM implements IPhotoPhotoModel {

    private Activity mActivity;
    private IPhotoActivityView mPhotoActivityView;
    private DirListAdapter dirListAdapter;
    private PhotoSelectAdapter photoSelectAdapter;

    private int modeMedia;
    private int modeLocal;
    private final PhotoPhotoModel photoPhotoModel;
    private int maxPhotoCount;

    public PhotoPhotoSelectVM(Activity activity, IPhotoActivityView photoActivityView, PhotoSelectAdapter photoSelectAdapter, DirListAdapter dirListAdapter){

        this.mActivity = activity;
        this.mPhotoActivityView = photoActivityView;
        this.photoSelectAdapter = photoSelectAdapter;
        this.dirListAdapter = dirListAdapter;

        initIntent();
        photoPhotoModel = new PhotoPhotoModel();
        photoPhotoModel.loadData(activity, this, modeMedia);

        PhotoOnItemClickListener photoOnItemClickListener = new PhotoOnItemClickListener();
        photoSelectAdapter.setOnItemClickListener(photoOnItemClickListener);

        dirListAdapter.setOnItemClickListener(new DirListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, DirPhotoModel dirPhotoModel) {

                changePhotoListData(dirPhotoModel.getPhotoList());
                mPhotoActivityView.onNotifyUI(dirPhotoModel.getDirName());
            }
        });
    }



    @Override
    public void onLoadPhotoDataSucc(int mediaType, ArrayList<FileModel> photoList) {
        addLocalIcon();
        photoSelectAdapter.notifyDataSetChanged(photoList);
        mPhotoActivityView.onPhotoUISucc(0);
    }

    @Override
    public void onLoadDirDataSucc(int mediaType, ArrayList<DirPhotoModel> dirList) {
        dirListAdapter.notifyDataSetChanged(dirList);
        mPhotoActivityView.onPhotoUISucc(1);
    }

    @Override
    public void onLoadPhotoDataFail() {
        addLocalIcon();
        mPhotoActivityView.onPhotoUIFail();
    }

    class PhotoOnItemClickListener implements PhotoSelectAdapter.OnItemClickListener{

        @Override
        public void onItemClick(View view, int position, FileModel fileModel) {
            switch (fileModel.getType()){
                case FileModel.TYPE_CLICK_CAMERA:
                    break;
                case FileModel.TYPE_CLICK_VIDEO:
                    break;
                case FileModel.TYPE_IMAGE:
                    if(maxPhotoCount == 1){
                        ArrayList<String> photoList = new ArrayList<>();
                        photoList.add(fileModel.getFile().getAbsolutePath());
                        mPhotoActivityView.onFinishActivityByPhoto(photoList);
                    }else {
                        startPreviewUI(view, true, photoPhotoModel.getImageList().indexOf(fileModel));
                    }
                    break;
                case FileModel.TYPE_VIDEO:
                    mPhotoActivityView.onFinishActivityByVideo(fileModel.getFile().getAbsolutePath());
                    break;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (resultCode == Activity.RESULT_OK && data!=null) {
            switch (requestCode){
                //查看大图返回
                case Constant.REQUEST_PREVIEW_ACTIVITY:
                    ArrayList<FileModel> checkList = PhotoWeakReference.getCheckList();
                    boolean isFinish = data.getBooleanExtra(Constant.INTENT_FINISH, false);
                    if(isFinish){
                        ArrayList<String> list = new ArrayList<>();
                        for (FileModel fm : checkList){
                            list.add(fm.getFile().getAbsolutePath());
                        }
                        mPhotoActivityView.onFinishActivityByPhoto(list);
                    }else{
                        mPhotoActivityView.onNotifyCheckPhotoUI(checkList);
                    }
                    break;
                case Constant.REQUEST_CAMERA_PHOTO:
                    finishResultData(data);
                    break;
                case Constant.REQUEST_CAMERA_VIDEO:
                    finishResultData(data);
                    break;
            }
        }
    }

    private void finishResultData(Intent data){

        String imagePath = data.getStringExtra("imagePath");
        String videoPath = data.getStringExtra("videoPath");
        if(!TextUtils.isEmpty(imagePath)){
            ArrayList<String> photoList = new ArrayList<>();
            photoList.add(imagePath);
            mPhotoActivityView.onFinishActivityByPhoto(photoList);
        }else if(!TextUtils.isEmpty(videoPath)){
            mPhotoActivityView.onFinishActivityByVideo(videoPath);
        }
    }

    private void initIntent(){

        Intent intent = mActivity.getIntent();
        modeMedia = intent.getIntExtra(Constant.INTENT_MODE_MEDIA, Constant.MODE_MEDIA_IMAGE);
        modeLocal = intent.getIntExtra(Constant.INTENT_MODE_LOCAL, Constant.MODE_LOCAL_NOT);
        maxPhotoCount = intent.getIntExtra(Constant.INTENT_MAX_PHOTO, Constant.MAX_SELECT_PHOTO_COUNT);
    }

    public void changePhotoListData(ArrayList<FileModel> list){

        photoPhotoModel.getDataList().clear();
        photoPhotoModel.getDataList().addAll(list);
        addLocalIcon();
        photoSelectAdapter.notifyDataSetChanged(photoPhotoModel.getDataList());
    }


    /**
     * 显示预览界面
     */
    public void startPreviewUI(View view, boolean isAllPhoto, int index){

        ArrayList<FileModel> photoList = null;
        if(isAllPhoto){
            photoList = photoPhotoModel.getImageList();
        }else{
            photoList = photoSelectAdapter.getCheckList();
        }

        showPreViewUI(view, photoList, index);
    }

    private void showPreViewUI(View view, ArrayList<FileModel> photoList, int index){

        if(photoList.size() == 0){
            mPhotoActivityView.onShowToast("", R.string.PhotoSelectActivity_dont_select_image);
            return ;
        }

        ArrayList<FileModel> checkList = photoSelectAdapter.getCheckList();

        Intent intent = new Intent(mActivity, PhotoPreviewActivity.class);
        intent.putExtra(Constant.INTENT_INDEX, index);
        intent.putExtra(Constant.INTENT_MAX_PHOTO, maxPhotoCount);

        PhotoWeakReference.setPhotoList(photoList);
        PhotoWeakReference.setCheckList(checkList);

        if(view != null) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeClipRevealAnimation(view,
                    0, 0, view.getWidth(), view.getHeight());
            ActivityCompat.startActivityForResult(mActivity, intent, Constant.REQUEST_PREVIEW_ACTIVITY, compat.toBundle());
        }else{
            mActivity.startActivityForResult(intent, Constant.REQUEST_PREVIEW_ACTIVITY);
        }
    }

    /**
     * 添加拍摄和录制入口
     */
    private void addLocalIcon(){

        if(modeLocal == Constant.MODE_LOCAL_ALL){
            photoPhotoModel.getDataList().add(0, new FileModel(null, FileModel.TYPE_CLICK_CAMERA, 0));
            photoPhotoModel.getDataList().add(1, new FileModel(null, FileModel.TYPE_CLICK_VIDEO, 0));
        }else if(modeLocal == Constant.MODE_LOCAL_CAMERA){
            photoPhotoModel.getDataList().add(0, new FileModel(null, FileModel.TYPE_CLICK_CAMERA, 0));
        }else if(modeLocal == Constant.MODE_LOCAL_VIDEO){
            photoPhotoModel.getDataList().add(1, new FileModel(null, FileModel.TYPE_CLICK_VIDEO, 0));
        }else if(modeLocal == Constant.MODE_LOCAL_NOT){

        }
    }
}
