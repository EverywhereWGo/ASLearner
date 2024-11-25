package com.example.selftest.demo5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SingleTextAdapter extends RecyclerView.Adapter<SingleTextAdapter.SingleTextViewHolder> {

    private String textToDisplay;

    public SingleTextAdapter(String textToDisplay) {
        this.textToDisplay = textToDisplay;
    }

    @NonNull
    @Override
    public SingleTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new SingleTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleTextViewHolder holder, int position) {
        holder.textView.setText(textToDisplay);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class SingleTextViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public SingleTextViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
