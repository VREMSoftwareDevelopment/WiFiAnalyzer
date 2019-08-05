/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.vrem.wifianalyzer.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.Set;

import androidx.preference.PreferenceManager;

import static android.content.SharedPreferences.Editor;
import static android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PreferenceManager.class)
public class RepositoryTest {
    private final static String KEY_VALUE = "xyz";

    @Mock
    private Context context;
    @Mock
    private SharedPreferences sharedPreferences;
    @Mock
    private OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;
    @Mock
    private Editor editor;
    @Mock
    private Resources resources;

    private Repository fixture;

    @Before
    public void setUp() {
        mockStatic(PreferenceManager.class);
        fixture = new Repository(context);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(PreferenceManager.class);
        verifyNoMoreInteractions(resources);
        verifyNoMoreInteractions(context);
//        verifyNoMoreInteractions(sharedPreferences);
        verifyNoMoreInteractions(onSharedPreferenceChangeListener);
        verifyNoMoreInteractions(editor);
    }

    @Test
    public void testInitializeDefaultValues() {
        // execute
        fixture.initializeDefaultValues();
        // validate
        verifyStatic(PreferenceManager.class);
        PreferenceManager.setDefaultValues(context, R.xml.settings, false);
    }

    @Test
    public void testSaveString() {
        // setup
        int keyIndex = R.string.app_full_name;
        String value = "1111";
        withPreferenceManager();
        withSave(keyIndex);
        // execute
        fixture.save(keyIndex, value);
        // validate
        verifySave(keyIndex, value);
        verifyPreferenceManager();
    }

    @Test
    public void testSaveInteger() {
        // setup
        int keyIndex = R.string.app_full_name;
        int value = 1111;
        withPreferenceManager();
        withSave(keyIndex);
        // execute
        fixture.save(keyIndex, value);
        // validate
        verifySave(keyIndex, Integer.toString(value));
        verifyPreferenceManager();
    }

    @Test
    public void testGetString() {
        // setup
        int keyIndex = R.string.app_full_name;
        String value = "1111";
        String defaultValue = "2222";
        withPreferenceManager();
        when(context.getString(keyIndex)).thenReturn(KEY_VALUE);
        when(sharedPreferences.getString(KEY_VALUE, defaultValue)).thenReturn(value);
        // execute
        String actual = fixture.getString(keyIndex, defaultValue);
        // validate
        assertEquals(value, actual);
        verify(context).getString(keyIndex);
        verify(sharedPreferences).getString(KEY_VALUE, "" + defaultValue);
        verifyPreferenceManager();
    }

    @Test
    public void testGetStringAsInteger() {
        // setup
        int keyIndex = R.string.app_full_name;
        int value = 1111;
        int defaultValue = 2222;
        withPreferenceManager();
        when(context.getString(keyIndex)).thenReturn(KEY_VALUE);
        when(sharedPreferences.getString(KEY_VALUE, "" + defaultValue)).thenReturn("" + value);
        // execute
        int actual = fixture.getStringAsInteger(keyIndex, defaultValue);
        // validate
        assertEquals(value, actual);
        verify(context).getString(keyIndex);
        verify(sharedPreferences).getString(KEY_VALUE, "" + defaultValue);
        verifyPreferenceManager();
    }

    @Test
    public void testGetStringAsIntegerThrowsException() {
        // setup
        int keyIndex = R.string.app_full_name;
        int defaultValue = 2222;
        withPreferenceManager();
        when(context.getString(keyIndex)).thenReturn(KEY_VALUE);
        withSave(keyIndex);
        // execute
        int actual = fixture.getStringAsInteger(keyIndex, defaultValue);
        // validate
        assertEquals(defaultValue, actual);
        verify(context).getString(keyIndex);
        verifyPreferenceManager();
    }

    @Test
    public void testGetInteger() {
        // setup
        int keyIndex = R.string.app_full_name;
        int value = 1111;
        int defaultValue = 2222;
        withPreferenceManager();
        when(context.getString(keyIndex)).thenReturn(KEY_VALUE);
        when(sharedPreferences.getInt(KEY_VALUE, defaultValue)).thenReturn(value);
        // execute
        int actual = fixture.getInteger(keyIndex, defaultValue);
        // validate
        assertEquals(value, actual);
        verify(context).getString(keyIndex);
        verify(sharedPreferences).getInt(KEY_VALUE, defaultValue);
        verifyPreferenceManager();
    }

    @Test
    public void testGetIntegerThrowsException() {
        // setup
        int keyIndex = R.string.app_full_name;
        int defaultValue = 2222;
        withPreferenceManager();
        when(context.getString(keyIndex)).thenReturn(KEY_VALUE);
        when(sharedPreferences.getInt(KEY_VALUE, defaultValue)).thenThrow(new RuntimeException());
        withSave(keyIndex);
        // execute
        int actual = fixture.getInteger(keyIndex, defaultValue);
        // validate
        assertEquals(defaultValue, actual);
        verify(context).getString(keyIndex);
        verify(sharedPreferences).getInt(KEY_VALUE, defaultValue);
        verifySave(keyIndex, Integer.toString(defaultValue));
        verifyPreferenceManager();
    }

