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
package com.vrem.util

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PackageInfoFlags
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.wifi.ScanResult
import android.net.wifi.WifiSsid
import android.os.Build
import android.util.DisplayMetrics
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class CompatUtilsTest {

    private val context: Context = mock()
    private val contextWrapper: ContextWrapper = mock()
    private val resources: Resources = mock()
    private val configuration: Configuration = mock()
    private val displayMetrics: DisplayMetrics = mock()
    private val drawable: Drawable = mock()
    private val packageManager: PackageManager = mock()
    private val packageInfo: PackageInfo = mock()
    private val scanResult: ScanResult = mock()
    private val wifiSsid: WifiSsid = mock()

    private lateinit var newLocale: Locale

    @Before
    fun setUp() {
        newLocale = Locale.US
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(context)
        verifyNoMoreInteractions(contextWrapper)
        verifyNoMoreInteractions(resources)
        verifyNoMoreInteractions(configuration)
        verifyNoMoreInteractions(displayMetrics)
        verifyNoMoreInteractions(drawable)
        verifyNoMoreInteractions(packageManager)
        verifyNoMoreInteractions(packageInfo)
        verifyNoMoreInteractions(scanResult)
        verifyNoMoreInteractions(wifiSsid)
    }

    @Test
    fun testCreateContext() {
        // setup
        whenever(context.resources).thenReturn(resources)
        whenever(resources.configuration).thenReturn(configuration)
        whenever(context.createConfigurationContext(configuration)).thenReturn(contextWrapper)
        whenever(contextWrapper.baseContext).thenReturn(context)
        // execute
        val actual: Context = context.createContext(newLocale)
        // validate
        assertEquals(contextWrapper, actual)
        assertEquals(context, (actual as ContextWrapper).baseContext)
        verify(configuration).setLocale(newLocale)
        verify(context).createConfigurationContext(configuration)
        verify(context).resources
        verify(contextWrapper).baseContext
        verify(resources).configuration
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    @Suppress("DEPRECATION")
    fun testCreateContextLegacy() {
        // setup
        whenever(context.resources).thenReturn(resources)
        whenever(resources.configuration).thenReturn(configuration)
        whenever(resources.displayMetrics).thenReturn(displayMetrics)
        // execute
        val actual: Context = context.createContext(newLocale)
        // validate
        assertEquals(context, actual)
        assertEquals(newLocale, configuration.locale)
        verify(resources).displayMetrics
        verify(resources).updateConfiguration(configuration, displayMetrics)
        verify(context).resources
        verify(resources).configuration
    }

    @Test
    fun testContextPackageInfo() {
        // setup
        val packageName = "Package Name"
        whenever(context.packageManager).thenReturn(packageManager)
        whenever(context.packageName).thenReturn(packageName)
        whenever(packageManager.getPackageInfo(eq(packageName), any<PackageInfoFlags>())).thenReturn(packageInfo)
        // execute
        val actual = context.packageInfo()
        // validate
        assertEquals(packageInfo, actual)
        verify(packageManager).getPackageInfo(eq(packageName), any<PackageInfoFlags>())
        verify(context).packageName
        verify(context).packageManager
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.S_V2])
    @Suppress("DEPRECATION")
    fun testContextPackageInfoLegacy() {
        // setup
        val packageName = "Package Name"
        whenever(context.packageManager).thenReturn(packageManager)
        whenever(context.packageName).thenReturn(packageName)
        whenever(packageManager.getPackageInfo(packageName, 0)).thenReturn(packageInfo)
        // execute
        val actual = context.packageInfo()
        // validate
        assertEquals(packageInfo, actual)
        verify(packageManager).getPackageInfo(packageName, 0)
        verify(context).packageName
        verify(context).packageManager
    }

    @Test
    fun testScanResultSSID() {
        // setup
        val expected = "SSID"
        val ssid = "\"$expected\""
        whenever(scanResult.wifiSsid).thenReturn(wifiSsid)
        whenever(wifiSsid.toString()).thenReturn(ssid)
        // execute
        val actual = scanResult.ssid()
        // validate
        assertEquals(expected, actual)
        verify(scanResult).wifiSsid
    }

    @Test
    fun testScanResultSSIDWhenWifiSsidNull() {
        // setup
        whenever(scanResult.wifiSsid).thenReturn(null)
        // execute
        val actual = scanResult.ssid()
        // validate
        assertEquals(String.EMPTY, actual)
        verify(scanResult).wifiSsid
    }

    @Test
    fun testScanResultSSIDWhenNull() {
        // setup
        whenever(scanResult.wifiSsid).thenReturn(wifiSsid)
        whenever(wifiSsid.toString()).thenReturn(null)
        // execute
        val actual = scanResult.ssid()
        // validate
        assertEquals(String.EMPTY, actual)
        verify(scanResult).wifiSsid
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.S_V2])
    @Suppress("DEPRECATION")
    fun testScanResultSSIDLegacy() {
        // setup
        val expected = "SSID"
        val ssid = "\"$expected\""
        scanResult.SSID = ssid
        // execute
        val actual = scanResult.ssid()
        // validate
        assertEquals(expected, actual)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.S_V2])
    @Suppress("DEPRECATION")
    fun testScanResultSSIDLegacyWhenNull() {
        // setup
        scanResult.SSID = null
        // execute
        val actual = scanResult.ssid()
        // validate
        assertEquals(String.EMPTY, actual)
    }

}