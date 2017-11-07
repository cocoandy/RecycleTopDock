package com.gavin.city.recycletopdock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wx_bin on 2017/9/10.
 * 对recycleview adapter进行简单封装
 * 实现添加多个头部和底部
 * 实现点击功能
 * 实现长按功能
 * 更多联系:
 * 邮箱：wx_bin@sina.com
 * 个人网站：www.wulias.com
 * <p>
 * 注意：在有头部的时候，局部刷新会出现问题，问题的原因是因为局部刷新传的position是List的position但是不是Recycle Item的position
 * getPosition()
 * notifyMItemChanged()
 * notifyMItemRemoved()
 * notifyMItemInserted()
 */

public abstract class BaseRecycleAdapter<E extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<E> {
    public static final int TYPE_NORMAL = -99;
    public Context context;
    public List mDatas;
    ArrayList<View> headviews = new ArrayList();
    ArrayList<View> footviews = new ArrayList();
    OnItemClickListener itemClickListener;//点击事件
    OnItemLongClickListener itemLongClickListener;//长按事件

    public BaseRecycleAdapter(Context context, List mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public E onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != TYPE_NORMAL) {
            if (headviews.size() > 0 && viewType < headviews.size())
                return (E) new ViewHolder(headviews.get(viewType));
            if (footviews.size() > 0 && viewType >= (getItemCount() - footviews.size()))
                return (E) new ViewHolder(footviews.get(footviews.size() - (getItemCount() - viewType)));
        }
        return onCreateViewHolders(parent, viewType);
    }

    @Override
    public void onBindViewHolder(final E holder, int index) {
        if (getItemCounts() == 0) return;
        final int position = index % getItemCounts();
        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getItemViewType(position) == TYPE_NORMAL) {//数据内容
                        itemClickListener.onItemClick((ViewHolder) holder, getRealPosition(holder));
                    } else if (position < headviews.size()) {//头部点击
                        itemClickListener.onHeadClick((ViewHolder) holder, position);
                    } else {
                        itemClickListener.onFootClick((ViewHolder) holder, footviews.size() - (getItemCounts() - position));
                    }

                }
            });
        }
        if (itemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (getItemViewType(position) == TYPE_NORMAL) {//数据内容
                        return itemLongClickListener.onItemLongClick((ViewHolder) holder, getRealPosition(holder));
                    } else if (position < headviews.size()) {//头部长按
                        return itemLongClickListener.onHeadLongClick((ViewHolder) holder, position);
                    } else {//底部长按
                        return itemLongClickListener.onFootLongClick((ViewHolder) holder, footviews.size() - (getItemCounts() - position));
                    }

                }
            });

        }
        if (getItemViewType(position) != TYPE_NORMAL) return;
        onBindViewHolders(holder, getRealPosition(holder));
    }

    @Override
    public int getItemViewType(int index) {//数据类型//后面多布局的时候最好不要用数字来标记
        if (getItemCounts() == 0) return TYPE_NORMAL;
        int position = index % getItemCounts();
        if (headviews.size() > position) return position;
        if (position >= (getItemCounts() - footviews.size())) return position;
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {//设置数据源的大小 数据大小+头部+底部
        return mDatas.size() + headviews.size() + footviews.size();
    }

    public int getItemCounts() {//设置数据源的大小 数据大小+头部+底部
        return mDatas.size() + headviews.size() + footviews.size();
    }

    /**
     * ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
//            if ((getRealPosition(this)+headviews.size())!=TYPE_NORMAL) return;
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        if (getItemCounts() == 0) return 0;
        int position = holder.getLayoutPosition() % getItemCounts();
        return position - headviews.size();
    }

    public abstract E onCreateViewHolders(ViewGroup parent, int viewType);

    public abstract void onBindViewHolders(E holder, int position);

    public interface OnItemClickListener {
        void onItemClick(BaseRecycleAdapter.ViewHolder holder, int position);

        void onHeadClick(BaseRecycleAdapter.ViewHolder holder, int position);

        void onFootClick(BaseRecycleAdapter.ViewHolder holder, int position);

    }

    interface OnItemLongClickListener {
        boolean onItemLongClick(BaseRecycleAdapter.ViewHolder holder, int position);

        boolean onHeadLongClick(BaseRecycleAdapter.ViewHolder holder, int position);

        boolean onFootLongClick(BaseRecycleAdapter.ViewHolder holder, int position);
    }

    public int getPosition(int positon) {
        return headviews.size() + positon;
    }

    /**
     * 自定义item刷新
     *
     * @param positon
     */
    public void notifyMItemChanged(int positon) {
        notifyItemChanged(getPosition(positon));
    }

    /**
     * 自定义删除item刷新
     *
     * @param positon
     */
    public void notifyMItemRemoved(int positon) {
        notifyItemRemoved(positon + headviews.size());
    }

    /**
     * 自定义添加item局部刷新
     *
     * @param positon
     */
    public void notifyMItemInserted(int positon) {
        notifyItemInserted(positon + headviews.size());
    }


    /**
     * 添加数据
     *
     * @param datas
     */
    public void addDatas(List datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 点击事件
     *
     * @param itemClickListener
     */
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 长按事件
     */
    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    /**
     * 添加头部
     *
     * @param headerView
     */
    public void addHeaderView(View headerView) {
        headviews.add(headerView);
        notifyItemInserted(headviews.size() - 1);
    }

    /**
     * 添加底部
     *
     * @param footView
     */
    public void addFootView(View footView) {
        footviews.add(footView);
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * 移除HeaderView
     *
     * @param view
     */
    public void removeHeadViewItem(View view) {
        for (int i = 0; i < headviews.size(); i++) {
            if (headviews.get(i) == view) {
                headviews.remove(i);
                this.notifyItemRemoved(i);
                return;
            }
        }
    }

    /**
     * 移除FootView
     *
     * @param view
     */
    public void removeFootViewItem(View view) {
        for (int i = 0; i < footviews.size(); i++) {
            if (headviews.get(i) == view) {
                headviews.remove(i);
                this.notifyItemRemoved(getItemCount() - footviews.size() + i);
                return;
            }
        }
    }
}
