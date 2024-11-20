package com.example.selftest.xrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.selftest.R;
import com.example.selftest.swiperecyclerview.MySwipeRecyclerViewAdapter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XRecyclerViewActivity extends AppCompatActivity {
    // 参考文档：https://www.jb51.net/program/311019vhc.htm
    private static final String TAG = "XRecyclerViewActivity";
    private static final List<String> dataList = new ArrayList<>(Arrays.asList(
            "APPLE", "Bannna", "Cherry", "Date", "Elderberry", "Fig", "Grape", "Honeydew", "Kiwi", "Lemon",
            "Mango", "Nectarine", "Orange", "Papaya", "Quince", "Raspberry", "Strawberry", "Tangerine", "Watermelon",
            "Apricot", "Blackberry", "Blueberry", "Cantaloupe", "Coconut", "Cranberry", "Currant", "Durian", "Guava",
            "Jackfruit", "Kumquat", "Lychee"
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xrecycler_view);
        XRecyclerView recyclerView = findViewById(R.id.x_recycler_view);
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_image, null);
        ImageView headerImageView = headerView.findViewById(R.id.header_image_view);
        // header
        Glide.with(this)
                .load(R.drawable.lyy)
//                .error(R.drawable.grape)
                .placeholder(R.drawable.apple)
                .into(headerImageView);
        recyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        recyclerView.addHeaderView(headerView);

        // adapter
        MyXRecyclerViewAdapter adapter = new MyXRecyclerViewAdapter(dataList);
        recyclerView.setAdapter(adapter);
        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // refresh
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.getDefaultFootView().setLoadingHint("Loading");
        recyclerView.getDefaultFootView().setNoMoreHint("Loading Done");
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh!!!!!!!");
                recyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore!!!!!!");
                recyclerView.loadMoreComplete();
            }
        });
    }
}