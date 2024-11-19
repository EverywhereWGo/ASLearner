package com.example.selftest.internalstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selftest.R;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InternalStorageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "InternalStorageActivity";
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5L, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>());
    private EditText edit_username;
    private EditText edit_password;
    private Button btn_save_userMessage;
    private Button btn_read;
    private TextView tv_message;
    private Gson gson = new Gson();
    private static final String FILE_NAME = "internal_data.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_storage);
        edit_username = findViewById(R.id.edit_username);
        edit_password = findViewById(R.id.edit_password);
        btn_save_userMessage = findViewById(R.id.btn_save_userMessage);
        btn_read = findViewById(R.id.btn_read);
        tv_message = findViewById(R.id.tv_message);
        btn_save_userMessage.setOnClickListener(this);
        btn_read.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_userMessage:
                String username = edit_username.getText().toString();
                String password = edit_password.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        FileOutputStream fos = null;
                        try {
                            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            // bean -> Json
                            User user = new User(username, password);
                            String jsonStr = gson.toJson(user);
                            // 写入内存data/data/包名/files
                            fos.write(jsonStr.getBytes());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                Toast.makeText(this, "Json文件写入内存成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_read:
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        FileInputStream fis = null;
                        try {
                            fis = openFileInput(FILE_NAME);
                            //fis.available() 判断文件有多少个字节
                            byte[] bytes = new byte[fis.available()];
                            while (fis.read(bytes) != -1) {
                                String jsonStr = new String(bytes);
                                User user = gson.fromJson(jsonStr, User.class);
                                // 将设置UI的操作切换到主线程执行
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_message.setText(jsonStr);
                                    }
                                });
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Toast.makeText(this, "从内存读取Json文件成功", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 平缓关闭线程池
        threadPoolExecutor.shutdown();
        try {
            if (!threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                // 超时未关闭，则强制关闭
                threadPoolExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            // 捕获中断异常，重新设置中断状态
            Thread.currentThread().interrupt();
            // 强制关闭线程池
            threadPoolExecutor.shutdownNow();
        }
    }
}