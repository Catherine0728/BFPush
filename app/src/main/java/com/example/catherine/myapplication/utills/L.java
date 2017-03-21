package com.example.catherine.myapplication.utills;

import android.util.Log;

import com.example.catherine.myapplication.BuildConfig;


/**
 * Created by jiang on 2016/10/11.
 */

public class L {
    private static final String TAG = "BFPush";

    public static final boolean debug = BuildConfig.DEBUG;

    public static void i(String msg) {
        //if (debug) {
        Log.i(TAG, msg);
        //}
    }

    public static void line(String msg) {
        //if (debug) {
        Log.i(TAG, "---------------------" + msg);
        //}
    }

    public static void lineModel(String msg) {
        Log.i(TAG, "---------------------");
        Log.i(TAG, msg);
        Log.i(TAG, "---------------------");
    }
}
