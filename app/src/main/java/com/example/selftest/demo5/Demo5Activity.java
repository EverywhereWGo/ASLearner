package com.example.selftest.demo5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.selftest.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo5Activity extends AppCompatActivity {
    private static final String TAG = "Demo5Activity";
    private static final List<String> dataList = new ArrayList<>(Arrays.asList(
            "APPLE", "Bannna", "Cherry", "Date", "Elderberry", "Fig", "Grape", "Honeydew", "Kiwi", "Lemon",
            "Mango", "Nectarine", "Orange", "Papaya", "Quince", "Raspberry", "Strawberry", "Tangerine", "Watermelon",
            "Apricot", "Blackberry", "Blueberry", "Cantaloupe", "Coconut", "Cranberry", "Currant", "Durian", "Guava",
            "Jackfruit", "Kumquat", "Lychee"
    ));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo5);
        RecyclerView recyclerView = findViewById(R.id.nesting_recycler_view);
        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // adapter
        recyclerView.setAdapter(new MyRecyclerAdapter(dataList,this));
    }
}