package com.li.recyclviewshelf;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MenuAdapter1 extends RecyclerView.Adapter<MenuAdapter1.MyViewHolder> {

    /* 首页子菜单适配器*/
    private Context mContext;
    private List<String> mData;


    public MenuAdapter1(Context context, List<String> list) {
        this.mContext = context;
        this.mData = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_menu, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {


        myViewHolder.tv_item_menu.setText(mData.get(position));
        myViewHolder.rl_device_1.setBackgroundColor(mContext.getResources().getColor(R.color.social_f7));
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClick(view, position);
            }
        });

    }

    public List<String> getmData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_menu;
        RelativeLayout rl_device_1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_menu = itemView.findViewById(R.id.tv_item_menu);
            rl_device_1 = itemView.findViewById(R.id.rl_device_1);
        }
    }

    public void update(List<String> tea1) {
        this.mData = tea1;
        notifyDataSetChanged();
    }

    public void SetOnclickLister(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onClick(View view, int menuId);
    }
}
