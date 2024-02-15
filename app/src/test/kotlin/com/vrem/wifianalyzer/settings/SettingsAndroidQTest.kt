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

import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class SettingsAndroidQTest {
    private val repository: Repository = mock()
    private val fixture = spy(Settings(repository))

    @Before
    fun setUp() {
        doReturn(true).whenever(fixture).minVersionQ()
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun testWiFiOffOnExitAndroidQ() {
        // execute
        val actual = fixture.wiFiOffOnExit()
        // validate
        assertFalse(actual)
        verify(fixture).minVersionQ()
    }

}