package com.dayslar.mytalk.Models;

public class Record {

    //для таблицы записей
    public static final String RECORD_ID = "_id"; //идентификатор записи
    public static final String RECORD_FILE_PATCH = "file_patch"; //путь к записе на диске
    public static final String RECORD_FILE_NAME = "file_name"; // имя файла
    public static final String RECORD_PHONE_NUMBER = "phone_number"; //свой номер телефона
    public static final String RECORD_CALL_NUMBER = "call_number"; //номер телефона звнящего
    public static final String RECORD_CALL_DURATION = "call_duration"; //продолжительность звонка
    public static final String RECORD_IS_CALL = "is_call"; //определяет сам звонишь или нет
    public static final String RECORD_CALL_TIME = "call_time"; //время начала звонка
    public static final String RECORD_CONTACT_NAME = "contact_name"; //если тот кто звонит есть в контактах, записывается его имя
    public static final String RECORD_IS_CALL_ANSWER = "is_call_answer"; //определяет произошел ответ на вопрос, или нет
    public static final String RECORD_MANAGER_NAME = "manager_name"; //имя манеджера ответившего на звонок
    public static final String RECORD_IS_CALLING = "is_calling"; //определяет звонит телефон, или нет
    public static final String RECORD_CONTACT_PHOTO = "contact_photo"; //путь к фотке контакта
    public static final String RECORD_SUBDIVISION = "subdivision"; //устанавливает организацию у записи

    private int id;
    private String filePatch;
    private String fileName;
    private String phoneNumber;
    private String callNumber;
    private int callDuration;
    private String subdivision;
    private long callTime;
    private String contactName;
    private String managerName;
    private boolean isCall;
    private boolean isCallAnswer;


    public void setId(int id) {
        this.id = id;
    }

    public void setFilePatch(String filePatch) {
        this.filePatch = filePatch;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public void setCallDuration(int callDuration) {
        this.callDuration = callDuration;
    }

    public void setIsCall(boolean isCall) {
        this.isCall = isCall;
    }

    public void setCallTime(long callTime) {
        this.callTime = callTime;
    }

    public void setIsCallAnswer(boolean isCallAnswer) {
        this.isCallAnswer = isCallAnswer;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }

    public int getId() {
        return id;
    }

    public String getFilePatch() {
        return filePatch;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public int getCallDuration() {
        return callDuration;
    }

    public boolean isCall() {
        return isCall;
    }

    public long getCallTime() {
        return callTime;
    }

    public String getContactName() {
        return contactName;
    }

    public boolean isCallAnswer() {
        return isCallAnswer;
    }

    public String getManagerName() {
        return managerName;
    }

    public String getSubdivision() {
        return subdivision;
    }
}
