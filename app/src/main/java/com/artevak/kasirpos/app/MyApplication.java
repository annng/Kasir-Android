package com.artevak.kasirpos.app;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

/**
 * Created by lenovo on 17/5/6.
 */

public class MyApplication extends Application {
    private static MyApplication instance;

    public MyApplication() {
        instance = this;
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            synchronized (MyApplication.class) {
                if (instance == null) {
                    instance = new MyApplication();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(
                this
        ); { }
        FirebaseCrashlytics.getInstance();
    }
}
