package com.example.selftest.circleindicator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.selftest.R;


public class Fragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, true);
        TextView textView = view.findViewById(R.id.text_view_fragment1);
        textView.setText("这是Fragment1的内容");
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setTitle("Page 1");
        }
        return view;
    }
}