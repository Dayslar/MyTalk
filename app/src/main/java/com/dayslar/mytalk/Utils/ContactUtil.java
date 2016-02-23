package com.dayslar.mytalk.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

public class ContactUtil {

    public static final int CONTACT_NAME = 0;
    public static final int CONTACT_PHOTO = 1;

    public static String[] getContactInfo(Context context, String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String name = "Неизвестный контакт";
        String photo = "";
        String[] query = new String[] {
                BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup.PHOTO_URI,
                ContactsContract.PhoneLookup.TYPE};

        ContentResolver contentResolver = context.getContentResolver();

        try (Cursor contactLookup = contentResolver.query(uri, query, null, null, null)) {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();

                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                photo = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_URI));

            }
        }

        String[] phoneMap = new String[2];

        phoneMap[CONTACT_NAME] = name;
        phoneMap[CONTACT_PHOTO] = photo;

        return phoneMap;
    }

}
