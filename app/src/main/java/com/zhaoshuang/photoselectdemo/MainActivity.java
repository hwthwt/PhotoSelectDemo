package com.zhaoshuang.photoselectdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhaoshuang.photoselectlib.util.Constant;
import com.zhaoshuang.photoselectlib.util.PhotoSelectIntent;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && data!=null){
            int type = data.getIntExtra(Constant.RESULT_TYPE, Constant.RESULT_TYPE_PHOTO);
            switch (type){
                case Constant.RESULT_TYPE_PHOTO: {

                    ArrayList<String> photoList = (ArrayList<String>) data.getSerializableExtra(Constant.RESULT_PHOTOS);
                    StringBuilder sb = new StringBuilder("已选中相片: \n ");
                    for (String path : photoList) {
                        sb.append(path + "\n");
                    }
                    textView.setText(sb.toString());
                }
                    break;
                case Constant.RESULT_TYPE_VIDEO: {

                    String videoPath = data.getStringExtra(Constant.RESULT_VIDEO_PATH);
                    textView.setText("已选中视频: \n "+videoPath);
                }
                    break;
            }
        }
    }

    public void openAlbum(View v){

        PhotoSelectIntent.gotoPhotoSelectActivity(this, Constant.MODE_MEDIA_ALL, Constant.MODE_LOCAL_ALL, 1);
    }
}
