/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.wifi.manager.WiFiManagerWrapper
import org.junit.After
import org.junit.Test

class ScannerCallbackTest {
    private val wiFiManagerWrapper: WiFiManagerWrapper = mock()
    private val cache: Cache = mock()
    private val scanner: Scanner = mock()
    private val wifiInfo: WifiInfo = mock()
    private val scanResults: List<ScanResult> = listOf()
    private val fixture: ScannerCallback = ScannerCallback(wiFiManagerWrapper, cache)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(cache)
        verifyNoMoreInteractions(scanner)
        verifyNoMoreInteractions(wiFiManagerWrapper)
    }

    @Test
    fun testOnSuccess() {
        // setup
        whenever(wiFiManagerWrapper.scanResults()).thenReturn(scanResults)
        whenever(wiFiManagerWrapper.wiFiInfo()).thenReturn(wifiInfo)
        // execute
        fixture.onSuccess()
        // validate
        verify(wiFiManagerWrapper).scanResults()
        verify(wiFiManagerWrapper).wiFiInfo()
        verify(cache).add(scanResults)
        verify(cache).wifiInfo = wifiInfo
    }

}