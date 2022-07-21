package com.artevak.kasirpos.app

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.artevak.kasirpos.app.MyApplication
import com.artevak.kasirpos.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

/**
 * Created by lenovo on 17/5/6.
 */
class MyApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
//        MobileAds.initialize(
//            this
//        )
        run {}
        FirebaseCrashlytics.getInstance()
        startKoin {
            androidContext(this@MyApplication)
            modules(appComponent)
        }
    }

    companion object {
        private var instance: MyApplication? = null
        fun getInstance(): MyApplication? {
            if (instance == null) {
                synchronized(MyApplication::class.java) {
                    if (instance == null) {
                        instance = MyApplication()
                    }
                }
            }
            return instance
        }
    }

    init {
        instance = this
    }
}