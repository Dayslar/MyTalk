package com.dayslar.mytalk.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dayslar.mytalk.Models.Manager;
import com.dayslar.mytalk.Models.Record;
import com.dayslar.mytalk.Models.SharedRecord;
import com.dayslar.mytalk.Utils.Setting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MySQLiteSimpleController implements RecordDAO {

    private SQLiteDatabase db;
    private Context context;

    public MySQLiteSimpleController(Context context) {
        this.context = context;
        db = new DbHelper(context).getWritableDatabase();
    }

    public void addManager(Manager manager){
        ContentValues cv = new ContentValues();

        cv.put(Manager.MANAGER_NAME, manager.getName());
        cv.put(Manager.MANAGER_PHOTO_PATCH, manager.getPhotoPatch());

        long id = db.insert(DbHelper.MY_MANAGER_TABLE, null, cv);
        Log.d("RECORD", "Запись добавлена: " + id);
    }

    public void updateManager(int id, Manager manager){
        ContentValues cv = new ContentValues();

        cv.put(Manager.MANAGER_NAME, manager.getName());
        cv.put(Manager.MANAGER_PHOTO_PATCH, manager.getPhotoPatch());

        try {
            db.update(DbHelper.MY_MANAGER_TABLE, cv, Manager.MANAGER_ID + "=?", new String[]{id + ""});
        }

        catch (Exception e){
            Log.d("RECORD_ERROR", e.toString());
        }

    }

    public void deleteManager(int id){
        db.delete(DbHelper.MY_MANAGER_TABLE, Manager.MANAGER_ID + "=" + id, null);
        Log.d("RECORD", "Запись с " + Manager.MANAGER_ID + " = " + id + " успешно удалена");
    }

    public List<Manager> getManagers(){
        ArrayList<Manager> managers = new ArrayList<>();

        Cursor cursor = db.query(DbHelper.MY_MANAGER_TABLE, null, null, null, null, null, null);

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex(Manager.MANAGER_ID));
                String name = cursor.getString(cursor.getColumnIndex(Manager.MANAGER_NAME));
                String photoPatch = cursor.getString(cursor.getColumnIndex(Manager.MANAGER_PHOTO_PATCH));

                Manager manager = new Manager(name, photoPatch);
                manager.setID(id);
                managers.add(manager);
            }
            while (cursor.moveToNext());

        }
        else
            cursor.close();

        return managers;
    }

    public void addSharedRecord(SharedRecord record){
        ContentValues cv = new ContentValues();

        Setting.loadSetting(context);
        cv.put(Record.RECORD_FILE_PATCH, record.getFilePatch());
        cv.put(Record.RECORD_FILE_NAME, record.getFileName());
        cv.put(Record.RECORD_PHONE_NUMBER, record.getPhoneNumber());
        cv.put(Record.RECORD_CALL_NUMBER, record.getCallNumber());
        cv.put(Record.RECORD_CALL_DURATION, record.getCallDuration());
        cv.put(Record.RECORD_IS_CALL, record.isCall());
        cv.put(Record.RECORD_CONTACT_NAME, record.getContactName());
        cv.put(Record.RECORD_CALL_TIME, record.getCallTime());
        cv.put(Record.RECORD_IS_CALL_ANSWER, record.isCallAnswer());
        cv.put(Record.RECORD_MANAGER_NAME, record.getManagerName());
        cv.put(Record.RECORD_SUBDIVISION, Setting.settingSubdivision);

        if (db != null)
            db.insert(DbHelper.MY_RECORD_TABLE, null, cv);
    }

    public void addRecord(Record record){
        ContentValues cv = new ContentValues();

        Setting.loadSetting(context);
        cv.put(Record.RECORD_FILE_PATCH, record.getFilePatch());
        cv.put(Record.RECORD_FILE_NAME, record.getFileName());
        cv.put(Record.RECORD_PHONE_NUMBER, record.getPhoneNumber());
        cv.put(Record.RECORD_CALL_NUMBER, record.getCallNumber());
        cv.put(Record.RECORD_CALL_DURATION, record.getCallDuration());
        cv.put(Record.RECORD_IS_CALL, record.isCall());
        cv.put(Record.RECORD_CONTACT_NAME, record.getContactName());
        cv.put(Record.RECORD_CALL_TIME, record.getCallTime());
        cv.put(Record.RECORD_IS_CALL_ANSWER, record.isCallAnswer());
        cv.put(Record.RECORD_MANAGER_NAME, record.getManagerName());
        cv.put(Record.RECORD_SUBDIVISION, Setting.settingSubdivision);

        if (db != null)
            db.insert(DbHelper.MY_RECORD_TABLE, null, cv);
    }

    public Record getRecord(int id){

        Record record = null;
        Cursor cursor = db.query(DbHelper.MY_RECORD_TABLE, null, "_id = ?", new String[]{id + ""}, null, null, null);

        if (cursor.moveToFirst()){
            record = readRecord(cursor);
        }
        cursor.close();

        return record;
    }

    public void deleteRecord(int id){
        try {
            if (new File(getFilePatchCallRecordItem(id)).delete()) {
                Log.d("RECORD", "Запись с _id = " + id + " успешно удалена");
            }
            else
                Log.d("RECORD", "Не удалось удалить запись, возможно она отсутствует на диске");
        }
        catch (Exception e){
            Log.e("RECORD_ERROR", e.toString());
            Log.d("RECORD_ERROR", "При удалении записи произошла непредвиденная ошибка!");
        }

        db.delete(DbHelper.MY_RECORD_TABLE, Record.RECORD_ID + "=" + id, null);
    }

    public List<Record> getRecords(){
            ArrayList<Record> records = new ArrayList<>();
            Cursor cursor = db.query(DbHelper.MY_RECORD_TABLE, null, null, null, null, null, null);

            if (cursor.moveToFirst()){
                do {
                    records.add(readRecord(cursor));
                }
                while (cursor.moveToNext());
            }
            else {
                cursor.close();
            }

            return records;
        }

    private String getFilePatchCallRecordItem(int id){
        String filePatch = "";
        try {
            Cursor cursor = db.rawQuery("SELECT *FROM " + DbHelper.MY_RECORD_TABLE + " WHERE " + Record.RECORD_ID + "=?", new String[]{id + ""});
            if (cursor.moveToFirst()) {
                filePatch = cursor.getString(cursor.getColumnIndex(Record.RECORD_FILE_PATCH));
            }
            cursor.close();
        }
        catch (Exception e){
            Log.e("RECORD_ERROR", e.toString());
            Log.d("RECORD_ERROR", "Не найдено записей, или запись с _id = " + id + " повреждена");
        }

        return filePatch;

    }

    private Record readRecord(Cursor cursor){
        Record record = new Record();
        record.setId(cursor.getInt(cursor.getColumnIndex(Record.RECORD_ID)));
        record.setFilePatch(cursor.getString(cursor.getColumnIndex(Record.RECORD_FILE_PATCH)));
        record.setFileName(cursor.getString(cursor.getColumnIndex(Record.RECORD_FILE_NAME)));
        record.setPhoneNumber(cursor.getString(cursor.getColumnIndex(Record.RECORD_PHONE_NUMBER)));
        record.setCallNumber(cursor.getString(cursor.getColumnIndex(Record.RECORD_CALL_NUMBER)));
        record.setContactName(cursor.getString(cursor.getColumnIndex(Record.RECORD_CONTACT_NAME)));
        record.setCallDuration(cursor.getInt(cursor.getColumnIndex(Record.RECORD_CALL_DURATION)));
        record.setCallTime(cursor.getLong(cursor.getColumnIndex(Record.RECORD_CALL_TIME)));
        record.setIsCallAnswer(cursor.getInt(cursor.getColumnIndex(Record.RECORD_IS_CALL_ANSWER)) == 1);
        record.setIsCall(cursor.getInt(cursor.getColumnIndex(Record.RECORD_IS_CALL)) == 1);
        record.setManagerName(cursor.getString(cursor.getColumnIndex(Record.RECORD_MANAGER_NAME)));
        record.setSubdivision(cursor.getString(cursor.getColumnIndex(Record.RECORD_SUBDIVISION)));

        return record;
    }

}
