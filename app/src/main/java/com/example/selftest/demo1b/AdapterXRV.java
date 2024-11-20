package com.example.selftest.demo1b;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.selftest.R;


import java.util.List;

public class AdapterXRV extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_SINGLE_IMAGE = 0;
    private static final int TYPE_DOUBLE_IMAGES = 1;
    private List<String> imageUrls;
    private Context context;


    public AdapterXRV(List<String> imageUrls, Context context) {
        this.context = context;
        this.imageUrls = imageUrls;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SINGLE_IMAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.image_view_single_demo1b, parent, false);
            return new SingleImageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.image_view_double_demo1b, parent, false);
            return new DoubleImagesViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_SINGLE_IMAGE) {
            SingleImageViewHolder singleHolder = (SingleImageViewHolder) holder;
            Glide.with(context).load(imageUrls.get(position)).into(singleHolder.imageView);
        } else {
            DoubleImagesViewHolder doubleHolder = (DoubleImagesViewHolder) holder;
            Glide.with(context).load(imageUrls.get(position)).into(doubleHolder.imageView1);
            if (position < imageUrls.size() - 1) {
                Glide.with(context).load(imageUrls.get(position + 1)).into(doubleHolder.imageView2);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? TYPE_SINGLE_IMAGE : TYPE_DOUBLE_IMAGES;
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class SingleImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SingleImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_single_demo1_b);
        }
    }

    public static class DoubleImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView1;
        ImageView imageView2;

        public DoubleImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView1 = itemView.findViewById(R.id.image_double_demo1_b1);
            imageView2 = itemView.findViewById(R.id.image_double_demo1_b2);
        }
    }
}
