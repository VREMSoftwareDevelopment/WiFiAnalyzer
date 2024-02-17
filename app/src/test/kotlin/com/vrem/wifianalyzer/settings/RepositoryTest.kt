/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.vrem.wifianalyzer.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class RepositoryTest {
    private val keyIndex = R.string.app_full_name
    private val keyValue = "xyz"
    private val context: Context = mock()
    private val sharedPreferences: SharedPreferences = mock()
    private val onSharedPreferenceChangeListener: OnSharedPreferenceChangeListener = mock()
    private val editor: Editor = mock()
    private val resources: Resources = mock()
    private val fixture = spy(Repository(context))

    @Before
    fun setUp() {
        doReturn(sharedPreferences).whenever(fixture).defaultSharedPreferences(context)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(resources)
        verifyNoMoreInteractions(context)
        verifyNoMoreInteractions(sharedPreferences)
        verifyNoMoreInteractions(onSharedPreferenceChangeListener)
        verifyNoMoreInteractions(editor)
    }

    @Test
    fun initializeDefaultValues() {
        // setup
        doNothing().whenever(fixture).defaultValues(context, R.xml.settings, false)
        // execute
        fixture.initializeDefaultValues()
        // validate
        verify(fixture).defaultValues(context, R.xml.settings, false)
    }

    @Test
    fun saveString() {
        // setup
        val value = "1111"
        withSave(value)
        // execute
        fixture.save(keyIndex, value)
        // validate
        verifySave(value)
        verifyPreferenceManager()
    }

    @Test
    fun saveInteger() {
        // setup
        val value = 1111
        withSave(value.toString())
        // execute
        fixture.save(keyIndex, value)
        // validate
        verifySave(value.toString())
        verifyPreferenceManager()
    }

    @Test
    fun string() {
        // setup
        val value = "1111"
        val defaultValue = "2222"
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(value).whenever(sharedPreferences).getString(keyValue, defaultValue)
        // execute
        val actual = fixture.string(keyIndex, defaultValue)
        // validate
        assertThat(actual).isEqualTo(value)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getString(keyValue, "" + defaultValue)
        verifyPreferenceManager()
    }

    @Test
    fun stringAsInteger() {
        // setup
        val value = 1111
        val defaultValue = 2222
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(value.toString()).whenever(sharedPreferences).getString(keyValue, defaultValue.toString())
        // execute
        val actual = fixture.stringAsInteger(keyIndex, defaultValue)
        // validate
        assertThat(actual).isEqualTo(value)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getString(keyValue, defaultValue.toString())
        verifyPreferenceManager()
    }

    @Test
    fun stringAsIntegerThrowsException() {
        // setup
        val defaultValue = 2222
        val defaultValueAsString = defaultValue.toString()
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doThrow(RuntimeException()).whenever(sharedPreferences).getString(keyValue, defaultValueAsString)
        withSave(defaultValueAsString)
        // execute
        val actual = fixture.stringAsInteger(keyIndex, defaultValue)
        // validate
        assertThat(actual).isEqualTo(defaultValue)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getString(keyValue, defaultValueAsString)
        verifySave(defaultValueAsString)
        verifyPreferenceManager()
    }

    @Test
    fun integer() {
        // setup
        val value = 1111
        val defaultValue = 2222
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(value).whenever(sharedPreferences).getInt(keyValue, defaultValue)
        // execute
        val actual = fixture.integer(keyIndex, defaultValue)
        // validate
        assertThat(actual).isEqualTo(value)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getInt(keyValue, defaultValue)
        verifyPreferenceManager()
    }

    @Test
    fun integerThrowsException() {
        // setup
        val defaultValue = 2222
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doThrow(RuntimeException()).whenever(sharedPreferences).getInt(keyValue, defaultValue)
        withSave(defaultValue.toString())
        // execute
        val actual = fixture.integer(keyIndex, defaultValue)
        // validate
        assertThat(actual).isEqualTo(defaultValue)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getInt(keyValue, defaultValue)
        verifySave(defaultValue.toString())
        verifyPreferenceManager()
    }

    @Test
    fun resourceBoolean() {
        // setup
        val keyIndex = R.bool.wifi_off_on_exit_default
        doReturn(resources).whenever(context).resources
        doReturn(true).whenever(resources).getBoolean(keyIndex)
        // execute
        val actual = fixture.resourceBoolean(keyIndex)
        // validate
        assertThat(actual).isTrue()
        verify(context).resources
        verify(resources).getBoolean(keyIndex)
    }

    @Test
    fun boolean() {
        // setup
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(true).whenever(sharedPreferences).getBoolean(keyValue, false)
        // execute
        val actual = fixture.boolean(keyIndex, false)
        // validate
        assertThat(actual).isTrue()
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getBoolean(keyValue, false)
        verifyPreferenceManager()
    }

    @Test
    fun booleanThrowsException() {
        // setup
        val defaultValue = true
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doThrow(RuntimeException()).whenever(sharedPreferences).getBoolean(keyValue, defaultValue)
        withSave()
        // execute
        val actual = fixture.boolean(keyIndex, defaultValue)
        // validate
        assertThat(actual).isTrue()
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getBoolean(keyValue, defaultValue)
        verifySave()
        verifyPreferenceManager()
    }

    @Test
    fun registerOnSharedPreferenceChangeListener() {
        // setup
        doNothing().whenever(sharedPreferences)
            .registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        // execute
        fixture.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        // verify
        verify(sharedPreferences).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        verifyPreferenceManager()
    }

    @Test
    fun stringSet() {
        // setup
        val expected = setOf("123")
        val defaultValues = setOf("567")
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(expected).whenever(sharedPreferences).getStringSet(keyValue, defaultValues)
        // execute
        val actual = fixture.stringSet(keyIndex, defaultValues)
        // validate
        assertThat(actual).isEqualTo(expected)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getStringSet(keyValue, defaultValues)
        verifyPreferenceManager()
    }

    @Test
    fun stringSetThrowsException() {
        // setup
        val expected = setOf("567")
        doThrow(RuntimeException()).whenever(sharedPreferences).getStringSet(keyValue, expected)
        withSave(expected)
        // execute
        val actual = fixture.stringSet(keyIndex, expected)
        // validate
        assertThat(actual).isEqualTo(expected)
        verify(sharedPreferences).getStringSet(keyValue, expected)
        verifySave(expected)
        verifyPreferenceManager()
    }

    @Test
    fun saveStringSet() {
        // setup
        val keyIndex = R.string.app_full_name
        val values = setOf("123")
        withSave(values)
        // execute
        fixture.saveStringSet(keyIndex, values)
        // validate
        verifySave(values)
        verifyPreferenceManager()
    }

    @Test
    fun stringWhenGetStringReturnsNull() {
        // setup
        val keyValue = "123"
        val defaultValue = "default value"
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(null).whenever(sharedPreferences).getString(keyValue, defaultValue)
        // execute
        val actual = fixture.string(keyIndex, defaultValue)
        // validate
        assertThat(actual).isEqualTo(defaultValue)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getString(keyValue, defaultValue)
    }


    private fun verifySave(values: Set<String>) {
        verify(context).getString(keyIndex)
        verify(sharedPreferences).edit()
        verify(editor).putStringSet(keyValue, values)
        verify(editor).apply()
    }

    private fun verifySave(value: String) {
        verify(context).getString(keyIndex)
        verify(sharedPreferences).edit()
        verify(editor).putString(keyValue, value)
        verify(editor).apply()
    }

    private fun verifySave() {
        verify(context).getString(keyIndex)
        verify(sharedPreferences).edit()
        verify(editor).putBoolean(keyValue, true)
        verify(editor).apply()
    }

    private fun withSave(value: String) {
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(editor).whenever(sharedPreferences).edit()
        doReturn(editor).whenever(editor).putString(keyValue, value)
        doNothing().whenever(editor).apply()
    }

    private fun withSave() {
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(editor).whenever(sharedPreferences).edit()
        doReturn(editor).whenever(editor).putBoolean(keyValue, true)
        doNothing().whenever(editor).apply()
    }

    private fun withSave(value: Set<String>) {
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(editor).whenever(sharedPreferences).edit()
        doReturn(editor).whenever(editor).putStringSet(keyValue, value)
        doNothing().whenever(editor).apply()
    }

    private fun verifyPreferenceManager() {
        verify(fixture, atLeastOnce()).defaultSharedPreferences(context)
    }

}