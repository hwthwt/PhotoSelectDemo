package com.zhaoshuang.photoselectlib.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoshuang.photoselectlib.R;
import com.zhaoshuang.photoselectlib.adapter.PreviewAdapter;
import com.zhaoshuang.photoselectlib.util.Constant;
import com.zhaoshuang.photoselectlib.util.StatusBarUtil;
import com.zhaoshuang.photoselectlib.util.ToastUtil;
import com.zhaoshuang.photoselectlib.viewModle.PhotoPreviewVM;

/**
 * Created by zhaoshuang on 2017/11/14.
 * MVVM: view 预览界面
 */

public class PhotoPreviewActivity extends Activity implements IPhotoPreViewActivityView{

    private int currentIndex;

    private PhotoPreviewVM photoPreviewVM;
    private PreviewAdapter previewAdapter;

    private ViewPager vp_preview;
    private ImageView iv_close;
    private TextView tv_index;
    private ImageView iv_check;
    private LinearLayout ll_finish;
    private TextView tv_check_num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        initUI();
        initIntent();
        photoPreviewVM = new PhotoPreviewVM(this, this, previewAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        StatusBarUtil.setColor(this, Color.BLACK, false);
    }

    private void initUI(){

        vp_preview = findViewById(R.id.vp_preview);
        iv_close = findViewById(R.id.iv_close);
        tv_index = findViewById(R.id.tv_index);
        iv_check = findViewById(R.id.iv_check);
        ll_finish = findViewById(R.id.ll_finish);
        tv_check_num = findViewById(R.id.tv_check_num);
    }

    private void initIntent() {

        Intent intent = getIntent();

        currentIndex = intent.getIntExtra(Constant.INTENT_INDEX, 0);

        previewAdapter = new PreviewAdapter(this, null);
        vp_preview.setAdapter(previewAdapter);
    }

    private void initData(int checkCount) {

        tv_check_num.setText(checkCount+"");

        vp_preview.setCurrentItem(currentIndex);

        vp_preview.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                photoPreviewVM.scrollPagerChangeState(position);
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ll_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishResult(true);
            }
        });

        iv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = vp_preview.getCurrentItem();
                photoPreviewVM.onCheckClick(currentItem);
            }
        });
    }

    public void finishResult(boolean isFinish){

        photoPreviewVM.finishActivity(isFinish);
    }

    @Override
    public void onBackPressed() {
        finishResult(false);
    }

    @Override
    public void onPhotoUISucc(int checkCount) {
        initData(checkCount);
    }

    @Override
    public void onPhotoUIFail() {
        ToastUtil.show(this, R.string.PhotoPreviewActivity_error);
        finish();
    }

    @Override
    public void onFinishActivity(boolean isFinish) {

        Intent intent = new Intent();
        intent.putExtra(Constant.INTENT_FINISH, isFinish);
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public void onClickCheckButton(boolean isMaxCount, boolean checkState, int checkNum) {

        if(isMaxCount){
            ToastUtil.show(this, R.string.PhotoSelectAdapter_max_num);
        }else {
            if (checkState) {
                iv_check.setImageResource(R.mipmap.select_check);
            } else {
                iv_check.setImageResource(R.mipmap.select_not);
            }
            tv_check_num.setText(checkNum + "");
        }
    }

    @Override
    public void onPageScrollCheckChange(boolean checkState, String currentIndexStr) {

        tv_index.setText(currentIndexStr);
        if(checkState){
            iv_check.setImageResource(R.mipmap.select_check);
        }else{
            iv_check.setImageResource(R.mipmap.select_not);
        }
    }

    @Override
    public void onShowToast(String text, int id) {

        if(!TextUtils.isEmpty(text)){
            ToastUtil.show(this, text);
        }else if(id != 0){
            ToastUtil.show(this, id);
        }
    }
}
