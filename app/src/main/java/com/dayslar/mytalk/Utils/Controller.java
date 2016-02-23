package com.dayslar.mytalk.Utils;


import android.content.Context;

import com.dayslar.mytalk.Database.MySQLiteSimpleController;


public class Controller {

    public static MySQLiteSimpleController getSQL(Context context) {
            return new MySQLiteSimpleController(context);
    }

    public static MyMusic getMyMusic() {
        return new MyMusic();
    }

}
