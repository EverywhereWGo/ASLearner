package com.example.selftest.circleindicator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.selftest.R;

public class Fragment2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        TextView textView = view.findViewById(R.id.text_view_fragment2);
        textView.setText("这是Fragment2的内容");
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setTitle("Page 2");
        }
        return view;
    }
}
