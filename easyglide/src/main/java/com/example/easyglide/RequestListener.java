package com.example.easyglide;

import android.graphics.Bitmap;

//请求监听接口
public interface RequestListener {

    //处理图片
    boolean onSuccess(Bitmap bitmap);

    boolean onFailure();
}
