package com.example.myglide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easyglide.MyGlide;
import com.example.easyglide.RequestListener;
import com.example.myglide.bean.Portfolio;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyHolder> {

    private Context context;
    private ArrayList<Portfolio> portfolios;

    public MyRecycleAdapter(Context context,String json){
        this.context=context;
        portfolios=new ArrayList<>();
        Gson gson=new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements=jsonParser.parse(json).getAsJsonArray();
        for (JsonElement jsonElement : jsonElements) {
            portfolios.add(gson.fromJson(jsonElement, Portfolio.class));
        }
    }

    /**
     * 初始化控件
     */
    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        Context context;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            imageView=itemView.findViewById(R.id.PI);
            textView=itemView.findViewById(R.id.TT);
        }
    }

    /**
     * 创建viewholder
     * 引入xml给viewholder
     */
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parant, int i) {
        View view= LayoutInflater.from(parant.getContext()).inflate(R.layout.picture_item,parant,false);
        MyHolder myHolder=new MyHolder(view);
        return myHolder;
    }

    /**
     *操作Item的地方
     */
    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {
        myHolder.textView.setText(portfolios.get(i).getProjectName());
        myHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(myHolder.context,Main2Activity.class);
                myHolder.context.startActivity(intent);
            }
        });
        MyGlide.with(myHolder.context).load("http://120.78.148.54/images/portfolio/"+portfolios.get(i).getImgPath()).listener(new RequestListener() {
            @Override
            public boolean onSuccess(Bitmap bitmap) {
                Log.e("MyRecycleAdapter>>>>","成功获取图片！");
                return false;
            }

            @Override
            public boolean onFailure() {
                Log.e("MyRecycleAdapter>>>>","获取图片失败！");
                return false;
            }
        }).into(myHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return portfolios.size();
    }
}
