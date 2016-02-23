package com.dayslar.mytalk.Utils;


import com.dayslar.mytalk.Models.Record;

import java.io.OutputStream;
import java.net.Socket;

public interface UploadInterface {

    //отправка информации
    boolean sendInfo(OutputStream outputStream, Record record);

    //отправка файла
    boolean sendFile(OutputStream outputStream, String fileName);

    //закрытие сокета
    boolean closeSocket(Socket socket);
}
