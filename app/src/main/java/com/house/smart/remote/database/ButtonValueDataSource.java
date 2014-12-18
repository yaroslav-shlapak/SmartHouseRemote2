package com.house.smart.remote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.house.smart.remote.SmartHouseButtons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by y.shlapak on Dec 18, 2014.
 */
public class ButtonValueDataSource extends ValueDataSource {

    public ButtonValueDataSource(Context context) {
        super(context);
    }

    private final String[] allColumns = {SmartHouseSQLiteHelper.COLUMN_ID, SmartHouseSQLiteHelper.COLUMN_BUTTON_NAME, SmartHouseSQLiteHelper.COLUMN_BUTTON_STRING};
    public void addButtonValue(ButtonValue buttonValue) {

        if (!isTableExisting(SmartHouseSQLiteHelper.BUTTONS_TABLE_NAME, SmartHouseButtons.getSize())) {
            ContentValues values = new ContentValues();
            values.put(SmartHouseSQLiteHelper.COLUMN_ID, buttonValue.getId());
            values.put(SmartHouseSQLiteHelper.COLUMN_BUTTON_NAME, buttonValue.getButtonName());
            values.put(SmartHouseSQLiteHelper.COLUMN_BUTTON_STRING, buttonValue.getButtonString());

            long insertId = db.insert(SmartHouseSQLiteHelper.BUTTONS_TABLE_NAME, null,
                    values);
            Cursor cursor = db.query(SmartHouseSQLiteHelper.BUTTONS_TABLE_NAME,
                    allColumns, SmartHouseSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            cursor.close();
            Log.v("debug", "button was added");
        }

    }

    public ButtonValue getButtonValue(int id) {

        Cursor cursor = db.query(SmartHouseSQLiteHelper.BUTTONS_TABLE_NAME, allColumns,
                SmartHouseSQLiteHelper.COLUMN_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();

        if (cursor != null) {
            return new ButtonValue(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        }
        return null;
    }

    // Getting All Contacts
    public List<ButtonValue> getAllButtonValues() {

        List<ButtonValue> buttonValuesList = new ArrayList<ButtonValue>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SmartHouseSQLiteHelper.BUTTONS_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ButtonValue buttonValue = new ButtonValue();
                buttonValue.setId(Integer.parseInt(cursor.getString(0)));
                buttonValue.setButtonImage(cursor.getString(1));
                buttonValue.setButtonString(cursor.getString(2));
                // Adding contact to list
                buttonValuesList.add(buttonValue);
            } while (cursor.moveToNext());
        }

        // return contact list
        return buttonValuesList;
    }

    public int getButtonValuesCount() {
        String countQuery = "select * from" + SmartHouseSQLiteHelper.BUTTONS_TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateButtonValue(ButtonValue buttonValue) {

        ContentValues values = new ContentValues();
        values.put(SmartHouseSQLiteHelper.COLUMN_BUTTON_NAME, buttonValue.getButtonName());
        values.put(SmartHouseSQLiteHelper.COLUMN_BUTTON_STRING, buttonValue.getButtonString());

        // updating row
        return db.update(SmartHouseSQLiteHelper.BUTTONS_TABLE_NAME, values, SmartHouseSQLiteHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(buttonValue.getId()) });
    }

    public void deleteButtonValue(ButtonValue buttonValue) {
        db.delete(SmartHouseSQLiteHelper.BUTTONS_TABLE_NAME, SmartHouseSQLiteHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(buttonValue.getId()) });
        db.close();
    }

}
