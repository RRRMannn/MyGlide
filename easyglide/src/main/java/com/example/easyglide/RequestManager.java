package com.example.easyglide;

import java.util.concurrent.LinkedBlockingQueue;

public class RequestManager {

    //单例模式
    private static RequestManager requestManager;

    private RequestManager() {
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
        if (!requests.contains(bitmapRequest)) {
            requests.add(bitmapRequest);
        }
    }

    //线程数组（相当于服务窗口）
    BitmapDispatcher[] bitmapDispatchers;

    /**
     * 开始服务
     */
    private void start() {
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
    private void stop() {
        if (bitmapDispatchers != null && bitmapDispatchers.length > 0) {
            for (BitmapDispatcher bitmapDispatcher : bitmapDispatchers) {
                if (!bitmapDispatcher.isInterrupted()) {
                    bitmapDispatcher.interrupt();
                }
            }
        }
    }

}
