package com.example.selftest.demo5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selftest.R;

import java.util.List;

/**
 * @author LMH
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TEXT = 0;
    private static final int TYPE_RECYCLER_VIEW = 1;
    List<String> dataList;
    private Context context;

    public MyRecyclerAdapter(List<String> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 2 ? TYPE_RECYCLER_VIEW : TYPE_TEXT;
    }


    static class TextHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public TextHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view_demo1b);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TEXT) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_layout, null);
            return new TextHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment3_demo1_b, null);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) (200 * context.getResources().getDisplayMetrics().density));
            view.setLayoutParams(layoutParams);
            return new RecyclerViewHolder(view);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_TEXT) {
            TextHolder textHolder = (TextHolder) holder;
            textHolder.textView.setText(dataList.get(position));
        } else {
            RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
            // layout
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
            // 通过OnTouch方式 解决滑动冲突
            recyclerViewHolder.recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                            // 当用户按下或者移动的时候，我们告诉父组件，不要拦截我的事件（这个时候子组件是可以正常响应事件的）
                            ((ViewGroup) v.getParent()).requestDisallowInterceptTouchEvent(true);
                            break;
                        case MotionEvent.ACTION_UP:
                            // 拿起之后就会告诉父组件可以阻止
                            ((ViewGroup) v.getParent()).requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    return false;
                }
            });
            // adapter
            recyclerViewHolder.recyclerView.setAdapter(new SingleTextAdapter("aaaaaaa"));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
