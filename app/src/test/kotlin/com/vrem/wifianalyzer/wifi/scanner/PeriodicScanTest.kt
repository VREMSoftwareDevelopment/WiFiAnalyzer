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
package com.vrem.wifianalyzer.wifi.scanner

import android.os.Handler
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.settings.Settings
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

class PeriodicScanTest {
    private val handler = Mockito.mock(Handler::class.java)
    private val settings = Mockito.mock(Settings::class.java)
    private val scanner = Mockito.mock(ScannerService::class.java)
    private val fixture = PeriodicScan(scanner, handler, settings)

    @Test
    fun testRun() {
        //_setup
        val delayInterval = 1000L
        val scanSpeed = 15
        whenever(settings.scanSpeed()).thenReturn(scanSpeed)
        // execute
        fixture.run()
        // validate
        verify(scanner).update()
        verify(handler).removeCallbacks(fixture)
        verify(handler).postDelayed(fixture, scanSpeed * delayInterval)
    }

    @Test
    fun testStop() {
        // execute
        fixture.stop()
        // validate
        verify(handler).removeCallbacks(fixture)
    }

    @Test
    fun testStart() {
        // setup
        val delayInitial = 1L
        // execute
        fixture.start()
        // validate
        verify(handler).removeCallbacks(fixture)
        verify(handler).postDelayed(fixture, delayInitial)
    }
}