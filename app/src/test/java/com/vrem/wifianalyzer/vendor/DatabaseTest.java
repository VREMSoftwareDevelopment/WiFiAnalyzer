/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.vrem.wifianalyzer.vendor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseTest {
    static final String MAC_ADDDRES = "00:23:AB:8C:DF:10";
    static final String VENDOR_NAME = "CISCO SYSTEMS, INC.";
    static final long ID = 213L;

    @Mock private SQLiteDatabase sqliteDatabase;
    @Mock private Cursor cursor;
    @Mock private ContentValues contentValues;
    @Mock private Context context;

    class DatabaseMock extends Database {
        public DatabaseMock() {
            super(context);
        }

        @Override
        public SQLiteDatabase getWritableDatabase() {
            return sqliteDatabase;
        }

        @Override
        public SQLiteDatabase getReadableDatabase() {
            return sqliteDatabase;
        }

        @Override
        ContentValues getContentValues() {
            return contentValues;
        }
    }

    private Database fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new DatabaseMock();
    }

    @Test
    public void testOnCreate() throws Exception {
        // execute
        fixture.onCreate(sqliteDatabase);
        // validate
        verify(sqliteDatabase).execSQL(Database.TABLE_CREATE);
    }

    @Test
    public void testOnUpgrade() throws Exception {
        // execute
        fixture.onUpgrade(sqliteDatabase, 0, 1);
        // validate
        verify(sqliteDatabase).execSQL(Database.TABLE_DROP);
        verify(sqliteDatabase).execSQL(Database.TABLE_CREATE);
    }

    @Test
    public void testInsert() throws Exception {
        // setup
        when(sqliteDatabase.insert(Database.TABLE_NAME, null, contentValues)).thenReturn(ID);
        // execute
        long actual = fixture.insert(MAC_ADDDRES, VENDOR_NAME);
        // validate
        verify(sqliteDatabase).insert(Database.TABLE_NAME, null, contentValues);
        verify(contentValues).put(Database.COLUMN_MAC, MacAddress.clean(MAC_ADDDRES));
        verify(contentValues).put(Database.COLUMN_NAME, VENDOR_NAME);
        assertEquals(ID, actual);
    }

    @Test
    public void testFind() throws Exception {
        // setup
        int index = 2;
        when(sqliteDatabase.rawQuery(Database.SELECT_BY_MAC, new String[]{MacAddress.clean(MAC_ADDDRES)})).thenReturn(cursor);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.getColumnIndex(Database.COLUMN_NAME)).thenReturn(index);
        when(cursor.getString(index)).thenReturn(VENDOR_NAME);
        // execute
        String actual = fixture.find(MAC_ADDDRES);
        // validate
        verify(sqliteDatabase).rawQuery(Database.SELECT_BY_MAC, new String[]{MacAddress.clean(MAC_ADDDRES)});
        verify(cursor).moveToFirst();
        verify(cursor).getColumnIndex(Database.COLUMN_NAME);
        verify(cursor).getString(index);
        assertEquals(VENDOR_NAME, actual);
    }

    @Test
    public void testFindNoRowFound() throws Exception {
        // setup
        when(sqliteDatabase.rawQuery(Database.SELECT_BY_MAC, new String[]{MacAddress.clean(MAC_ADDDRES)})).thenReturn(cursor);
        when(cursor.moveToFirst()).thenReturn(false);
        // execute
        String actual = fixture.find(MAC_ADDDRES);
        // validate
        verify(sqliteDatabase).rawQuery(Database.SELECT_BY_MAC, new String[]{MacAddress.clean(MAC_ADDDRES)});
        verify(cursor).moveToFirst();
        assertNull(actual);
    }

    @Test
    public void testFindAll() throws Exception {
        // setup
        expectedFindAll();
        // execute
        List<Database.VendorData> actual = fixture.findAll();
        // validate
        verifyFindAll();
        assertEquals(1, actual.size());
        assertEquals(ID, actual.get(0).id);
        assertEquals(MAC_ADDDRES, actual.get(0).mac);
        assertEquals(VENDOR_NAME, actual.get(0).name);
    }

    private void expectedFindAll() {
        when(sqliteDatabase.rawQuery(Database.SELECT_ALL, null)).thenReturn(cursor);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.isAfterLast()).thenReturn(false).thenReturn(true);
        when(cursor.getColumnIndex(Database.COLUMN_ID)).thenReturn(0);
        when(cursor.getLong(0)).thenReturn(ID);
        when(cursor.getColumnIndex(Database.COLUMN_MAC)).thenReturn(1);
        when(cursor.getString(1)).thenReturn(MAC_ADDDRES);
        when(cursor.getColumnIndex(Database.COLUMN_NAME)).thenReturn(2);
        when(cursor.getString(2)).thenReturn(VENDOR_NAME);
    }

    private void verifyFindAll() {
        verify(sqliteDatabase).rawQuery(Database.SELECT_ALL, null);
        verify(cursor).moveToFirst();
        verify(cursor, times(2)).isAfterLast();
        verify(cursor).moveToNext();
        verify(cursor).getColumnIndex(Database.COLUMN_ID);
        verify(cursor).getLong(0);
        verify(cursor).getColumnIndex(Database.COLUMN_MAC);
        verify(cursor).getString(1);
        verify(cursor).getColumnIndex(Database.COLUMN_NAME);
        verify(cursor).getString(2);
    }

}