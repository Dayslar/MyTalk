package com.dayslar.mytalk.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.dayslar.mytalk.R;
import com.dayslar.mytalk.Utils.Setting;

public class PinActivity extends AppCompatActivity {

    private Context context;
    private PinLockView pinLockView;
    private IndicatorDots indicatorDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_layout);

        context = this;

        initializeToolbar();
        initializePinView();

    }

    private void initializePinView() {

        pinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        indicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);

        pinLockView.attachIndicatorDots(indicatorDots);
        pinLockView.setPinLockListener(initPinLockListener());

        pinLockView.setPinLength(4);
        pinLockView.setTextColor(getResources().getColor(R.color.white));
    }

    private PinLockListener initPinLockListener(){
        return new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                Setting.loadSetting(context);

                if (pin.equals(Setting.settingPassword)){
                    startActivity(new Intent(context, PreferencesActivity.class));
                }

            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {

            }
        };
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Ввод пароля");
    }
}
