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

package com.vrem.wifianalyzer.wifi.model

import android.net.wifi.ScanResult
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import com.vrem.wifianalyzer.R
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class WiFiStandardTest {

    @Test
    fun testWidth() {
        assertEquals(7, WiFiStandard.entries.size)
    }

    @Test
    fun testNameResource() {
        assertEquals(R.string.wifi_standard_unknown, WiFiStandard.UNKNOWN.textResource)
        assertEquals(R.string.wifi_standard_legacy, WiFiStandard.LEGACY.textResource)
        assertEquals(R.string.wifi_standard_n, WiFiStandard.N.textResource)
        assertEquals(R.string.wifi_standard_ac, WiFiStandard.AC.textResource)
        assertEquals(R.string.wifi_standard_ax, WiFiStandard.AX.textResource)
        assertEquals(R.string.wifi_standard_ad, WiFiStandard.AD.textResource)
        assertEquals(R.string.wifi_standard_be, WiFiStandard.BE.textResource)
    }

    @Test
    fun testImageResource() {
        assertEquals(R.drawable.ic_wifi_unknown, WiFiStandard.UNKNOWN.imageResource)
        assertEquals(R.drawable.ic_wifi_legacy, WiFiStandard.LEGACY.imageResource)
        assertEquals(R.drawable.ic_wifi_4, WiFiStandard.N.imageResource)
        assertEquals(R.drawable.ic_wifi_5, WiFiStandard.AC.imageResource)
        assertEquals(R.drawable.ic_wifi_6, WiFiStandard.AX.imageResource)
        assertEquals(R.drawable.ic_wifi_unknown, WiFiStandard.AD.imageResource)
        assertEquals(R.drawable.ic_wifi_unknown, WiFiStandard.BE.imageResource)
    }

    @Test
    fun testWiFIStandard() {
        assertEquals(ScanResult.WIFI_STANDARD_UNKNOWN, WiFiStandard.UNKNOWN.wiFiStandardId)
        assertEquals(ScanResult.WIFI_STANDARD_LEGACY, WiFiStandard.LEGACY.wiFiStandardId)
        assertEquals(ScanResult.WIFI_STANDARD_11N, WiFiStandard.N.wiFiStandardId)
        assertEquals(ScanResult.WIFI_STANDARD_11AC, WiFiStandard.AC.wiFiStandardId)
        assertEquals(ScanResult.WIFI_STANDARD_11AX, WiFiStandard.AX.wiFiStandardId)
        assertEquals(ScanResult.WIFI_STANDARD_11AD, WiFiStandard.AD.wiFiStandardId)
        assertEquals(ScanResult.WIFI_STANDARD_11BE, WiFiStandard.BE.wiFiStandardId)
    }

    @Config(sdk = [Build.VERSION_CODES.Q])
    @Test
    fun testFindOneLegacy() {
        // setup
        val scanResult: ScanResult = mock()
        // execute
        val actual = WiFiStandard.findOne(scanResult)
        // validate
        assertEquals(WiFiStandard.UNKNOWN, actual)
        verifyNoMoreInteractions(scanResult)
    }

    @Test
    fun testFindOne() {
        withTestDatas().forEach {
            println(it)
            validate(it.expected, it.input)
        }
    }

    private fun validate(expected: WiFiStandard, wiFiStandardId: Int) {
        // setup
        val scanResult: ScanResult = mock()
        doReturn(wiFiStandardId).whenever(scanResult).wifiStandard
        // execute
        val actual = WiFiStandard.findOne(scanResult)
        // validate
        assertEquals(expected, actual)
        verify(scanResult).wifiStandard
        verifyNoMoreInteractions(scanResult)
    }

    private data class TestData(val expected: WiFiStandard, val input: Int)

    private fun withTestDatas() =
        listOf(
            TestData(WiFiStandard.UNKNOWN, ScanResult.WIFI_STANDARD_UNKNOWN),
            TestData(WiFiStandard.LEGACY, ScanResult.WIFI_STANDARD_LEGACY),
            TestData(WiFiStandard.N, ScanResult.WIFI_STANDARD_11N),
            TestData(WiFiStandard.AC, ScanResult.WIFI_STANDARD_11AC),
            TestData(WiFiStandard.AX, ScanResult.WIFI_STANDARD_11AX),
            TestData(WiFiStandard.AD, ScanResult.WIFI_STANDARD_11AD),
            TestData(WiFiStandard.BE, ScanResult.WIFI_STANDARD_11BE),
            TestData(WiFiStandard.UNKNOWN, ScanResult.WIFI_STANDARD_UNKNOWN - 1),
            TestData(WiFiStandard.UNKNOWN, ScanResult.WIFI_STANDARD_11BE + 1)
        )

}