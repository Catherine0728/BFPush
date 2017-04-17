package com.example.catherine.myapplication.utills;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;

import com.example.catherine.myapplication.R;
import com.example.catherine.myapplication.feature.IPush;
import com.example.catherine.myapplication.model.TokenModel;
import com.example.catherine.myapplication.receiver.EMHuaweiPushReceiver;
import com.example.catherine.myapplication.receiver.JpushReceiver;
import com.example.catherine.myapplication.receiver.MIUIMessageReceiver;
import com.huawei.android.pushagent.api.PushException;
import com.huawei.android.pushagent.api.PushManager;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


/**
 * Created by jiang on 2016/10/8.
 */

public class Push {

    /**
     * 初始化配置
     *
     * @param context
     * @param debug
     */
    public static void register(Context context, boolean debug) {
        register(context, debug, null);
    }

    /**
     * 注册
     *
     * @param context
     * @param debug
     * @param IPush   根据机型来判断的注册方式
     */
    public static void register(Context context, boolean debug, IPush IPush) {
        if (context == null)
            return;
        if (RomUtil.rom() == Target.EMUI) {
            if (IPush != null) {
                EMHuaweiPushReceiver.registerInterface(IPush);
            }
            PushManager.requestToken(context);
        } else if (RomUtil.rom() == Target.MIUI) {
            if (IPush != null) {
                MIUIMessageReceiver.registerInterface(IPush);
            }
            if (shouldInit(context)) {
                MiPushClient.registerPush(context, Const.APP_ID, Const.APP_KEY);
            }
            if (debug) {
                LoggerInterface newLogger = new LoggerInterface() {
                    @Override
                    public void setTag(String tag) {
                        // ignore
                    }

                    @Override
                    public void log(String content, Throwable t) {
                        L.i("content" + content + " exception: " + t.toString());
                    }

                    @Override
                    public void log(String content) {
                        L.i("miui: " + content);
                    }
                };
                Logger.setLogger(context, newLogger);
            }

        } else if (RomUtil.rom() == Target.JPUSH) {
            if (IPush != null) {
                JpushReceiver.registerInterface(IPush);
            }
            JPushInterface.init(context);
            JPushInterface.setDebugMode(debug);
            String rid = JPushInterface.getRegistrationID(context);
            L.line(rid);
        }


    }

    /**
     * 注册
     *
     * @param context
     * @param IPush   根据接受推送的灵敏度来注册这些推送，所以首先应该全部注册
     */
    public static void register(Context context, IPush IPush) {
        if (context == null)
            return;
        if (IPush != null) {
            EMHuaweiPushReceiver.registerInterface(IPush);
            MIUIMessageReceiver.registerInterface(IPush);
            JpushReceiver.registerInterface(IPush);

        }
        PushManager.requestToken(context);
        if (shouldInit(context)) {
            MiPushClient.registerPush(context, Const.APP_ID, Const.APP_KEY);
        }
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                L.i("content" + content + " exception: " + t.toString());
            }

