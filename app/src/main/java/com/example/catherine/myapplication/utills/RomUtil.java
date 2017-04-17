package com.example.catherine.myapplication.utills;

import com.example.catherine.myapplication.model.BuildProperties;

import java.io.IOException;
import java.util.Random;


/**
 * Created by jiang on 2016/10/8.
 */

public class RomUtil {
    private static Target mTarget = null;
    private static int receiveMode = -1;//设置一个变量来表示最快接受到信息的是哪一种推送方式。
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_HANDY_MODE_SF = "ro.miui.has_handy_mode_sf";
    private static final String KEY_MIUI_REAL_BLUR = "ro.miui.has_real_blur";

    private static final String KEY_FLYME_ICON = "persist.sys.use.flyme.icon";
    private static final String KEY_FLYME_PUBLISHED = "ro.flyme.published";
    private static final String KEY_FLYME_FLYME = "ro.meizu.setupwizard.flyme";


    /**
     * 华为rom
     *
     * @return
     */
    private static boolean isEMUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.containsKey(KEY_EMUI_VERSION_CODE);
        } catch (final IOException e) {
            return false;
        }
    }

    /**
     * 小米rom
     *
     * @return
     */

    private static boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            /*String rom = "" + prop.getProperty(KEY_MIUI_VERSION_CODE, null) + prop.getProperty(KEY_MIUI_VERSION_NAME, null)+prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null);
            Log.d("Android_Rom", rom);*/
            return prop.containsKey(KEY_MIUI_VERSION_CODE)
                    || prop.containsKey(KEY_MIUI_VERSION_NAME)
                    || prop.containsKey(KEY_MIUI_REAL_BLUR)
                    || prop.containsKey(KEY_MIUI_HANDY_MODE_SF);
        } catch (final IOException e) {
            return false;
        }
    }

    /**
     * 魅族rom
     *
     * @return
     */
    private static boolean isFlyme() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.containsKey(KEY_FLYME_ICON)
                    || prop.containsKey(KEY_FLYME_PUBLISHED)
                    || prop.containsKey(KEY_FLYME_FLYME);
        } catch (final IOException e) {
            return false;
        }
    }


    /**
     * 根据机型来判断当前应该用哪一种rom
     */
    public static Target rom() {
        if (RomUtil.receiveMode != -1) {
            switch (RomUtil.receiveMode) {
                /**
                 * 0:华为
                 * 1:小米
                 * 2:魅族
                 * 3:极光
                 * */
                case 0://华为
                    mTarget = Target.EMUI;
                    break;
                case 1://小米
                    mTarget = Target.MIUI;
                    break;
                case 2://魅族
                    mTarget = Target.FLYME;
                    break;
                case 3://极光
                    mTarget = Target.JPUSH;
                    break;
            }
        } else {
            if (mTarget != null)
                return mTarget;

            if (isEMUI()) {
                mTarget = Target.EMUI;
                return mTarget;
            }
            if (isMIUI()) {
                mTarget = Target.MIUI;
                return mTarget;
            }
            if (isFlyme()) {
                mTarget = Target.FLYME;
                return mTarget;
            }
            mTarget = Target.JPUSH;

        }
        return mTarget;

    }

    /**
     * 得到当前的最快接受推送方式
     */
    public static void setReceiveMode(int receiveMode) {
        System.out.println("RomUtil.receiveMode===>" + RomUtil.receiveMode + "===" + receiveMode);
        if (RomUtil.receiveMode != -1) return;
        RomUtil.receiveMode = receiveMode;
    }

}
