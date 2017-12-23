package com.mycode.xml.pure2048;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xia_m on 2017/12/17/0017.
 */

public class SettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private List<SettingMenu> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClickListener mOnItemClickListener = null;

    public SettingAdapter(Context context, List<SettingMenu> datas){
        this. mContext=context;
        this. mDatas=datas;
        inflater=LayoutInflater. from(mContext);
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            switch(view.getId())
            {
                case R.id.switch1:
                    Log.d("MyAdapter","switch");
                    break;
                case R.id.root_item:
                case R.id.root_item2:
                    //注意这里使用getTag方法获取position
                    mOnItemClickListener.onItemClick(view,(int)view.getTag());
                    break;
            }

        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    @Override
    public int getItemCount() {

        return mDatas.size();
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int itemViewType=getItemViewType(position);

        switch(itemViewType)
        {
            case 0: {
                BaseViewHolder hv=(BaseViewHolder)holder;
                hv.tv_name.setText(mDatas.get(position).getTitle());
                hv.sw.setChecked(mDatas.get(position).getSwtchSta());
                break;
            }
            case 1:{
                BaseViewHolder2 hv=(BaseViewHolder2)holder;
                hv.tv_name.setText(mDatas.get(position).getTitle());
                hv.tv_val.setText(mDatas.get(position).getVal());
                break;
            }
        }
        holder.itemView.setTag(position);
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        RecyclerView.ViewHolder holder=null;
        switch(viewType)
        {
            case 0:
                view = inflater.inflate(R.layout.base_list_item,parent, false);
                holder= new BaseViewHolder(view);
                break;
            case 1:
                view = inflater.inflate(R.layout.base_list_item2,parent, false);
                holder= new BaseViewHolder2(view);
                break;
        }
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getType();
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name=null;
        Switch sw=null;

        public BaseViewHolder(View itemView) {
            super(itemView);
            tv_name=(TextView) itemView.findViewById(R.id.item_tv);
            sw=(Switch) itemView.findViewById(R.id.switch1);
//            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    if(b)
//                    {
//                        Log.d("MyAdapter","check");
//                        mDatas.get(0).setSwitchSta(true);
//                    }
//                    else
//                    {
//                        Log.d("MyAdapter","uncheck");
//                        mDatas.get(0).setSwitchSta(false);
//                    }
//                }
//            });
        }

    }

    class BaseViewHolder2 extends RecyclerView.ViewHolder{
        TextView tv_name=null;
        TextView tv_val=null;
        public BaseViewHolder2(View itemView) {
            super(itemView);
            tv_name = (TextView)itemView.findViewById(R.id.item_name);
            tv_val = (TextView)itemView.findViewById(R.id.item_val);
        }
    }
}
