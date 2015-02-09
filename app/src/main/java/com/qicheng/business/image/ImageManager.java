package com.qicheng.business.image;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.qicheng.R;
import com.qicheng.framework.util.StringUtil;
import com.qicheng.util.Const;

/**
 * Created by NO1 on 2015/2/5.
 */
public class ImageManager {


    private static DisplayImageOptions getDefaultCacheDisplayOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_photo) // resource or drawable
                .showImageForEmptyUri(R.drawable.ic_default_photo) // resource or drawable
                .showImageOnFail(R.drawable.ic_default_photo) // resource or drawable
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        return options;
    }

    private static DisplayImageOptions getPortraitDisplayOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_portrait) // resource or drawable
                .showImageForEmptyUri(R.drawable.ic_default_portrait) // resource or drawable
                .showImageOnFail(R.drawable.ic_default_portrait) // resource or drawable
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        return options;
    }

    private static DisplayImageOptions getDetailCacheDisplayOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_photo) // resource or drawable
                .showImageForEmptyUri(R.drawable.ic_default_photo) // resource or drawable
                .showImageOnFail(R.drawable.ic_default_photo) // resource or drawable
                .cacheInMemory(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .cacheOnDisk(true)
                .build();
        return options;
    }

    /**
     * 下载显示头像
     *
     * @param portraitUri
     * @param imageView
     */
    public static void displayPortrait(String portraitUri, ImageAware imageView) {
        if (StringUtil.isEmpty(portraitUri)) {
            imageView.setImageDrawable(Const.Application.getResources().getDrawable(R.drawable.ic_default_portrait));
            return;
        }
        displayImageComplete(portraitUri, imageView, getPortraitDisplayOption(), null, null);
    }

    /**
     * 显示原图
     *
     * @param originUri   原图所在URL
     * @param imageView   显示的ImageView
     * @param thumbnail   缩略图Drawable，可为null
     * @param progressBar 进度条，可为null
     */
    public static void displayImageXlarge(String originUri, ImageAware imageView, final Drawable thumbnail, final ProgressBar progressBar) {
        if (StringUtil.isEmpty(originUri)) {
            imageView.setImageDrawable(Const.Application.getResources().getDrawable(R.drawable.ic_default_photo));
            return;
        }
        displayImageComplete(originUri, imageView, getDetailCacheDisplayOption(),
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        if (progressBar != null) {
                            progressBar.setProgress(0);
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        if (thumbnail != null)
                            ((ImageView) view).setImageDrawable(thumbnail);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        if (progressBar != null)
                            progressBar.setProgress(Math.round(100.0f * current / total));
                    }
                });
    }

    /**
     * 基本的图片下载与显示方法
     *
     * @param imageUri
     * @param imageView
     */
    public static void displayImageDefault(String imageUri, ImageAware imageView) {

        displayImageComplete(imageUri, imageView, getDefaultCacheDisplayOption(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
//                holder.progressBar.setProgress(0);
//                holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                holder.progressBar.setVisibility(View.GONE);
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
//                holder.progressBar.setProgress(Math.round(100.0f * current / total));
            }
        });
    }

    /**
     * 可完全自定义的图片下载显示方法
     *
     * @param imageUri
     * @param imageView
     * @param options
     * @param loadingListener
     * @param progressListener
     */
    public static void displayImageComplete(String imageUri, ImageAware imageView, DisplayImageOptions options, ImageLoadingListener loadingListener, ImageLoadingProgressListener progressListener) {
        ImageLoader.getInstance().displayImage(imageUri, imageView, options, loadingListener, progressListener);
    }


}
