package com.dayslar.mytalk.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.dayslar.mytalk.Utils.SocketThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketClientService extends Service {

    public static int CLIENT_COUNT = 0;
    private static ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();

        executorService = Executors.newFixedThreadPool(2);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int position = intent.getIntExtra("POSITION", -1);

        if (position != -1) {
            CLIENT_COUNT++;
            executorService.execute(new SocketThread(this, position, CLIENT_COUNT));
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }
}
