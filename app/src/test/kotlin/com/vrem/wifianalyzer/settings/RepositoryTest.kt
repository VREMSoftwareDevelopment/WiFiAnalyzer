/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.R
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoryTest {
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
    fun testInitializeDefaultValues() {
        // setup
        doNothing().whenever(fixture).defaultValues(context, R.xml.settings, false)
        // execute
        fixture.initializeDefaultValues()
        // validate
        verify(fixture).defaultValues(context, R.xml.settings, false)
    }

    @Test
    fun testSaveString() {
        // setup
        val keyIndex = R.string.app_full_name
        val value = "1111"
        withSave(keyIndex, value)
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
        withSave(keyIndex, value.toString())
        // execute
        fixture.save(keyIndex, value)
        // validate
        verifySave(keyIndex, value.toString())
        verifyPreferenceManager()
    }

    @Test
    fun testString() {
        // setup
        val keyIndex = R.string.app_full_name
        val value = "1111"
        val defaultValue = "2222"
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(value).whenever(sharedPreferences).getString(keyValue, defaultValue)
        // execute
        val actual = fixture.string(keyIndex, defaultValue)
        // validate
        assertEquals(value, actual)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getString(keyValue, "" + defaultValue)
        verifyPreferenceManager()
    }

    @Test
    fun testStringAsInteger() {
        // setup
        val keyIndex = R.string.app_full_name
        val value = 1111
        val defaultValue = 2222
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(value.toString()).whenever(sharedPreferences).getString(keyValue, defaultValue.toString())
        // execute
        val actual = fixture.stringAsInteger(keyIndex, defaultValue)
        // validate
        assertEquals(value, actual)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getString(keyValue, defaultValue.toString())
        verifyPreferenceManager()
    }

    @Test
    fun testStringAsIntegerThrowsException() {
        // setup
        val keyIndex = R.string.app_full_name
        val defaultValue = 2222
        val defaultValueAsString = defaultValue.toString()
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doThrow(RuntimeException()).whenever(sharedPreferences).getString(keyValue, defaultValueAsString)
        withSave(keyIndex, defaultValueAsString)
        // execute
        val actual = fixture.stringAsInteger(keyIndex, defaultValue)
        // validate
        assertEquals(defaultValue, actual)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getString(keyValue, defaultValueAsString)
        verifySave(keyIndex, defaultValueAsString)
        verifyPreferenceManager()
    }

    @Test
    fun testInteger() {
        // setup
        val keyIndex = R.string.app_full_name
        val value = 1111
        val defaultValue = 2222
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(value).whenever(sharedPreferences).getInt(keyValue, defaultValue)
        // execute
        val actual = fixture.integer(keyIndex, defaultValue)
        // validate
        assertEquals(value, actual)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getInt(keyValue, defaultValue)
        verifyPreferenceManager()
    }

    @Test
    fun testIntegerThrowsException() {
        // setup
        val keyIndex = R.string.app_full_name
        val defaultValue = 2222
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doThrow(RuntimeException()).whenever(sharedPreferences).getInt(keyValue, defaultValue)
        withSave(keyIndex, defaultValue.toString())
        // execute
        val actual = fixture.integer(keyIndex, defaultValue)
        // validate
        assertEquals(defaultValue, actual)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getInt(keyValue, defaultValue)
        verifySave(keyIndex, defaultValue.toString())
        verifyPreferenceManager()
    }

    @Test
    fun testResourceBoolean() {
        // setup
        val keyIndex = R.bool.wifi_off_on_exit_default
        doReturn(resources).whenever(context).resources
        doReturn(true).whenever(resources).getBoolean(keyIndex)
        // execute
        val actual = fixture.resourceBoolean(keyIndex)
        // validate
        assertTrue(actual)
        verify(context).resources
        verify(resources).getBoolean(keyIndex)
    }

    @Test
    fun testBoolean() {
        // setup
        val keyIndex = R.string.app_full_name
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(true).whenever(sharedPreferences).getBoolean(keyValue, false)
        // execute
        val actual = fixture.boolean(keyIndex, false)
        // validate
        assertTrue(actual)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getBoolean(keyValue, false)
        verifyPreferenceManager()
    }

    @Test
    fun testBooleanThrowsException() {
        // setup
        val defaultValue = true
        val keyIndex = R.string.app_full_name
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doThrow(RuntimeException()).whenever(sharedPreferences).getBoolean(keyValue, defaultValue)
        withSave(keyIndex, defaultValue)
        // execute
        val actual = fixture.boolean(keyIndex, defaultValue)
        // validate
        assertTrue(actual)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getBoolean(keyValue, defaultValue)
        verifySave(keyIndex, defaultValue)
        verifyPreferenceManager()
    }

    @Test
    fun testRegisterOnSharedPreferenceChangeListener() {
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
    fun testStringSet() {
        // setup
        val keyIndex = R.string.app_full_name
        val expected = setOf("123")
        val defaultValues = setOf("567")
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(expected).whenever(sharedPreferences).getStringSet(keyValue, defaultValues)
        // execute
        val actual = fixture.stringSet(keyIndex, defaultValues)
        // validate
        assertEquals(expected, actual)
        verify(context).getString(keyIndex)
        verify(sharedPreferences).getStringSet(keyValue, defaultValues)
        verifyPreferenceManager()
    }

    @Test
    fun testStringSetThrowsException() {
        // setup
        val keyIndex = R.string.app_full_name
        val expected = setOf("567")
        doThrow(RuntimeException()).whenever(sharedPreferences).getStringSet(keyValue, expected)
        withSave(keyIndex, expected)
        // execute
        val actual = fixture.stringSet(keyIndex, expected)
        // validate
        assertEquals(expected, actual)
        verify(sharedPreferences).getStringSet(keyValue, expected)
        verifySave(keyIndex, expected)
        verifyPreferenceManager()
    }

    @Test
    fun testSaveStringSet() {
        // setup
        val keyIndex = R.string.app_full_name
        val values = setOf("123")
        withSave(keyIndex, values)
        // execute
        fixture.saveStringSet(keyIndex, values)
        // validate
        verifySave(keyIndex, values)
        verifyPreferenceManager()
    }

    private fun verifySave(keyIndex: Int, values: Set<String>) {
        verify(context).getString(keyIndex)
        verify(sharedPreferences).edit()
        verify(editor).putStringSet(keyValue, values)
        verify(editor).apply()
    }

    private fun verifySave(keyIndex: Int, value: String) {
        verify(context).getString(keyIndex)
        verify(sharedPreferences).edit()
        verify(editor).putString(keyValue, value)
        verify(editor).apply()
    }

    private fun verifySave(keyIndex: Int, value: Boolean) {
        verify(context).getString(keyIndex)
        verify(sharedPreferences).edit()
        verify(editor).putBoolean(keyValue, value)
        verify(editor).apply()
    }

    private fun withSave(keyIndex: Int, value: String) {
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(editor).whenever(sharedPreferences).edit()
        doReturn(editor).whenever(editor).putString(keyValue, value)
        doNothing().whenever(editor).apply()
    }

    private fun withSave(keyIndex: Int, value: Boolean) {
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(editor).whenever(sharedPreferences).edit()
        doReturn(editor).whenever(editor).putBoolean(keyValue, value)
        doNothing().whenever(editor).apply()
    }

    private fun withSave(keyIndex: Int, value: Set<String>) {
        doReturn(keyValue).whenever(context).getString(keyIndex)
        doReturn(editor).whenever(sharedPreferences).edit()
        doReturn(editor).whenever(editor).putStringSet(keyValue, value)
        doNothing().whenever(editor).apply()
    }

    private fun verifyPreferenceManager() {
        verify(fixture, atLeastOnce()).defaultSharedPreferences(context)
    }

}