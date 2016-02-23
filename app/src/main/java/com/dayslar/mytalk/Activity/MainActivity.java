package com.dayslar.mytalk.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayslar.mytalk.Adapters.ManagersAdapter;
import com.dayslar.mytalk.Models.SharedRecord;
import com.dayslar.mytalk.R;
import com.dayslar.mytalk.Service.TelephonyService;
import com.dayslar.mytalk.Utils.BlurBuilder;
import com.dayslar.mytalk.Utils.Controller;
import com.dayslar.mytalk.Utils.Setting;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initializeViews();
        initializeCall();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!new SharedRecord(this).isCalling()) {
            toolbar.inflateMenu(R.menu.main_menu);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!new SharedRecord(this).isCalling())
            dialogExit().show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Setting.loadSetting(this);

        gridView.setAdapter(new ManagersAdapter(this, Controller.getSQL(this).getManagers()));
        screenLockOff();
    }

    private void initializeViews(){
        context = this;

        initializeToolbar();

        gridView = (GridView) findViewById(R.id.gridView);
        fab = (FloatingActionButton) findViewById(R.id.addManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditManagerActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.tvEndCall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyService.endCall();
            }
        });
    }

    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.setting:
                        Intent intent = new Intent(context, PinActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    private void initializeCall() {
        SharedRecord sharedRecord = new SharedRecord(this);

        if (sharedRecord.isCalling()){
            fab.setVisibility(View.GONE);
            findViewById(R.id.callLayout).setVisibility(View.VISIBLE);


            ((TextView) findViewById(R.id.contactNumber)).setText(sharedRecord.getCallNumber());
            ((TextView) findViewById(R.id.contactName)).setText(sharedRecord.getContactName());

            String contactImage = sharedRecord.getContactPhoto();
            if (contactImage.length() > 2){

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),Uri.parse(contactImage));
                    ((ImageView) findViewById(R.id.backgroundImage)).setImageBitmap(BlurBuilder.blur(this, bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ((ImageView) findViewById(R.id.contactPhoto)).setImageURI(Uri.parse(contactImage));
            }
        }

        else {
            fab.setVisibility(View.VISIBLE);
            findViewById(R.id.callLayout).setVisibility(View.GONE);
        }
    }

    private void screenLockOff() {
        Window wind = this.getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    private AlertDialog dialogExit(){
        return new AlertDialog.Builder(this, R.style.AlertDialogCustomLight)
                .setMessage("Вы уверены что ходите выйти из приложения?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
        .create();
    }
}
