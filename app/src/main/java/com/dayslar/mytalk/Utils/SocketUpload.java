package com.dayslar.mytalk.Utils;

import com.dayslar.mytalk.Models.Record;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SocketUpload implements UploadInterface {

    public boolean sendInfo(OutputStream outputStream, Record record){
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try {

            dataOutputStream.writeUTF(record.getFileName());
            dataOutputStream.writeUTF(record.getPhoneNumber());
            dataOutputStream.writeUTF(record.getCallNumber());
            dataOutputStream.writeInt(record.getCallDuration());
            dataOutputStream.writeBoolean(record.isCall());
            dataOutputStream.writeLong(record.getCallTime());
            dataOutputStream.writeUTF(record.getContactName());
            dataOutputStream.writeBoolean(record.isCallAnswer());
            dataOutputStream.writeUTF(record.getManagerName());
            dataOutputStream.writeUTF(record.getSubdivision());

            dataOutputStream.flush();
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public boolean sendFile(OutputStream outputStream, String fileName){
        File file = new File(fileName);

        try(FileInputStream fileInputStream = new FileInputStream(file);
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {

            dataOutputStream.writeUTF(getHash(file));
            dataOutputStream.flush();

            int buff;
            byte[] buffer = new byte[8 * 1024];

            while (true){
                buff = fileInputStream.read(buffer);
                if (buff != -1)
                    outputStream.write(buffer, 0, buff);
                else
                    break;
            }

            outputStream.flush();
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public boolean closeSocket(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
                return true;
            }
            else{
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getHash(File file) {
        MessageDigest md;
        StringBuilder hash = new StringBuilder();

        try(FileInputStream fis = new FileInputStream(file)) {
            md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8 *1024];

            int buff;
            while ((buff = fis.read(buffer)) != -1) {
                md.update(buffer, 0, buff);
            }

            byte[] bytes = md.digest();
            for (byte aByte : bytes) {
                hash.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash.toString();
    }
}
