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
package com.vrem.wifianalyzer.wifi.manager

import android.net.wifi.WifiManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.*

class WiFiSwitchTest {
    private val wifiManager: WifiManager = mock()
    private val fixture = spy(WiFiSwitch(wifiManager))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(wifiManager)
    }

    @Suppress("DEPRECATION")
    @Test
    fun on() {
        // setup
        whenever(wifiManager.setWifiEnabled(true)).thenReturn(true)
        // execute
        val actual = fixture.on()
        // validate
        assertThat(actual).isTrue()
        verify(wifiManager).isWifiEnabled = true
    }

    @Suppress("DEPRECATION")
    @Test
    fun off() {
        // setup
        whenever(wifiManager.setWifiEnabled(false)).thenReturn(true)
        // execute
        val actual = fixture.off()
        // validate
        assertThat(actual).isTrue()
        verify(wifiManager).isWifiEnabled = false
    }

    @Test
    fun onWithAndroidQ() {
        // setup
        doReturn(true).whenever(fixture).minVersionQ()
        doNothing().whenever(fixture).startWiFiSettings()
        // execute
        val actual = fixture.on()
        // validate
        assertThat(actual).isTrue()
        verify(fixture).startWiFiSettings()
        verify(fixture).minVersionQ()
    }
}