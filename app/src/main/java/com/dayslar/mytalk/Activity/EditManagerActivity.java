package com.dayslar.mytalk.Activity;

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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dayslar.mytalk.Models.Manager;
import com.dayslar.mytalk.R;
import com.dayslar.mytalk.Utils.Controller;

public class EditManagerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQ_CODE_PICK_IMAGE = 1;

    private EditText editManagerName;
    private ImageView imageProfileManager;

    private int id;
    private String photoPatch;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_manager);

        initializeView();
        initializeIntentSource();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvImageLabel:
                clickTvImageLabel();
                break;

            case R.id.btnCancel:
                btnCancel();
                break;

            case R.id.btnSave:
                btnSave();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQ_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {

                    if (null != data.getData()) {
                        Uri selectedImage = data.getData();
                        imageProfileManager.setImageURI(selectedImage);

                        photoPatch = selectedImage.toString();
                    }

                    else imageProfileManager.setImageResource(R.drawable.contact_no_image);
                }
                break;
        }
    }

    private void initializeView(){
        initializeToolbar();
        initializeFabs();
        initializeTvImageLabel();

        imageProfileManager = (ImageView) findViewById(R.id.imageProfileManager);
        editManagerName = (EditText) findViewById(R.id.editNameManager);

    }

    private void initializeTvImageLabel() {
        TextView tvImageLabel = (TextView) findViewById(R.id.tvImageLabel);
        tvImageLabel.setOnClickListener(this);
    }

    private void initializeFabs() {
        FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.btnSave);
        FloatingActionButton fabCancel = (FloatingActionButton) findViewById(R.id.btnCancel);
        fabSave.setOnClickListener(this);
        fabCancel.setOnClickListener(this);
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Редактирование");
    }

    private void initializeIntentSource() {
        Intent intent = getIntent();
        id = intent.getIntExtra(Manager.MANAGER_ID, -1);

        if (id != -1) {
            name = intent.getStringExtra(Manager.MANAGER_NAME);
            photoPatch = intent.getStringExtra(Manager.MANAGER_PHOTO_PATCH);

            editManagerName.setText(name);
            if (photoPatch != null && !photoPatch.equals(""))
                imageProfileManager.setImageURI(Uri.parse(photoPatch));
        }
    }

    private void btnSave(){
        Manager manager;
        name = editManagerName.getText().toString();
        if (name.equals("")) {
            Toast.makeText(this, "Вы не ввели имя", Toast.LENGTH_SHORT).show();
        }
        else{
            manager = new Manager(name, photoPatch);

            if (id == -1) Controller.getSQL(this).addManager(manager);
            else Controller.getSQL(this).updateManager(id, manager);

            Toast.makeText(this, "Менеджер успешно добавлен", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void btnCancel(){
        cancelAlertDialog().show();
    }

    private void clickTvImageLabel(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.putExtra("crop", "true");
        i.putExtra("aspectX", 1);
        i.putExtra("aspectY", 1);
        i.putExtra("outputX", 120);
        i.putExtra("outputY", 120);
        i.putExtra("scale", true);
        i.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        i.putExtra("noFaceDetection", false);
        i.putExtra("return-data", false);
        startActivityForResult(i, REQ_CODE_PICK_IMAGE);
    }

    @Override
    public void onBackPressed() {
        cancelAlertDialog().show();
    }

    private AlertDialog cancelAlertDialog(){
        return new AlertDialog.Builder(this, R.style.AlertDialogCustomLight)
                .setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        synchronized (this){
                            finish();
                        }
                    }
                })
                .setNegativeButton("Остаться", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setTitle("Предупреждение")
                .setMessage("Вы действительно хотите выйти? Все не сохраненные данные будут стерты!")
                .create();
    }
}
