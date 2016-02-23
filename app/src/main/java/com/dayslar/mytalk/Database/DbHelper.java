package com.dayslar.mytalk.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dayslar.mytalk.Models.Manager;
import com.dayslar.mytalk.Models.Record;

public class DbHelper extends SQLiteOpenHelper {

    public static final String MY_DB_NAME = "RecordDB";
    public static final String MY_RECORD_TABLE = "MyRecords";
    public static final String MY_MANAGER_TABLE = "MyManager";
    public static final int MY_DB_VERSION = 1;



    public DbHelper(Context context) {
        super(context, MY_DB_NAME, null, MY_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createRecordTable(db);
        createManagerTable(db);
    }

    private void createManagerTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MY_MANAGER_TABLE + "(" +
                Manager.MANAGER_ID + " integer primary key autoincrement," +
                Manager.MANAGER_NAME + " text, " +
                Manager.MANAGER_PHOTO_PATCH + " text);");
    }

    private void createRecordTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MY_RECORD_TABLE + "( " +
                Record.RECORD_ID + " integer primary key autoincrement," +
                Record.RECORD_FILE_PATCH + " text," +
                Record.RECORD_FILE_NAME + " text," +
                Record.RECORD_PHONE_NUMBER + " text," +
                Record.RECORD_CALL_NUMBER + " text," +
                Record.RECORD_CALL_DURATION + " integer," +
                Record.RECORD_CALL_TIME + " bigint(19)," +
                Record.RECORD_IS_CALL_ANSWER + " tinyint(1)," +
                Record.RECORD_CONTACT_NAME + " text," +
                Record.RECORD_MANAGER_NAME + " text," +
                Record.RECORD_IS_CALL + " tinyint(1)," +
                Record.RECORD_SUBDIVISION + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
