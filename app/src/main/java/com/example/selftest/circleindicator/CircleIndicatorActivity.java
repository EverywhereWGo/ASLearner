package com.example.selftest.circleindicator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.selftest.R;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator3;

public class CircleIndicatorActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private CircleIndicator3 circleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_indicator);

        viewPager2 = findViewById(R.id.viewPager);
        circleIndicator = findViewById(R.id.circleIndicator);
        MyPager2Adapter adapter = new MyPager2Adapter(this);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new Fragment1());
        fragmentList.add(new Fragment2());
        fragmentList.add(new Fragment3());
        List<String> titleList = new ArrayList<>();
        titleList.add("Page 1");
        titleList.add("Page 2");
        titleList.add("Page 3");
        for (int i = 0; i < fragmentList.size(); i++) {
            adapter.addFragment(fragmentList.get(i), titleList.get(i));
        }

        viewPager2.setAdapter(adapter);

        // 将CircleIndicator与ViewPager2绑定，需要注意这里的绑定方式略有不同
        circleIndicator.setViewPager(viewPager2);
    }
}