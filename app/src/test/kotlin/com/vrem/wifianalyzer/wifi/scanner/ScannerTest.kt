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
package com.vrem.wifianalyzer.wifi.scanner

import com.vrem.wifianalyzer.permission.PermissionService
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.manager.WiFiManagerWrapper
import com.vrem.wifianalyzer.wifi.model.WiFiData
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

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
    fun stop() {
        // setup
        whenever(settings.wiFiOffOnExit()).thenReturn(false)
        // execute
        fixture.stop()
        // validate
        assertThat(fixture.registered()).isEqualTo(0)
        verify(settings).wiFiOffOnExit()
        verify(wiFiManagerWrapper, never()).disableWiFi()
        verify(periodicScan).stop()
        verify(scanResultsReceiver).unregister()
    }

    @Test
    fun stopWithDisableWiFiOnExit() {
        // setup
        whenever(settings.wiFiOffOnExit()).thenReturn(true)
        // execute
        fixture.stop()
        // validate
        assertThat(fixture.registered()).isEqualTo(0)
        verify(wiFiManagerWrapper).disableWiFi()
        verify(periodicScan).stop()
        verify(scanResultsReceiver).unregister()
        verify(settings).wiFiOffOnExit()
    }

    @Test
    fun pause() {
        // execute
        fixture.pause()
        // validate
        verify(periodicScan).stop()
        verify(scanResultsReceiver).unregister()
    }

    @Test
    fun resume() {
        // execute
        fixture.resume()
        // validate
        verify(periodicScan).start()
    }

    @Test
    fun running() {
        // setup
        whenever(periodicScan.running).thenReturn(true)
        // execute
        val actual = fixture.running()
        // validate
        assertThat(actual).isTrue()
        verify(periodicScan).running
    }

    @Test
    fun register() {
        // setup
        assertThat(fixture.registered()).isEqualTo(3)
        // execute
        fixture.register(updateNotifier2)
        // validate
        assertThat(fixture.registered()).isEqualTo(4)
    }

    @Test
    fun unregister() {
        // setup
        assertThat(fixture.registered()).isEqualTo(3)
        // execute
        fixture.unregister(updateNotifier2)
        // validate
        assertThat(fixture.registered()).isEqualTo(2)
    }

    @Test
    fun update() {
        // setup
        whenever(transformer.transformToWiFiData()).thenReturn(wiFiData)
        whenever(permissionService.enabled()).thenReturn(true)
        // execute
        fixture.update()
        // validate
        assertThat(fixture.wiFiData()).isEqualTo(wiFiData)
        verify(wiFiManagerWrapper).enableWiFi()
        verify(permissionService).enabled()
        verify(scanResultsReceiver).register()
        verify(wiFiManagerWrapper).startScan()
        verify(scannerCallback).onSuccess()
        verify(transformer).transformToWiFiData()
        verifyUpdateNotifier(1)
    }

    @Test
    fun updateShouldScanResultsOnce() {
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
    fun updateWithRequirementPermissionDisabled() {
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
    fun toggleWhenRunning() {
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
    fun toggleWhenNotRunning() {
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