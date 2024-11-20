package com.example.selftest.swiperecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.example.selftest.R;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SwipeRecyclerViewActivity extends AppCompatActivity {
    private static final String TAG = "SwipeRecyclerViewActivi";
    private static final List<String> dataList = new ArrayList<>(Arrays.asList(
            "APPLE", "Bannna", "Cherry", "Date", "Elderberry", "Fig", "Grape", "Honeydew", "Kiwi", "Lemon",
            "Mango", "Nectarine", "Orange", "Papaya", "Quince", "Raspberry", "Strawberry", "Tangerine", "Watermelon",
            "Apricot", "Blackberry", "Blueberry", "Cantaloupe", "Coconut", "Cranberry", "Currant", "Durian", "Guava",
            "Jackfruit", "Kumquat", "Lychee"
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_recycler_view);
        SwipeRecyclerView recyclerView = findViewById(R.id.swipe_recycler_view);
        // adapter
        MySwipeRecyclerViewAdapter adapter = new MySwipeRecyclerViewAdapter(this);
        adapter.setListItems(dataList);
        recyclerView.setAdapter(adapter);
        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // refresh
        recyclerView.setAutoLoadMore(false);
        recyclerView.useDefaultLoadMore();
        recyclerView.setLoadMoreListener(new SwipeRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore");
                recyclerView.loadMoreFinish(true, false);
            }
        });



    }
}