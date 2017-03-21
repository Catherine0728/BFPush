package com.example.catherine.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.catherine.myapplication.model.PushMessage;
import com.example.catherine.myapplication.feature.IPush;
import com.example.catherine.myapplication.utills.L;
import com.example.catherine.myapplication.utills.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JpushReceiver extends BroadcastReceiver {
    private static final String TAG = "JpushReceiver";
    private static IPush iPush;

    public static void registerInterface(IPush iPush1) {
        iPush = iPush1;
    }

    public static IPush getPushInterface() {
        return iPush;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        L.line(TAG + " onReceive" + intent.getAction());
        L.lineModel(printBundle(bundle));

        int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        String messageId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        String extraMessage = bundle.getString(JPushInterface.EXTRA_EXTRA);

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            L.i(TAG + "接收Registration Id : " + regId);
            if (iPush != null)
                iPush.onRegister(context, regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            L.i(TAG + " 接收到推送下来的自定义消息: " + extraMessage);
            if (iPush != null) {
                PushMessage message = new PushMessage();
                message.setTitle(bundle.getString(JPushInterface.EXTRA_TITLE));
                message.setMessageID(messageId);
                message.setMessage(bundle.getString(JPushInterface.EXTRA_MESSAGE));
                message.setExtra(extraMessage);
                message.setTarget(Target.JPUSH);
                iPush.onCustomMessage(context, message);
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

            L.i(TAG + "接收到推送下来的通知的ID: " + notifactionId);
            if (iPush != null) {
                PushMessage message = new PushMessage();
                message.setNotifyID(notifactionId);
                message.setMessageID(messageId);
                message.setTitle(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
                message.setMessage(bundle.getString(JPushInterface.EXTRA_ALERT));
                message.setExtra(extraMessage);
                message.setTarget(Target.JPUSH);
                iPush.onMessage(context, message);
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            L.i(TAG + "用户点击打开了通知");
            if (iPush != null) {
                PushMessage message = new PushMessage();
                message.setNotifyID(notifactionId);
                message.setMessageID(messageId);
                message.setTitle(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
                message.setMessage(bundle.getString(JPushInterface.EXTRA_ALERT));
                message.setExtra(extraMessage);
                message.setTarget(Target.JPUSH);
                iPush.onMessageClicked(context, message);
            }


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            L.i(TAG + "用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            L.i(TAG + intent.getAction() + " connected state change to " + connected);
        } else {
            L.i(TAG + " Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:{" + key + ", " + bundle.getInt(key) + "}");
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:{" + key + "," + bundle.getBoolean(key) + "}");
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    L.line("This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:{" + key + ", [" +
                                myKey + " - " + json.optString(myKey) + "]}");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:{" + key + ", " + bundle.getString(key) + "}");
            }
        }
        return sb.toString();
    }

    public static void clearPushInterface() {
        iPush = null;
    }

}
