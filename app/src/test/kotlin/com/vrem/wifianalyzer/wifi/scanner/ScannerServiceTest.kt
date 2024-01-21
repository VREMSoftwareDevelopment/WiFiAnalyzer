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
package com.vrem.wifianalyzer.wifi.scanner

import android.os.Handler
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.manager.WiFiManagerWrapper
import org.junit.After
import org.junit.Assert.*
import org.junit.Test

class ScannerServiceTest {
    private val wiFiManagerWrapper: WiFiManagerWrapper = mock()
    private val mainActivity: MainActivity = mock()
    private val handler: Handler = mock()
    private val settings: Settings = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(wiFiManagerWrapper)
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(handler)
        verifyNoMoreInteractions(settings)
    }

    @Test
    fun testMakeScannerService() {
        // setup
        // execute
        val actual = makeScannerService(mainActivity, wiFiManagerWrapper, handler, settings) as Scanner
        // validate
        assertEquals(wiFiManagerWrapper, actual.wiFiManagerWrapper)
        assertEquals(settings, actual.settings)
        assertNotNull(actual.transformer)
        assertNotNull(actual.periodicScan)
        assertNotNull(actual.scannerCallback)
        assertNotNull(actual.scanResultsReceiver)
        assertFalse(actual.running())
    }

}