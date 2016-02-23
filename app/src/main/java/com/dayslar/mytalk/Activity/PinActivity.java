package com.dayslar.mytalk.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;

import com.dayslar.mytalk.R;
import com.dayslar.mytalk.Utils.Setting;

import me.philio.pinentry.PinEntryView;

public class PinActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_layout);

        context = this;

        initializeToolbar();
        initializePinView();

    }

    private void initializePinView() {
        PinEntryView pinEntryView = (PinEntryView) findViewById(R.id.pin_entry_simple);
        pinEntryView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(Setting.settingPassword)) {
                    startActivity(new Intent(context, PreferencesActivity.class));
                }

            }
        });
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Ввод пароля");
    }
}
