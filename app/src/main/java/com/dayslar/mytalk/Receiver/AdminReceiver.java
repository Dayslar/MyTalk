package com.dayslar.mytalk.Receiver;


import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AdminReceiver extends DeviceAdminReceiver {

    void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, "Приложение \"Мои разговоры\" доступно для удаления. ");
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, "Операция успешно завершена!");
    }


}
