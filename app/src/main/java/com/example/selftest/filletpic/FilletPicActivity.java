package com.example.selftest.filletpic;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Outline;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.selftest.R;

public class FilletPicActivity extends AppCompatActivity {
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillet_pic);
        imageView1 = findViewById(R.id.fillet_image_view1);
        imageView2 = findViewById(R.id.fillet_image_view2);
        imageView3 = findViewById(R.id.fillet_image_view3);
        // 方式一：自定义View
        imageView1.setImageResource(R.drawable.lyy);
        // 方式二：ViewOutlineProvider
        imageView2.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                // 设置圆角半径为5
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 50f);
                // 设置按钮为圆形
//                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        });
        imageView2.setClipToOutline(true);
        imageView2.setImageResource(R.drawable.lyy);

        // 方式三 Glide
        // 设置圆角半径为10dp
        int radius = 30;
        //统一设置4个角
        RequestOptions options = new RequestOptions().transform(new RoundedCorners(radius));
        //单独设置某个角
//        RequestOptions options = new RequestOptions().transform(new GranularRoundedCorners(radius, radius, radius, radius));
        Glide.with(this)
                .load(R.drawable.lyy)
                .apply(options)
                .into(imageView3);

    }
}