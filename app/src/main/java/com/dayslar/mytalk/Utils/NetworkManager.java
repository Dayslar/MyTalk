package com.dayslar.mytalk.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dayslar.mytalk.Models.Record;
import com.dayslar.mytalk.Service.SocketClientService;

import java.util.List;

public class NetworkManager {

    public static void unloadCallRecords(Context context){
        Log.d("RECORD", "Начинается выгрузка данных");
        Setting.loadSetting(context);
        if (Setting.isUnLoad) {
            List<Record> records = Controller.getSQL(context).getRecords();
            for (int i = 0; i < records.size(); i++) {
                int id = records.get(i).getId();
                startServiceUnload(context, id);
            }
        }
    }

    private static void startServiceUnload(Context context, int id) {
        Intent intent = new Intent(context, SocketClientService.class);
        intent.putExtra("POSITION", id);
        context.startService(intent);
    }
}
