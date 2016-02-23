package com.dayslar.mytalk.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dayslar.mytalk.Utils.NetworkManager;


public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getExtras() != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            int networkType = intent.getExtras().getInt(ConnectivityManager.EXTRA_NETWORK_TYPE);

            NetworkInfo ni = cm.getNetworkInfo(networkType);
            if (ni.isConnected())
                NetworkManager.unloadCallRecords(context);
        }
    }
}