package com.example.selftest.demo1b;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.selftest.R;
import com.example.selftest.magicindicator.Fragment1;
import com.example.selftest.magicindicator.Fragment2;
import com.example.selftest.magicindicator.Fragment3;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class Demo1bActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private MagicIndicator magicIndicator;
    private List<String> titleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic_indicator);
        viewPager2 = findViewById(R.id.view_pager);
        magicIndicator = findViewById(R.id.magic_indicator);
        titleList.add("页面1");
        titleList.add("页面2");
        titleList.add("页面3");
        com.example.selftest.demo1b.MyPagerAdapter adapter = new MyPagerAdapter(this);
        adapter.addFragment();
        viewPager2.setAdapter(adapter);
        // MagicIndicator
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setText(titleList.get(index));
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager2.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                // 创建一个线性页面指示器对象
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                // 设置指示器的模式为根据内容自适应宽度（WRAP_CONTENT模式）
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                // 设置指示器的颜色
                linePagerIndicator.setColors(Color.RED);
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        // 将ViewPager2的页面切换与MagicIndicator的指示器状态同步
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                magicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                magicIndicator.onPageScrollStateChanged(state);
            }
        });
    }
}