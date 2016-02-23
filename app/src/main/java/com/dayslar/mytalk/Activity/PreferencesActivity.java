package com.dayslar.mytalk.Activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dayslar.mytalk.R;
import com.dayslar.mytalk.Receiver.AdminReceiver;
import com.dayslar.mytalk.Utils.Setting;

public class PreferencesActivity extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int REQUEST_CODE_ENABLE_ADMIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_layout);
        addPreferencesFromResource(R.xml.preferences);

        initializeToolbar();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }



    private void activeAdmin() {
        ComponentName mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);

        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
    }

    private void disableAdmin() {
        ComponentName mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        devicePolicyManager.removeActiveAdmin(mDeviceAdminSample);
    }

    private void initializeToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Boolean isAdmin = sharedPreferences.getBoolean(getResources().getString(R.string.chAdminKey), false);

        if (isAdmin)
            activeAdmin();
        else
            disableAdmin();

        Setting.loadSetting(this, sharedPreferences);
    }
}

