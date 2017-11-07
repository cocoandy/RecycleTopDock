package com.gavin.city.recycletopdock;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class MRecycleAdapter extends BaseRecycleAdapter<MRecycleAdapter.MViewHolde> {
    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;

    public MRecycleAdapter(Context context, List mDatas) {
        super(context, mDatas);
    }

    @Override
    public MViewHolde onCreateViewHolders(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_list_item, parent, false);
        return new MViewHolde(view);
    }

    @Override
    public void onBindViewHolders(MViewHolde holder, int position) {
        if (holder instanceof MViewHolde) {
            if (position % 2 == 0) {
                holder.rlContentWrapper.setBackgroundColor(
                        ContextCompat.getColor(context, R.color.bg_a));
            } else {
                holder.rlContentWrapper.setBackgroundColor(
                        ContextCompat.getColor(context, R.color.bg_b));
            }

            DataInfo dataInfo = (DataInfo) mDatas.get(position);
            holder.tvName.setText(dataInfo.name);
            holder.tvGender.setText(dataInfo.gender);
            holder.tvProfession.setText(dataInfo.profession);

            if (position == 0) {
                holder.tvHeader.setVisibility(View.VISIBLE);
                holder.tvHeader.setText(dataInfo.topDock);
                holder.itemView.setTag(FIRST_STICKY_VIEW);
            } else {
                DataInfo dataInfo1 = (DataInfo) mDatas.get(position - 1);
                if (!TextUtils.equals(dataInfo.topDock, dataInfo1.topDock)) {
                    holder.tvHeader.setVisibility(View.VISIBLE);
                    holder.tvHeader.setText(dataInfo.topDock);
                    holder.itemView.setTag(HAS_STICKY_VIEW);
                } else {
                    holder.tvHeader.setVisibility(View.GONE);
                    holder.itemView.setTag(NONE_STICKY_VIEW);
                }
            }
            holder.itemView.setContentDescription(dataInfo.topDock);
        }
    }


    public class MViewHolde extends BaseRecycleAdapter.ViewHolder {
        public TextView tvHeader;
        public RelativeLayout rlContentWrapper;
        public TextView tvName;
        public TextView tvGender;
        public TextView tvProfession;

        public MViewHolde(View itemView) {
            super(itemView);
            tvHeader = (TextView) itemView.findViewById(R.id.tv_header_view);
            rlContentWrapper = (RelativeLayout) itemView.findViewById(R.id.rl_content_wrapper);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvGender = (TextView) itemView.findViewById(R.id.tv_gender);
            tvProfession = (TextView) itemView.findViewById(R.id.tv_profession);
        }
    }
}
