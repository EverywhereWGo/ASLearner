package com.example.selftest.demo1b;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.selftest.R;

import java.util.List;

public class AdapterLV extends BaseAdapter {
    private static final int TYPE_SINGLE_IMAGE = 0;
    private static final int TYPE_DOUBLE_IMAGES = 1;

    private Context context;
    private List<String> imageUrls;

    public AdapterLV(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls == null ? 0 : imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case TYPE_SINGLE_IMAGE:
                convertView = getStyle1View(position, convertView, parent);
                break;
            case TYPE_DOUBLE_IMAGES:
                convertView = getStyle2View(position, convertView, parent);
                break;
            default:
                break;
        }
        return convertView;
    }

    private View getStyle1View(int position, View convertView, ViewGroup parent) {
        final SingleImageViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.image_view_single_demo1b, null);
            holder = new SingleImageViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SingleImageViewHolder) convertView.getTag();
        }
        Glide.with(context).load(imageUrls.get(position)).into(holder.imageView);
        return convertView;
    }

    private View getStyle2View(int position, View convertView, ViewGroup parent) {
        final DoubleImagesViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.image_view_double_demo1b, null);
            holder = new DoubleImagesViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (DoubleImagesViewHolder) convertView.getTag();
        }
        Glide.with(context).load(imageUrls.get(position)).into(holder.imageView1);
        if (position < imageUrls.size() - 1) {
            Glide.with(context).load(imageUrls.get(position + 1)).into(holder.imageView2);
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? TYPE_SINGLE_IMAGE : TYPE_DOUBLE_IMAGES;
    }

    public static class SingleImageViewHolder extends ViewHolder {
        ImageView imageView;

        public SingleImageViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image_single_demo1_b);
        }
    }

    public static class DoubleImagesViewHolder extends ViewHolder {
        ImageView imageView1;
        ImageView imageView2;

        public DoubleImagesViewHolder(View view) {
            super(view);
            imageView1 = view.findViewById(R.id.image_double_demo1_b1);
            imageView2 = view.findViewById(R.id.image_double_demo1_b2);
        }
    }


    private static class ViewHolder {
        public ViewHolder(View view) {
        }
    }
}
