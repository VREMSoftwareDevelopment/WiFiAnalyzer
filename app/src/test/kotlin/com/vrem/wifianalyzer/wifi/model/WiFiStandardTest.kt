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
import com.vrem.wifianalyzer.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class WiFiStandardTest {

    @Test
    fun width() {
        assertThat(WiFiStandard.entries).hasSize(7)
    }

    @Test
    fun nameResource() {
        assertThat(WiFiStandard.UNKNOWN.textResource).isEqualTo(R.string.wifi_standard_unknown)
        assertThat(WiFiStandard.LEGACY.textResource).isEqualTo(R.string.wifi_standard_legacy)
        assertThat(WiFiStandard.N.textResource).isEqualTo(R.string.wifi_standard_n)
        assertThat(WiFiStandard.AC.textResource).isEqualTo(R.string.wifi_standard_ac)
        assertThat(WiFiStandard.AX.textResource).isEqualTo(R.string.wifi_standard_ax)
        assertThat(WiFiStandard.AD.textResource).isEqualTo(R.string.wifi_standard_ad)
        assertThat(WiFiStandard.BE.textResource).isEqualTo(R.string.wifi_standard_be)
    }

    @Test
    fun imageResource() {
        assertThat(WiFiStandard.UNKNOWN.imageResource).isEqualTo(R.drawable.ic_wifi_unknown)
        assertThat(WiFiStandard.LEGACY.imageResource).isEqualTo(R.drawable.ic_wifi_legacy)
        assertThat(WiFiStandard.N.imageResource).isEqualTo(R.drawable.ic_wifi_4)
        assertThat(WiFiStandard.AC.imageResource).isEqualTo(R.drawable.ic_wifi_5)
        assertThat(WiFiStandard.AX.imageResource).isEqualTo(R.drawable.ic_wifi_6)
        assertThat(WiFiStandard.AD.imageResource).isEqualTo(R.drawable.ic_wifi_unknown)
        assertThat(WiFiStandard.BE.imageResource).isEqualTo(R.drawable.ic_wifi_unknown)
    }

    @Test
    fun wiFIStandard() {
        assertThat(WiFiStandard.UNKNOWN.wiFiStandardId).isEqualTo(ScanResult.WIFI_STANDARD_UNKNOWN)
        assertThat(WiFiStandard.LEGACY.wiFiStandardId).isEqualTo(ScanResult.WIFI_STANDARD_LEGACY)
        assertThat(WiFiStandard.N.wiFiStandardId).isEqualTo(ScanResult.WIFI_STANDARD_11N)
        assertThat(WiFiStandard.AC.wiFiStandardId).isEqualTo(ScanResult.WIFI_STANDARD_11AC)
        assertThat(WiFiStandard.AX.wiFiStandardId).isEqualTo(ScanResult.WIFI_STANDARD_11AX)
        assertThat(WiFiStandard.AD.wiFiStandardId).isEqualTo(ScanResult.WIFI_STANDARD_11AD)
        assertThat(WiFiStandard.BE.wiFiStandardId).isEqualTo(ScanResult.WIFI_STANDARD_11BE)
    }

    @Config(sdk = [Build.VERSION_CODES.Q])
    @Test
    fun findOneLegacy() {
        // setup
        val scanResult: ScanResult = mock()
        // execute
        val actual = WiFiStandard.findOne(scanResult)
        // validate
        assertThat(actual).isEqualTo(WiFiStandard.UNKNOWN)
        verifyNoMoreInteractions(scanResult)
    }

    @Test
    fun findOne() {
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
        assertThat(actual).isEqualTo(expected)
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