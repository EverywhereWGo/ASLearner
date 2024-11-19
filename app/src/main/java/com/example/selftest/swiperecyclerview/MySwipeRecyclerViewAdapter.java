package com.example.selftest.swiperecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selftest.R;

import java.util.ArrayList;
import java.util.List;

public class MySwipeRecyclerViewAdapter extends RecyclerView.Adapter<MySwipeRecyclerViewAdapter.ViewHolder> {
    private List<String> dataList;
    private Context context;

    public MySwipeRecyclerViewAdapter(Context context) {
        this.context = context;
        dataList = new ArrayList<>();
    }

    public void setListItems(List<String> dataList) {
        this.dataList = dataList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = dataList.get(position);
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
