/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.scanner

import android.os.Handler
import com.nhaarman.mockitokotlin2.*
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.manager.WiFiManagerWrapper
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Test

class ScannerServiceTest {
    private val wiFiManagerWrapper: WiFiManagerWrapper = mock()
    private val handler: Handler = mock()
    private val settings: Settings = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(wiFiManagerWrapper)
        verifyNoMoreInteractions(handler)
        verifyNoMoreInteractions(settings)
    }

    @Test
    fun testMakeScannerService() {
        // setup
        val delayInitial = 1L
        // execute
        val actual = makeScannerService(wiFiManagerWrapper, handler, settings)
        // validate
        assertTrue(actual is Scanner)
        assertTrue(actual.running())
        verify(handler).removeCallbacks(any())
        verify(handler).postDelayed(any(), eq(delayInitial))
    }
}