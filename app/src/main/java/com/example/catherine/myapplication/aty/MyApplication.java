package com.example.catherine.myapplication.aty;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by catherine on 17/3/8.
 */

public class MyApplication extends Application {

    public static final String TAG = "BFPush";

    @Override
    public void onCreate() {
        super.onCreate();
        //打开Log
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);

    }

}
