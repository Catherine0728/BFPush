package com.example.catherine.myapplication.receiver;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.catherine.myapplication.feature.IPush;
import com.example.catherine.myapplication.aty.MyApplication;
import com.example.catherine.myapplication.model.PushMessage;
import com.example.catherine.myapplication.utills.JHandler;
import com.example.catherine.myapplication.utills.L;
import com.example.catherine.myapplication.utills.Target;
import com.huawei.android.pushagent.PushReceiver;

/**
 * Created by catherine
 */

public class EMHuaweiPushReceiver extends PushReceiver {
    public static String TAG = "EMHuaweiPushReceiver=======";

    private static String mToken = null;
    private static IPush mIPush;

    public static void registerInterface(IPush IPush) {
        mIPush = IPush;
    }

    public static IPush getIPush() {
        return mIPush;
    }

    public static String getmToken() {
        return mToken;
    }


    @Override
    public void onToken(final Context context, final String token, Bundle extras) {
        final String belongId = extras.getString("belongId");
        String content = "获取token和belongId成功，token = " + token + ",belongId = " + belongId;
        L.i(TAG + content);
        mToken = token;
        if (mIPush != null) {
            JHandler.handler().post(new Runnable() {
                @Override
                public void run() {
                    mIPush.onRegister(context, belongId);
                }
            });
        }
    }

    @Override
    public void onPushMsg(Context context, byte[] bytes, String s) {
        super.onPushMsg(context, bytes, s);
        L.i(TAG + "onPushMsg...");
    }


    /**
     * @param context
     * @param msg
     * @param bundle
     * @return 推送消息下来时会自动回调onPushMsg方法实现应用透传消息处理。本接口必须被实现
     */
    @Override
    public boolean onPushMsg(final Context context, byte[] msg, Bundle bundle) {
        //这里是透传消息， msg是透传消息的字节数组 bundle字段没用
        L.i(TAG + "onPushMsg: " + msg.toString());
        try {
            String content = new String(msg, "UTF-8");
            if (mIPush != null) {
                final PushMessage pushMessage = new PushMessage();
                pushMessage.setMessage(content);
                //华为的sdk在透传的时候无法实现extra字段，这里要注意
                pushMessage.setExtra("{}");
                pushMessage.setTarget(Target.EMUI);
                JHandler.handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mIPush.onCustomMessage(context, pushMessage);
                    }
                });
            }
            L.i(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 在华为的sdk中，通知栏的事件只有三种:
     * <p>
     * NOTIFICATION_OPENED, //通知栏中的通知被点击打开
     * NOTIFICATION_CLICK_BTN, //通知栏中通知上的按钮被点击
     * PLUGINRSP, //标签上报回应
     *
     * @param context
     * @param event
     * @param extras
     */
    public void onEvent(final Context context, Event event, Bundle extras) {
        L.i(TAG + "onEvent: " + extras);

        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
            final String content = "收到通知附加消息： " + extras.getString(BOUND_KEY.pushMsgKey);
            final PushMessage message = new PushMessage();
            message.setMessage(content);
            if (mIPush != null) {
                JHandler.handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mIPush.onMessageClicked(context, message);
                    }
                });
            }

            L.i(content);
        } else if (Event.PLUGINRSP.equals(event)) {
            final int TYPE_LBS = 1;
            final int TYPE_TAG = 2;
            int reportType = extras.getInt(BOUND_KEY.PLUGINREPORTTYPE, -1);
            final boolean isSuccess = extras.getBoolean(BOUND_KEY.PLUGINREPORTRESULT, false);
            String message = "";
            if (TYPE_LBS == reportType) {
                message = "LBS report result :";
            } else if (TYPE_TAG == reportType) {//标签
                message = "alias set ";
                if (mIPush != null) {
                    JHandler.handler().post(new Runnable() {
                        @Override
                        public void run() {
                            mIPush.onAlias(context, String.valueOf(isSuccess));
                        }
                    });
                }

            }
            L.i(message + isSuccess);
            super.onEvent(context, event, extras);
        }
    }


    public static void clearIPush() {
        mIPush = null;
    }

    /**
     * @param context
     * @param pushState 供子类继承实现，获取push连接状态的查询结果
     */
    @Override
    public void onPushState(Context context, boolean pushState) {
        try {
            String content = "查询push通道状态： " + (pushState ? "已连接" : "未连接");
            L.i(TAG + "onPushState" + content);
            mIPush.onLog(context, content);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
