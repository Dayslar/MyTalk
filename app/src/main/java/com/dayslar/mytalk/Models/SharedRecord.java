package com.dayslar.mytalk.Models;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedRecord extends Record {

    private SharedPreferences sharedPreferences;
    public static final String SHARED_RECORD = "shader_record";

    public SharedRecord(Context context) {
        synchronized (this){
            sharedPreferences = context.getSharedPreferences(SHARED_RECORD, Context.MODE_MULTI_PROCESS);
        }
    }

    @Override
    public synchronized void setFilePatch(String filePatch) {
        sharedPreferences.edit().putString(RECORD_FILE_PATCH, filePatch).apply();
    }

    @Override
    public synchronized void setFileName(String fileName) {
        sharedPreferences.edit().putString(RECORD_FILE_NAME, fileName).apply();
    }

    @Override
    public synchronized void setPhoneNumber(String myPhoneNumber) {
        sharedPreferences.edit().putString(RECORD_PHONE_NUMBER, myPhoneNumber).apply();
    }

    @Override
    public synchronized void setCallNumber(String callPhoneNumber) {
        sharedPreferences.edit().putString(RECORD_CALL_NUMBER, callPhoneNumber).apply();
    }

    @Override
    public synchronized void setCallDuration(int callDuration) {
        sharedPreferences.edit().putInt(RECORD_CALL_DURATION, callDuration).apply();

    }

    @Override
    public synchronized void setIsCall(boolean isCall) {
        sharedPreferences.edit().putBoolean(RECORD_IS_CALL, isCall).apply();
    }

    @Override
    public synchronized void setCallTime(long callTime) {
        sharedPreferences.edit().putLong(RECORD_CALL_TIME, callTime).apply();
    }

    @Override
    public synchronized void setIsCallAnswer(boolean isCallAnswer) {
        sharedPreferences.edit().putBoolean(RECORD_IS_CALL_ANSWER, isCallAnswer).apply();
    }

    @Override
    public synchronized void setManagerName(String managerName) {
        sharedPreferences.edit().putString(RECORD_MANAGER_NAME, managerName).apply();
    }

    @Override
    public synchronized void setContactName(String contactName) {
        sharedPreferences.edit().putString(RECORD_CONTACT_NAME, contactName).apply();
    }

    public synchronized void setContactPhoto(String contactPhoto){
        sharedPreferences.edit().putString(RECORD_CONTACT_PHOTO, contactPhoto).apply();
    }

    public synchronized void setIsCalling(Boolean isCalling){
        sharedPreferences.edit().putBoolean(RECORD_IS_CALLING, isCalling).apply();
    }

    @Override
    public synchronized String getFilePatch() {
        return sharedPreferences.getString(RECORD_FILE_PATCH, "");
    }

    @Override
    public synchronized String getFileName() {
        return sharedPreferences.getString(RECORD_FILE_NAME, "");
    }

    @Override
    public synchronized String getPhoneNumber() {
        return sharedPreferences.getString(RECORD_PHONE_NUMBER, "");
    }

    @Override
    public synchronized String getCallNumber() {
        return sharedPreferences.getString(RECORD_CALL_NUMBER, "");
    }

    @Override
    public synchronized int getCallDuration() {
        return sharedPreferences.getInt(RECORD_CALL_DURATION, 0);
    }

    @Override
    public synchronized boolean isCall() {
        return sharedPreferences.getBoolean(RECORD_IS_CALL, false);
    }

    @Override
    public synchronized long getCallTime() {
        return sharedPreferences.getLong(RECORD_CALL_TIME, 0);
    }

    @Override
    public synchronized String getContactName() {
        return sharedPreferences.getString(RECORD_CONTACT_NAME, "");
    }


    public synchronized String getContactPhoto(){
        return sharedPreferences.getString(RECORD_CONTACT_PHOTO, "");
    }

    public synchronized boolean isCalling(){
        return sharedPreferences.getBoolean(RECORD_IS_CALLING, false);
    }

    @Override
    public synchronized boolean isCallAnswer() {
        return sharedPreferences.getBoolean(RECORD_IS_CALL_ANSWER, false);
    }

    @Override
    public synchronized String getManagerName() {
        return sharedPreferences.getString(RECORD_MANAGER_NAME, "");

    }
}
