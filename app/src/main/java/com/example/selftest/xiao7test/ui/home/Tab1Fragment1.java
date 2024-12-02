package com.example.selftest.xiao7test.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selftest.R;
import com.example.selftest.demo6.Demo6Activity;

/**
 * @author LMH
 */
public class Tab1Fragment1 extends Fragment {
    private static final String TAG = "Tab1Fragment1";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment1, container, true);
        Context context = getContext();
        Intent intent = new Intent(getActivity(),Demo6Activity.class);
        // RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.xiao7_t1f1_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.Adapter<RecyclerView.ViewHolder> adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            private static final String data = "313232131ss";

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
                MyHolder myHolder = new Tab1Fragment1.MyHolder(view);
                return myHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                MyHolder myHolder = (Tab1Fragment1.MyHolder) holder;
                myHolder.textView.setText(data);
                myHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(intent);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return 30;
            }
        };
        recyclerView.setAdapter(adapter);


        return view;
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
