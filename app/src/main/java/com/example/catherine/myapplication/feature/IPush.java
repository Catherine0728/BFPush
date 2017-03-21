package com.example.catherine.myapplication.feature;

import android.content.Context;

import com.example.catherine.myapplication.model.PushMessage;

/**
 * Created by catherine on 17/3/13.
 */

public interface IPush {
    /**
     * 注册成功之后回调
     *
     * @param context
     * @param registerID
     */
    void onRegister(Context context, String registerID);

    /**
     * 取消注册成功
     *
     * @param context
     */
    void onUnRegister(Context context);

    /**
     * 暂停推送
     *
     * @param context
     */
    void onPaused(Context context);

    /**
     * 开启推送
     *
     * @param context
     */
    void onResume(Context context);

    /**
     * 通知下来之后
     *
     * @param context
     * @param pushMessage
     */
    void onMessage(Context context, PushMessage pushMessage);

    /**
     * 通知栏被点击之后
     *
     * @param context
     * @param pushMessage
     */
    void onMessageClicked(Context context, PushMessage pushMessage);

    /**
     * 透传消息
     *
     * @param context
     * @param pushMessage
     */
    void onCustomMessage(Context context, PushMessage pushMessage);


    /**
     * 别名设置成功的回调
     *
     * @param context
     * @param alias
     */
    void onAlias(Context context, String alias);

    /**
     * tags设置成功的回调
     *
     * @param context
     * @param tags
     */
    void onTags(Context context, String tags);

    /**
     * 一些消息的回调
     */
    void onLog(Context context, String tags);
}