            @Override
            public void log(String content) {
                L.i("miui: " + content);
            }
        };
        Logger.setLogger(context, newLogger);
        JPushInterface.init(context);
        JPushInterface.setDebugMode(true);// TODO: 17/4/17 设置debug模式默认为true
        String rid = JPushInterface.getRegistrationID(context);
        L.line(rid);


    }

    /**
     * 用于小米推送的注册
     *
     * @param context
     * @return
     */
    private static boolean shouldInit(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static void unregister(Context context) {
        if (context == null)
            return;
        if (RomUtil.rom() == Target.EMUI) {
            EMHuaweiPushReceiver.clearIPush();
            PushManager.deregisterToken(context, getToken(context).getToken());
            return;

        }
        if (RomUtil.rom() == Target.MIUI) {
            MIUIMessageReceiver.clearIPush();
            MiPushClient.unregisterPush(context);
            return;
        }
        if (RomUtil.rom() == Target.JPUSH) {
            JpushReceiver.clearPushInterface();
            JPushInterface.stopPush(context);
            return;
        }

    }


    /**
     * @param IPush
     */
    public static void setIPush(IPush IPush) {
        if (IPush == null)
            return;
        if (RomUtil.rom() == Target.EMUI) {
            EMHuaweiPushReceiver.registerInterface(IPush);
            return;

        }
        if (RomUtil.rom() == Target.MIUI) {
            MIUIMessageReceiver.registerInterface(IPush);
            return;
        }
        if (RomUtil.rom() == Target.JPUSH) {
            JpushReceiver.registerInterface(IPush);
            return;
        }
    }


    /**
     * 设置别名，
     * 华为不支持alias的写法，所以只能用tag，tag只能放map，所以alias作为value,key为name
     *
     * @param context
     * @param alias
     */
    public static void setAlias(final Context context, String alias) {
        if (TextUtils.isEmpty(alias))
            return;
        if (RomUtil.rom() == Target.EMUI) {
            Map<String, String> tag = new HashMap<>();
            tag.put("name", alias);
            PushManager.setTags(context, tag);
            return;

        }
        if (RomUtil.rom() == Target.MIUI) {
            MiPushClient.setAlias(context, alias, null);

            return;
        }
        if (RomUtil.rom() == Target.JPUSH) {
            JPushInterface.setAlias(context, alias, new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                    if (i == 0) { // 这里极光规定0代表成功
                        if (JpushReceiver.getPushInterface() != null) {
                            L.i("JPushInterface.setAlias");
                            JpushReceiver.getPushInterface().onAlias(context, s);

                        }
                    }
                }
            });
            return;
        }
    }

    /**
     * 设置别名，
     * 华为不支持alias的写法，所以只能用tag，tag只能放map，所以alias作为value,key为name
     *
     * @param context
     * @param tags
     */
    public static void setTag(final Context context, final String tags, boolean isSubscribe) {
        if (TextUtils.isEmpty(tags))
            return;
        if (RomUtil.rom() == Target.EMUI) {
            Map<String, String> tag = new HashMap<>();
            tag.put("name", tags);
            PushManager.setTags(context, isSubscribe ? tag : null);
            return;

        }
        if (RomUtil.rom() == Target.MIUI) {
            if (isSubscribe) {
                MiPushClient.subscribe(context, tags, null);

            } else {
                MiPushClient.unsubscribe(context, tags, null);

            }
            return;
        }
        if (RomUtil.rom() == Target.JPUSH) {

            // ","隔开的多个 转换成 Set
            String[] sArray = tags.split(",");
            Set<String> tagSet = new LinkedHashSet<String>();
            for (String sTagItme : sArray) {
                if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
                    L.line(context.getString(R.string.error_tag_empty));
                    return;
                }
                tagSet.add(sTagItme);
            }

            JPushInterface.setTags(context, isSubscribe ? tagSet : null, new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                    if (i == 0) { // 这里极光规定0代表成功
                        if (JpushReceiver.getPushInterface() != null) {
                            L.i("JPushInterface.setTag");
                            JpushReceiver.getPushInterface().onTags(context, set.toString());

                        }
                    } else if (i == 6002) {
                        String logs = "Failed to set  tags due to timeout. Try again after 60s.";
                        L.i(logs);
                        if (ExampleUtil.isConnected(context)) {
                            JpushReceiver.getPushInterface().onTags(context, tags);

                        } else {
                            L.i("No network");
                        }
                    }
                }
            });
        }
    }


    /**
     * 获取唯一的token
     *
     * @param context
     * @return
     */

    public static TokenModel getToken(Context context) {
        if (context == null)
            return null;
        TokenModel result = new TokenModel();
        result.setTarget(RomUtil.rom());
        if (RomUtil.rom() == Target.EMUI) {
            result.setToken(EMHuaweiPushReceiver.getmToken());
        }
        if (RomUtil.rom() == Target.MIUI) {
            result.setToken(MiPushClient.getRegId(context));
        }
        if (RomUtil.rom() == Target.JPUSH) {
            result.setToken(JPushInterface.getRegistrationID(context));
        }
        return result;

    }


    /**
     * 停止推送
     */
    public static void pause(Context context) {
        if (context == null)
            return;
        IPush IPush;
        if (RomUtil.rom() == Target.EMUI) {
            PushManager.enableReceiveNormalMsg(context, false);
            PushManager.enableReceiveNotifyMsg(context, false);
            IPush = EMHuaweiPushReceiver.getIPush();
            if (IPush != null) {
                IPush.onPaused(context);
            }
            return;

        }
        if (RomUtil.rom() == Target.MIUI) {
            MiPushClient.pausePush(context, null);
            IPush = MIUIMessageReceiver.getIPush();
            if (IPush != null) {
                IPush.onPaused(context);
            }
            return;
        }
        if (RomUtil.rom() == Target.JPUSH) {
            if (!JPushInterface.isPushStopped(context)) {
                JPushInterface.stopPush(context);
                IPush = JpushReceiver.getPushInterface();
                if (IPush != null) {
                    IPush.onPaused(context);
                }
            }
            return;
        }
    }


    /**
     * 开始推送
     */
    public static void resume(Context context) {
        if (context == null)
            return;
        IPush IPush;
        if (RomUtil.rom() == Target.EMUI) {
            PushManager.enableReceiveNormalMsg(context, true);
            PushManager.enableReceiveNotifyMsg(context, true);
            IPush = EMHuaweiPushReceiver.getIPush();
            if (IPush != null) {
                IPush.onResume(context);
            }
            return;
        }
        if (RomUtil.rom() == Target.MIUI) {
            MiPushClient.resumePush(context, null);
            IPush = MIUIMessageReceiver.getIPush();
            if (IPush != null) {
                IPush.onResume(context);
            }
            return;
        }
        if (RomUtil.rom() == Target.JPUSH) {
            if (JPushInterface.isPushStopped(context)) {
                JPushInterface.resumePush(context);
                IPush = JpushReceiver.getPushInterface();
                if (IPush != null) {
                    IPush.onResume(context);
                }
            }
            return;
        }
    }

    /**
     * 查看通道状态
     */
    public static void getState(Context context) {
        if (RomUtil.rom() == Target.EMUI) {
            EMHuaweiPushReceiver.getPushState(context);
        }
    }

    /**
     * 查询所有的tag
     */
    public static synchronized Map<String, String> getTags(Context context) throws
            PushException {
        Map<String, String> tags = new HashMap<>();
        if (RomUtil.rom() == Target.EMUI) {
            tags = PushManager.getTags(context);
            L.line(tags.toString());

        }
        return tags;

    }


}
