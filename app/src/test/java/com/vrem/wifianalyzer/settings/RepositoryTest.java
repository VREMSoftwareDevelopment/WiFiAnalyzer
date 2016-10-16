/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.vrem.wifianalyzer.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static android.content.SharedPreferences.Editor;
import static android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PreferenceManager.class)
public class RepositoryTest {
    @Mock
    private SharedPreferences sharedPreferences;
    @Mock
    private OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;
    @Mock
    private Editor editor;

    private String keyValue;
    private Context context;
    private Resources resources;
    private Repository fixture;

    @Before
    public void setUp() {
        mockStatic(PreferenceManager.class);

        keyValue = "xyz";

        context = MainContextHelper.INSTANCE.getMainActivity();
        resources = MainContextHelper.INSTANCE.getResources();

        when(PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(sharedPreferences);

        fixture = new Repository();
    }

    @After
    public void tearDown() {
        verifyStatic();
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testInitializeDefaultValues() throws Exception {
        fixture.initializeDefaultValues();
    }

    @Test
    public void testSave() throws Exception {
        // setup
        int keyIndex = R.string.app_name;
        int value = 1111;
        withSave(keyIndex);
        // execute
        fixture.save(keyIndex, value);
        // validate
        verifySave(keyIndex, value);
    }

    private void verifySave(int keyIndex, int value) {
        verify(context).getString(keyIndex);
        verify(editor).putString(keyValue, "" + value);
        verify(editor).apply();
    }

    private void withSave(int keyIndex) {
        when(context.getString(keyIndex)).thenReturn(keyValue);
        when(sharedPreferences.edit()).thenReturn(editor);
    }

    @Test
    public void testGetString() throws Exception {
        // setup
        int keyIndex = R.string.app_name;
        String value = "1111";
        String defaultValue = "2222";
        when(context.getString(keyIndex)).thenReturn(keyValue);
        when(sharedPreferences.getString(keyValue, defaultValue)).thenReturn(value);
        // execute
        String actual = fixture.getString(keyIndex, defaultValue);
        // validate
        assertEquals(value, actual);
        verify(context).getString(keyIndex);
        verify(sharedPreferences).getString(keyValue, "" + defaultValue);
    }

    @Test
    public void testGetStringAsInteger() throws Exception {
        // setup
        int keyIndex = R.string.app_name;
        int value = 1111;
        int defaultValue = 2222;
        when(context.getString(keyIndex)).thenReturn(keyValue);
        when(sharedPreferences.getString(keyValue, "" + defaultValue)).thenReturn("" + value);
        // execute
        int actual = fixture.getStringAsInteger(keyIndex, defaultValue);
        // validate
        assertEquals(value, actual);
        verify(context).getString(keyIndex);
        verify(sharedPreferences).getString(keyValue, "" + defaultValue);
    }

    @Test
    public void testGetStringAsIntegerThrowsException() throws Exception {
        // setup
        int keyIndex = R.string.app_name;
        int defaultValue = 2222;
        when(context.getString(keyIndex)).thenReturn(keyValue);
        when(sharedPreferences.getString(keyValue, "" + defaultValue)).thenThrow(new RuntimeException());
        withSave(keyIndex);
        // execute
        int actual = fixture.getStringAsInteger(keyIndex, defaultValue);
        // validate
        assertEquals(defaultValue, actual);
        verify(context).getString(keyIndex);
        verify(sharedPreferences).getString(keyValue, "" + defaultValue);
        verifySave(keyIndex, defaultValue);
    }

    @Test
    public void testGetResourceInteger() throws Exception {
        // setup
        int keyIndex = R.integer.scan_interval_max;
        int expected = 1111;
        when(resources.getInteger(keyIndex)).thenReturn(expected);
        // execute
        int actual = fixture.getResourceInteger(keyIndex);
        // validate
        assertEquals(expected, actual);
        verify(resources).getInteger(keyIndex);
    }

    @Test
    public void testGetInteger() throws Exception {
        // setup
        int keyIndex = R.string.app_name;
        int value = 1111;
        int defaultValue = 2222;
        when(context.getString(keyIndex)).thenReturn(keyValue);
        when(sharedPreferences.getInt(keyValue, defaultValue)).thenReturn(value);
        // execute
        int actual = fixture.getInteger(keyIndex, defaultValue);
        // validate
        assertEquals(value, actual);
        verify(context).getString(keyIndex);
        verify(sharedPreferences).getInt(keyValue, defaultValue);
    }

    @Test
    public void testGetIntegerThrowsException() throws Exception {
        // setup
        int keyIndex = R.string.app_name;
        int defaultValue = 2222;
        when(context.getString(keyIndex)).thenReturn(keyValue);
        when(sharedPreferences.getInt(keyValue, defaultValue)).thenThrow(new RuntimeException());
        withSave(keyIndex);
        // execute
        int actual = fixture.getInteger(keyIndex, defaultValue);
        // validate
        assertEquals(defaultValue, actual);
        verify(context).getString(keyIndex);
        verify(sharedPreferences).getInt(keyValue, defaultValue);
        verifySave(keyIndex, defaultValue);
    }

    @Test
    public void testRegisterOnSharedPreferenceChangeListener() throws Exception {
        // execute
        fixture.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        // verify
        verify(sharedPreferences).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

}