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
package com.vrem.wifianalyzer.navigation.options

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.navigation.options.OptionAction.Companion.findOptionAction
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.scanner.ScannerService
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class OptionActionTest {
    private val mainActivity: MainActivity = RobolectricUtil.INSTANCE.activity
    private val scannerService: ScannerService = INSTANCE.scannerService
    private val settings: Settings = INSTANCE.settings

    @After
    fun tearDown() {
        verifyNoMoreInteractions(scannerService)
        verifyNoMoreInteractions(settings)
        INSTANCE.restore()
    }

    @Test
    fun testScannerAction() {
        // execute
        scannerAction()
        // validate
        verify(scannerService).toggle()
    }

    @Test
    fun testWiFiBandAction2() {
        // execute
        wiFiBandAction2()
        // validate
        verify(settings).wiFiBand(WiFiBand.GHZ2)
    }

    @Test
    fun testWiFiBandAction5() {
        // execute
        wiFiBandAction5()
        // validate
        verify(settings).wiFiBand(WiFiBand.GHZ5)
    }

    @Test
    fun testWiFiBandAction6() {
        // execute
        wiFiBandAction6()
        // validate
        verify(settings).wiFiBand(WiFiBand.GHZ6)
    }

    @Test
    fun testFilterAction() {
        filterAction()
    }

    @Test
    fun testOptionAction() {
        assertEquals(6, OptionAction.values().size)
    }

    @Test
    fun testGetKey() {
        assertEquals(-1, OptionAction.NO_ACTION.key)
        assertEquals(R.id.action_scanner, OptionAction.SCANNER.key)
        assertEquals(R.id.action_filter, OptionAction.FILTER.key)
        assertEquals(R.id.action_wifi_band_2ghz, OptionAction.WIFI_BAND_2.key)
        assertEquals(R.id.action_wifi_band_5ghz, OptionAction.WIFI_BAND_5.key)
        assertEquals(R.id.action_wifi_band_6ghz, OptionAction.WIFI_BAND_6.key)
    }

    @Test
    fun testGetAction() {
        assertTrue(OptionAction.NO_ACTION.action == noAction)
        assertTrue(OptionAction.SCANNER.action == scannerAction)
        assertTrue(OptionAction.FILTER.action == filterAction)
        assertTrue(OptionAction.WIFI_BAND_2.action == wiFiBandAction2)
        assertTrue(OptionAction.WIFI_BAND_5.action == wiFiBandAction5)
        assertTrue(OptionAction.WIFI_BAND_6.action == wiFiBandAction6)
    }

    @Test
    fun testGetOptionAction() {
        assertEquals(OptionAction.NO_ACTION, findOptionAction(OptionAction.NO_ACTION.key))
        assertEquals(OptionAction.SCANNER, findOptionAction(OptionAction.SCANNER.key))
        assertEquals(OptionAction.FILTER, findOptionAction(OptionAction.FILTER.key))
        assertEquals(OptionAction.WIFI_BAND_2, findOptionAction(OptionAction.WIFI_BAND_2.key))
        assertEquals(OptionAction.WIFI_BAND_5, findOptionAction(OptionAction.WIFI_BAND_5.key))
        assertEquals(OptionAction.WIFI_BAND_6, findOptionAction(OptionAction.WIFI_BAND_6.key))
    }

    @Test
    fun testGetOptionActionInvalidKey() {
        assertEquals(OptionAction.NO_ACTION, findOptionAction(-99))
        assertEquals(OptionAction.NO_ACTION, findOptionAction(99))
    }
}