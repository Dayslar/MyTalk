package com.dayslar.mytalk.Service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.dayslar.mytalk.Activity.MainActivity;
import com.dayslar.mytalk.Models.SharedRecord;
import com.dayslar.mytalk.R;
import com.dayslar.mytalk.Utils.ContactUtil;
import com.dayslar.mytalk.Utils.Controller;
import com.dayslar.mytalk.Utils.NetworkManager;
import com.dayslar.mytalk.Utils.Setting;

import java.io.File;
import java.lang.reflect.Method;

public class TelephonyService extends Service {

    public static final int NOTIFICATION_ID = 222;
    private Notification notification;

    private static final String DIRECTORY = "/.MyRecord";
    private static final String RECORD_LOG = "RECORD";
    private static final String NEW_OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL";
    private static final String PHONE_STATE = "android.intent.action.PHONE_STATE";
    private static final String PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";
    private static final String RECORD_MASK = ".3gp";

    private static SharedRecord sharedRecord;
    private static long startRecord;

    @Override
    public void onCreate() {
        super.onCreate();

        notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Мои разговоры")
                .setContentText("Служба запущена")
                .setWhen(System.currentTimeMillis())
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, notification);

        Setting.loadSetting(this);
        sharedRecord = new SharedRecord(this);

        if (intent.getAction() != null) {
            sharedRecord.setPhoneNumber(((TelephonyManager)
                    getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number());

            switch (intent.getAction()) {
                case NEW_OUTGOING_CALL:
                    stateOUTGOINGCALL(this, intent);
                    break;

                case PHONE_STATE:
                    String phoneState = intent.getStringExtra(android.telephony.TelephonyManager.EXTRA_STATE);

                    if (phoneState.equals(android.telephony.TelephonyManager.EXTRA_STATE_RINGING)) {
                        stateRUNNING(this, intent);
                    }

                    if (phoneState.equals(android.telephony.TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                        stateOFFHOOK(this);
                    }

                    if (phoneState.equals(android.telephony.TelephonyManager.EXTRA_STATE_IDLE)) {
                        stateIDLE(this);
                    }

                    break;
            }
        }

        return START_NOT_STICKY;
    }

    //вызывается когда звоним кому либо
    private void stateOUTGOINGCALL(Context context, Intent intent){
        sharedRecord = new SharedRecord(this);
        sharedRecord.setCallNumber(intent.getExtras().getString(PHONE_NUMBER));

        String[] contactInfo = ContactUtil.getContactInfo(context, sharedRecord.getCallNumber());

        sharedRecord.setContactName(contactInfo[ContactUtil.CONTACT_NAME]);
        sharedRecord.setContactPhoto(contactInfo[ContactUtil.CONTACT_PHOTO]);
        sharedRecord.setIsCall(true);
        sharedRecord.setCallTime(System.currentTimeMillis());
        sharedRecord.setIsCalling(false);

        Log.d(RECORD_LOG, "Звоним на " + sharedRecord.getPhoneNumber() + " c " + sharedRecord.getCallNumber());
    }

    //вызывается когда получаем звонок
    private void stateRUNNING(final Context context, Intent intent){
        sharedRecord = new SharedRecord(this);
        sharedRecord.setCallNumber(intent.getExtras().getString("incoming_number") != null
                ?intent.getExtras().getString("incoming_number"):
                "Скрытый номер");
        sharedRecord.setIsCalling(true);

        String[] contactInfo = ContactUtil.getContactInfo(context, sharedRecord.getCallNumber());

        sharedRecord.setContactName(contactInfo[ContactUtil.CONTACT_NAME]);
        sharedRecord.setContactPhoto(contactInfo[ContactUtil.CONTACT_PHOTO]);
        sharedRecord.setCallTime(System.currentTimeMillis());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                    Intent startIntent = new Intent(context, MainActivity.class);
                    startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(startIntent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        Log.d(RECORD_LOG, "Получаем звонок от " + sharedRecord.getCallNumber() + " на " + sharedRecord.getPhoneNumber());
    }

    //вызывется когда поднимает трубку, или начинается разговор
    private void stateOFFHOOK(Context context){
        sharedRecord = new SharedRecord(this);
        sharedRecord.setFileName(System.currentTimeMillis() + RECORD_MASK);
        sharedRecord.setFilePatch(getPatchDirectory() + sharedRecord.getFileName());
        sharedRecord.setIsCalling(false);

        if (sharedRecord.isCall()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(Setting.delayIsCall * 1000);

                        if (sharedRecord.isCall()) {
                            recordServiceCommand(RecordService.RECORD_TYPE_START);

                            sharedRecord.setIsCallAnswer(true);
                            startRecord = System.currentTimeMillis();

                            Log.d(RECORD_LOG, "Начинается разговор c " + sharedRecord.getCallNumber());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }

        else {
            Intent intent = new Intent(context, RecordService.class);
            intent.putExtra(RecordService.RECORD_TYPE, RecordService.RECORD_TYPE_START);
            startService(intent);

            sharedRecord.setIsCallAnswer(true);
            startRecord = System.currentTimeMillis();
            Log.d(RECORD_LOG, "Начинается разговор c " + sharedRecord.getCallNumber());
        }
    }

    //вызывается когда ложим трубку
    private void stateIDLE(Context context){
        sharedRecord = new SharedRecord(context);

        recordServiceCommand(RecordService.RECORD_TYPE_STOP);

        long stopRecord = System.currentTimeMillis();
        int durationCall = 0;

        if (sharedRecord.isCallAnswer()) {
            durationCall = (int) (stopRecord - startRecord) / 1000;
        }

        sharedRecord.setCallDuration(durationCall);
        Controller.getSQL(context).addSharedRecord(new SharedRecord(context));

        Log.d(RECORD_LOG, "Разговор закончен c " + sharedRecord.getCallNumber());
        Log.d(RECORD_LOG, "Продолжительность " + sharedRecord.getCallDuration() + " c");


    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }


    /** Сервес отвечающий за запись звонка имееет три метода
     * @method startRecord - начинает запись
     * @method stopRecord - прекращает запись
     * @method releaseRecord - очищает занятые сервисом ресурсы */

    public static class RecordService extends Service {

        private static MediaRecorder mediaRecorder;
        private static boolean isRecording = false;

        public static final String RECORD_TYPE = "record_type";
        public static final int RECORD_TYPE_START = 0;
        public static final int RECORD_TYPE_STOP = 1;

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            int type = intent.getIntExtra(RECORD_TYPE, 2);

            switch (type){
                case RECORD_TYPE_START:
                    startRecord(new SharedRecord(this).getFilePatch());
                    break;

                case RECORD_TYPE_STOP:
                    stopRecord();
                    break;

                case 2:
                    stopSelf(startId);
            }

            return START_NOT_STICKY;
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return new Binder();
        }

        private void startRecord(String fileName) {
            try {
                releaseRecord();
                createFile(fileName);

                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mediaRecorder.setAudioChannels(1);
                mediaRecorder.setOutputFile(fileName);

                mediaRecorder.prepare();
                mediaRecorder.start();

                isRecording = true;

                Log.d("RECORD", "Начинается запись");

            } catch (Exception e) {
                Log.d("RECORD", e.toString());
                e.printStackTrace();
            }
        }


        private void stopRecord() {
            if (mediaRecorder != null && isRecording) mediaRecorder.stop();
            isRecording = false;

            Log.d("RECORD", "Запись закончена");

            if (!sharedRecord.isCall()) {
                clearSharedRecordCall();
                startHomeActivity(this);
            }

            clearSharedRecordCall();
            NetworkManager.unloadCallRecords(this);
        }

        private void releaseRecord() {
            if (mediaRecorder != null) {
                mediaRecorder.release();
                mediaRecorder = null;
            }
        }

        private void createFile(String fileName) {
            File outFile = new File(fileName);
            if (outFile.exists()) {
                System.out.println(outFile.delete());
            }
        }

        //очищает данные о последнем звонке из локального хранилища
        private void clearSharedRecordCall(){
            sharedRecord.setFileName("");
            sharedRecord.setManagerName("");
            sharedRecord.setCallNumber("");
            sharedRecord.setCallTime(0);
            sharedRecord.setCallDuration(0);
            sharedRecord.setContactPhoto("");
            sharedRecord.setContactName("");
            sharedRecord.setFilePatch("");
            sharedRecord.setIsCall(false);
            sharedRecord.setIsCallAnswer(false);
            sharedRecord.setIsCalling(false);
        }

    }

    //запускает стартовую активити
    private static void startHomeActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    //возвращает путь к файлу с записью
    private String getPatchDirectory(){
        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder
                .append(Environment.getExternalStorageDirectory().getAbsolutePath())
                .append(DIRECTORY);

        File file = new File(fileNameBuilder.toString());
        if (!file.exists())
            System.out.println(file.mkdir());

        fileNameBuilder.append("/");

        return fileNameBuilder.toString();
    }

    private void recordServiceCommand(int command) {
        Intent intent = new Intent(this, RecordService.class);
        intent.putExtra(RecordService.RECORD_TYPE, command);
        startService(intent);
    }

    //метод для сброса звонка
    public static void endCall(){
        try {
            String serviceManagerName = "android.os.ServiceManager";
            String serviceManagerNativeName = "android.os.ServiceManagerNative";
            String telephonyName = "com.android.internal.telephony.ITelephony";
            Class<?> telephonyClass;
            Class<?> telephonyStubClass;
            Class<?> serviceManagerClass;
            Class<?> serviceManagerNativeClass;
            Method telephonyEndCall;
            Object telephonyObject;
            Object serviceManagerObject;
            telephonyClass = Class.forName(telephonyName);
            telephonyStubClass = telephonyClass.getClasses()[0];
            serviceManagerClass = Class.forName(serviceManagerName);
            serviceManagerNativeClass = Class.forName(serviceManagerNativeName);
            Method getService = // getDefaults[29];
                    serviceManagerClass.getMethod("getService", String.class);
            Method tempInterfaceMethod = serviceManagerNativeClass.getMethod("asInterface", IBinder.class);
            Binder tmpBinder = new Binder();
            tmpBinder.attachInterface(null, "fake");
            serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder);
            IBinder retbinder = (IBinder) getService.invoke(serviceManagerObject, "phone");
            Method serviceMethod = telephonyStubClass.getMethod("asInterface", IBinder.class);
            telephonyObject = serviceMethod.invoke(null, retbinder);
            telephonyEndCall = telephonyClass.getMethod("endCall");
            telephonyEndCall.invoke(telephonyObject);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("RECORD_ERROR", e.toString());
        }
    }
}
