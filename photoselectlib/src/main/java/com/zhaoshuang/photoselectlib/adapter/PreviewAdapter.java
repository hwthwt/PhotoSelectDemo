package com.zhaoshuang.photoselectlib.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.zhaoshuang.photoselectlib.R;
import com.zhaoshuang.photoselectlib.bean.FileModel;
import com.zhaoshuang.photoselectlib.util.ImageDisplayUtil;
import com.zhaoshuang.photoselectlib.view.PhotoPreviewActivity;

import java.io.File;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by zhaoshuang on 17/8/29.
 * 预览界面的adapter
 */

public class PreviewAdapter extends PagerAdapter {

    private PhotoPreviewActivity activity;
    private List<FileModel> dataList;

    public PreviewAdapter(PhotoPreviewActivity activity, List<FileModel> dataList){
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        if(dataList == null){
            return 0;
        }else {
            return dataList.size();
        }
    }

    public void notifyDataSetChanged(List<FileModel> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        FileModel fileModel = dataList.get(position);

        View inflate = View.inflate(activity, R.layout.pager_photo, null);
        PhotoView iv_photo = inflate.findViewById(R.id.iv_photo);

        File file = fileModel.getFile();
        boolean isGif = file.getName().endsWith(".gif");
        ImageDisplayUtil.load(activity, fileModel.getFile(), iv_photo, isGif);

        iv_photo.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                activity.finishResult(false);
            }
        });

        container.addView(inflate);

        return inflate;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
