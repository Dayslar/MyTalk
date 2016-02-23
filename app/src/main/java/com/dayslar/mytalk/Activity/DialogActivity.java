package com.dayslar.mytalk.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dayslar.mytalk.R;
import com.dayslar.mytalk.Models.SharedRecord;
import com.dayslar.mytalk.Service.TelephonyService;


public class DialogActivity extends AppCompatActivity {

    private SharedRecord sharedRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity);

        sharedRecord = new SharedRecord(this);

        initializeToolbar();
        initializeView();

    }

    @Override
    protected void onResume() {
        super.onResume();

        screenLockOff();
    }

    //инициализируем все вью
    private void initializeView() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnEndCall);
        fab.setOnClickListener(fabClickListener());

        String photo = sharedRecord.getContactPhoto();
        if (photo.length() > 2)
            ((ImageView) findViewById(R.id.contactPhoto)).setImageURI(Uri.parse(photo));
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(sharedRecord.getContactName());
        toolbar.setSubtitle(sharedRecord.getCallNumber());
    }

    //код вызывается при нажатиии на кнопку скинуть вызов(fab на диолог активити)
    private View.OnClickListener fabClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyService.endCall();
            }
        };
    }

    private void screenLockOff() {
        Window wind = this.getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

}
