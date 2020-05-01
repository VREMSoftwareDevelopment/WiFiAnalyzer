/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.settings

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.res.Resources
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.R
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class RepositoryTest {
    private val keyValue = "xyz"

    private val context: Context = mock(Context::class.java)

    private val sharedPreferences: SharedPreferences = mock(SharedPreferences::class.java)

    private val onSharedPreferenceChangeListener: OnSharedPreferenceChangeListener = mock(OnSharedPreferenceChangeListener::class.java)

    private val editor: Editor = mock(Editor::class.java)

    private val resources: Resources = mock(Resources::class.java)

    private val fixture = spy(Repository(context))

    @Before
    fun setUp() {
        doReturn(sharedPreferences).whenever(fixture).getDefaultSharedPreferences(context)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(resources)
        verifyNoMoreInteractions(context)
        //        verifyNoMoreInteractions(sharedPreferences);
        verifyNoMoreInteractions(onSharedPreferenceChangeListener)
        verifyNoMoreInteractions(editor)
    }

    @Test
    fun testInitializeDefaultValues() {
        // setup
        doNothing().whenever(fixture).setDefaultValues(context, R.xml.settings, false)
        // execute
        fixture.initializeDefaultValues()
        // validate
        verify(fixture).setDefaultValues(context, R.xml.settings, false)
    }

    @Test
    fun testSaveString() {
        // setup
        val keyIndex = R.string.app_full_name
        val value = "1111"
        withSave(keyIndex)
        // execute
        fixture.save(keyIndex, value)
        // validate
        verifySave(keyIndex, value)
        verifyPreferenceManager()
    }

    @Test
    fun testSaveInteger() {
        // setup
        val keyIndex = R.string.app_full_name
        val value = 1111
        withSave(keyIndex)
        // execute
        fixture.save(keyIndex, value)
        // validate
        verifySave(keyIndex, Integer.toString(value))
        verifyPreferenceManager()
    }

    @Test
    fun testGetString() {
        // setup
        val keyIndex = R.string.app_full_name
        val value = "1111"
        val defaultValue = "2222"
        whenever(context.getString(keyIndex)).thenReturn(keyValue)
        whenever(sharedPreferences.getString(keyValue, defaultValue)).thenReturn(value)
        // execute
        val actual = fixture.getString(keyIndex, defaultValue)
        // validate
        Assert.assertEquals(value, actual)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getString(keyValue, "" + defaultValue)
        verifyPreferenceManager()
    }

    @Test
    fun testGetStringAsInteger() {
        // setup
        val keyIndex = R.string.app_full_name
        val value = 1111
        val defaultValue = 2222
        whenever(context.getString(keyIndex)).thenReturn(keyValue)
        whenever(sharedPreferences.getString(keyValue, "" + defaultValue)).thenReturn("" + value)
        // execute
        val actual = fixture.getStringAsInteger(keyIndex, defaultValue)
        // validate
        Assert.assertEquals(value.toLong(), actual.toLong())
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getString(keyValue, "" + defaultValue)
        verifyPreferenceManager()
    }

    @Test
    fun testGetStringAsIntegerThrowsException() {
        // setup
        val keyIndex = R.string.app_full_name
        val defaultValue = 2222
        whenever(context.getString(keyIndex)).thenReturn(keyValue)
        withSave(keyIndex)
        // execute
        val actual = fixture.getStringAsInteger(keyIndex, defaultValue)
        // validate
        Assert.assertEquals(defaultValue.toLong(), actual.toLong())
        verify(context).getString(keyIndex)
        verifyPreferenceManager()
    }

    @Test
    fun testGetInteger() {
        // setup
        val keyIndex = R.string.app_full_name
        val value = 1111
        val defaultValue = 2222
        whenever(context.getString(keyIndex)).thenReturn(keyValue)
        whenever(sharedPreferences.getInt(keyValue, defaultValue)).thenReturn(value)
        // execute
        val actual = fixture.getInteger(keyIndex, defaultValue)
        // validate
        Assert.assertEquals(value.toLong(), actual.toLong())
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getInt(keyValue, defaultValue)
        verifyPreferenceManager()
    }

    @Test
    fun testGetIntegerThrowsException() {
        // setup
        val keyIndex = R.string.app_full_name
        val defaultValue = 2222
        whenever(context.getString(keyIndex)).thenReturn(keyValue)
        whenever(sharedPreferences.getInt(keyValue, defaultValue)).thenThrow(RuntimeException())
        withSave(keyIndex)
        // execute
        val actual = fixture.getInteger(keyIndex, defaultValue)
        // validate
        Assert.assertEquals(defaultValue.toLong(), actual.toLong())
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getInt(keyValue, defaultValue)
        verifySave(keyIndex, Integer.toString(defaultValue))
        verifyPreferenceManager()
    }

    @Test
    fun testGetResourceBoolean() {
        // setup
        val keyIndex = R.bool.wifi_off_on_exit_default
        whenever(context.resources).thenReturn(resources)
        whenever(resources.getBoolean(keyIndex)).thenReturn(true)
        // execute
        val actual = fixture.getResourceBoolean(keyIndex)
        // validate
        Assert.assertTrue(actual)
        verify(context).resources
        verify(resources).getBoolean(keyIndex)
    }

    @Test
    fun testGetBoolean() {
        // setup
        val keyIndex = R.string.app_full_name
        whenever(context.getString(keyIndex)).thenReturn(keyValue)
        whenever(sharedPreferences.getBoolean(keyValue, false)).thenReturn(true)
        // execute
        val actual = fixture.getBoolean(keyIndex, false)
        // validate
        Assert.assertTrue(actual)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getBoolean(keyValue, false)
        verifyPreferenceManager()
    }

    @Test
    fun testGetBooleanThrowsException() {
        // setup
        val keyIndex = R.string.app_full_name
        whenever(context.getString(keyIndex)).thenReturn(keyValue)
        whenever(sharedPreferences.getBoolean(keyValue, true)).thenThrow(RuntimeException())
        withSave(keyIndex)
        // execute
        val actual = fixture.getBoolean(keyIndex, true)
        // validate
        Assert.assertTrue(actual)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getBoolean(keyValue, true)
        verifySave(keyIndex, true)
        verifyPreferenceManager()
    }

    @Test
    fun testRegisterOnSharedPreferenceChangeListener() {
        // execute
        fixture.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        // verify
        verify(sharedPreferences).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        verifyPreferenceManager()
    }

    @Test
    fun testGetStringSet() {
        // setup
        val keyIndex = R.string.app_full_name
        val expected = setOf("123")
        val defaultValues = setOf("567")
        whenever(context.getString(keyIndex)).thenReturn(keyValue)
        whenever(sharedPreferences.getStringSet(keyValue, defaultValues)).thenReturn(expected)
        // execute
        val actual = fixture.getStringSet(keyIndex, defaultValues)
        // validate
        Assert.assertEquals(expected, actual)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getStringSet(keyValue, defaultValues)
        verifyPreferenceManager()
    }

    @Test
    fun testGetStringSetThrowsException() {
        // setup
        val keyIndex = R.string.app_full_name
        val expected = setOf("567")
        whenever(context.getString(keyIndex)).thenReturn(keyValue)
        whenever(sharedPreferences.getStringSet(keyValue, expected)).thenThrow(RuntimeException())
        whenever(sharedPreferences.edit()).thenReturn(editor)
        // execute
        val actual = fixture.getStringSet(keyIndex, expected)
        // validate
        Assert.assertEquals(expected, actual)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getStringSet(keyValue, expected)
        verify(sharedPreferences).edit()
        verify(editor).putStringSet(keyValue, expected)
        verify(editor).apply()
        verifyPreferenceManager()
    }

    @Test
    fun testSaveStringSet() {
        // setup
        val keyIndex = R.string.app_full_name
        val values = setOf("123")
        whenever(context.getString(keyIndex)).thenReturn(keyValue)
        whenever(sharedPreferences.edit()).thenReturn(editor)
        // execute
        fixture.saveStringSet(keyIndex, values)
        // validate
        verify(context).getString(keyIndex)
        verify(sharedPreferences).edit()
        verify(editor).putStringSet(keyValue, values)
        verify(editor).apply()
        verifyPreferenceManager()
    }

    private fun verifySave(keyIndex: Int, value: String) {
        verify(context).getString(keyIndex)
        verify(editor).putString(keyValue, value)
        verify(editor).apply()
    }

    private fun verifySave(keyIndex: Int, value: Boolean) {
        verify(context).getString(keyIndex)
        verify(editor).putBoolean(keyValue, value)
        verify(editor).apply()
    }

    private fun withSave(keyIndex: Int) {
        whenever(context.getString(keyIndex)).thenReturn(keyValue)
        whenever(sharedPreferences.edit()).thenReturn(editor)
    }

    private fun verifyPreferenceManager() {
        verify(fixture, atLeastOnce()).getDefaultSharedPreferences(context)
    }

}