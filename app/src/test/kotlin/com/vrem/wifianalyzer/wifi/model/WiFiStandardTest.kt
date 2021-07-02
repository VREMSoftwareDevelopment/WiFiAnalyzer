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

package com.vrem.wifianalyzer.wifi.model

import android.net.wifi.ScanResult
import com.vrem.wifianalyzer.R
import org.junit.Assert.assertEquals
import org.junit.Test

class WiFiStandardTest {
    @Test
    fun testWidth() {
        assertEquals(5, WiFiStandard.values().size)
    }

    @Test
    fun testNameResource() {
        assertEquals(R.string.wifi_standard_unknown, WiFiStandard.UNKNOWN.textResource)
        assertEquals(R.string.wifi_standard_legacy, WiFiStandard.LEGACY.textResource)
        assertEquals(R.string.wifi_standard_n, WiFiStandard.N.textResource)
        assertEquals(R.string.wifi_standard_ac, WiFiStandard.AC.textResource)
        assertEquals(R.string.wifi_standard_ax, WiFiStandard.AX.textResource)
    }

    @Test
    fun testImageResource() {
        assertEquals(R.drawable.ic_wifi_unknown, WiFiStandard.UNKNOWN.imageResource)
        assertEquals(R.drawable.ic_wifi_legacy, WiFiStandard.LEGACY.imageResource)
        assertEquals(R.drawable.ic_wifi_4, WiFiStandard.N.imageResource)
        assertEquals(R.drawable.ic_wifi_5, WiFiStandard.AC.imageResource)
        assertEquals(R.drawable.ic_wifi_6, WiFiStandard.AX.imageResource)
    }

    @Test
    fun testFindOne() {
        assertEquals(WiFiStandard.UNKNOWN, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_UNKNOWN))
        assertEquals(WiFiStandard.LEGACY, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_LEGACY))
        assertEquals(WiFiStandard.N, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_11N))
        assertEquals(WiFiStandard.AC, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_11AC))
        assertEquals(WiFiStandard.AX, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_11AX))
        assertEquals(WiFiStandard.UNKNOWN, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_UNKNOWN - 1))
        assertEquals(WiFiStandard.UNKNOWN, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_11AX + 1))
    }

}