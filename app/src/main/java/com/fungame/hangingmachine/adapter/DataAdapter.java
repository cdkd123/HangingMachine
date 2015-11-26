package com.fungame.hangingmachine.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.fungame.hangingmachine.R;
import com.fungame.hangingmachine.entity.UserItem;
import com.fungame.hangingmachine.fragment.NavInfoFragment;

import java.util.List;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public class DataAdapter extends SimpleBaseAdapter<UserItem> {


    private InfoListListener btnItemClickListener;

    public void setBtnItemClickListener(InfoListListener btnItemClickListener) {
        this.btnItemClickListener = btnItemClickListener;
    }

    public interface InfoListListener {
        public abstract void onBtnItemClick(int position);
        public abstract void onBtnItemClick(int position, TextView left, TextView right);
    }

    public DataAdapter(Context context, List<UserItem> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.user_info_item;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {


        UserItem meta = (UserItem) getItem(position);

        final TextView tvLabel = holder.getView(R.id.tvLabel);
        final Button btnClick = holder.getView(R.id.btn);
        String label = meta.getLabel();

        if("公告".equals(label)){
            tvLabel.setText(meta.getBtn());
            btnClick.setVisibility(View.GONE);
        } else {
            btnClick.setVisibility(View.VISIBLE);
            switch(position){
                case 1:
                    label = context.getString(R.string.login_account) + ":" + meta.getLabel();
                    break;
                case 2:
                    label = context.getString(R.string.account_type) + ":" + meta.getLabel();

                    break;
                case 3:
                    label = context.getString(R.string.account_money) + ":" + meta.getLabel();

                    break;
                case 4:
                    label = context.getString(R.string.account_money_speed) + ":" + meta.getLabel();

                    break;
                case 5:
                    label = context.getString(R.string.account_level) + ":" + meta.getLabel();
                    break;
                case 6:
                    label = "今日推广时间:" + meta.getLabel();
                    break;
                default:
                    label = "index error!";
                    break;
            }

            tvLabel.setText(label);
            btnClick.setText(meta.getBtn());
        }


        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnItemClickListener != null) {
                    btnItemClickListener.onBtnItemClick(position);
                    btnItemClickListener.onBtnItemClick(position, tvLabel, btnClick);
                }

            }
        });

        if("TESTBUTTON".equals(meta.getBtn())){
            tvLabel.setVisibility(View.INVISIBLE);
            btnClick.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
