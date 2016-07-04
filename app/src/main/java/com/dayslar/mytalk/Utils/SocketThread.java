package com.dayslar.mytalk.Utils;


import android.content.Context;
import android.util.Log;

import com.dayslar.mytalk.Models.Record;
import com.dayslar.mytalk.Service.SocketClientService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SocketThread implements Runnable {

    private int recordId;
    private int threadID;

    private UploadInterface SHI;
    private Context context;

    public SocketThread(Context context, int recordId, int threadID){
        this.context = context;
        this.recordId = recordId;
        this.threadID = threadID;

        SHI = new SocketUpload();
    }

    @Override
    public void run() {
        Socket socket = null;

        try {
            Setting.loadSetting(context);

            InetAddress ipAddress = InetAddress.getByName(Setting.settingIp);
            Integer port = Integer.parseInt(Setting.settingPort);

            Record record = Controller.getSQL(context).getRecord(recordId);

            if (record == null)
                return;

            if (record.getFilePatch().equals("") && record.getCallDuration() > 0) {
                Controller.getSQL(context).deleteRecord(recordId);
                return;
            }

            Log.d("RECORD_SOCKET", "#" + threadID + ": Пробуем подключиться + " + ipAddress);

            socket = new Socket(ipAddress, port);
            socket.setSoTimeout(10000);

            Log.d("RECORD_SOCKET", "#" + threadID + ": Успешно подключились");

            OutputStream out = socket.getOutputStream();
            if (SHI.sendInfo(out, record)) {
                Log.d("RECORD_SOCKET", "#" + threadID + ": Данные успешно отправленны");
                if (record.isCallAnswer()) {
                    if (SHI.sendFile(out, record.getFilePatch())) {
                        Log.d("RECORD_SOCKET", "#" + threadID + ": Файл успешно выгружен");
                        Controller.getSQL(context).deleteRecord(record.getId());
                    } else {
                        Log.d("RECORD_SOCKET", "#" + threadID + ": Не удалось выгрузить файл");
                    }
                }
                else {
                    Controller.getSQL(context).deleteRecord(recordId);
                }
            }

            else {
                Log.d("RECORD_SOCKET", "#" + threadID + ": Не удалось отправить данные");
            }
        } catch (IOException e) {
            Log.e("RECORD_SOCKET", "#" + threadID + e.toString());

        } finally {
            if (SHI.closeSocket(socket))
                Log.d("RECORD_SOCKET", "#" + threadID + ": close");
            SocketClientService.CLIENT_COUNT--;
        }
    }
}