package com.example.selftest.externalstorage;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selftest.R;
import com.example.selftest.internalstorage.User;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExternalStorageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ExternalStorageActivity";
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5L, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>());
    private EditText edit_username;
    private EditText edit_password;
    private Button btn_save_userMessage;
    private Button btn_read;
    private TextView tv_message;
    private Gson gson;
    private Handler handler;
    private static final String FILE_NAME = "external_data.json";

    //外部状态可用
    boolean mExternalStorageAvailable = false;
    //外部状态可写
    boolean mExternalStorageWrite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_storage);
        gson = new Gson();
        handler = new Handler(Looper.getMainLooper());
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
                        //首先判断外部存储设备的状态
                        checkExternalStorage();
                        FileOutputStream fos = null;
                        try {
                            if (mExternalStorageWrite) {
                                fos = new FileOutputStream(new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), FILE_NAME));
                                // bean -> Json
                                User user = new User(username, password);
                                String jsonStr = gson.toJson(user);
                                // 写入内存data/data/包名/files
                                fos.write(jsonStr.getBytes());
                            } else {
                                Log.d(TAG, "外部存储媒介不可用");
                            }
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
                Toast.makeText(this, "Json文件写入外存成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_read:
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        checkExternalStorage();
                        FileInputStream fis = null;
                        try {
                            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), FILE_NAME);
                            if (mExternalStorageAvailable) {
                                fis = new FileInputStream(file);
                                //判断当前文件的字节个数
                                byte[] bytes = new byte[fis.available()];
                                while (fis.read(bytes) != -1) {
                                    String jsonStr = new String(bytes);
                                    User user = gson.fromJson(jsonStr, User.class);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            tv_message.setText(jsonStr);
                                        }
                                    });
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (fis != null) {
                                try {
                                    fis.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    private void checkExternalStorage() {
        //获取外部存储器的状态
        String state = Environment.getExternalStorageState();
        //主要有两种状态
        //外部状态可用
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //外部存储器可用 表示既可以写 也可以读
            mExternalStorageAvailable = true;
            mExternalStorageWrite = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            //外部存储器可用 表示仅可以读 不可写
            mExternalStorageAvailable = true;
            mExternalStorageWrite = false;
        } else {//表示为不可用的状态
            mExternalStorageAvailable = false;
            mExternalStorageWrite = false;
        }
    }
}
