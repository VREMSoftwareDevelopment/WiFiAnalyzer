/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.nhaarman.mockitokotlin2.*
import com.vrem.wifianalyzer.R
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class SettingsAndroidOTest {
    private val scanSpeedDefault = 5
    private val repository: Repository = mock()
    private val fixture = spy(Settings(repository))

    @Before
    fun setUp() {
        doReturn(false).whenever(fixture).minVersionQ()
        doReturn(false).whenever(fixture).versionP()
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun testScanSpeed() {
        // setup
        val defaultValue = 10
        val expected = 3
        whenever(repository.stringAsInteger(R.string.scan_speed_default, scanSpeedDefault)).thenReturn(defaultValue)
        whenever(repository.stringAsInteger(R.string.scan_speed_key, defaultValue)).thenReturn(expected)
        // execute
        val actual = fixture.scanSpeed()
        // validate
        assertEquals(expected.toLong(), actual.toLong())
        verify(repository).stringAsInteger(R.string.scan_speed_default, scanSpeedDefault)
        verify(repository).stringAsInteger(R.string.scan_speed_key, defaultValue)
        verify(fixture, never()).wiFiThrottleDisabled()
    }

    @Test
    fun testWiFiThrottleDisabled() {
        // execute
        val actual = fixture.wiFiThrottleDisabled()
        // validate
        assertFalse(actual)
        verify(repository, never()).resourceBoolean(any())
        verify(repository, never()).boolean(any(), any())
    }
}