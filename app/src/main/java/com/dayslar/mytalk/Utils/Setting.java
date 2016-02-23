package com.dayslar.mytalk.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.dayslar.mytalk.R;

public class Setting {

    private final static String settingPasswordDefault = "7766";
    private final static String settingIpDefault = "192.168.1.173";
    private final static String settingPortDefault = "8080";
    private final static int settingDelayIsCallDefault = 10;
    private final static String settingSubdivisionDefault = "";

    public static Boolean isRecord;
    public static Boolean isAdmin;
    public static Boolean isUnLoad;

    public static int delayIsCall;
    public static String settingPassword;
    public static String settingIp;
    public static String settingPort;
    public static String settingSubdivision;


    public static synchronized void loadSetting(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        changeInfo(context, sharedPreferences);
    }

    public static synchronized void loadSetting(Context context, SharedPreferences preferences){
        changeInfo(context, preferences);
    }

    private static void changeInfo(Context context, SharedPreferences sharedPreferences) {
        Resources resources = context.getResources();

        isRecord = sharedPreferences.getBoolean(resources.getString(R.string.chActiveRecordKey), true);
        isAdmin = sharedPreferences.getBoolean(resources.getString(R.string.chAdminKey), false);
        isUnLoad = sharedPreferences.getBoolean(resources.getString(R.string.chUnLoadRecordKey), false);

        delayIsCall = Integer.parseInt(sharedPreferences.getString(resources.getString(R.string.etDelayRecordIsCallKey), settingDelayIsCallDefault + ""));

        settingPassword = sharedPreferences.getString(resources.getString(R.string.etPasswordKey), settingPasswordDefault);
        settingIp = sharedPreferences.getString(resources.getString(R.string.etSettingIpKey), settingIpDefault);
        settingPort = sharedPreferences.getString(resources.getString(R.string.etSettingPortKey), settingPortDefault);
        settingSubdivision = sharedPreferences.getString(resources.getString(R.string.etSettingSubdivisionKey), settingSubdivisionDefault);
    }
}
