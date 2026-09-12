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
package com.vrem.wifianalyzer.navigation.items

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.export.Export
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.wifi.model.WiFiConnection
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.scanner.ScannerService
import kotlinx.coroutines.flow.MutableStateFlow
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class ExportItemTest {
    private val mainActivity = RobolectricUtil.INSTANCE.mainActivity
    private val export: Export = mock()
    private val intent: Intent = mock()
    private val componentName: ComponentName = mock()
    private val scanner: ScannerService = mock()

    private val fixture = ExportItem(export) { scanner }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(export)
        verifyNoMoreInteractions(componentName)
        verifyNoMoreInteractions(scanner)
    }

    @Test
    fun registered() {
        // Act & Assert
        assertThat(fixture.registered).isFalse
    }

    @Test
    fun visibility() {
        // Act & Assert
        assertThat(fixture.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun activate() {
        // Arrange
        val wiFiData = withWiFiData()
        doReturn(MutableStateFlow(wiFiData)).whenever(scanner).wiFiData()
        doReturn(intent).whenever(export).export(mainActivity, wiFiData.wiFiDetails)
        doReturn(componentName).whenever(intent).resolveActivity(mainActivity.packageManager)
        // Act
        fixture.activate(mainActivity, NavigationMenu.EXPORT)
        // Assert
        verify(scanner).wiFiData()
        verify(export).export(mainActivity, wiFiData.wiFiDetails)
        verify(intent).resolveActivity(mainActivity.packageManager)
    }

    @Test
    fun activateWithNoWiFiData() {
        // Arrange
        val wiFiData = WiFiData(listOf(), WiFiConnection.EMPTY)
        doReturn(MutableStateFlow(wiFiData)).whenever(scanner).wiFiData()
        // Act
        fixture.activate(mainActivity, NavigationMenu.EXPORT)
        // Assert
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("No Data")
        verify(scanner).wiFiData()
    }

    @Test
    fun activateWithNoExportAvailable() {
        // Arrange
        val wiFiData = withWiFiData()
        doReturn(MutableStateFlow(wiFiData)).whenever(scanner).wiFiData()
        doReturn(intent).whenever(export).export(mainActivity, wiFiData.wiFiDetails)
        doReturn(null).whenever(intent).resolveActivity(mainActivity.packageManager)
        // Act
        fixture.activate(mainActivity, NavigationMenu.EXPORT)
        // Assert
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("Export not available")
        verify(scanner).wiFiData()
        verify(export).export(mainActivity, wiFiData.wiFiDetails)
        verify(intent).resolveActivity(mainActivity.packageManager)
    }

    @Test
    fun activateThrowsException() {
        // Arrange
        val activity = spy(mainActivity)
        val packageManager = activity.packageManager
        val wiFiData = withWiFiData()
        val expected = "error"
        doReturn(MutableStateFlow(wiFiData)).whenever(scanner).wiFiData()
        doReturn(intent).whenever(export).export(activity, wiFiData.wiFiDetails)
        doReturn(componentName).whenever(intent).resolveActivity(packageManager)
        doThrow(RuntimeException(expected)).whenever(activity).startActivity(intent)
        // Act
        fixture.activate(activity, NavigationMenu.EXPORT)
        // Assert
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(expected)
        verify(scanner).wiFiData()
        verify(export).export(activity, wiFiData.wiFiDetails)
        verify(intent).resolveActivity(activity.packageManager)
    }

    private fun withWiFiData(): WiFiData = WiFiData(listOf(WiFiDetail.EMPTY), WiFiConnection.EMPTY)
}