    @Test
    public void testGetResourceBoolean() {
        // setup
        int keyIndex = R.bool.wifi_off_on_exit_default;
        when(context.getResources()).thenReturn(resources);
        when(resources.getBoolean(keyIndex)).thenReturn(true);
        // execute
        boolean actual = fixture.getResourceBoolean(keyIndex);
        // validate
        assertTrue(actual);
        verify(context).getResources();
        verify(resources).getBoolean(keyIndex);
    }

    @Test
    public void testGetBoolean() {
        // setup
        int keyIndex = R.string.app_full_name;
        withPreferenceManager();
        when(context.getString(keyIndex)).thenReturn(KEY_VALUE);
        when(sharedPreferences.getBoolean(KEY_VALUE, false)).thenReturn(true);
        // execute
        boolean actual = fixture.getBoolean(keyIndex, false);
        // validate
        assertTrue(actual);
        verify(context).getString(keyIndex);
        verify(sharedPreferences).getBoolean(KEY_VALUE, false);
        verifyPreferenceManager();
    }

    @Test
    public void testGetBooleanThrowsException() {
        // setup
        int keyIndex = R.string.app_full_name;
        withPreferenceManager();
        when(context.getString(keyIndex)).thenReturn(KEY_VALUE);
        when(sharedPreferences.getBoolean(KEY_VALUE, true)).thenThrow(new RuntimeException());
        withSave(keyIndex);
        // execute
        boolean actual = fixture.getBoolean(keyIndex, true);
        // validate
        assertTrue(actual);
        verify(context).getString(keyIndex);
        verify(sharedPreferences).getBoolean(KEY_VALUE, true);
        verifySave(keyIndex, true);
        verifyPreferenceManager();
    }

    @Test
    public void testRegisterOnSharedPreferenceChangeListener() {
        // setup
        withPreferenceManager();
        // execute
        fixture.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        // verify
        verify(sharedPreferences).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        verifyPreferenceManager();
    }

    @Test
    public void testGetStringSet() {
        // setup
        int keyIndex = R.string.app_full_name;
        Set<String> expected = Collections.singleton("123");
        Set<String> defaultValues = Collections.singleton("567");
        withPreferenceManager();
        when(context.getString(keyIndex)).thenReturn(KEY_VALUE);
        when(sharedPreferences.getStringSet(KEY_VALUE, defaultValues)).thenReturn(expected);
        // execute
        Set<String> actual = fixture.getStringSet(keyIndex, defaultValues);
        // validate
        assertEquals(expected, actual);
        verify(context).getString(keyIndex);
        verify(sharedPreferences).getStringSet(KEY_VALUE, defaultValues);
        verifyPreferenceManager();
    }

    @Test
    public void testGetStringSetThrowsException() {
        // setup
        int keyIndex = R.string.app_full_name;
        Set<String> expected = Collections.singleton("567");
        withPreferenceManager();
        when(context.getString(keyIndex)).thenReturn(KEY_VALUE);
        when(sharedPreferences.getStringSet(KEY_VALUE, expected)).thenThrow(new RuntimeException());
        when(sharedPreferences.edit()).thenReturn(editor);
        // execute
        Set<String> actual = fixture.getStringSet(keyIndex, expected);
        // validate
        assertEquals(expected, actual);
        verify(context).getString(keyIndex);
        verify(sharedPreferences).getStringSet(KEY_VALUE, expected);
        verify(sharedPreferences).edit();
        verify(editor).putStringSet(KEY_VALUE, expected);
        verify(editor).apply();
        verifyPreferenceManager();
    }

    @Test
    public void testSaveStringSet() {
        // setup
        int keyIndex = R.string.app_full_name;
        Set<String> values = Collections.singleton("123");
        withPreferenceManager();
        when(context.getString(keyIndex)).thenReturn(KEY_VALUE);
        when(sharedPreferences.edit()).thenReturn(editor);
        // execute
        fixture.saveStringSet(keyIndex, values);
        // validate
        verify(context).getString(keyIndex);
        verify(sharedPreferences).edit();
        verify(editor).putStringSet(KEY_VALUE, values);
        verify(editor).apply();
        verifyPreferenceManager();
    }

    private void verifySave(int keyIndex, String value) {
        verify(context).getString(keyIndex);
        verify(editor).putString(KEY_VALUE, value);
        verify(editor).apply();
    }

    private void verifySave(int keyIndex, boolean value) {
        verify(context).getString(keyIndex);
        verify(editor).putBoolean(KEY_VALUE, value);
        verify(editor).apply();
    }

    private void withSave(int keyIndex) {
        when(context.getString(keyIndex)).thenReturn(KEY_VALUE);
        when(sharedPreferences.edit()).thenReturn(editor);
    }

    private void withPreferenceManager() {
        when(PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(sharedPreferences);
    }

    private void verifyPreferenceManager() {
        verifyStatic(PreferenceManager.class, atLeastOnce());
        PreferenceManager.getDefaultSharedPreferences(context);
    }

}