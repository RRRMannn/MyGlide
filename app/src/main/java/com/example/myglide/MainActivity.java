package com.example.myglide;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.easyglide.RequestManager;

import java.util.Set;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MyRecycleAdapter adapter;

    String portfolios = "[{\"portfolioNo\":6,\"projectName\":\"哈哈哈\",\"type\":\"哈哈哈\",\"imgPath\":\"0IMG_20190523_162742.jpg\",\"context\":\"牛逼牛逼\"}, {\"portfolioNo\":7,\"projectName\":\"微扁平化时钟\",\"type\":\"UI设计\",\"imgPath\":\"mmexport1560226535025.jpg\",\"context\":\"微扁平化设计\"}, {\"portfolioNo\":1,\"projectName\":\"认真脸\",\"type\":\"哈哈哈\",\"imgPath\":\"4e2579f8dc4174fc.jpg\",\"context\":\"认真脸\"}, {\"portfolioNo\":2,\"projectName\":\"扁平化相机\",\"type\":\"UI设计\",\"imgPath\":\"0mmexport1554196894459.jpg\",\"context\":\"扁平化设计\"}, {\"portfolioNo\":3,\"projectName\":\"微扁平化相机\",\"type\":\"UI设计\",\"imgPath\":\"mmexport1554196898034.jpg\",\"context\":\"微扁平化设计\"}, {\"portfolioNo\":4,\"projectName\":\"写实相机\",\"type\":\"UI设计\",\"imgPath\":\"mmexport1554196901314.jpg\",\"context\":\"写实设计，没有想象中那么好看\"}, {\"portfolioNo\":15,\"projectName\":\"简单的点击事件\",\"type\":\"UI设计\",\"imgPath\":\"mmexport1560226542261.jpg\",\"context\":\"不错不错\"}, {\"portfolioNo\":8,\"projectName\":\"极简滑块\",\"type\":\"UI设计\",\"imgPath\":\"mmexport1560226536303.jpg\",\"context\":\"极简化设计\"}, {\"portfolioNo\":9,\"projectName\":\"响应式滑块\",\"type\":\"UI设计\",\"imgPath\":\"mmexport1560226537243.jpg\",\"context\":\"极简设计\"}, {\"portfolioNo\":10,\"projectName\":\"调节式滑块\",\"type\":\"UI设计\",\"imgPath\":\"mmexport1560226546396.jpg\",\"context\":\"极简设计\"}, {\"portfolioNo\":11,\"projectName\":\"极简按钮\",\"type\":\"UI设计\",\"imgPath\":\"mmexport1560226545363.jpg\",\"context\":\"极简设计\"}, {\"portfolioNo\":12,\"projectName\":\"高光按钮\",\"type\":\"UI设计\",\"imgPath\":\"mmexport1560226544458.jpg\",\"context\":\"不是很好看\"}, {\"portfolioNo\":13,\"projectName\":\"水晶按钮\",\"type\":\"UI设计\",\"imgPath\":\"mmexport1560226543537.jpg\",\"context\":\"水晶透明的感觉，挺满意\"}, {\"portfolioNo\":14,\"projectName\":\"底部导航栏\",\"type\":\"UI设计\",\"imgPath\":\"mmexport1560226540086.jpg\",\"context\":\"中规中矩\"}, {\"portfolioNo\":16,\"projectName\":\"仿KTV终端界面\",\"type\":\"UI设计\",\"imgPath\":\"mmexport1560226539102.jpg\",\"context\":\"一般一般啦\"}, {\"portfolioNo\":17,\"projectName\":\"应用引导界面\",\"type\":\"UI设计\",\"imgPath\":\"mmexport1560226538206.jpg\",\"context\":\"还行吧\"}]";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化MyGlide图片加载框架
        RequestManager.getInstance();

        if(!EasyPermissions.hasPermissions(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            EasyPermissions.requestPermissions(this,"需要读写外存，用于缓存图片",256, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
        }

        if(!EasyPermissions.hasPermissions(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            EasyPermissions.requestPermissions(this,"需要读写外存，用于缓存图片",256, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
        }

        recyclerView = findViewById(R.id.RV);
        adapter = new MyRecycleAdapter(this, portfolios);

        /**
         * RecycleView的各种花式布局都在此设置
         */
//        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        GridLayoutManager layoutManager=new GridLayoutManager(this,2){
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
            }
        };

//        MyLayoutManager layoutManager = new MyLayoutManager();

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestManager.getInstance().freeAll();
    }
}
