package com.example.selftest.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;

import com.example.selftest.R;
import com.example.selftest.circleindicator.CircleIndicatorActivity;
import com.example.selftest.coordinatorlayout.CoordinatorLayoutActivity;
import com.example.selftest.demo1b.Demo1bActivity;
import com.example.selftest.demo3b.Demo3bActivity;
import com.example.selftest.demo3c.Demo3cActivity;
import com.example.selftest.demo3d.Demo3dActivity;
import com.example.selftest.demo4a.Demo4aActivity;
import com.example.selftest.demo4b.Demo4bActivity;
import com.example.selftest.demo5.Demo5Activity;
import com.example.selftest.demo6.Demo6Activity;
import com.example.selftest.demo7.Demo7Activity;
import com.example.selftest.dialog.DialogActivity;
import com.example.selftest.downloadbutton.DownloadButtonActivity;
import com.example.selftest.externalstorage.ExternalStorageActivity;
import com.example.selftest.filletpic.FilletPicActivity;
import com.example.selftest.internalstorage.InternalStorageActivity;
import com.example.selftest.magicindicator.MagicIndicatorActivity;
import com.example.selftest.notification.NotificationActivity;
import com.example.selftest.service.ServiceActivity;
import com.example.selftest.swiperecyclerview.SwipeRecyclerViewActivity;
import com.example.selftest.viewpager.ViewPagerActivity;
import com.example.selftest.xiao7test.Xiao7TestActivity;
import com.example.selftest.xrecyclerview.XRecyclerViewActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LMH
 */
public class RootAcvitity extends AppCompatActivity {
    private List<Activity> activityList = new ArrayList<>(Arrays.asList(
            new NotificationActivity(), new ServiceActivity(), new ViewPagerActivity()
            , new SwipeRecyclerViewActivity(), new XRecyclerViewActivity(), new InternalStorageActivity()
            , new ExternalStorageActivity(), new MagicIndicatorActivity(), new CircleIndicatorActivity()
            , new Demo1bActivity(), new DialogActivity(), new FilletPicActivity(), new Demo3bActivity()
            , new Demo3cActivity(), new Demo3dActivity(), new Demo4aActivity(), new DownloadButtonActivity()
            , new Demo4bActivity(), new Demo5Activity(), new Demo6Activity(), new Demo7Activity(), new CoordinatorLayoutActivity()
            , new Xiao7TestActivity()
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_acvitity);
        //
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this);
        adapter.setListItems(activityList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new androidx.recyclerview.widget.DividerItemDecoration(this, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL));
    }
}