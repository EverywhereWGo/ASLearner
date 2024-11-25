package com.example.selftest.demo1b;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.selftest.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentLV extends Fragment {
    private ListView listView;
    private AdapterLV adapter;
    private List<String> imageUrls = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2_demo1_b, container, false);

        listView = view.findViewById(R.id.list_view_demo1b);

        // 初始化数据（这里简单模拟添加一些图片URL）
        for (int i = 0; i < 20; i++) {
            imageUrls.add("https://tse2-mm.cn.bing.net/th/id/OIP-C.KjBCUZMEXtdtR0wNIpc0IAHaEK?w=1920&h=1080&rs=1&pid=ImgDetMain");
        }

        adapter = new AdapterLV(getContext(), imageUrls);
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(Color.WHITE));
        listView.setDividerHeight(dpToPx(5));
        View headerView = getLayoutInflater().inflate(R.layout.header_image, null);
        ImageView headerImage = headerView.findViewById(R.id.header_image_view);
        headerImage.setImageResource(R.drawable.lyy);
        listView.addHeaderView(headerImage);
        return view;
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
