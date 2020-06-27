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
import com.nhaarman.mockitokotlin2.*
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.settings.Settings
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Test

class ScannerServiceTest {
    private val mainActivity: MainActivity = mock()
    private val wifiManager: WifiManager = mock()
    private val handler: Handler = mock()
    private val settings: Settings = mock()
    private val context: Context = mock()

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
        verify(handler).removeCallbacks(any())
        verify(handler).postDelayed(any(), eq(delayInitial))
    }
}