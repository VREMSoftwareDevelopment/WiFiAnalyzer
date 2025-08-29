/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2025 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters
import org.robolectric.annotation.Config

@RunWith(ParameterizedRobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class WiFiStandardParameterizedTest(
    val expected: WiFiStandard,
    val input: Int,
) {
    @Test
    fun findOne() {
        // setup
        val scanResult: ScanResult = mock()
        doReturn(input).whenever(scanResult).wifiStandard
        // execute
        val actual = WiFiStandard.findOne(scanResult)
        // validate
        assertThat(actual).isEqualTo(expected)
        verify(scanResult).wifiStandard
        verifyNoMoreInteractions(scanResult)
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{index}: {0} -> {1}")
        fun data(): List<Array<Any>> =
            listOf(
                arrayOf(WiFiStandard.UNKNOWN, ScanResult.WIFI_STANDARD_UNKNOWN),
                arrayOf(WiFiStandard.LEGACY, ScanResult.WIFI_STANDARD_LEGACY),
                arrayOf(WiFiStandard.N, ScanResult.WIFI_STANDARD_11N),
                arrayOf(WiFiStandard.AC, ScanResult.WIFI_STANDARD_11AC),
                arrayOf(WiFiStandard.AX, ScanResult.WIFI_STANDARD_11AX),
                arrayOf(WiFiStandard.AD, ScanResult.WIFI_STANDARD_11AD),
                arrayOf(WiFiStandard.BE, ScanResult.WIFI_STANDARD_11BE),
                arrayOf(WiFiStandard.UNKNOWN, ScanResult.WIFI_STANDARD_UNKNOWN - 1),
                arrayOf(WiFiStandard.UNKNOWN, ScanResult.WIFI_STANDARD_11BE + 1),
            )
    }
}
