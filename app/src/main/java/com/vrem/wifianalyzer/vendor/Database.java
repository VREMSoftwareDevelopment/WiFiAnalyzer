package com.vrem.wifianalyzer.vendor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "WiFiAnalyzerDB.db";

    static final String TABLE_NAME = "macvendorname";

    static final String COLUMN_ID = "id";
    static final String COLUMN_MAC = "mac";
    static final String COLUMN_NAME = "name";

    static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL,"
            + COLUMN_MAC + " TEXT UNIQUE NOT NULL,"
            + COLUMN_NAME + " TEXT NOT NULL)";
    static final String TABLE_DROP = "DROP TABLE IF EXISTS" + TABLE_NAME;
    static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
    static final String SELECT_BY_MAC = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_MAC + "=?";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
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
        ContentValues contentValues = getContentValues();
        contentValues.put(COLUMN_MAC, MacAddress.clean(mac));
        contentValues.put(COLUMN_NAME, name);
        return db.insert(TABLE_NAME, null, contentValues);
    }

    ContentValues getContentValues() {
        return new ContentValues();
    }

    public String find(String mac) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_BY_MAC, new String[]{MacAddress.clean(mac)});
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        }
        return null;
    }

    public List<VendorData> findAll() {
        List<VendorData> results = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                VendorData vendorData = new VendorData();
                vendorData.id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                vendorData.mac = cursor.getString(cursor.getColumnIndex(COLUMN_MAC));
                vendorData.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
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