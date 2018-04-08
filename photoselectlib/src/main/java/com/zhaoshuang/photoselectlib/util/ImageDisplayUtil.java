package com.zhaoshuang.photoselectlib.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zhaoshuang.photoselectlib.R;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by zhaoshuang on 2018/3/16.
 */

public class ImageDisplayUtil {



    public static void load(Context context, File resource, ImageView imageView) {

        MultiTransformation<Bitmap> bitmapMultiTransformation = new MultiTransformation<>(new CenterCrop(), new RoundedCorners(12));

        RequestOptions myOptions = new RequestOptions().centerCrop().transform(bitmapMultiTransformation).placeholder(R.drawable.default_photo);

        Glide.with(context).asBitmap().load(resource).apply(myOptions).into(imageView);
    }

    public static void load(Context context, int resource, ImageView imageView) {

        MultiTransformation<Bitmap> bitmapMultiTransformation = new MultiTransformation<>(new CenterCrop(), new RoundedCorners(12));

        RequestOptions myOptions = new RequestOptions().centerCrop().transform(bitmapMultiTransformation).placeholder(R.drawable.default_photo);

        Glide.with(context).asBitmap().load(resource).apply(myOptions).into(imageView);
    }

    public static void load(Context context, File file, PhotoView iv_photo, boolean isGif) {

        if(isGif) {
            Glide.with(context).asGif().load(file).into(iv_photo);
        }else{
            Glide.with(context).asBitmap().load(file).into(iv_photo);
        }
    }
}
