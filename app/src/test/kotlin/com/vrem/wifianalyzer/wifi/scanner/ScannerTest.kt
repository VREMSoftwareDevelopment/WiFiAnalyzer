/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2026 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class ScannerTest {
    private val settings: Settings = mock()
    private val wiFiManagerWrapper: WiFiManagerWrapper = mock()
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
        // Arrange
        whenever(settings.wiFiOffOnExit()).thenReturn(false)
        // Act
        fixture.stop()
        // Assert
        verify(settings).wiFiOffOnExit()
        verify(wiFiManagerWrapper, never()).disableWiFi()
        verify(periodicScan).stop()
        verify(scanResultsReceiver).unregister()
    }

    @Test
    fun stopWithDisableWiFiOnExit() {
        // Arrange
        whenever(settings.wiFiOffOnExit()).thenReturn(true)
        // Act
        fixture.stop()
        // Assert
        verify(wiFiManagerWrapper).disableWiFi()
        verify(periodicScan).stop()
        verify(scanResultsReceiver).unregister()
        verify(settings).wiFiOffOnExit()
    }

    @Test
    fun pause() {
        // Act
        fixture.pause()
        // Assert
        verify(periodicScan).stop()
        verify(scanResultsReceiver).unregister()
    }

    @Test
    fun resume() {
        // Act
        fixture.resume()
        // Assert
        verify(periodicScan).start()
    }

    @Test
    fun running() {
        // Arrange
        whenever(periodicScan.running).thenReturn(true)
        // Act
        val actual = fixture.running()
        // Assert
        assertThat(actual).isTrue
        verify(periodicScan).running
    }

    @Test
    fun update() {
        // Arrange
        whenever(transformer.transformToWiFiData()).thenReturn(wiFiData)
        whenever(permissionService.enabled()).thenReturn(true)
        // Act
        fixture.update()
        // Assert
        assertThat(fixture.wiFiData().value).isEqualTo(wiFiData)
        verify(wiFiManagerWrapper).enableWiFi()
        verify(permissionService).enabled()
        verify(scanResultsReceiver).register()
        verify(wiFiManagerWrapper).startScan()
        verify(scannerCallback).onSuccess()
        verify(transformer).transformToWiFiData()
    }

    @Test
    fun updateShouldScanResultsOnce() {
        // Arrange
        val expected = 3
        whenever(transformer.transformToWiFiData()).thenReturn(wiFiData)
        whenever(permissionService.enabled()).thenReturn(true)
        // Act
        repeat(expected) {
            fixture.update()
        }
        // Assert
        verify(wiFiManagerWrapper, times(expected)).enableWiFi()
        verify(permissionService, times(expected)).enabled()
        verify(scanResultsReceiver, times(expected)).register()
        verify(wiFiManagerWrapper, times(expected)).startScan()
        verify(scannerCallback).onSuccess()
        verify(transformer, times(expected)).transformToWiFiData()
    }

    @Test
    fun updateWithRequirementPermissionDisabled() {
        // Arrange
        whenever(transformer.transformToWiFiData()).thenReturn(wiFiData)
        whenever(permissionService.enabled()).thenReturn(false)
        // Act
        fixture.update()
        // Assert
        verify(wiFiManagerWrapper).enableWiFi()
        verify(permissionService).enabled()
        verify(scanResultsReceiver, never()).register()
        verify(wiFiManagerWrapper, never()).startScan()
        verify(scannerCallback, never()).onSuccess()
        verify(transformer).transformToWiFiData()
    }

    @Test
    fun toggleWhenRunning() {
        // Arrange
        fixture.periodicScan = periodicScan
        whenever(periodicScan.running).thenReturn(true)
        // Act
        fixture.toggle()
        // Assert
        verify(periodicScan).running
        verify(periodicScan).stop()
    }

    @Test
    fun toggleWhenNotRunning() {
        // Arrange
        fixture.periodicScan = periodicScan
        whenever(periodicScan.running).thenReturn(false)
        // Act
        fixture.toggle()
        // Assert
        verify(periodicScan).running
        verify(periodicScan).start()
    }
}
