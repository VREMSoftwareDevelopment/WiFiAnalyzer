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
package com.vrem.util

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PackageInfoFlags
import android.content.pm.Signature
import android.content.pm.SigningInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.wifi.ScanResult
import android.net.wifi.WifiSsid
import android.os.Build
import android.util.DisplayMetrics
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.util.Locale

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
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
    fun createContext() {
        // Arrange
        whenever(context.resources).thenReturn(resources)
        whenever(resources.configuration).thenReturn(configuration)
        whenever(context.createConfigurationContext(configuration)).thenReturn(contextWrapper)
        whenever(contextWrapper.baseContext).thenReturn(context)
        // Act
        val actual = context.createContext(newLocale)
        // Assert
        assertThat(actual).isEqualTo(contextWrapper)
        assertThat((actual as ContextWrapper).baseContext).isEqualTo(context)
        verify(configuration).setLocale(newLocale)
        verify(context).createConfigurationContext(configuration)
        verify(context).resources
        verify(contextWrapper).baseContext
        verify(resources).configuration
    }

    @Test
    fun contextPackageInfo() {
        // Arrange
        val packageName = "Package Name"
        whenever(context.packageManager).thenReturn(packageManager)
        whenever(context.packageName).thenReturn(packageName)
        whenever(packageManager.getPackageInfo(eq(packageName), any<PackageInfoFlags>())).thenReturn(packageInfo)
        // Act
        val actual = context.packageInfo()
        // Assert
        assertThat(actual).isEqualTo(packageInfo)
        verify(packageManager).getPackageInfo(eq(packageName), any<PackageInfoFlags>())
        verify(context).packageName
        verify(context).packageManager
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.S_V2])
    fun contextPackageInfoLegacy() {
        // Arrange
        val packageName = "Package Name"
        whenever(context.packageManager).thenReturn(packageManager)
        whenever(context.packageName).thenReturn(packageName)
        whenever(packageManager.getPackageInfo(packageName, 0)).thenReturn(packageInfo)
        // Act
        val actual = context.packageInfo()
        // Assert
        assertThat(actual).isEqualTo(packageInfo)
        verify(packageManager).getPackageInfo(packageName, 0)
        verify(context).packageName
        verify(context).packageManager
    }

    @Test
    fun contextSignature() {
        // Arrange
        val packageName = "Package Name"
        packageInfo.signingInfo = signingInfo(Signature("0a0b0c"))
        doReturn(packageManager).whenever(context).packageManager
        doReturn(packageName).whenever(context).packageName
        doReturn(packageInfo)
            .whenever(packageManager)
            .getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        // Act
        val actual = context.signature()
        // Assert
        assertThat(actual).isEqualTo("9909ec")
        verify(packageManager).getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        verify(context).packageName
        verify(context).packageManager
    }

    @Test
    fun contextSignatureWithMultipleSigners() {
        // Arrange
        val packageName = "Package Name"
        packageInfo.signingInfo = signingInfo(Signature("0a0b0c"), Signature("ffffff"))
        doReturn(packageManager).whenever(context).packageManager
        doReturn(packageName).whenever(context).packageName
        doReturn(packageInfo)
            .whenever(packageManager)
            .getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        // Act
        val actual = context.signature()
        // Assert
        assertThat(actual).isEqualTo("9909ec")
        verify(packageManager).getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        verify(context).packageName
        verify(context).packageManager
    }

    @Test
    fun contextSignatureWhenNoSignatures() {
        // Arrange
        val packageName = "Package Name"
        packageInfo.signingInfo = signingInfo()
        doReturn(packageManager).whenever(context).packageManager
        doReturn(packageName).whenever(context).packageName
        doReturn(packageInfo)
            .whenever(packageManager)
            .getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        // Act
        val actual = context.signature()
        // Assert
        assertThat(actual).isEqualTo(String.EMPTY)
        verify(packageManager).getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        verify(context).packageName
        verify(context).packageManager
    }

    @Test
    fun contextSignatureWhenPackageNotFound() {
        // Arrange
        val packageName = "Package Name"
        doReturn(packageManager).whenever(context).packageManager
        doReturn(packageName).whenever(context).packageName
        doThrow(PackageManager.NameNotFoundException())
            .whenever(packageManager)
            .getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        // Act
        val actual = context.signature()
        // Assert
        assertThat(actual).isEqualTo(String.EMPTY)
        verify(packageManager).getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        verify(context).packageName
        verify(context).packageManager
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.O_MR1])
    fun contextSignatureLegacy() {
        // Arrange
        val packageName = "Package Name"
        packageInfo.signatures = arrayOf(Signature("0a0b0c"))
        doReturn(packageManager).whenever(context).packageManager
        doReturn(packageName).whenever(context).packageName
        doReturn(packageInfo).whenever(packageManager).getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        // Act
        val actual = context.signature()
        // Assert
        assertThat(actual).isEqualTo("9909ec")
        verify(packageManager).getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        verify(context).packageName
        verify(context).packageManager
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.O_MR1])
    fun contextSignatureLegacyWhenNoSignatures() {
        // Arrange
        val packageName = "Package Name"
        packageInfo.signatures = arrayOf()
        doReturn(packageManager).whenever(context).packageManager
        doReturn(packageName).whenever(context).packageName
        doReturn(packageInfo).whenever(packageManager).getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        // Act
        val actual = context.signature()
        // Assert
        assertThat(actual).isEqualTo(String.EMPTY)
        verify(packageManager).getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        verify(context).packageName
        verify(context).packageManager
    }

    @Test
    fun scanResultSSID() {
        // Arrange
        val expected = "SSID"
        val ssid = "\"$expected\""
        whenever(scanResult.wifiSsid).thenReturn(wifiSsid)
        whenever(wifiSsid.toString()).thenReturn(ssid)
        // Act
        val actual = scanResult.ssid()
        // Assert
        assertThat(actual).isEqualTo(expected)
        verify(scanResult).wifiSsid
    }

    @Test
    fun scanResultSSIDWhenWifiSsidNull() {
        // Arrange
        whenever(scanResult.wifiSsid).thenReturn(null)
        // Act
        val actual = scanResult.ssid()
        // Assert
        assertThat(actual).isEqualTo(String.EMPTY)
        verify(scanResult).wifiSsid
    }

    @Test
    fun scanResultSSIDWhenNull() {
        // Arrange
        whenever(scanResult.wifiSsid).thenReturn(wifiSsid)
        whenever(wifiSsid.toString()).thenReturn(null)
        // Act
        val actual = scanResult.ssid()
        // Assert
        assertThat(actual).isEqualTo(String.EMPTY)
        verify(scanResult).wifiSsid
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.S_V2])
    fun scanResultSSIDLegacy() {
        // Arrange
        val expected = "SSID"
        val ssid = "\"$expected\""
        scanResult.SSID = ssid
        // Act
        val actual = scanResult.ssid()
        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.S_V2])
    fun scanResultSSIDLegacyWhenNull() {
        // Arrange
        scanResult.SSID = null
        // Act
        val actual = scanResult.ssid()
        // Assert
        assertThat(actual).isEqualTo(String.EMPTY)
    }

    private fun signingInfo(vararg signatures: Signature): SigningInfo =
        SigningInfo().also { shadowOf(it).setSignatures(arrayOf(*signatures)) }
}
