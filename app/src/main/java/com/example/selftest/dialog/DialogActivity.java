package com.example.selftest.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selftest.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;


/**
 * @author LMH
 */
public class DialogActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DialogActivity";
    private MyDialog myDialog;
    private Button mBtn;
    private boolean isRegistered = false;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        mBtn = findViewById(R.id.dialog_button_demo2);
        mBtn.setOnClickListener(this);
        // 注册EventBus订阅者
        if (!isRegistered) {
            EventBus.getDefault().register(this);
            isRegistered = true;
        } else {
            // 返回其他失败处理或者提示
        }
    }

    @Override
    protected void onDestroy() {
        if (isRegistered) {
            EventBus.getDefault().unregister(this);
            isRegistered = false;
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInputInfoEvent(InputInfoEvent event) {
        // callback
        String inputText = event.getInputText();
        Toast.makeText(DialogActivity.this
                , "EventBus方式回调->" + inputText
                , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_button_demo2:
                myDialog = new MyDialog(this, R.style.MyDialog)
                        .setMessage("这里展示信息")
                        .setTitle("出售")
                        .setConfirmCallBack("确认!", new MyDialog.OnConfirmCallback() {
                            @Override
                            public void onConfirm(MyDialog dialog) {
                                // callback
                                Log.d(TAG, "此处可以做确认后的处理");
                                // Intent的隐式调用
                                Intent intent = new Intent("DIALOG_TEST");
                                startActivity(intent);
                            }
                        })
                        .setCancelCallBack("取消!", new MyDialog.OnCancelCallback() {
                            @Override
                            public void onCancel(MyDialog dialog) {
                                // callback
                                Log.d(TAG, "此处可以做取消后的处理");
                            }
                        })
                        .setInputCallBack(new MyDialog.OnStringInputCallback() {
                            @Override
                            public void onStringInput(String input) {
                                // callback
                                Toast.makeText(DialogActivity.this
                                        , "callback方式回调->" + input
                                        , Toast.LENGTH_SHORT).show();
                            }
                        });
                myDialog.show();
                break;
            default:
                break;
        }
    }
}
