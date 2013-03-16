package com.googolmo.douban.doumailer;

import android.app.Application;
import com.googolmo.douban.doumailer.data.Provider;

/**
 * User: googolmo
 * Date: 13-2-8
 * Time: 上午10:37
 */
public class BaseApplication extends Application {

    private Provider mProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mProvider = new Provider(this);
    }

    public Provider getProvider() {
        return this.mProvider;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
