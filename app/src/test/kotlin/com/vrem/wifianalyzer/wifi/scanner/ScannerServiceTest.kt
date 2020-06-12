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

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Handler
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.settings.Settings
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

class ScannerServiceTest {
    private val mainActivity = mock(MainActivity::class.java)
    private val wifiManager = mock(WifiManager::class.java)
    private val handler = mock(Handler::class.java)
    private val settings = mock(Settings::class.java)
    private val context = mock(Context::class.java)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(wifiManager)
        verifyNoMoreInteractions(handler)
        verifyNoMoreInteractions(settings)
        verifyNoMoreInteractions(context)
    }

    @Test
    fun testMakeScannerService() {
        // setup
        val delayInitial = 1L
        whenever(mainActivity.applicationContext).thenReturn(context)
        whenever(context.getSystemService(Context.WIFI_SERVICE)).thenReturn(wifiManager)
        // execute
        val actual = makeScannerService(mainActivity, handler, settings)
        // validate
        assertTrue(actual is Scanner)
        assertTrue(actual.running())
        verify(mainActivity).applicationContext
        verify(context).getSystemService(Context.WIFI_SERVICE)
        verify(handler).removeCallbacks(ArgumentMatchers.any(PeriodicScan::class.java))
        verify(handler).postDelayed(ArgumentMatchers.any(PeriodicScan::class.java), ArgumentMatchers.eq(delayInitial))
    }
}