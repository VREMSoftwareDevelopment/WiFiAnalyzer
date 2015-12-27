package com.vrem.wifianalyzer.vendor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper implements BaseColumns {
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "WiFiAnalyzerDB.db";

    static final String TABLE_NAME = "macvendorname";

    static final String COLUMN_MAC = "mac";
    static final String COLUMN_NAME = "name";
    static final String [] ALL_COLUMNS = new String[] {_ID, COLUMN_NAME, COLUMN_MAC};
    static final String SORT_ORDER = COLUMN_NAME+","+COLUMN_MAC+","+_ID;

    static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
        + _ID + " INTEGER PRIMARY KEY NOT NULL,"
        + COLUMN_MAC + " TEXT UNIQUE NOT NULL,"
        + COLUMN_NAME + " TEXT NOT NULL)";
    static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_DROP);
        onCreate(db);
    }

    public long insert(String mac, String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getContentValues();
        values.put(COLUMN_MAC, MacAddress.clean(mac));
        values.put(COLUMN_NAME, name);
        return db.insert(TABLE_NAME, null, values);
    }

    ContentValues getContentValues() {
        return new ContentValues();
    }

    public String find(String mac) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
            TABLE_NAME,
            new String[] { COLUMN_NAME },
            COLUMN_MAC + "=?",
            new String[] { MacAddress.clean(mac) },
            null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
        }
        return null;
    }

    public List<VendorData> findAll() {
        List<VendorData> results = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null, SORT_ORDER);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                VendorData vendorData = new VendorData();
                vendorData.id = cursor.getLong(cursor.getColumnIndexOrThrow(_ID));
                vendorData.mac = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MAC));
                vendorData.name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                results.add(vendorData);
                cursor.moveToNext();
            }
        }
        return results;
    }

    public class VendorData {
        public long id;
        public String mac;
        public String name;
    }
}