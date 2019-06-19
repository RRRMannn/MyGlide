package com.example.easyglide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;

public class BitmapDispatcher extends Thread {

    //主线程
    private Handler handler = new Handler(Looper.getMainLooper());

    //阻塞队列，对请求队列的引用
    private LinkedBlockingQueue<BitmapRequest> requestQueue;

    public BitmapDispatcher(LinkedBlockingQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                //从队列中获取图片请求
                BitmapRequest br = requestQueue.take();
                //设置预加载图片
                showLoadingImage(br);
                //加载图片
                Bitmap bitmap = findBitmap(br);
                //将图片显示到ImageView
                showImageView(br, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void showImageView(final BitmapRequest br, final Bitmap bitmap) {
        if (bitmap != null && br.getImageView() != null && br.getUrlMd5().equals(br.getImageView().getTag())) {
            final ImageView imageView = br.getImageView();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (br.getRequestListener() != null) {
                        RequestListener requestListener = br.getRequestListener();
                        requestListener.onSuccess(bitmap);
                    }
                    imageView.setImageBitmap(bitmap);
                }
            });
        }
    }

    private Bitmap findBitmap(BitmapRequest br) {
        Bitmap bitmap = downloadBitmap(br.getUrl());
        RequestManager.getInstance().bitmap2MemoryCache(br.getUrlMd5(), bitmap);
        RequestManager.getInstance().bitmap2DiskCache(br.getUrlMd5() + ".png", bitmap);
        return bitmap;
    }

    private Bitmap downloadBitmap(String uri) {

        //保存到文件所使用的流对象
        FileOutputStream fos = null;

        //数据读取流
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            //创建一个URL对象
            URL url = new URL(uri);
            //使用HttpURLConnection通过URL读取数据
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    private void showLoadingImage(BitmapRequest br) {
        final int resId = br.getResId();
        final ImageView imageView = br.getImageView();
        if (resId > 0 && imageView != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(resId);
                }
            });
        }
    }
}
