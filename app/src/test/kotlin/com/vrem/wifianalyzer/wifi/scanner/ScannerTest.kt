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
package com.vrem.wifianalyzer.wifi.scanner

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import com.nhaarman.mockitokotlin2.*
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.manager.WiFiManagerWrapper
import com.vrem.wifianalyzer.wifi.model.WiFiData
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class ScannerTest {
    private val settings: Settings = mock()
    private val wiFiManagerWrapper: WiFiManagerWrapper = mock()
    private val updateNotifier1: UpdateNotifier = mock()
    private val updateNotifier2: UpdateNotifier = mock()
    private val updateNotifier3: UpdateNotifier = mock()
    private val wifiInfo: WifiInfo = mock()
    private val cache: Cache = mock()
    private val transformer: Transformer = mock()
    private val wiFiData: WiFiData = mock()
    private val periodicScan: PeriodicScan = mock()
    private val scanResults: List<ScanResult> = listOf()
    private val cacheResults: List<CacheResult> = listOf()
    private val fixture = Scanner(wiFiManagerWrapper, settings, cache, transformer)

    @Before
    fun setUp() {
        fixture.periodicScan = periodicScan
        fixture.register(updateNotifier1)
        fixture.register(updateNotifier2)
        fixture.register(updateNotifier3)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(settings)
        verifyNoMoreInteractions(wiFiManagerWrapper)
        verifyNoMoreInteractions(cache)
        verifyNoMoreInteractions(transformer)
        verifyNoMoreInteractions(periodicScan)
    }

    @Test
    fun testPeriodicScanIsSet() {
        assertNotNull(fixture.periodicScan)
    }

    @Test
    fun testRegister() {
        // setup
        assertEquals(3, fixture.registered())
        // execute
        fixture.register(updateNotifier2)
        // validate
        assertEquals(4, fixture.registered())
    }

    @Test
    fun testUnregister() {
        // setup
        assertEquals(3, fixture.registered())
        // execute
        fixture.unregister(updateNotifier2)
        // validate
        assertEquals(2, fixture.registered())
    }

    @Test
    fun testUpdate() {
        // setup
        withCache()
        withTransformer()
        withWiFiManagerWrapper()
        // execute
        fixture.update()
        // validate
        assertEquals(wiFiData, fixture.wiFiData())
        verifyCache()
        verifyTransformer()
        verifyWiFiManagerWrapper()
        verify(updateNotifier1).update(wiFiData)
        verify(updateNotifier2).update(wiFiData)
        verify(updateNotifier3).update(wiFiData)
    }

    @Test
    fun testStopWithIsWiFiOffOnExitTurnsOffWiFi() {
        // setup
        whenever(settings.wiFiOffOnExit()).thenReturn(true)
        // execute
        fixture.stop()
        // validate
        verify(settings).wiFiOffOnExit()
        verify(wiFiManagerWrapper).disableWiFi()
    }

    @Test
    fun testStopDoesNotTurnsOffWiFi() {
        // setup
        whenever(settings.wiFiOffOnExit()).thenReturn(false)
        // execute
        fixture.stop()
        // validate
        verify(settings).wiFiOffOnExit()
        verify(wiFiManagerWrapper, never()).enableWiFi()
    }

    private fun withCache() {
        whenever(cache.scanResults()).thenReturn(cacheResults)
        whenever(cache.wifiInfo()).thenReturn(wifiInfo)
    }

    private fun withTransformer() {
        whenever(transformer.transformToWiFiData(cacheResults, wifiInfo)).thenReturn(wiFiData)
    }

    private fun verifyCache() {
        verify(cache).add(scanResults, wifiInfo)
        verify(cache).scanResults()
        verify(cache).wifiInfo()
    }

    private fun verifyWiFiManagerWrapper() {
        verify(wiFiManagerWrapper).enableWiFi()
        verify(wiFiManagerWrapper).startScan()
        verify(wiFiManagerWrapper).scanResults()
        verify(wiFiManagerWrapper).wiFiInfo()
        verifyWiFiManagerStartScan()
    }

    private fun withWiFiManagerWrapper() {
        whenever(wiFiManagerWrapper.startScan()).thenReturn(true)
        whenever(wiFiManagerWrapper.scanResults()).thenReturn(scanResults)
        whenever(wiFiManagerWrapper.wiFiInfo()).thenReturn(wifiInfo)
        withWiFiManagerStartScan()
    }

    private fun verifyWiFiManagerStartScan() {
        verify(wiFiManagerWrapper).startScan()
    }

    private fun withWiFiManagerStartScan() {
        whenever(wiFiManagerWrapper.startScan()).thenReturn(true)
    }

    private fun verifyTransformer() {
        verify(transformer).transformToWiFiData(cacheResults, wifiInfo)
    }

    @Test
    fun testPause() {
        // setup
        fixture.periodicScan = periodicScan
        // execute
        fixture.pause()
        // validate
        verify(periodicScan).stop()
    }

    @Test
    fun testResume() {
        // setup
        fixture.periodicScan = periodicScan
        // execute
        fixture.resume()
        // validate
        verify(periodicScan).start()
    }

    @Test
    fun testToggleWhenRunning() {
        // setup
        fixture.periodicScan = periodicScan
        whenever(periodicScan.running).thenReturn(true)
        // execute
        fixture.toggle()
        // validate
        verify(periodicScan).running
        verify(periodicScan).stop()
    }

    @Test
    fun testToggleWhenNotRunning() {
        // setup
        fixture.periodicScan = periodicScan
        whenever(periodicScan.running).thenReturn(false)
        // execute
        fixture.toggle()
        // validate
        verify(periodicScan).running
        verify(periodicScan).start()
    }
}