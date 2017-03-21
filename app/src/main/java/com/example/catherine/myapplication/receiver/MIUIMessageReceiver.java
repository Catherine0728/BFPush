package com.example.catherine.myapplication.receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.catherine.myapplication.feature.IPush;
import com.example.catherine.myapplication.aty.MainActivity;
import com.example.catherine.myapplication.aty.MyApplication;
import com.example.catherine.myapplication.R;
import com.example.catherine.myapplication.model.PushMessage;
import com.example.catherine.myapplication.utills.JHandler;
import com.example.catherine.myapplication.utills.L;
import com.example.catherine.myapplication.utills.RomUtil;
import com.example.catherine.myapplication.utills.Target;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by catherine on 17/3/8.
 * onReceivePassThroughMessage用来接收服务器发送的透传消息，
 * onNotificationMessageClicked用来接收服务器发来的通知栏消息（用户点击通知栏时触发），
 * onNotificationMessageArrived用来接收服务器发来的通知栏消息（消息到达客户端时触发，并且可以接收应用在前台时不弹出通知的通知消息），
 * onCommandResult用来接收客户端向服务器发送命令消息后返回的响应，onReceiveRegisterResult用来接受客户端向服务器发送注册命令消息后返回的响应。
 */

public class MIUIMessageReceiver extends PushMessageReceiver {
    private static String TAG = "MIUIMessageReceiver====";
    private String mRegId;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mUserAccount;
    private String mStartTime;
    private String mEndTime;

    private static IPush iPush;

    /**
     * @param pushInterface
     */
    public static void registerInterface(IPush pushInterface) {
        iPush = pushInterface;
    }

    public static IPush getIPush() {
        return iPush;
    }

    public static void clearIPush() {
        iPush = null;
    }

    /***
     *
     *透传消息到达后
     */
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        log("onReceivePassThroughMessage ", message.toString());
        MainActivity.logList.add(0, getSimpleDate() + " " + mMessage);
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount = message.getUserAccount();
        }
        getPushMessage(context, message);
    }

    /**
     * 用户点击后
     */

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        log("onNotificationMessageClicked ", message.toString());
        MainActivity.logList.add(0, getSimpleDate() + " " + mMessage);
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount = message.getUserAccount();
        }
        getPushMessage(context, message);

    }

    /**
     * 通知消息到达后
     * <p>
     * 对于应用在前台时不弹出通知的通知消息，
     * SDK会将消息通过广播方式传给AndroidManifest中注册的PushMessageReceiver的子类的onNotificationMessageArrived方
     * 法(在MIUI上，如果没有收到onNotificationMessageArrived回调，是因为使用的MIUI版本还不支持该特性，需要升级到MIUI7之后。非MIUI手机都可以收到这个回调)。
     */
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        log("onNotificationMessageArrived ", message.toString());
        MainActivity.logList.add(0, getSimpleDate() + " " + mMessage);
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount = message.getUserAccount();
        }
        getPushMessage(context, message);

    }

    private void log(String tag, String message) {
        L.i(TAG + tag + message);

    }


    private void getPushMessage(final Context context, MiPushMessage miPushMessage) {
        String content = miPushMessage.getContent();
        String userCount = miPushMessage.getUserAccount();
        String title = miPushMessage.getTitle();
        String messageID = miPushMessage.getMessageId();
        int passThrough = miPushMessage.getPassThrough();
        String alias = miPushMessage.getAlias();
        String topic = miPushMessage.getTopic();
        HashMap<String, String> extra = (HashMap<String, String>) miPushMessage.getExtra();
        boolean isNotified = miPushMessage.isNotified();
        final PushMessage pushMessage = new PushMessage();
        pushMessage.setMessage(content);
        pushMessage.setAlias(alias);
        pushMessage.setMessageID(messageID);
        pushMessage.setNotified(isNotified);
        pushMessage.setPassThrough(passThrough);
        pushMessage.setTopic(topic);
        pushMessage.setTarget(Target.MIUI);
        pushMessage.setTitle(title);
        pushMessage.setExtra(extra.toString());
        pushMessage.setUserAccount(userCount);
        if (iPush != null) {
            L.lineModel(pushMessage.toString());
            JHandler.handler().post(new Runnable() {
                @Override
                public void run() {
                    iPush.onMessage(context, pushMessage);
                }
            });
        }
    }

    /**
     * 客户端向服务器发送命令消息后返回的响应
     */

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        log("onCommandResult ", message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                log = context.getString(R.string.register_success);
            } else {
                log = context.getString(R.string.register_fail);
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                log = context.getString(R.string.set_alias_success, mAlias);
            } else {
                log = context.getString(R.string.set_alias_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                log = context.getString(R.string.unset_alias_success, mAlias);
            } else {
                log = context.getString(R.string.unset_alias_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mUserAccount = cmdArg1;
                log = context.getString(R.string.set_account_success, mUserAccount);
            } else {
                log = context.getString(R.string.set_account_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mUserAccount = cmdArg1;
                log = context.getString(R.string.unset_account_success, mUserAccount);
            } else {
                log = context.getString(R.string.unset_account_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
                log = context.getString(R.string.subscribe_topic_success, mTopic);
            } else {
                log = context.getString(R.string.subscribe_topic_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
                log = context.getString(R.string.unsubscribe_topic_success, mTopic);
            } else {
                log = context.getString(R.string.unsubscribe_topic_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
                log = context.getString(R.string.set_accept_time_success, mStartTime, mEndTime);
            } else {
                log = context.getString(R.string.set_accept_time_fail, message.getReason());
            }
        } else {
            log = message.getReason();
        }
        MainActivity.logList.add(0, getSimpleDate() + "    " + log);
        if (iPush != null) {
            iPush.onLog(context, log);
        }

    }


    /**
     * 接受客户端向服务器发送注册命令消息后返回的响应
     */

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        log("onReceiveRegisterResult ", message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                log = context.getString(R.string.register_success) + mRegId;
            } else {
                log = context.getString(R.string.register_fail);
            }
        } else {
            log = message.getReason();
        }
        if (iPush != null) {
            iPush.onRegister(context, log);
        }

    }

    @SuppressLint("SimpleDateFormat")
    private static String getSimpleDate() {
        return new SimpleDateFormat("MM-dd hh:mm:ss").format(new Date());
    }

}
