package com.example.fanfeng.recyclerheaddemo;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan on 2017/9/7.
 */

public abstract class ClassAdapter<C extends RecyclerView.ViewHolder, S extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_CONTENT = 0;
    //存放班级header布局所在的position
    private List<Integer> mHeaderIndex = new ArrayList<>();

    /**
     * 是否为头布局
     * @param position
     * @return
     */
    private boolean isHeaderView(int position){

        return mHeaderIndex.contains(new Integer(position));

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            //班级header布局
            return onCreateHeaderViewHolder(parent, viewType);
        }else {
            //学生布局
            return onCreateContentViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //如果是head布局
        if(isHeaderView(position)){     //班级header布局填充
            onBindHeaderViewHolder((C)holder, getHeadRealCount(position));
        }else {      //学生信息填充
            //根据position获取position对应的学生所在班级
            int classId = getStudentOfClass(position);
            //获取改班级head所在的position位置
            int classOfPosition = mHeaderIndex.get(classId);
            //根据当前位置position和班级head布局的position，计算当前学生在班级中的位置
            int studentIndex = position - classOfPosition - 1;
            onBindContentViewHolder((S)holder, classId, studentIndex);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(isHeaderView(position)){
            return TYPE_HEADER;
        }else{
            return TYPE_CONTENT;
        }
    }

    /**
     * 条目的总数量
     * @return
     */
    @Override
    public int getItemCount() {
        mHeaderIndex.clear();
        int count = 0;
        int headSize = getHeadersCount();
        for (int i = 0; i < headSize; i++) {
            if(i != 0){
                count++;
            }
            mHeaderIndex.add(new Integer(count));

            count += getContentCountForHeader(i);
        }
        Log.e("fan", "--getItemCount:" + count + "--headSize" + headSize);
        return count + 1;
    }

    /**
     * 获取position是第几个头布局
     * @param position
     * @return
     */
    private int getHeadRealCount(int position){
        return mHeaderIndex.indexOf(new Integer(position));
    }

    /**
     * 根据value获取所属的key
     * @return
     */
    private int getStudentOfClass(int position){

        for (int i = 0; i < mHeaderIndex.size(); i++) {
            if(mHeaderIndex.get(i) > position){
                return i-1;
            }
        }

        return mHeaderIndex.size() - 1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

                @Override
                public int getSpanSize(int position) {

                    int viewType = getItemViewType(position);
                    if(viewType == TYPE_HEADER){
                        return ((GridLayoutManager) layoutManager).getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder){
        int position = holder.getLayoutPosition();
        if (isHeaderView(position))
        {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams)
            {

                StaggeredGridLayoutManager.LayoutParams p =
                        (StaggeredGridLayoutManager.LayoutParams) lp;

                p.setFullSpan(true);
            }
        }
    }

    /**
     * 头布局的总数
     * @return
     */
    public abstract int getHeadersCount();

    /**
     * 头布局对应内容的总数（也就是改头布局里面有多少条item）
     * @param headerPosition 第几个头布局
     * @return
     */
    public abstract int getContentCountForHeader(int headerPosition);

    /**
     * 创建头布局
     * @param parent
     * @param viewType
     * @return
     */
    public abstract C onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    /**
     * 创建内容布局
     * @param parent
     * @param viewType
     * @return
     */
    public abstract S onCreateContentViewHolder(ViewGroup parent, int viewType);

    /**
     * 填充头布局的数据
     * @param holder
     * @param position
     */
    public abstract void onBindHeaderViewHolder(C holder, int position);

    /**
     * 填充
     * @param holder
     * @param HeaderPosition
     * @param ContentPositionForHeader
     */
    public abstract void onBindContentViewHolder(S holder, int HeaderPosition, int ContentPositionForHeader);

}
