package com.zhaoshuang.photoselectlib.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaoshuang.photoselectlib.R;
import com.zhaoshuang.photoselectlib.adapter.DirListAdapter;
import com.zhaoshuang.photoselectlib.adapter.PhotoSelectAdapter;
import com.zhaoshuang.photoselectlib.bean.FileModel;
import com.zhaoshuang.photoselectlib.util.Constant;
import com.zhaoshuang.photoselectlib.util.PhotoWeakReference;
import com.zhaoshuang.photoselectlib.util.StatusBarUtil;
import com.zhaoshuang.photoselectlib.util.ToastUtil;
import com.zhaoshuang.photoselectlib.viewModle.PhotoPhotoSelectVM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoshuang on 17/8/24.
 * MVVM: View
 */

public class PhotoSelectActivity extends Activity implements IPhotoActivityView {

    private Activity mContext;

    private PhotoSelectAdapter photoSelectAdapter;
    private DirListAdapter dirListAdapter;

    private int maxPhotoSize;
    private ValueAnimator va;

    private PhotoPhotoSelectVM photoSelectVM;
    private ImageView iv_close;
    private RelativeLayout rl_down;
    private TextView tv_title;
    private ImageView iv_down;
    private RelativeLayout rl_content;
    private RecyclerView rv_photo;
    private RecyclerView rv_dir;
    private ProgressBar pb_pro;
    private RelativeLayout rl_bottom;
    private TextView tv_preview;
    private LinearLayout ll_finish;
    private TextView tv_check_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_open_anim, R.anim.activity_stay);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);

        mContext = this;

        StatusBarUtil.setColor(mContext, Color.WHITE, true);

        initUI();
        initIntent();
        initData();

        pb_pro.setVisibility(View.VISIBLE);
        photoSelectVM = new PhotoPhotoSelectVM(mContext, PhotoSelectActivity.this, photoSelectAdapter, dirListAdapter);
    }

    private void initUI(){

        iv_close = findViewById(R.id.iv_close);
        rl_down = findViewById(R.id.rl_down);
        tv_title = findViewById(R.id.tv_title);
        iv_down = findViewById(R.id.iv_down);
        rl_content = findViewById(R.id.rl_content);
        rv_photo = findViewById(R.id.rv_photo);
        rv_dir = findViewById(R.id.rv_dir);
        pb_pro = findViewById(R.id.pb_pro);
        rl_bottom = findViewById(R.id.rl_bottom);
        tv_preview = findViewById(R.id.tv_preview);
        ll_finish = findViewById(R.id.ll_finish);
        tv_check_num = findViewById(R.id.tv_check_num);
    }

    private void initIntent(){

        Intent intent = getIntent();
        maxPhotoSize = intent.getIntExtra(Constant.INTENT_MAX_PHOTO, Constant.MAX_SELECT_PHOTO_COUNT);
    }

    private void initData() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        rv_photo.setLayoutManager(gridLayoutManager);
        photoSelectAdapter = new PhotoSelectAdapter(mContext, null, maxPhotoSize);
        rv_photo.setAdapter(photoSelectAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rv_dir.setLayoutManager(linearLayoutManager);
        dirListAdapter = new DirListAdapter(PhotoSelectActivity.this, null);
        rv_dir.setAdapter(dirListAdapter);

        photoSelectAdapter.setOnCheckClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<FileModel> checkList = photoSelectAdapter.getCheckList();
                tv_check_num.setText(checkList.size()+"");
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoSelectVM.startPreviewUI(null, false, 0);
            }
        });

        rl_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDirList();
            }
        });
        rl_down.setClickable(false);

        ll_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                for (FileModel fm : photoSelectAdapter.getCheckList()){
                    list.add(fm.getFile().getAbsolutePath());
                }
                finishPhoto(list);
            }
        });
    }

    /**
     * 更改相册数据并刷新
     * @param name 相册文件夹
     */
    public void changePhotoListUI(String name){

        tv_title.setText(name);
        changeDirList();
    }

    /**
     * 显示图片文件夹列表
     */
    private void changeDirList(){

        if(va != null){
            return ;
        }

        if(rv_dir.isShown()) {
            rl_bottom.setVisibility(View.VISIBLE);

            iv_down.setImageResource(R.drawable.down_select);
            dirAnim(false);
        }else{
            rv_dir.setVisibility(View.VISIBLE);
            rl_bottom.setVisibility(View.GONE);

            iv_down.setImageResource(R.drawable.top_select);
            dirAnim(true);
        }
    }

    //文件夹列表显示时的动画
    private void dirAnim(final boolean isShow){

        if(isShow){
            va = ValueAnimator.ofInt(-rl_content.getHeight(), 0).setDuration(300);
        }else{
            va = ValueAnimator.ofInt(0, -rl_content.getHeight()).setDuration(300);
        }

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                rv_dir.setY(value);
            }
        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(!isShow) {
                    rv_dir.setVisibility(View.GONE);
                }
                va = null;
            }
        });
        va.start();
    }

    private void finishVideo(String path){

        if(!TextUtils.isEmpty(path)){
            Intent intent = new Intent();
            intent.putExtra(Constant.RESULT_VIDEO_PATH, path);
            intent.putExtra(Constant.RESULT_TYPE, Constant.RESULT_TYPE_VIDEO);
            setResult(RESULT_OK, intent);
            finish();
        }else{
            ToastUtil.show(mContext, R.string.PhotoSelectActivity_video_edit_fail);
        }
    }

    /**
     * 设置返回数据, 并关闭界面
     * @param resultList
     */
    private void finishPhoto(ArrayList<String> resultList){

        if(resultList.size() == 0){
            ToastUtil.show(mContext, R.string.PhotoSelectActivity_not_click_image);
            return ;
        }

        Intent intent = new Intent();
        intent.putExtra(Constant.RESULT_PHOTOS, resultList);
        intent.putExtra(Constant.RESULT_TYPE, Constant.RESULT_TYPE_PHOTO);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void notifyCheckListUI(ArrayList<FileModel> newCheckList) {

        ArrayList<FileModel> checkList = photoSelectAdapter.getCheckList();
        checkList.clear();
        checkList.addAll(newCheckList);
        photoSelectAdapter.notifyDataSetChanged();

        tv_check_num.setText(checkList.size()+"");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        photoSelectVM.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_stay, R.anim.activity_close_anim);
    }

    @Override
    public void onBackPressed() {
        if(rv_dir.getVisibility() == View.VISIBLE){
            dirAnim(false);
            return ;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PhotoWeakReference.cancel();
    }


    @Override
    public void onPhotoUISucc(int mediaType) {

        switch (mediaType){
            case 0:
                pb_pro.setVisibility(View.GONE);
                break;
            case 1:
                rl_down.setClickable(true);
                break;
        }
    }

    @Override
    public void onPhotoUIFail() {

        pb_pro.setVisibility(View.GONE);
    }

    @Override
    public void onFinishActivityByPhoto(ArrayList<String> resultList) {

        finishPhoto(resultList);
    }

    @Override
    public void onFinishActivityByVideo(String path) {

        finishVideo(path);
    }

    @Override
    public void onNotifyUI(String dirName) {

        changePhotoListUI(dirName);
    }

    @Override
    public void onNotifyCheckPhotoUI(ArrayList<FileModel> newCheckList) {

        notifyCheckListUI(newCheckList);
    }


    @Override
    public void onShowToast(String text, int id) {

        if(!TextUtils.isEmpty(text)){
            ToastUtil.show(mContext, text);
        }else if(id != 0){
            ToastUtil.show(mContext, id);
        }
    }
}
