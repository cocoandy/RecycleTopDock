package com.gavin.city.recycletopdock;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LinearLayoutManager mLayoutManager;    //recycle布局管理
    MRecycleAdapter mAdapter;
    RecyclerView mRecyclerView;
    Context mContext;
    List<DataInfo> mDatas = new ArrayList<>();
    TextView headText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle);
        headText = (TextView) findViewById(R.id.tv_header_view);
        mContext = this;
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MRecycleAdapter(mContext, mDatas);
        mRecyclerView.setAdapter(mAdapter);

        EndLessOnScrollListener mEndLessOnScrollListener = new EndLessOnScrollListener(mLayoutManager) {

            @Override
            public void onRScrolled(RecyclerView recyclerView, int dx, int dy, int firstVisibleItem) {
                if (firstVisibleItem >= 1) {//如果添加有头部则 >= 头部个数
                    headText.setVisibility(View.VISIBLE);
                    View stickyInfoView = recyclerView.findChildViewUnder(headText.getMeasuredWidth() / 2, 5);

                    if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                        headText.setText(String.valueOf(stickyInfoView.getContentDescription()));
                    }

                    View transInfoView = recyclerView.findChildViewUnder(
                            headText.getMeasuredWidth() / 2, headText.getMeasuredHeight() + 1);

                    if (transInfoView != null && transInfoView.getTag() != null) {

                        int transViewStatus = (int) transInfoView.getTag();
                        int dealtY = transInfoView.getTop() - headText.getMeasuredHeight();

                        if (transViewStatus == mAdapter.HAS_STICKY_VIEW) {
                            if (transInfoView.getTop() > 0) {
                                headText.setTranslationY(dealtY);
                            } else {
                                headText.setTranslationY(0);
                            }
                        } else if (transViewStatus == mAdapter.NONE_STICKY_VIEW) {
                            headText.setTranslationY(0);
                        }
                    }
                } else {//头部的时候隐藏
                    headText.setVisibility(View.GONE);
                }
            }
        };

        mRecyclerView.addOnScrollListener(mEndLessOnScrollListener);
        mAdapter.setItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecycleAdapter.ViewHolder holder, int position) {
//                mDatas.add(new DataInfo(
//                        "2017年6月12日", "Soly" , "women" , "good" ));
                mDatas.remove(6);
                mAdapter.notifyItemRemoved(6);
            }

            @Override
            public void onHeadClick(BaseRecycleAdapter.ViewHolder holder, int position) {
                mDatas.get(0).setName("Zhang san");
                mAdapter.notifyItemChanged(mAdapter.getPosition(0));
            }

            @Override
            public void onFootClick(BaseRecycleAdapter.ViewHolder holder, int position) {
                mDatas.add(new DataInfo(
                        "2017年6月12日", "Soly", "women", "good"));
                mAdapter.notifyItemInserted(mAdapter.getPosition(mDatas.size()-1));
            }
        });

        View head = LayoutInflater.from(mContext).inflate(R.layout.view_head, mRecyclerView, false);
        View foot = LayoutInflater.from(mContext).inflate(R.layout.view_foot, mRecyclerView, false);
        mAdapter.addHeaderView(head);
        mAdapter.addFootView(foot);
    }

    public void initData() {
        for (int index = 0; index < 50; index++) {
            if (index < 5) {
                mDatas.add(new DataInfo(
                        "2017年6月12日", "Soly" + index, "women", "good" + index));
            } else if (index < 15) {
                mDatas.add(new DataInfo(
                        "2017年8月1日", "Gavin" + index, "man" + index, "good" + index));
            } else if (index < 25) {
                mDatas.add(new DataInfo(
                        "2017年11月11日", "Sandy" + index, "man" + index, "good" + index));
            } else {
                mDatas.add(new DataInfo(
                        "2017年12月25日", "Aiwen" + index, "women" + index, "good" + index));
            }
        }
    }
}
