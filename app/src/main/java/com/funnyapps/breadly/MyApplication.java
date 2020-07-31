package com.funnyapps.breadly;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Application sInstance;

    public static Application get() {
        return sInstance;
    }


    public static Context getContext() {
        return sInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        sInstance = null;
    }
}
