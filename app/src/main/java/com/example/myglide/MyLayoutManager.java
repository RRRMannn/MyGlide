package com.example.myglide;

import android.support.v7.widget.RecyclerView;

public class MyLayoutManager extends RecyclerView.LayoutManager {
    /**
     * 没有要修改itemview布局参数的话,默认以下写法
     */
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) return;

    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }
}
