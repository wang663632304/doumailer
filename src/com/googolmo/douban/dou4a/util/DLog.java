package com.googolmo.douban.dou4a.util;

import android.util.Log;

/**
 * User: googolmo
 * Date: 13-2-9
 * Time: 上午10:44
 */
public class DLog {

    public static void d(String tag, String msg) {
        if (Configuration.getDebug()) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (Configuration.getDebug()) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (Configuration.getDebug()) {
            Log.w(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (Configuration.getDebug()) {
            Log.i(tag, msg);
        }
    }

}
