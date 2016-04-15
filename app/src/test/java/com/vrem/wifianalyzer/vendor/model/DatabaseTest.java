/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.vendor.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseTest {
    private static final String MAC_ADDRESS = "00:23:AB:8C:DF:10";
    private static final String VENDOR_NAME = "CISCO SYSTEMS, INC.";
    private static final long ID = 213L;
    private static final int INDEX = 2;

    @Mock private SQLiteDatabase sqliteDatabase;
    @Mock private Cursor cursor;
    @Mock private ContentValues contentValues;
    @Mock private Context context;

    private Database fixture;

    @Before
    public void setUp() throws Exception {

        fixture = new DatabaseMock(context);
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
        long actual = fixture.insert(MAC_ADDRESS, VENDOR_NAME);
        // validate
        verify(sqliteDatabase).insert(Database.TABLE_NAME, null, contentValues);
        verify(contentValues).put(Database.COLUMN_MAC, MacAddress.clean(MAC_ADDRESS));
        verify(contentValues).put(Database.COLUMN_NAME, VENDOR_NAME);
        assertEquals(ID, actual);
    }

    @Test
    public void testFind() throws Exception {
        // setup
        withQuery(true);
        when(cursor.getColumnIndexOrThrow(Database.COLUMN_NAME)).thenReturn(INDEX);
        when(cursor.getString(INDEX)).thenReturn(VENDOR_NAME);
        // execute
        String actual = fixture.find(MAC_ADDRESS);
        // validate
        verifyQuery();
        verify(cursor).getColumnIndexOrThrow(Database.COLUMN_NAME);
        verify(cursor).getString(INDEX);
        assertEquals(VENDOR_NAME, actual);
    }

    @Test
    public void testFindNoRowFound() throws Exception {
        // setup
        withQuery(false);
        // execute
        String actual = fixture.find(MAC_ADDRESS);
        // validate
        verifyQuery();
        verify(cursor, never()).getColumnIndexOrThrow(Database.COLUMN_NAME);
        verify(cursor, never()).getString(anyInt());
        assertNull(actual);
    }

    private void withQuery(boolean moveToFirstReturn) {
        when(sqliteDatabase
            .query(
                Database.TABLE_NAME,
                new String[]{Database.COLUMN_NAME},
                Database.COLUMN_MAC + "=?",
                    new String[]{MacAddress.clean(MAC_ADDRESS)},
                null, null, null)
            ).thenReturn(cursor);
        when(cursor.moveToFirst()).thenReturn(moveToFirstReturn);
    }

    private void verifyQuery() {
        verify(sqliteDatabase)
            .query(Database.TABLE_NAME,
                new String[]{Database.COLUMN_NAME},
                Database.COLUMN_MAC + "=?",
                    new String[]{MacAddress.clean(MAC_ADDRESS)},
                null, null, null);
        verify(cursor).moveToFirst();
    }

    @Test
    public void testFindAll() throws Exception {
        // setup
        expectedFindAll();
        // execute
        List<VendorData> actual = fixture.findAll();
        // validate
        verifyFindAll();
        assertEquals(1, actual.size());
        assertEquals(ID, actual.get(0).getId());
        assertEquals(MAC_ADDRESS, actual.get(0).getMac());
        assertEquals(VENDOR_NAME, actual.get(0).getName());
    }

    private void expectedFindAll() {
        when(sqliteDatabase.query(Database.TABLE_NAME, Database.ALL_COLUMNS, null, null, null, null, Database.SORT_ORDER)).thenReturn(cursor);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.isAfterLast()).thenReturn(false).thenReturn(true);
        when(cursor.getColumnIndexOrThrow(Database._ID)).thenReturn(0);
        when(cursor.getLong(0)).thenReturn(ID);
        when(cursor.getColumnIndexOrThrow(Database.COLUMN_MAC)).thenReturn(1);
        when(cursor.getString(1)).thenReturn(MAC_ADDRESS);
        when(cursor.getColumnIndexOrThrow(Database.COLUMN_NAME)).thenReturn(2);
        when(cursor.getString(2)).thenReturn(VENDOR_NAME);
    }

    private void verifyFindAll() {
        verify(sqliteDatabase).query(Database.TABLE_NAME, Database.ALL_COLUMNS, null, null, null, null, Database.SORT_ORDER);
        verify(cursor).moveToFirst();
        verify(cursor, times(2)).isAfterLast();
        verify(cursor).moveToNext();
        verify(cursor).getColumnIndexOrThrow(Database._ID);
        verify(cursor).getLong(0);
        verify(cursor).getColumnIndexOrThrow(Database.COLUMN_MAC);
        verify(cursor).getString(1);
        verify(cursor).getColumnIndexOrThrow(Database.COLUMN_NAME);
        verify(cursor).getString(2);
    }

    class DatabaseMock extends Database {
        public DatabaseMock(@NonNull Context context) {
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
        public ContentValues getContentValues() {
            return contentValues;
        }
    }


}