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
package com.vrem.wifianalyzer.wifi.manager

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class WiFiManagerWrapperTest {
    private val wifiManager: WifiManager = mock()
    private val wiFiSwitch: WiFiSwitch = mock()
    private val wifiInfo: WifiInfo = mock()
    private val fixture = spy(WiFiManagerWrapper(wifiManager, wiFiSwitch))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(wifiManager)
        verifyNoMoreInteractions(wiFiSwitch)
    }

    @Test
    fun wiFiEnabled() {
        // Arrange
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        // Act
        val actual = fixture.wiFiEnabled()
        // Assert
        assertThat(actual).isTrue
        verify(wifiManager).isWifiEnabled
    }

    @Test
    fun wiFiEnabledWithException() {
        // Arrange
        whenever(wifiManager.isWifiEnabled).thenThrow(RuntimeException())
        // Act
        val actual = fixture.wiFiEnabled()
        // Assert
        assertThat(actual).isFalse
        verify(wifiManager).isWifiEnabled
    }

    @Test
    fun enableWiFi() {
        // Arrange
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        // Act
        val actual = fixture.enableWiFi()
        // Assert
        assertThat(actual).isTrue
        verify(wifiManager).isWifiEnabled
    }

    @Test
    fun enableWiFiWhenDisabled() {
        // Arrange
        whenever(wifiManager.isWifiEnabled).thenReturn(false)
        whenever(wiFiSwitch.on()).thenReturn(true)
        // Act
        val actual = fixture.enableWiFi()
        // Assert
        assertThat(actual).isTrue
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).on()
    }

    @Test
    fun enableWiFiWhenDisabledAndSwitchFails() {
        // Arrange
        whenever(wifiManager.isWifiEnabled).thenReturn(false)
        whenever(wiFiSwitch.on()).thenReturn(false)
        // Act
        val actual = fixture.enableWiFi()
        // Assert
        assertThat(actual).isFalse
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).on()
    }

    @Test
    fun enableWiFiWithException() {
        // Arrange
        whenever(wifiManager.isWifiEnabled).thenReturn(false)
        whenever(wiFiSwitch.on()).thenThrow(RuntimeException())
        // Act
        val actual = fixture.enableWiFi()
        // Assert
        assertThat(actual).isFalse
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).on()
    }

    @Test
    fun disableWiFi() {
        // Arrange
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        whenever(wiFiSwitch.off()).thenReturn(true)
        // Act
        val actual = fixture.disableWiFi()
        // Assert
        assertThat(actual).isTrue
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).off()
    }

    @Test
    fun disableWiFiWhenEnabledAndSwitchFails() {
        // Arrange
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        whenever(wiFiSwitch.off()).thenReturn(false)
        // Act
        val actual = fixture.disableWiFi()
        // Assert
        assertThat(actual).isFalse
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).off()
    }

    @Test
    fun disableWiFiWhenDisabled() {
        // Arrange
        whenever(wifiManager.isWifiEnabled).thenReturn(false)
        // Act
        val actual = fixture.disableWiFi()
        // Assert
        assertThat(actual).isTrue
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch, never()).off()
    }

    @Test
    fun disableWiFiWithException() {
        // Arrange
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        whenever(wiFiSwitch.off()).thenThrow(RuntimeException())
        // Act
        val actual = fixture.disableWiFi()
        // Assert
        assertThat(actual).isFalse
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).off()
    }

    @Test
    fun startScan() {
        // Arrange
        whenever(wifiManager.startScan()).thenReturn(true)
        // Act
        val actual = fixture.startScan()
        // Assert
        assertThat(actual).isTrue
        verify(wifiManager).startScan()
    }

    @Test
    fun startScanWithException() {
        // Arrange
        whenever(wifiManager.startScan()).thenThrow(RuntimeException())
        // Act
        val actual = fixture.startScan()
        // Assert
        assertThat(actual).isFalse
        verify(wifiManager).startScan()
    }

    @Test
    fun scanResults() {
        // Arrange
        val expected = listOf<ScanResult>()
        whenever(wifiManager.scanResults).thenReturn(expected)
        // Act
        val actual = fixture.scanResults()
        // Assert
        assertThat(actual).isSameAs(expected)
        verify(wifiManager).scanResults
    }

    @Test
    fun scanResultsWhenWiFiManagerReturnsNullScanResults() {
        // Arrange
        whenever(wifiManager.scanResults).thenReturn(null)
        // Act
        val actual = fixture.scanResults()
        // Assert
        assertThat(actual).isNotNull()
        assertThat(actual).isEmpty()
        verify(wifiManager).scanResults
    }

    @Test
    fun scanResultsWithException() {
        // Arrange
        whenever(wifiManager.scanResults).thenThrow(RuntimeException())
        // Act
        val actual = fixture.scanResults()
        // Assert
        assertThat(actual).isNotNull()
        assertThat(actual).isEmpty()
        verify(wifiManager).scanResults
    }

    @Test
    fun wiFiInfo() {
        // Arrange
        whenever(wifiManager.connectionInfo).thenReturn(wifiInfo)
        // Act
        val actual = fixture.wiFiInfo()
        // Assert
        assertThat(actual).isSameAs(wifiInfo)
        verify(wifiManager).connectionInfo
    }

    @Test
    fun wiFiInfoWithException() {
        // Arrange
        whenever(wifiManager.connectionInfo).thenThrow(RuntimeException())
        // Act
        val actual = fixture.wiFiInfo()
        // Assert
        assertThat(actual).isNull()
        verify(wifiManager).connectionInfo
    }

    @Test
    fun is5GHzBandSupported() {
        // Arrange
        whenever(wifiManager.is5GHzBandSupported).thenReturn(true)
        // Act
        val actual = fixture.is5GHzBandSupported()
        // Assert
        assertThat(actual).isTrue
        verify(wifiManager).is5GHzBandSupported
    }

    @Test
    fun is6GHzBandSupported() {
        // Arrange
        doReturn(false).whenever(fixture).minVersionR()
        // Act
        val actual = fixture.is6GHzBandSupported()
        // Assert
        assertThat(actual).isFalse
        verify(wifiManager, never()).is6GHzBandSupported
        verify(fixture).minVersionR()
    }

    @Test
    fun is6GHzBandSupportedWithAndroidR() {
        // Arrange
        doReturn(true).whenever(fixture).minVersionR()
        whenever(wifiManager.is6GHzBandSupported).thenReturn(true)
        // Act
        val actual = fixture.is6GHzBandSupported()
        // Assert
        assertThat(actual).isTrue
        verify(wifiManager).is6GHzBandSupported
        verify(fixture).minVersionR()
    }

    @Test
    fun isScanThrottleEnabledSupported() {
        // Arrange
        doReturn(false).whenever(fixture).minVersionR()
        // Act
        val actual = fixture.isScanThrottleEnabled()
        // Assert
        assertThat(actual).isFalse
        verify(wifiManager, never()).isScanThrottleEnabled
        verify(fixture).minVersionR()
    }

    @Test
    fun isScanThrottleEnabledSupportedWithAndroidR() {
        // Arrange
        doReturn(true).whenever(fixture).minVersionR()
        whenever(wifiManager.isScanThrottleEnabled).thenReturn(true)
        // Act
        val actual = fixture.isScanThrottleEnabled()
        // Assert
        assertThat(actual).isTrue
        verify(wifiManager).isScanThrottleEnabled
        verify(fixture).minVersionR()
    }
}
