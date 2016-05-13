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
    public void setUp() throws Exception {
        mockStatic(PreferenceManager.class);

        keyValue = "xyz";

        context = MainContextHelper.INSTANCE.getContext();
        resources = MainContextHelper.INSTANCE.getResources();

        when(PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(sharedPreferences);

        fixture = new Repository();
    }

    @After
    public void tearDown() throws Exception {
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