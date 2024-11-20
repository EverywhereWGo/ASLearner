package com.example.selftest.demo1b;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.selftest.R;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FragmentXRV extends Fragment {
    private static final String TAG = "FragmentRV";
    private XRecyclerView xRecyclerView;
    private List<String> imageUrls = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_demo1_b, container, false);
        xRecyclerView = view.findViewById(R.id.xrecycler_view_demo1b);
        // 初始化数据（这里简单模拟添加一些图片URL）
        for (int i = 0; i < 20; i++) {
            imageUrls.add("https://tse2-mm.cn.bing.net/th/id/OIP-C.KjBCUZMEXtdtR0wNIpc0IAHaEK?w=1920&h=1080&rs=1&pid=ImgDetMain");
        }
        // adapter
        AdapterXRV adapter = new AdapterXRV(imageUrls, getContext());
        xRecyclerView.setAdapter(adapter);
        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        xRecyclerView.setLayoutManager(linearLayoutManager);
        //
        xRecyclerView.addItemDecoration(new MyItemDecoration(getContext(), 5));
        // refresh
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.getDefaultFootView().setLoadingHint("Loading");
        xRecyclerView.getDefaultFootView().setNoMoreHint("Loading Done");
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh!!!!!!!");
                xRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore!!!!!!");
                xRecyclerView.loadMoreComplete();
            }
        });
        return view;

    }
}
