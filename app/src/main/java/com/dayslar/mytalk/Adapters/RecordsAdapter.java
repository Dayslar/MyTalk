package com.dayslar.mytalk.Adapters;

import android.content.Context;
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

import com.dayslar.mytalk.Models.Record;
import com.dayslar.mytalk.R;
import com.dayslar.mytalk.Utils.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecordsAdapter extends BaseAdapter {

    private List<Record> records;
    private LayoutInflater inflater;
    private Context context;

    public RecordsAdapter(Context context, List<Record> records) {
        this.records = records;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.record_item, null);

        ImageView imageIsCall = (ImageView) convertView.findViewById(R.id.imageIsCall);
        TextView tvInfo = (TextView) convertView.findViewById(R.id.tvInfoCall);
        TextView tvCallPhone = (TextView) convertView.findViewById(R.id.tvCallPhone);
        TextView tvCallDuration = (TextView) convertView.findViewById(R.id.tvCallDuration);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        ImageView btnFunction = (ImageView) convertView.findViewById(R.id.btnFunction);

        final int positionCall = position;

        boolean isCall = records.get(position).isCall();
        String callPhoneNumber = records.get(position).getCallNumber();
        int callDuration = records.get(position).getCallDuration();
        long callTime = records.get(position).getCallTime();

        if (isCall) {
            imageIsCall.setBackgroundResource(R.drawable.ic_arrow_up);
            tvInfo.setText("Исходящий вызов на");
        }
        else {
            imageIsCall.setBackgroundResource(R.drawable.ic_arrow_down);
            tvInfo.setText("Входящий вызов от");
        }

        tvCallPhone.setText(callPhoneNumber);
        tvCallDuration.setText("Продолжительность: " + callDuration);
        tvTime.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(callTime)));


        btnFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupMenu(v, records.get(positionCall)).show();
            }
        });

        return convertView;
    }

    private PopupMenu createPopupMenu(final View v, final Record record){
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.startRecord:
                        Controller.getMyMusic().playMusic(record.getFilePatch());
                        break;

                    case R.id.stopRecord:
                        Controller.getMyMusic().stopMusic();
                        break;

                    case R.id.deleteRecord:
                        Controller.getSQL(context).deleteRecord(record.getId());
                        records.remove(record);
                        notifyDataSetChanged();
                        break;

                    case R.id.allInfoRecordCall:
                        Toast.makeText(context, "Подробная информация", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.record_item_menu, popupMenu.getMenu());

        return popupMenu;
    }
}
