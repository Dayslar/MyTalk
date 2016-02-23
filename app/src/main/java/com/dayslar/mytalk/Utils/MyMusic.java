package com.dayslar.mytalk.Utils;

import android.media.MediaPlayer;
import android.util.Log;

public class MyMusic{

    private static MediaPlayer mediaPlayer;

    //воспроизводит звук, в качестве параметра примимает путь к файлу
    public void playMusic(String fileName) {
        try {
            releaseMusic();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }

        catch (Exception e){
            Log.d("RECORD", e.toString());
            e.printStackTrace();
        }
    }

    //останавлявает воспроизвидение
    public void stopMusic() {
        if (mediaPlayer != null)
            mediaPlayer.stop();
    }

    //очищает занятое плеером место
    public void releaseMusic() {
        if (mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer = null;
    }
}
