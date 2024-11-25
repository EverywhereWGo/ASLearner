package com.example.selftest.externalstorage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    // 参考：https://blog.csdn.net/qq_24398367/article/details/110947737
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
    private ActivityResultLauncher launcher;
    private static final String FILE_NAME = "external_data.json";
    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String FOLDER_PATH = "/LMH";
    private static final int WRITE_EXTERNAL_STORAGE = 1;
    private static final int READ_EXTERNAL_STORAGE = 2;
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
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        write2ExternalStorage();
                    } else {
                        Log.d(TAG, "没有权限");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_userMessage:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        write2ExternalStorage();
                    } else {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        intent.setData(Uri.parse("package:" + this.getPackageName()));
                        launcher.launch(intent);
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this
                                , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                , WRITE_EXTERNAL_STORAGE);
                    } else {
                        // 如果已经授权就可以直接执行
                        write2ExternalStorage();
                    }
                } else {
                    write2ExternalStorage();
                }

                break;
            case R.id.btn_read:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        readFromExternalStorage();
                    } else {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        intent.setData(Uri.parse("package:" + this.getPackageName()));
                        launcher.launch(intent);
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this
                                , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                                , READ_EXTERNAL_STORAGE);
                    } else {
                        // 如果已经授权就可以直接执行
                        readFromExternalStorage();
                    }
                } else {
                    readFromExternalStorage();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    write2ExternalStorage();
                } else {
                    Toast.makeText(this, "没有写入外部内存的权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readFromExternalStorage();
                } else {
                    Toast.makeText(this, "没有读取外部内存的权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void readFromExternalStorage() {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                checkExternalStorage();
                FileInputStream fis = null;
                try {
                    File file = new File(ROOT_PATH + FOLDER_PATH, FILE_NAME);
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
        Toast.makeText(this, "读取成功", Toast.LENGTH_SHORT).show();
    }

    private void write2ExternalStorage() {
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
                checkExternalStorage();
                FileOutputStream fos = null;
                try {
                    if (mExternalStorageWrite) {
                        // 创建文件夹
                        File file = new File(ROOT_PATH + FOLDER_PATH);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        fos = new FileOutputStream(new File(ROOT_PATH + FOLDER_PATH, FILE_NAME));
                        // bean -> Json
                        User user = new User(username, password);
                        String jsonStr = gson.toJson(user);
                        fos.write(jsonStr.getBytes());
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
