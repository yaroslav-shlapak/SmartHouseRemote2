package com.house.smart.remote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by y.shlapak on Dec 18, 2014.
 */
public class UdpValueDataSource extends ValueDataSource{

    private String[] allColumns = {SmartHouseSQLiteHelper.COLUMN_ID, SmartHouseSQLiteHelper.COLUMN_IP, SmartHouseSQLiteHelper.COLUMN_PORT};

    public UdpValueDataSource(Context context) {
        super(context);
    }

    public void addUdpValue(UdpValue udpValue) {

        if (!isTableExisting(SmartHouseSQLiteHelper.UDP_TABLE_NAME, 0)) {
                ContentValues values = new ContentValues();
            values.put(SmartHouseSQLiteHelper.COLUMN_ID, udpValue.getId());
            values.put(SmartHouseSQLiteHelper.COLUMN_IP, udpValue.getIp());
            values.put(SmartHouseSQLiteHelper.COLUMN_PORT, udpValue.getPort());

            long insertId = db.insert(SmartHouseSQLiteHelper.UDP_TABLE_NAME, null,
                    values);
            Cursor cursor = db.query(SmartHouseSQLiteHelper.UDP_TABLE_NAME,
                    allColumns, SmartHouseSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            cursor.close();
            db.close();
            Log.v("debug", "IP and port was added");
        }
    }

    public UdpValue getUdpValue(int id) {

        Cursor cursor = db.query(SmartHouseSQLiteHelper.UDP_TABLE_NAME, allColumns,
                SmartHouseSQLiteHelper.COLUMN_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();

        return new UdpValue(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
    }

    // Getting All Contacts
    public List<UdpValue> getAllUdpValues() {

        List<UdpValue> udpValuesList = new ArrayList<UdpValue>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SmartHouseSQLiteHelper.COLUMN_BUTTON_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UdpValue udpValue = new UdpValue();
                udpValue.setId(Integer.parseInt(cursor.getString(0)));
                udpValue.setIp(cursor.getString(1));
                udpValue.setPort(cursor.getString(2));
                // Adding contact to list
                udpValuesList.add(udpValue);
            } while (cursor.moveToNext());
        }

        // return contact list
        return udpValuesList;
    }

    public int getUdpValuesCount() {
        String countQuery = "select * from" + SmartHouseSQLiteHelper.BUTTONS_TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateUdpValue(ButtonValue buttonValue) {

        ContentValues values = new ContentValues();
        values.put(SmartHouseSQLiteHelper.COLUMN_IP, buttonValue.getButtonName());
        values.put(SmartHouseSQLiteHelper.COLUMN_PORT, buttonValue.getButtonString());

        // updating row
        return db.update(SmartHouseSQLiteHelper.UDP_TABLE_NAME, values, SmartHouseSQLiteHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(buttonValue.getId()) });
    }

    public void deleteUdpValue(ButtonValue buttonValue) {
        db.delete(SmartHouseSQLiteHelper.UDP_TABLE_NAME, SmartHouseSQLiteHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(buttonValue.getId()) });
        db.close();
    }
}
