package com.allen.audi.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * @author : AIDAN SU
 * @className : SystemUtil.java
 * @classDescription : 系统工具类
 * @createTime : 2014-4-1
 */
public class SystemUtil {

    protected static final String PREFS_FILE = "device_id.xml";
    protected static final String PREFS_DEVICE_ID = "device_id";
    protected static UUID uuid;
    private static final String SCHEME = "package";
    /**
     * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.1及之前版本)
     */
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    /**
     * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2)
     */
    private static final String APP_PKG_NAME_22 = "pkg";

    public static final String MOBILE_NAME = Build.MANUFACTURER;

    private static final String DEVICE_XIAOMI = "Xiaomi";
    private static final String DEVICE_HUAWEI = "HUAWEI";
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";


    // 其他android系统
    public static final int SYS_OTHERS = 1001;
    // 小米系统
    public static final int SYS_MIUI = 1002;
    // 华为系统
    public static final int SYS_EMUI = 1003;

    /**
     * 启动新Activity
     *
     * @param classic 目标Activity类
     * @param bundle  参数
     */
    public static void launch(Context context, Class<? extends Activity> classic, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(context, classic);
        context.startActivity(intent);
    }


    /**
     * 从manifest.xml文件里面获得程序版本号
     *
     * @return
     */
    public static String getVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String version;
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception e) {
            version = "1";
        }
        return version;
    }

    /**
     * 根据类名获取对象实例
     *
     * @param className 类名
     */
    public static Object getObject(String className) {
        Object object = null;
        if (StringUtil.isNotEmpty(className)) {
            try {
                object = Class.forName(className).newInstance();
            } catch (ClassNotFoundException cnf) {
            } catch (InstantiationException ie) {
            } catch (IllegalAccessException ia) {
            }
        }
        return object;
    }

    /**
     * 判断存储卡是否存在
     *
     * @return
     */
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 打开软键盘
     */
    public static void openKeyboard(final Context context) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 500);
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        // 2.3（ApiLevel 9）以上，使用SDK提供的接口  
        if (apiLevel >= 9) {
            intent.setAction(Settings.ACTION_APPLICATION_SETTINGS);
            Uri uri = Uri.fromParts(SCHEME, packageName, null);
            intent.setData(uri);

            // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
        } else {
            // 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。  
            final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22 : APP_PKG_NAME_21);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra(appPkgName, packageName);
        }
        context.startActivity(intent);
    }

    /**
     * 获取APP安装包信息
     *
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        if (context == null) {
            return new PackageInfo();
        }
        Context mContext = context instanceof Activity ? context.getApplicationContext() : context;
        PackageInfo info = null;
        try {
            info = mContext.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        if (info == null) info = new PackageInfo();
        return info;
    }

    /**
     * 获取系统的版本信息
     *
     * @return
     */
    public static String[] getSystemInfo() {
        String[] version = {"null", "null", "null", "null"};
        String str1 = "/proc/version";
        String str2;
        String[] arrayOfString;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            version[0] = arrayOfString[2];//KernelVersion
            localBufferedReader.close();
        } catch (IOException e) {
        }
        version[1] = Build.VERSION.RELEASE;// firmware version
        version[2] = Build.MODEL;//model
        version[3] = Build.DISPLAY;//system version
        return version;
    }


    /**
     * 检测某个应用是否安装
     *
     * @param context
     * @param packageName 这个应用的包名
     * @return
     * @author swallow
     * @createTime 2015/7/22
     * @lastModify 2015/7/22
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 去拔号页面
     *
     * @param context
     * @param telNum
     * @return
     * @author YUHANG JO
     * @createTime 15/9/18
     * @lastModify 15/9/18
     */
    public static void goToDial(Context context, String telNum) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + telNum));
        context.startActivity(intent);
    }

    /**
     * 获取资源id
     *
     * @param context
     * @param name
     * @param type
     * @param packageName
     * @return
     * @author swallow
     * @createTime 2015/7/28
     * @lastModify 2015/7/28
     */
    public static int getResourceId(Context context, String name, String type, String packageName) {
        Resources themeResources = null;
        PackageManager pm = context.getPackageManager();
        try {
            themeResources = pm.getResourcesForApplication(packageName);
            return themeResources.getIdentifier(name, type, packageName);
        } catch (NameNotFoundException e) {

            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Apk是否处于调试模式
     *
     * @param
     * @return
     * @author swallow
     * @createTime 2015/10/29
     * @lastModify 2015/10/29
     */
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 获取系统标识 - 1001:android系统，1002:小米系统，1003:华为系统
     *
     * @param
     * @return
     * @author Gunangzhao Cai
     * @createTime 2017-08-18
     * @lastModify 2017-08-18
     */
    public static int getSystemCode() {
        int sysCode = SYS_OTHERS;
        if (Build.MANUFACTURER.equalsIgnoreCase(DEVICE_HUAWEI)) {
            try {
                String emuiApi = getSystemBuildProperties(KEY_EMUI_API_LEVEL);
                //先用emuiAPI来判断，没有的话，则用ro.build.version.emui来判断
                if (!TextUtils.isEmpty(emuiApi) && TextUtils.isDigitsOnly(emuiApi) && Long.parseLong(emuiApi) >= 10) {
                    sysCode = SYS_EMUI;
                } else {
                    String emuiVersion = getSystemBuildProperties(KEY_EMUI_VERSION);
                    if (!TextUtils.isEmpty(emuiVersion)) {
                        // 若EMUI版本大于EMUI 4.1，则初始化华为推送，否则初始化极光推送
                        sysCode = StringUtil.compareVersion(emuiVersion.replace("EmotionUI_", ""), "4.1") > 0 ? SYS_EMUI : SYS_OTHERS;
                    }
                }
            } catch (Throwable e) {
                sysCode = SYS_OTHERS;
            }
        } else if (Build.MANUFACTURER.equalsIgnoreCase(DEVICE_XIAOMI)) {
            sysCode = SYS_MIUI;
        }
        return sysCode;
    }

    /**
     * 通过反射获取/system/build.prop 里面的properties，Build里面的配置也是从这个文件里面获取的，这里直接
     * 反射Build的getString方法了，不用自己写文件流获取，防止没有权限
     *
     * @param key
     * @return
     */
    public static String getSystemBuildProperties(String key) {
        try {
            Method method = Build.class.getDeclaredMethod("getString", String.class);
            method.setAccessible(true);
            return (String) method.invoke(null, key);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从view里面提取activity，因为如果view.context拿出来的context,又可能是TintContextWrapper等包装类，需要层层提取出activity
     *
     * @param view
     * @return Activity
     */
    public static Activity getActivityFromView(View view) {
        if (view == null) {
            return null;
        }
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
