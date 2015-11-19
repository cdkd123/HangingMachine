package com.fungame.hangingmachine.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by tom on 2015/11/19.
 * descripton :
 */
public class TostHelper  extends Toast {
    public TostHelper(Context context) {
        super(context);
    }

    public static void shortToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
