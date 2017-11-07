package com.gavin.city.recycletopdock;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by wx_bin on 2017/8/24.
 * 上拉加载更多，在Activity中RecyclerView监听OnScrollListener
 * 然后在onLoadMore回调中调用加载数据的操作
 * 可以用于实现顶部停靠悬浮效果在onScolled毁掉中操作
 * 更多联系:
 * 邮箱：wx_bin@sina.com
 * 个人网站：www.wulias.com
 */

public abstract class EndLessOnScrollListener extends RecyclerView.OnScrollListener {

    //声明一个LinearLayoutManager
    private LinearLayoutManager mLinearLayoutManager;

    //当前页，从0开始
    private int currentPage = 0;
    //已经加载出来的Item的数量
    private int totalItemCount;

    //主要用来存储上一个totalItemCount
    private int previousTotal = 0;

    //在屏幕上可见的item数量
    private int visibleItemCount;

    //在屏幕可见的Item中的第一个
    private int firstVisibleItem;

    //是否正在上拉数据
    private boolean loading = true;

    public EndLessOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        onRScrolled(recyclerView, dx, dy, firstVisibleItem);
    }

    /**
     * 监听滑动，得到第一个可见Item
     * @param recyclerView
     * @param dx
     * @param dy
     * @param firstVisibleItem 第一个可见Item
     */
    public abstract void onRScrolled(RecyclerView recyclerView, int dx, int dy, int firstVisibleItem);
}