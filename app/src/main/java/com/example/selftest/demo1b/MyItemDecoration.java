package com.example.selftest.demo1b;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyItemDecoration extends RecyclerView.ItemDecoration {
    private final int space;

    public MyItemDecoration(Context context, int itemSpacing) {
        // 将dp单位转换为像素值
        this.space = (int) (itemSpacing * context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int itemCount = state.getItemCount();
        if (position > 0 && position < itemCount - 1) {
            outRect.bottom = space;
        }
    }
}
