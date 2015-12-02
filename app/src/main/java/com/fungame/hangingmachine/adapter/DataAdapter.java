package com.fungame.hangingmachine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fungame.hangingmachine.ClockService;
import com.fungame.hangingmachine.R;
import com.fungame.hangingmachine.entity.MyOpenUser;

import java.util.List;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public class DataAdapter extends SimpleBaseAdapter<MyOpenUser> {


    private InfoListListener btnItemClickListener;
    private ClockService service; 

    public void setBtnItemClickListener(InfoListListener btnItemClickListener) {
        this.btnItemClickListener = btnItemClickListener;
    }


    public interface InfoListListener {
        public abstract void onBtnItemClick(int position);
        public abstract void onBtnItemClick(int position, TextView left, TextView right);
    }

    public DataAdapter(Context context, List<MyOpenUser> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.open_user_info_item;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {

        MyOpenUser meta = (MyOpenUser) getItem(position);

        final TextView tvLabel1 = holder.getView(R.id.tvLabel1);
        final TextView tvLabel2 = holder.getView(R.id.tvLabel2);
        final TextView tvLabel3 = holder.getView(R.id.tvLabel3);
        final TextView tvLabel4 = holder.getView(R.id.tvLabel4);
        String label = meta.getUserName();

        if("test".equals(meta.getUserName())){
            tvLabel1.setVisibility(View.INVISIBLE);
            tvLabel2.setVisibility(View.INVISIBLE);
            tvLabel3.setVisibility(View.INVISIBLE);
            tvLabel4.setVisibility(View.INVISIBLE);
        } else {
            tvLabel1.setVisibility(View.VISIBLE);
            tvLabel2.setVisibility(View.VISIBLE);
            tvLabel3.setVisibility(View.VISIBLE);
            tvLabel4.setVisibility(View.VISIBLE);
            tvLabel1.setText(meta.getUserName());
            tvLabel2.setText(meta.getVersion());
            tvLabel3.setText(meta.getLevel());
            tvLabel4.setText(meta.getIsTuiguang());
        }

        return convertView;
    }

    public void setService(ClockService service) {
        this.service = service;
    }
}
