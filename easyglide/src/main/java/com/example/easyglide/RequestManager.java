package com.example.easyglide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestManager {

    //单例模式
    private static RequestManager requestManager;

    private RequestManager() {
        bitmapMap = new ConcurrentHashMap<>();
        start();
    }

    public static RequestManager getInstance() {
        if (requestManager == null) {
            synchronized (RequestManager.class) {
                if (requestManager == null) {
                    requestManager = new RequestManager();
                }
            }
        }

        return requestManager;
    }

    //创建图片请求队列
    private LinkedBlockingQueue<BitmapRequest> requests = new LinkedBlockingQueue<>();

    /**
     * 将图片请求添加到队列
     *
     * @param bitmapRequest 一个图片请求
     */
    public void addBitmapRequest(BitmapRequest bitmapRequest) {
        if (bitmapRequest == null) {
            return;
        }

        File diskCachePicture = null;
        try {
            diskCachePicture = new File(Environment.getExternalStorageDirectory().getCanonicalPath() + "/MyGlide/" + bitmapRequest.getUrlMd5() + ".png");

            if (bitmapMap != null && bitmapMap.containsKey(bitmapRequest.getUrlMd5())) {
                //从内存中获取图片
                Log.e("RequestManager>>>>>", "从内存加载图片...");
                bitmapRequest.getImageView().setImageBitmap(bitmapMap.get(bitmapRequest.getUrlMd5()));
            } else if (diskCachePicture.exists()) {
                //从硬盘中获取图片
                Log.e("RequestManager>>>>>", "从外存加载图片...");
                bitmapRequest.getImageView().setImageURI(Uri.fromFile(diskCachePicture));
                //添加缓存
                Bitmap bitmap = BitmapFactory.decodeFile(diskCachePicture.getPath());
                RequestManager.getInstance().bitmap2MemoryCache(bitmapRequest.getUrlMd5(), bitmap);
            } else {
                Log.e("RequestManager>>>>>", "从网络加载图片...");
                if (!requests.contains(bitmapRequest)) {
                    requests.add(bitmapRequest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //线程数组（相当于服务窗口）
    private BitmapDispatcher[] bitmapDispatchers;

    /**
     * 开始服务
     */
    public void start() {
        stop();
        startAllDispatcher();
    }

    /**
     * 开启所有窗口服务
     */
    private void startAllDispatcher() {
        //获取单个应用最大线程数
        int threadCount = Runtime.getRuntime().availableProcessors();
        bitmapDispatchers = new BitmapDispatcher[threadCount];
        for (int i = 0; i < threadCount; i++) {
            bitmapDispatchers[i] = new BitmapDispatcher(requests);
            bitmapDispatchers[i].start();
        }
    }

    /**
     * 停止服务
     */
    public void stop() {
        if (bitmapDispatchers != null && bitmapDispatchers.length > 0) {
            for (BitmapDispatcher bitmapDispatcher : bitmapDispatchers) {
                if (!bitmapDispatcher.isInterrupted()) {
                    bitmapDispatcher.interrupt();
                }
            }
        }
    }

    /**
     * 释放资源，防止内存泄漏
     */
    public void freeAll(){
        //停止服务
        stop();
        //释放资源
        bitmapMap = null;
        requestManager = null;
        requests = null;
    }

    //内存缓存机制
    private ConcurrentHashMap<String, Bitmap> bitmapMap;

    /**
     * 添加图片内存缓存
     *
     * @param key   图片标识
     * @param value 图片bitmap
     */
    public void bitmap2MemoryCache(String key, Bitmap value) {
        if (bitmapMap.size() > 20) {
            bitmapMap.clear();
        }
        if (!bitmapMap.containsKey(key)) {
            bitmapMap.put(key, value);
        }
    }

    /**
     * 添加图片外存缓存
     *
     * @param fileName 图片名称
     * @param bitmap   图片bitmap
     */
    public void bitmap2DiskCache(String fileName, Bitmap bitmap) {
        try {
            File diskCache = new File(Environment.getExternalStorageDirectory().getCanonicalPath() + "/MyGlide");
            //文件夹不存在，则创建它
            if (!diskCache.exists()) {
                diskCache.mkdirs();
            }
            File diskCachePicture = new File(diskCache, fileName);
            //文件夹不存在，则创建它
            if (!diskCachePicture.exists()) {
                FileOutputStream fileOutputStream = new FileOutputStream(diskCachePicture);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.close();
                fileOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
