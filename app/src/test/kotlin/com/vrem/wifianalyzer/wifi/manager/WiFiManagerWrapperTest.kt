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
package com.vrem.wifianalyzer.wifi.manager

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.*

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
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        // execute
        val actual = fixture.wiFiEnabled()
        // validate
        assertThat(actual).isTrue()
        verify(wifiManager).isWifiEnabled
    }

    @Test
    fun wiFiEnabledWithException() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenThrow(RuntimeException())
        // execute
        val actual = fixture.wiFiEnabled()
        // validate
        assertThat(actual).isFalse()
        verify(wifiManager).isWifiEnabled
    }

    @Test
    fun enableWiFi() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        // execute
        val actual = fixture.enableWiFi()
        // validate
        assertThat(actual).isTrue()
        verify(wifiManager).isWifiEnabled
    }

    @Test
    fun enableWiFiWhenDisabled() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(false)
        whenever(wiFiSwitch.on()).thenReturn(true)
        // execute
        val actual = fixture.enableWiFi()
        // validate
        assertThat(actual).isTrue()
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).on()
    }

    @Test
    fun enableWiFiWithException() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(false)
        whenever(wiFiSwitch.on()).thenThrow(RuntimeException())
        // execute
        val actual = fixture.enableWiFi()
        // validate
        assertThat(actual).isFalse()
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).on()
    }

    @Test
    fun disableWiFi() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        whenever(wiFiSwitch.off()).thenReturn(true)
        // execute
        val actual = fixture.disableWiFi()
        // validate
        assertThat(actual).isTrue()
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).off()
    }

    @Test
    fun disableWiFiWhenDisabled() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(false)
        // execute
        val actual = fixture.disableWiFi()
        // validate
        assertThat(actual).isTrue()
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch, never()).off()
    }

    @Test
    fun disableWiFiWithException() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        whenever(wiFiSwitch.off()).thenThrow(RuntimeException())
        // execute
        val actual = fixture.disableWiFi()
        // validate
        assertThat(actual).isFalse()
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).off()
    }

    @Suppress("DEPRECATION")
    @Test
    fun startScan() {
        // setup
        whenever(wifiManager.startScan()).thenReturn(true)
        // execute
        val actual = fixture.startScan()
        // validate
        assertThat(actual).isTrue()
        verify(wifiManager).startScan()
    }

    @Suppress("DEPRECATION")
    @Test
    fun startScanWithException() {
        // setup
        whenever(wifiManager.startScan()).thenThrow(RuntimeException())
        // execute
        val actual = fixture.startScan()
        // validate
        assertThat(actual).isFalse()
        verify(wifiManager).startScan()
    }

    @Test
    fun scanResults() {
        // setup
        val expected = listOf<ScanResult>()
        whenever(wifiManager.scanResults).thenReturn(expected)
        // execute
        val actual = fixture.scanResults()
        // validate
        assertThat(actual).isSameAs(expected)
        verify(wifiManager).scanResults
    }

    @Test
    fun scanResultsWhenWiFiManagerReturnsNullScanResults() {
        // setup
        whenever(wifiManager.scanResults).thenReturn(null)
        // execute
        val actual = fixture.scanResults()
        // validate
        assertThat(actual).isNotNull()
        assertThat(actual).isEmpty()
        verify(wifiManager).scanResults
    }

    @Test
    fun scanResultsWithException() {
        // setup
        whenever(wifiManager.scanResults).thenThrow(RuntimeException())
        // execute
        val actual = fixture.scanResults()
        // validate
        assertThat(actual).isNotNull()
        assertThat(actual).isEmpty()
        verify(wifiManager).scanResults
    }

    @Suppress("DEPRECATION")
    @Test
    fun wiFiInfo() {
        // setup
        whenever(wifiManager.connectionInfo).thenReturn(wifiInfo)
        // execute
        val actual = fixture.wiFiInfo()
        // validate
        assertThat(actual).isSameAs(wifiInfo)
        verify(wifiManager).connectionInfo
    }

    @Suppress("DEPRECATION")
    @Test
    fun wiFiInfoWithException() {
        // setup
        whenever(wifiManager.connectionInfo).thenThrow(RuntimeException())
        // execute
        val actual = fixture.wiFiInfo()
        // validate
        assertThat(actual).isNull()
        verify(wifiManager).connectionInfo
    }

    @Test
    fun is5GHzBandSupported() {
        // setup
        whenever(wifiManager.is5GHzBandSupported).thenReturn(true)
        // execute
        val actual = fixture.is5GHzBandSupported()
        // validate
        assertThat(actual).isTrue()
        verify(wifiManager).is5GHzBandSupported
    }

    @Test
    fun is6GHzBandSupported() {
        // setup
        doReturn(false).whenever(fixture).minVersionR()
        // execute
        val actual = fixture.is6GHzBandSupported()
        // validate
        assertThat(actual).isFalse()
        verify(wifiManager, never()).is6GHzBandSupported
        verify(fixture).minVersionR()
    }

    @Test
    fun is6GHzBandSupportedWithAndroidR() {
        // setup
        doReturn(true).whenever(fixture).minVersionR()
        whenever(wifiManager.is6GHzBandSupported).thenReturn(true)
        // execute
        val actual = fixture.is6GHzBandSupported()
        // validate
        assertThat(actual).isTrue()
        verify(wifiManager).is6GHzBandSupported
        verify(fixture).minVersionR()
    }

    @Test
    fun isScanThrottleEnabledSupported() {
        // setup
        doReturn(false).whenever(fixture).minVersionR()
        // execute
        val actual = fixture.isScanThrottleEnabled()
        // validate
        assertThat(actual).isFalse()
        verify(wifiManager, never()).isScanThrottleEnabled
        verify(fixture).minVersionR()
    }

    @Test
    fun isScanThrottleEnabledSupportedWithAndroidR() {
        // setup
        doReturn(true).whenever(fixture).minVersionR()
        whenever(wifiManager.isScanThrottleEnabled).thenReturn(true)
        // execute
        val actual = fixture.isScanThrottleEnabled()
        // validate
        assertThat(actual).isTrue()
        verify(wifiManager).isScanThrottleEnabled
        verify(fixture).minVersionR()
    }

}