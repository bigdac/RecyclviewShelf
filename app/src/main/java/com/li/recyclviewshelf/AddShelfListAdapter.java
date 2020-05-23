package com.li.recyclviewshelf;

import android.content.Context;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddShelfListAdapter extends RecyclerView.Adapter<AddShelfListAdapter.MyViewHolder> {


    /* 用户分组适配器*/
    private Context mContext;
    private List<MyShelf> mData;
    private int type ;
    private int width;
    public AddShelfListAdapter(Context context, List<MyShelf> list, int click , int width) {
        this.mContext = context;
        this.mData = list;
        isChecked.clear();
        isChecked.add(click);
        this.width = width;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_addshelflist, viewGroup, false);
        if (mData.get(i).getSize()==0){
            view.getLayoutParams().width = 0;
        }else {
            view.getLayoutParams().width =mData.get(i).getSize()*(width/mData.size()) ;
        }
        MyViewHolder holder = new MyViewHolder(view);
        return holder;


    }

    @Override
    public int getItemViewType(int position) {
        return  position;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        if (isChecked.contains(position)){
            myViewHolder.rl_1.setBackground(mContext.getDrawable(R.drawable.shelf_bj1));
        }else {
            myViewHolder.rl_1.setBackground(mContext.getDrawable(R.drawable.shelf_bj2));
        }
        myViewHolder.tvDeviceCodeT.setText(mData.get(position).getLength()+"");

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isChecked.contains(position)){
                    isChecked.clear();
                    isChecked.add(position);
                    notifyDataSetChanged();
                }

                itemClickListerner.onClick(view,position);
            }
        });

    }
    ArrayList<Integer> isChecked = new ArrayList<>();

    //  多选 实现多选功能
    /*多选*/
    SparseBooleanArray mSelectedPositions = new SparseBooleanArray();

    private void setItemChecked(int position, boolean isChecked) {
        mSelectedPositions.put(position, isChecked);
    }
    //获得选中条目的结果
    public  int getSelectedPos() {

        return isChecked.get(0);
    }

    //根据位置判断条目是否选中
    private boolean isItemChecked(int position) {
        return mSelectedPositions.get(position);
    }

    //获得选中条目的结果
    public ArrayList<MyShelf> getSelectedItem() {
        ArrayList<MyShelf> selectList = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            if (isItemChecked(i)) {
                selectList.add(mData.get(i));
            }
        }
        return selectList;
    }

    public List<MyShelf> getmData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_1)
        TextView tvDeviceCodeT;
        @BindView(R.id.rl_1)
        RelativeLayout rl_1;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void update(List<MyShelf> tea1) {
        this.mData = tea1;
        notifyDataSetChanged();
    }
    public void update( MyShelf  tea1) {
        int pos = mData.indexOf(tea1);
        mData.set(pos,tea1);
        notifyItemChanged(pos);
    }
    public void cleanChoose() {
        mSelectedPositions.clear();
    }

    public void SetOnclickLister(OnItemClickListerner itemClickListerner) {
        this.itemClickListerner = itemClickListerner;
    }



    OnItemClickListerner itemClickListerner;

    public interface OnItemClickListerner {
        void onClick(View view, int position);
    }


}
