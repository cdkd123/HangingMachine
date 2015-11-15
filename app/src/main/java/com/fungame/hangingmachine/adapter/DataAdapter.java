package com.fungame.hangingmachine.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.fungame.hangingmachine.R;
import com.fungame.hangingmachine.entity.UserItem;

import java.util.List;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public class DataAdapter extends SimpleBaseAdapter<UserItem> {

    public DataAdapter(Context context, List<UserItem> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.user_info_item;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {


        UserItem meta = (UserItem) getItem(position);

        TextView tvLabel = holder.getView(R.id.tvLabel);
        Button btnClick = holder.getView(R.id.btn);
        String label = meta.getLabel();
        switch(position){
            case 0:
                label = context.getString(R.string.login_account) + ":" + meta.getLabel();
                break;
            case 1:
                label = context.getString(R.string.account_type) + ":" + meta.getLabel();

                break;
            case 2:
                label = context.getString(R.string.account_money) + ":" + meta.getLabel();

                break;
            case 3:
                label = context.getString(R.string.account_money_speed) + ":" + meta.getLabel();

                break;
            case 4:
                label = context.getString(R.string.account_level) + ":" + meta.getLabel();
                break;
            case 5:
                label = "今日推广时间:" + meta.getLabel();
                break;
            default:
                label = "index error!";
                break;
        }

        tvLabel.setText(label);
        btnClick.setText(meta.getBtn());

        return convertView;
    }
}
