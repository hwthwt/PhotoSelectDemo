package com.zhaoshuang.photoselectlib.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoshuang.photoselectlib.R;
import com.zhaoshuang.photoselectlib.bean.DirPhotoModel;
import com.zhaoshuang.photoselectlib.bean.FileModel;
import com.zhaoshuang.photoselectlib.util.ImageDisplayUtil;
import com.zhaoshuang.photoselectlib.view.PhotoSelectActivity;

import java.util.ArrayList;

/**
 * Created by zhaoshuang on 17/8/31.
 * 相册文件夹列表的adapter
 */

public class DirListAdapter extends RecyclerView.Adapter {

    private PhotoSelectActivity activity;
    private ArrayList<DirPhotoModel> dataList;
    private OnItemClickListener onItemClickListener;

    public DirListAdapter(PhotoSelectActivity activity, ArrayList<DirPhotoModel> dataList){
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(activity, R.layout.item_dir_list, null));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position, DirPhotoModel dirPhotoModel);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        MyViewHolder vh = (MyViewHolder) holder;
        final DirPhotoModel dirPhotoModel = dataList.get(position);
        ArrayList<FileModel> photoList = dirPhotoModel.getPhotoList();

        if(photoList==null || photoList.size()==0){
            return ;
        }

        final FileModel fileModel = photoList.get(0);
        ImageDisplayUtil.load(activity, fileModel.getFile(), vh.iv_cover);

        vh.tv_dir_name.setText(dirPhotoModel.getDirName()+"  "+"("+photoList.size()+")");

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position, dirPhotoModel);
            }
        });
    }

    public void notifyDataSetChanged(ArrayList<DirPhotoModel> dataList){
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
        private TextView tv_dir_name;
        private View itemView;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            this.itemView = itemView;
            iv_cover = itemView.findViewById(R.id.iv_cover);
            tv_dir_name = itemView.findViewById(R.id.tv_dir_name);
        }
    }
}
