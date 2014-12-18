package com.house.smart.remote.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SmartHouseSQLiteHelper extends SQLiteOpenHelper {

    public static final String BUTTONS_TABLE_NAME = "button_values";
    public static final String UDP_TABLE_NAME = "udp_table";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_BUTTON_NAME = "button_name";
    public static final String COLUMN_BUTTON_STRING = "button_string";
    //public static final String COLUMN_BUTTON_IMAGE = "button_image";
    public static final String COLUMN_IP = "ip";
    public static final String COLUMN_PORT = "port";
    public static final String DATABASE_NAME = "smarthouseremote.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String BUTTONS_TABLE_CREATE = "create table "
            + BUTTONS_TABLE_NAME + "(" + COLUMN_ID
            + " integer primary key, " + COLUMN_BUTTON_NAME + " text not null," + COLUMN_BUTTON_STRING /*+ " text not null," + COLUMN_BUTTON_IMAGE*/
            + " text not null);";

    private static final String UDP_TABLE_CREATE = "create table "
            + UDP_TABLE_NAME + "(" + COLUMN_ID
            + " integer primary key, " + COLUMN_IP + " text, " + COLUMN_PORT + " text);";


    public SmartHouseSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(BUTTONS_TABLE_CREATE);
        database.execSQL(UDP_TABLE_CREATE);
        Log.v("debug", "creating buttons database");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SmartHouseSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + BUTTONS_TABLE_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + UDP_TABLE_CREATE);
        onCreate(db);
    }

}