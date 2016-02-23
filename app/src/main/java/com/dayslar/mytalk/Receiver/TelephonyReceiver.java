package com.dayslar.mytalk.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dayslar.mytalk.Service.TelephonyService;
import com.dayslar.mytalk.Utils.Setting;


public class TelephonyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Setting.loadSetting(context);

        if (Setting.isRecord) {
            intent.setClass(context, TelephonyService.class);
            context.startService(intent);
        }
    }


}
