package com.example.selftest.xiao7test.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.selftest.R;

/**
 * @author LMH
 */
public class Tab1Fragment3 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, container, true);
        TextView textView = view.findViewById(R.id.text_view_fragment3);
        textView.setText("这是Fragment3的内容");
        return view;
    }
}
