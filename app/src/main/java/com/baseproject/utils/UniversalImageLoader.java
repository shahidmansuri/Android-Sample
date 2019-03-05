package com.baseproject.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.baseproject.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by ZEBRONICS-1 on 1/29/2018.
 */

public class UniversalImageLoader {
    private final ImageLoader imageLoader;
    Context context;

    public UniversalImageLoader(Context context) {
        this.context = context;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance(); // Get singleton instance
    }

    public void setSimpleImage(String imageUri, ImageView imageView) {
        // Load image, decode it to Bitmap and display Bitmap in ImageView (or any other view
        //	which implements ImageAware interface)
        imageLoader.displayImage(imageUri, imageView);
    }

    public void setImageBitmap(String imageUri, final ImageView imageView) {
        imageLoader.loadImage(imageUri, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                imageView.setImageBitmap(loadedImage);
                // Do whatever you want with Bitmap
            }
        });
    }

    public void setImageProgress(String imageUri, final ImageView imageView) {
        /*DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();

        imageLoader.displayImage("http://hdwpro.com/wp-content/uploads/2017/11/Natural-Wallpaper.jpg", imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mBinder.pbImageLoader.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mBinder.pbImageLoader.setVisibility(View.GONE);
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.no_media));
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mBinder.pbImageLoader.setVisibility(View.GONE);
                imageView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                mBinder.pbImageLoader.setVisibility(View.VISIBLE);
                if ((total != 0) && (current != 0)) {
                    mBinder.pbImageLoader.setProgress(Math.round(100.0f * current / total));
                }
            }
        });*/
    }

}
