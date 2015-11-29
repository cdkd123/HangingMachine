package com.fungame.hangingmachine.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fungame.hangingmachine.R;


/**
 * 
 * @author tom 所有界面用到的actionbar，顶部可配置
 * 
 */
public class UIItemView extends RelativeLayout {

	private TextView tvText;
	Context context;
	private View view;

	private void initView(Context context, AttributeSet attrs) {


		// 有一个默认布局，在没有设置属性的情况下，有用
		int layoutId = R.layout.user_info_item;
		view = inflate(context, layoutId, null);
		tvText = (TextView) view.findViewById(R.id.tvLabel);
		addView(view);
	}

	public UIItemView(Context context) {
		super(context, null);
		this.context = context;
		initView(context, null);
	}

	public UIItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	public void setValues(int icon_update, String string, String version) {
		tvText.setText(string);
	}

	public View getView(int tvLabel) {
		return view.findViewById(tvLabel);
	}
}
