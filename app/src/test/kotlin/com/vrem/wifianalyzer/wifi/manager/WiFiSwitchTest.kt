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

import android.app.Activity
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings.Panel
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class WiFiSwitchTest {
    private val wifiManager: WifiManager = mock()
    private val activity: Activity = mock()
    private val intentArgumentCaptor = argumentCaptor<Intent>()
    private val fixture = spy(WiFiSwitch(wifiManager, activity))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(wifiManager)
        verifyNoMoreInteractions(activity)
    }

    @Test
    fun on() {
        // Arrange
        doReturn(false).whenever(fixture).minVersionQ()
        whenever(wifiManager.setWifiEnabled(true)).thenReturn(true)
        // Act
        val actual = fixture.on()
        // Assert
        assertThat(actual).isTrue
        verify(fixture).minVersionQ()
        verify(wifiManager).isWifiEnabled = true
    }

    @Test
    fun off() {
        // Arrange
        doReturn(false).whenever(fixture).minVersionQ()
        whenever(wifiManager.setWifiEnabled(false)).thenReturn(true)
        // Act
        val actual = fixture.off()
        // Assert
        assertThat(actual).isTrue
        verify(fixture).minVersionQ()
        verify(wifiManager).isWifiEnabled = false
    }

    @Test
    fun startWiFiSettingsStartsWiFiPanelOnInjectedActivity() {
        // Arrange
        doNothing().whenever(activity).startActivity(intentArgumentCaptor.capture())
        // Act
        fixture.startWiFiSettings()
        // Assert
        val intent = intentArgumentCaptor.firstValue
        assertThat(intent.action).isEqualTo(Panel.ACTION_WIFI)
        verify(activity).startActivity(intent)
    }

    @Test
    fun onWithAndroidQ() {
        // Arrange
        doReturn(true).whenever(fixture).minVersionQ()
        doNothing().whenever(fixture).startWiFiSettings()
        // Act
        val actual = fixture.on()
        // Assert
        assertThat(actual).isTrue
        verify(fixture).startWiFiSettings()
        verify(fixture).minVersionQ()
    }
}
