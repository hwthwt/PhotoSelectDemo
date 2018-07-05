package com.zhaoshuang.photoselectlib.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhaoshuang.photoselectlib.R;
import com.zhaoshuang.photoselectlib.bean.FileModel;
import com.zhaoshuang.photoselectlib.util.ImageDisplayUtil;
import com.zhaoshuang.photoselectlib.util.ToastUtil;
import com.zhaoshuang.photoselectlib.util.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zhaoshuang on 17/8/24.
 */

public class PhotoSelectAdapter extends RecyclerView.Adapter {

    private int maxPhotoSize;
    private Activity activity;
    private ArrayList<FileModel> dataList;
    private int imgWidth;
    private ArrayList<FileModel> checkList = new ArrayList<>();
    private View.OnClickListener onCheckClickListener;
    private OnItemClickListener onItemClickListener;
    private boolean selectImageMode;

    public PhotoSelectAdapter(Activity activity, ArrayList<FileModel> dataList, int maxPhotoSize){

        this.activity = activity;
        this.dataList = dataList;
        this.maxPhotoSize = maxPhotoSize;

        int margin = (int) activity.getResources().getDimension(R.dimen.basic_activity_margin);
        int dp3 = (int) activity.getResources().getDimension(R.dimen.dp3);
        imgWidth = (Utils.getWindowWidth(activity)-margin*2-dp3)/3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(activity, R.layout.item_photo, null));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position, FileModel fileModel);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final MyViewHolder vh = (MyViewHolder) holder;
        final FileModel fileModel = dataList.get(position);

        vh.iv_not_click.setVisibility(View.GONE);

        switch (fileModel.getType()){
            //拍摄照片
            case FileModel.TYPE_CLICK_CAMERA:
                vh.iv_check.setVisibility(View.GONE);
                vh.tv_time.setVisibility(View.GONE);
                vh.iv_cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClick(view, position, fileModel);
                    }
                });
                Glide.with(activity).clear(vh.iv_cover);
                vh.iv_cover.setImageResource(R.drawable.camera_select);

                break;
            ///录制视频
            case FileModel.TYPE_CLICK_VIDEO:
                vh.iv_check.setVisibility(View.GONE);
                vh.tv_time.setVisibility(View.GONE);
                vh.iv_cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClick(view, position, fileModel);
                    }
                });
                Glide.with(activity).clear(vh.iv_cover);
                vh.iv_cover.setImageResource(R.drawable.video_select);
                if(checkList.size() > 0){
                    vh.iv_not_click.setVisibility(View.VISIBLE);
                }

                break;
            case FileModel.TYPE_IMAGE:
                vh.tv_time.setVisibility(View.GONE);

                if(maxPhotoSize == 1){
                    vh.iv_check.setVisibility(View.GONE);
                }else{
                    vh.iv_check.setVisibility(View.VISIBLE);
                    if(checkList.contains(fileModel)){
                        vh.iv_check.setImageResource(R.mipmap.select_check);
                    }else{
                        vh.iv_check.setImageResource(R.mipmap.select_not);
                    }

                    vh.iv_check.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(checkList.contains(fileModel)){
                                checkList.remove(fileModel);
                                vh.iv_check.setImageResource(R.mipmap.select_not);
                                if(checkList.size()==0){
                                    selectImageMode = false;
                                    notifyDataSetChanged();
                                }
                            }else if(checkList.size() < maxPhotoSize){
                                checkList.add(fileModel);
                                vh.iv_check.setImageResource(R.mipmap.select_check);
                                if(checkList.size() > 0 && !selectImageMode){
                                    selectImageMode = true;
                                    notifyDataSetChanged();
                                }
                            }else{
                                ToastUtil.show(activity, activity.getString(R.string.PhotoSelectAdapter_max_num));
                            }
                            onCheckClickListener.onClick(view);
                        }
                    });
                }

                setCoverImg(vh, fileModel, position);

                break;
            case FileModel.TYPE_VIDEO:
                vh.tv_time.setVisibility(View.VISIBLE);

                if(maxPhotoSize == 1){
                    vh.iv_check.setVisibility(View.GONE);
                }else{
                    vh.iv_check.setVisibility(View.VISIBLE);
                }

                SimpleDateFormat format = new SimpleDateFormat("mm:ss");
                String timeStr = format.format(new Date(fileModel.getDuration()));
                vh.tv_time.setText(timeStr);

                setCoverImg(vh, fileModel, position);

                if(checkList.size() > 0){
                    vh.iv_not_click.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    public void setOnCheckClickListener(View.OnClickListener onCheckClickListener){
        this.onCheckClickListener = onCheckClickListener;
    }

    public ArrayList<FileModel> getCheckList(){
        return checkList;
    }

    private void setCoverImg(MyViewHolder vh, final FileModel fileModel, final int position){

        File file = fileModel.getFile();

        if(file.exists()) {
            ImageDisplayUtil.load(activity, fileModel.getFile(), vh.iv_cover);
        }else{
            ImageDisplayUtil.load(activity, R.drawable.default_photo, vh.iv_cover);
        }

        vh.iv_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position, fileModel);
            }
        });
    }

    public void notifyDataSetChanged(ArrayList<FileModel> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(dataList == null){
            return 0;
        }else {
            return dataList.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_cover;
        private ImageView iv_check;
        private TextView tv_time;
        private ImageView iv_not_click;

        public MyViewHolder(View itemView) {
            super(itemView);

            ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imgWidth);
            itemView.setLayoutParams(layoutParams1);

            iv_cover = itemView.findViewById(R.id.iv_cover);
            iv_check = itemView.findViewById(R.id.iv_check);
            tv_time = itemView.findViewById(R.id.tv_time);
            iv_not_click = itemView.findViewById(R.id.iv_not_click);
        }
    }
}
