package com.dayslar.mytalk.Adapters;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.dayslar.mytalk.Activity.DialogActivity;
import com.dayslar.mytalk.Activity.EditManagerActivity;
import com.dayslar.mytalk.Models.Manager;
import com.dayslar.mytalk.Models.SharedRecord;
import com.dayslar.mytalk.R;
import com.dayslar.mytalk.Utils.Controller;

import java.util.List;

public class ManagersAdapter extends BaseAdapter {

    private List<Manager> managers;
    private LayoutInflater inflater;
    private Context context;

    public ManagersAdapter(Context context, List<Manager> managers){
        this.context = context;
        this.managers = managers;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return managers.size();
    }

    @Override
    public Object getItem(int position) {
        return managers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.manager_item, null);

        Manager manager = managers.get(position);
        ViewHolder viewHolder = new ViewHolder();

        viewHolder.imageManager = (ImageView) convertView.findViewById(R.id.imageManager);
        viewHolder.tvNameManager = (TextView) convertView.findViewById(R.id.tvNameManger);
        viewHolder.btnFunction = (ImageView) convertView.findViewById(R.id.btnFunction);

        viewHolder.tvNameManager.setText(manager.getName());
        viewHolder.imageManager.setImageResource(R.drawable.contact_no_image);
        viewHolder.btnFunction.setOnClickListener(btnFunctionOnClickListener(manager));

        setImageProfile(viewHolder.imageManager, manager.getPhotoPatch());
        convertView.setOnClickListener(btnAnswerCall(manager));

        return convertView;
    }

    // создает попуп меню при нажатию на оверлей кнопку
    private View.OnClickListener btnFunctionOnClickListener(final Manager manager){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBtnFunctionPopupMenu(v, manager).show();
            }
        };
    }

    //кнопка для ответа на звонок
    private View.OnClickListener btnAnswerCall(final Manager manager){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedRecord sharedRecord = new SharedRecord(context);
                if (sharedRecord.isCalling()) {
                    sharedRecord.setManagerName(manager.getName());

                    Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
                    buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
                    context.sendOrderedBroadcast(buttonDown, "android.permission.CALL_PRIVILEGED");

                    Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
                    buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
                    context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");

                    context.startActivity(new Intent(context, DialogActivity.class));

                    Toast.makeText(context, "На звонок ответил(а) " + manager.getName(), Toast.LENGTH_LONG).show();

                    lockScreen();

                }

                else Toast.makeText(context, "Сейчас никто не звонит", Toast.LENGTH_SHORT).show();

            }
        };
    }

    //создает попуп меню
    private PopupMenu createBtnFunctionPopupMenu(View v, final Manager manager){
        PopupMenu popupMenu = new PopupMenu(context, v);

        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.manager_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.readManager:
                        btnReadManager(manager);
                        break;

                    case R.id.deleteManager:
                        btnDeleteManager(manager);
                        break;
                }
                return true;
            }
        });

        return popupMenu;
    }


    //вызывается при нажатии на редактировать в меню
    private void btnReadManager(Manager manager){
        Intent newIntent = new Intent(context, EditManagerActivity.class);

        newIntent.putExtra(Manager.MANAGER_ID, manager.getID());
        newIntent.putExtra(Manager.MANAGER_NAME, manager.getName());
        newIntent.putExtra(Manager.MANAGER_PHOTO_PATCH, manager.getPhotoPatch());

        context.startActivity(newIntent);
    }

    //вызывается при нажатиии удалить в меню
    private void btnDeleteManager(Manager manager){
        deleteManagerDialog(manager).show();
    }

    //изменяет фотографию профиля
    private void setImageProfile(ImageView imageManager, String photoPatch){
        if (photoPatch != null && photoPatch.length() > 2)
            try {
                Bitmap pick = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(photoPatch));
                imageManager.setImageBitmap(pick);
            } catch (Exception e) {
                Log.d("RECORD_ERROR", e.toString());
            }
    }

    //диолог удаления менеджера
    private AlertDialog deleteManagerDialog(final Manager manager){
        return new AlertDialog.Builder(context, R.style.AlertDialogCustomLight)
                .setTitle("Подтверждение удаления")
                .setMessage("Вы уверены что хотите удалить менеджера?")
                .setPositiveButton("Да, удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Controller.getSQL(context).deleteManager(manager.getID());
                        managers.remove(manager);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Нет, не уверен", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
    }

    private void lockScreen() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DevicePolicyManager mDPM;
                mDPM = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                mDPM.lockNow();
            }
        }, 700);
    }

    static class ViewHolder{
        ImageView imageManager;
        TextView tvNameManager;
        ImageView btnFunction;
    }
}
