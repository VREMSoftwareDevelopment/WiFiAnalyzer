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
package com.vrem.wifianalyzer.wifi.manager

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import com.nhaarman.mockitokotlin2.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Test

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
    fun testWiFiEnabled() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        // execute
        val actual = fixture.wiFiEnabled()
        // validate
        assertTrue(actual)
        verify(wifiManager).isWifiEnabled
    }

    @Test
    fun testWiFiEnabledWithException() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenThrow(RuntimeException())
        // execute
        val actual = fixture.wiFiEnabled()
        // validate
        assertFalse(actual)
        verify(wifiManager).isWifiEnabled
    }

    @Suppress("DEPRECATION")
    @Test
    fun testEnableWiFi() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        // execute
        val actual = fixture.enableWiFi()
        // validate
        assertTrue(actual)
        verify(wifiManager).isWifiEnabled
        verify(wifiManager, never()).isWifiEnabled = any()
    }

    @Test
    fun testEnableWiFiWhenDisabled() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(false)
        whenever(wiFiSwitch.on()).thenReturn(true)
        // execute
        val actual = fixture.enableWiFi()
        // validate
        assertTrue(actual)
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).on()
    }

    @Test
    fun testEnableWiFiWithException() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(false)
        whenever(wiFiSwitch.on()).thenThrow(RuntimeException())
        // execute
        val actual = fixture.enableWiFi()
        // validate
        assertFalse(actual)
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).on()
    }

    @Test
    fun testDisableWiFi() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        whenever(wiFiSwitch.off()).thenReturn(true)
        // execute
        val actual = fixture.disableWiFi()
        // validate
        assertTrue(actual)
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).off()
    }

    @Test
    fun testDisableWiFiWhenDisabled() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(false)
        // execute
        val actual = fixture.disableWiFi()
        // validate
        assertTrue(actual)
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch, never()).off()
    }

    @Test
    fun testDisableWiFiWithException() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        whenever(wiFiSwitch.off()).thenThrow(RuntimeException())
        // execute
        val actual = fixture.disableWiFi()
        // validate
        assertFalse(actual)
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).off()
    }

    @Suppress("DEPRECATION")
    @Test
    fun testStartScan() {
        // setup
        whenever(wifiManager.startScan()).thenReturn(true)
        // execute
        val actual = fixture.startScan()
        // validate
        assertTrue(actual)
        verify(wifiManager).startScan()
    }

    @Suppress("DEPRECATION")
    @Test
    fun testStartScanWithException() {
        // setup
        whenever(wifiManager.startScan()).thenThrow(RuntimeException())
        // execute
        val actual = fixture.startScan()
        // validate
        assertFalse(actual)
        verify(wifiManager).startScan()
    }

    @Test
    fun testScanResults() {
        // setup
        val expected = listOf<ScanResult>()
        whenever(wifiManager.scanResults).thenReturn(expected)
        // execute
        val actual = fixture.scanResults()
        // validate
        assertSame(expected, actual)
        verify(wifiManager).scanResults
    }

    @Test
    fun testScanResultsWhenWiFiManagerReturnsNullScanResults() {
        // setup
        whenever(wifiManager.scanResults).thenReturn(null)
        // execute
        val actual = fixture.scanResults()
        // validate
        assertNotNull(actual)
        assertTrue(actual.isEmpty())
        verify(wifiManager).scanResults
    }

    @Test
    fun testScanResultsWithException() {
        // setup
        whenever(wifiManager.scanResults).thenThrow(RuntimeException())
        // execute
        val actual = fixture.scanResults()
        // validate
        assertNotNull(actual)
        assertTrue(actual.isEmpty())
        verify(wifiManager).scanResults
    }

    @Test
    fun testWiFiInfo() {
        // setup
        whenever(wifiManager.connectionInfo).thenReturn(wifiInfo)
        // execute
        val actual = fixture.wiFiInfo()
        // validate
        assertSame(wifiInfo, actual)
        verify(wifiManager).connectionInfo
    }

    @Test
    fun testWiFiInfoWithException() {
        // setup
        whenever(wifiManager.connectionInfo).thenThrow(RuntimeException())
        // execute
        val actual = fixture.wiFiInfo()
        // validate
        assertNull(actual)
        verify(wifiManager).connectionInfo
    }

    @Test
    fun testIs5GHzBandSupported() {
        // setup
        doReturn(false).whenever(fixture).minVersionL()
        // execute
        val actual = fixture.is5GHzBandSupported()
        // validate
        assertFalse(actual)
        verify(wifiManager, never()).is5GHzBandSupported
        verify(fixture).minVersionL()
    }

    @Test
    fun testIs5GHzBandSupportedWithAndroidL() {
        // setup
        doReturn(true).whenever(fixture).minVersionL()
        whenever(wifiManager.is5GHzBandSupported).thenReturn(true)
        // execute
        val actual = fixture.is5GHzBandSupported()
        // validate
        assertTrue(actual)
        verify(wifiManager).is5GHzBandSupported
        verify(fixture).minVersionL()
    }

    @Test
    fun testIs6GHzBandSupported() {
        // setup
        doReturn(false).whenever(fixture).minVersionR()
        // execute
        val actual = fixture.is6GHzBandSupported()
        // validate
        assertFalse(actual)
        verify(wifiManager, never()).is6GHzBandSupported
        verify(fixture).minVersionR()
    }

    @Test
    fun testIs6GHzBandSupportedWithAndroidR() {
        // setup
        doReturn(true).whenever(fixture).minVersionR()
        whenever(wifiManager.is6GHzBandSupported).thenReturn(true)
        // execute
        val actual = fixture.is6GHzBandSupported()
        // validate
        assertTrue(actual)
        verify(wifiManager).is6GHzBandSupported
        verify(fixture).minVersionR()
    }

}