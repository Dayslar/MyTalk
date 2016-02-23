package com.dayslar.mytalk.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.dayslar.mytalk.Adapters.RecordsAdapter;
import com.dayslar.mytalk.R;
import com.dayslar.mytalk.Utils.Controller;


public class ListRecordsActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_records_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        listView = (ListView) findViewById(R.id.listView);

        setSupportActionBar(toolbar);

    }

    @Override
    protected void onResume() {
        super.onResume();
        listView.setAdapter(new RecordsAdapter(this, Controller.getSQL(this).getRecords()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_records_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_update:
                listView.setAdapter(new RecordsAdapter(this, Controller.getSQL(this).getRecords()));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
