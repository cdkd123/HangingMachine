package com.fungame.hangingmachine.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStructure;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fungame.hangingmachine.R;
import com.fungame.hangingmachine.util.TostHelper;

/**
 * Created by admin on 15/11/24.
 */
public class InputDialog extends Dialog {



    private EditText etText1, etText2;
    private InputDialogInterface confirmListener;

    public void setConfirmListener(InputDialogInterface confirmListener) {
        this.confirmListener = confirmListener;
    }

    public EditText getText2() {
        return etText2;
    }

    public EditText getText1() {
        return etText1;
    }

    public TextView getConfirm() {
        return btnModify;
    }

    public interface InputDialogInterface {
        public void onBtnClick(String et1, String et2);

    }

    InputDialogInterface dlgClickListener;
    Button btnModify;

    public InputDialog(Context context) {
        super(context);

        setContentView(R.layout.dialog_input);

        etText1 = (EditText) findViewById(R.id.etUser);
        etText2 = (EditText) findViewById(R.id.etPwd);
        btnModify = (Button)findViewById(R.id.btnModify);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textUp = etText1.getText().toString();
                String textDown = etText2.getText().toString();
                if(confirmListener != null){
                    confirmListener.onBtnClick(textUp, textDown);
                }
            }
        });
    }

}
