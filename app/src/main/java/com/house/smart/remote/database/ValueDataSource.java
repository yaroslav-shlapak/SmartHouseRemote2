package com.house.smart.remote.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by y.shlapak on Dec 18, 2014.
 */
public class ValueDataSource {

    protected SQLiteDatabase db;
    protected final SmartHouseSQLiteHelper dbHelper;

    public ValueDataSource(Context context) {
        this.dbHelper = new SmartHouseSQLiteHelper(context);
    }

    public void open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
    }

    public void close() {
        this.dbHelper.close();
    }

    public boolean isTableExisting(String tableName, int tableRowsNumber) {
        Cursor cursor = db.rawQuery("SELECT count(*) FROM " + tableName, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if(cursor != null) {
            Log.v("Debug", String.valueOf(count));
            if(count >= tableRowsNumber) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
}
