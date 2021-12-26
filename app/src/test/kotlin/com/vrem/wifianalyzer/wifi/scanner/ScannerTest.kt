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

import com.nhaarman.mockitokotlin2.*
import com.vrem.wifianalyzer.permission.PermissionService
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.manager.WiFiManagerWrapper
import com.vrem.wifianalyzer.wifi.model.WiFiData
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ScannerTest {
    private val settings: Settings = mock()
    private val wiFiManagerWrapper: WiFiManagerWrapper = mock()
    private val updateNotifier1: UpdateNotifier = mock()
    private val updateNotifier2: UpdateNotifier = mock()
    private val updateNotifier3: UpdateNotifier = mock()
    private val transformer: Transformer = mock()
    private val scanResultsReceiver: ScanResultsReceiver = mock()
    private val scannerCallback: ScannerCallback = mock()
    private val permissionService: PermissionService = mock()
    private val wiFiData: WiFiData = mock()
    private val periodicScan: PeriodicScan = mock()
    private val fixture = Scanner(wiFiManagerWrapper, settings, permissionService, transformer)

    @Before
    fun setUp() {
        fixture.periodicScan = periodicScan
        fixture.scanResultsReceiver = scanResultsReceiver
        fixture.scannerCallback = scannerCallback

        fixture.register(updateNotifier1)
        fixture.register(updateNotifier2)
        fixture.register(updateNotifier3)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(settings)
        verifyNoMoreInteractions(wiFiManagerWrapper)
        verifyNoMoreInteractions(transformer)
        verifyNoMoreInteractions(periodicScan)
        verifyNoMoreInteractions(permissionService)
        verifyNoMoreInteractions(scanResultsReceiver)
        verifyNoMoreInteractions(scannerCallback)
    }

    @Test
    fun testStop() {
        // setup
        whenever(settings.wiFiOffOnExit()).thenReturn(false)
        // execute
        fixture.stop()
        // validate
        assertEquals(0, fixture.registered())
        verify(settings).wiFiOffOnExit()
        verify(wiFiManagerWrapper, never()).disableWiFi()
        verify(periodicScan).stop()
        verify(scanResultsReceiver).unregister()
    }

    @Test
    fun testStopWithDisableWiFiOnExit() {
        // setup
        whenever(settings.wiFiOffOnExit()).thenReturn(true)
        // execute
        fixture.stop()
        // validate
        assertEquals(0, fixture.registered())
        verify(wiFiManagerWrapper).disableWiFi()
        verify(periodicScan).stop()
        verify(scanResultsReceiver).unregister()
        verify(settings).wiFiOffOnExit()
    }

    @Test
    fun testPause() {
        // execute
        fixture.pause()
        // validate
        verify(periodicScan).stop()
        verify(scanResultsReceiver).unregister()
    }

    @Test
    fun testResume() {
        // execute
        fixture.resume()
        // validate
        verify(periodicScan).start()
    }

    @Test
    fun testRunning() {
        // setup
        whenever(periodicScan.running).thenReturn(true)
        // execute
        val actual = fixture.running()
        // validate
        assertTrue(actual)
        verify(periodicScan).running
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
        whenever(transformer.transformToWiFiData()).thenReturn(wiFiData)
        whenever(permissionService.enabled()).thenReturn(true)
        // execute
        fixture.update()
        // validate
        assertEquals(wiFiData, fixture.wiFiData())
        verify(wiFiManagerWrapper).enableWiFi()
        verify(permissionService).enabled()
        verify(scanResultsReceiver).register()
        verify(wiFiManagerWrapper).startScan()
        verify(scannerCallback).onSuccess()
        verify(transformer).transformToWiFiData()
        verifyUpdateNotifier(1)
    }

    @Test
    fun testUpdateShouldScanResultsOnce() {
        // setup
        val expected = 3
        whenever(transformer.transformToWiFiData()).thenReturn(wiFiData)
        whenever(permissionService.enabled()).thenReturn(true)
        // execute
        for (i in 0 until expected) {
            fixture.update()
        }
        // validate
        verify(wiFiManagerWrapper, times(expected)).enableWiFi()
        verify(permissionService, times(expected)).enabled()
        verify(scanResultsReceiver, times(expected)).register()
        verify(wiFiManagerWrapper, times(expected)).startScan()
        verify(scannerCallback).onSuccess()
        verify(transformer, times(expected)).transformToWiFiData()
        verifyUpdateNotifier(expected)
    }

    @Test
    fun testUpdateWithRequirementPermissionDisabled() {
        // setup
        whenever(transformer.transformToWiFiData()).thenReturn(wiFiData)
        whenever(permissionService.enabled()).thenReturn(false)
        // execute
        fixture.update()
        // validate
        verify(wiFiManagerWrapper).enableWiFi()
        verify(permissionService).enabled()
        verify(scanResultsReceiver, never()).register()
        verify(wiFiManagerWrapper, never()).startScan()
        verify(scannerCallback, never()).onSuccess()
        verify(transformer).transformToWiFiData()
        verifyUpdateNotifier(1)
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

    private fun verifyUpdateNotifier(expected: Int) {
        verify(updateNotifier1, times(expected)).update(wiFiData)
        verify(updateNotifier2, times(expected)).update(wiFiData)
        verify(updateNotifier3, times(expected)).update(wiFiData)
    }

}