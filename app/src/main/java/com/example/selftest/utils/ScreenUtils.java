package com.example.selftest.utils;

import android.content.Context;
import android.content.res.Configuration;

public class ScreenUtils {
    public static int getScreenWidthDp(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        return configuration.screenWidthDp;
    }

    public static int getScreenHeightDp(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        return configuration.screenHeightDp;
    }
}
