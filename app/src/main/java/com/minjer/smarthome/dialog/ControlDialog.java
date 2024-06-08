package com.minjer.smarthome.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.minjer.smarthome.R;

public class ControlDialog extends AlertDialog {
    private OnClickListener listener;

    private TextView tvCancel;
    private TextView tvConfirm;

    private TextView tvTitle;
    private TextView tvContent;

    private String title;
    private String message;

    public ControlDialog(Context context) {
        super(context, R.style.ControlDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_dialog_layout);

        setCanceledOnTouchOutside(false);

        //设置对话框的显示位置
        setDialogStartPositon();

        //绑定控件
        initView();

        //初始化个组件的内容
        initText();

        //设置点击事件触发
        initEvent();

    }

    private void setDialogStartPositon() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置起始位置
//        lp.x  = 100;
//        lp.y = 100;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

    }

    private void initEvent() {

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onCancelClick();

                dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onConfirmClick();

                dismiss();
            }
        });
    }

    private void initText() {
        if (title != null) tvTitle.setText(title);
        if (message != null) tvContent.setText(message);
        tvCancel.setText("取消");
        tvConfirm.setText("确认");
    }

    public ControlDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public ControlDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    private void initView() {

        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
    }


    //提供给外部使用的方法
    public ControlDialog setOnClickListener(OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnClickListener {
        void onCancelClick();

        void onConfirmClick();
    }
}
