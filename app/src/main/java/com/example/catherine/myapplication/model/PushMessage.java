package com.example.catherine.myapplication.model;

import com.example.catherine.myapplication.utills.Target;

/**
 * 将消息整合成Message model
 */

public final class PushMessage {
    private int notifyID; //极光有
    private String messageID;//消息ID
    private String title;//标题
    private String message;//推送的内容
    private String extra;//消息体
    private Target target;//标记当前运用的是什么推送


    //根据小米的推送添加
    private int passThrough;//小米有，应该是用来区分消息和透传的。通知消失是0，透传是1
    private String alias;
    private String topic;
    private String userAccount;
    private boolean isNotified;//小米有，true是通知，false 是透传

    public int getNotifyID() {
        return notifyID;
    }

    public void setNotifyID(int notifyID) {
        this.notifyID = notifyID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }


    public int getPassThrough() {
        return passThrough;
    }

    public void setPassThrough(int passThrough) {
        this.passThrough = passThrough;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public boolean isNotified() {
        return isNotified;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
    }

    @Override
    public String toString() {
        return "PushMessage{" +
                "notifyID=" + notifyID +
                ", messageID='" + messageID + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", extra='" + extra + '\'' +
                ", target=" + target +
                ", passThrough=" + passThrough +
                ", alias='" + alias + '\'' +
                ", topic='" + topic + '\'' +
                ", userAccount='" + userAccount + '\'' +
                ", isNotified=" + isNotified +
                '}';
    }
}
