package com.fungame.hangingmachine.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

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
        return R.layout.nav_header_main;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {


        if(position == 0){

        } else if(position == 1){

        }
        return convertView;
    }
}
