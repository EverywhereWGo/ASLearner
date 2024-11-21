package com.example.selftest.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.selftest.R;

import org.greenrobot.eventbus.EventBus;

public class MyDialog extends AlertDialog {
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView tvTitle;
    private TextView tvContent;
    private EditText etInput;
    private String title;
    private String message;
    private String cancel;
    private String confirm;
    // 回调接口
    private OnStringInputCallback onStringInputCallback;
    private OnCancelCallback onCancelCallback;
    private OnConfirmCallback onConfirmCallback;

    protected MyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_demo2);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        setDialogStartPosition();
        initView();
        initText();
        initEvent();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initEvent() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancelCallback != null) {
                    onCancelCallback.onCancel(MyDialog.this);
                    if (onStringInputCallback != null) {
                        String inputString = etInput.getText().toString();
                        onStringInputCallback.onStringInput(inputString);
                    }
                }
                dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConfirmCallback != null) {
                    onConfirmCallback.onConfirm(MyDialog.this);
                    // EventBus方式
                    String inputText = etInput.getText().toString().trim();
                    // 使用EventBus发送输入的信息事件
                    EventBus.getDefault().post(new InputInfoEvent(inputText));
                }
                dismiss();
            }
        });
    }

    private void initText() {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText("title");
        }
        if (!TextUtils.isEmpty(message)) {
            tvContent.setText("message");
        }
        if (!TextUtils.isEmpty(cancel)) {
            tvCancel.setText("取消");
        }
        if (!TextUtils.isEmpty(confirm)) {
            tvConfirm.setText("确认");
        }
    }


    private void initView() {
        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        etInput = findViewById(R.id.editText_input);
    }

    private void setDialogStartPosition() {
        Window window = getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    // 定义回调接口
    public interface OnStringInputCallback {
        void onStringInput(String input);
    }

    public interface OnCancelCallback {
        void onCancel(MyDialog dialog);
    }

    public interface OnConfirmCallback {
        void onConfirm(MyDialog dialog);
    }

    public MyDialog setInputCallBack(OnStringInputCallback callback) {
        this.onStringInputCallback = callback;
        return this;
    }

    public MyDialog setCancelCallBack(String cancel, OnCancelCallback callback) {
        this.cancel = cancel;
        this.onCancelCallback = callback;
        return this;
    }

    public MyDialog setConfirmCallBack(String confirm, OnConfirmCallback callback) {
        this.confirm = confirm;
        this.onConfirmCallback = callback;
        return this;
    }

    public MyDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public MyDialog setTitle(String title) {
        this.title = title;
        return this;
    }
}
