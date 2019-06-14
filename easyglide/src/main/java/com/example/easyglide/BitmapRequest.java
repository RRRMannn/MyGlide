package com.example.easyglide;

import android.content.Context;
import android.media.Image;
import android.widget.ImageView;

import java.lang.ref.SoftReference;

//单个图片请求
public class BitmapRequest {

    //图片请求地址
    private String url;

    //图片加载控件
    private SoftReference<ImageView> imageView;

    //预加载图片
    private int resId;

    //图片标识,防止图片错位
    private String urlMd5;

    //回调对象
    private RequestListener requestListener;


    private Context context;

    //设置上下文
    public BitmapRequest(Context context){
        this.context=context;
    }

    //设置图片加载地址
    public BitmapRequest load(String url){
        this.url=url;
        return this;
    }

    //设置预加载图片
    public BitmapRequest loading(int resId){
        this.resId=resId;
        return this;
    }

    //设置监听
    public BitmapRequest listener(RequestListener listener){
        this.requestListener=listener;
        return this;
    }

    //加载图片
    public void into(ImageView imageView){
        imageView.setTag(this.urlMd5);
        this.imageView=new SoftReference<>(imageView);

        //将图片请求添加到请求队列
    }

    public String getUrl() {
        return url;
    }

    public ImageView getImageView() {
        return imageView.get();
    }

    public int getResId() {
        return resId;
    }

    public String getUrlMd5() {
        return urlMd5;
    }

    public RequestListener getRequestListener() {
        return requestListener;
    }

    public Context getContext() {
        return context;
    }
}
