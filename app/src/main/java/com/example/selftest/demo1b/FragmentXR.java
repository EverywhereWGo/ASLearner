package com.example.selftest.demo1b;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selftest.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentXR extends Fragment {
    private RecyclerView recyclerView;
    private AdapterXR adapter;
    private List<String> imageUrls = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3_demo1_b, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_demo1b);

        // 设置RecyclerView为两列的网格布局
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // 初始化数据（这里简单模拟添加一些图片URL）
        for (int i = 0; i < 20; i++) {
            imageUrls.add("https://tse2-mm.cn.bing.net/th/id/OIP-C.KjBCUZMEXtdtR0wNIpc0IAHaEK?w=1920&h=1080&rs=1&pid=ImgDetMain");
        }
        //
        recyclerView.addItemDecoration(new MyItemDecoration(getContext(), 5));
        // adapter
        adapter = new AdapterXR(imageUrls, getContext());
        recyclerView.setAdapter(adapter);
        // layout
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }
}
