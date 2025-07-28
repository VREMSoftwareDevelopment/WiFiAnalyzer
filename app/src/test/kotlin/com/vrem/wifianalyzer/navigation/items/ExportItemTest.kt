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
package com.vrem.wifianalyzer.navigation.items

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.export.Export
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.wifi.model.WiFiConnection
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
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
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
class ExportItemTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val export: Export = mock()
    private val intent: Intent = mock()
    private val componentName: ComponentName = mock()
    private val scanner = MainContextHelper.INSTANCE.scannerService

    private val fixture = ExportItem(export)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(export)
        verifyNoMoreInteractions(componentName)
        verifyNoMoreInteractions(scanner)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun registered() {
        // execute & validate
        assertThat(fixture.registered).isFalse
    }

    @Test
    fun visibility() {
        // execute & validate
        assertThat(fixture.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun activate() {
        // setup
        val wiFiData: WiFiData = withWiFiData()
        doReturn(wiFiData).whenever(scanner).wiFiData()
        doReturn(intent).whenever(export).export(mainActivity, wiFiData.wiFiDetails)
        doReturn(componentName).whenever(intent).resolveActivity(any())
        // execute
        fixture.activate(mainActivity, NavigationMenu.EXPORT)
        // validate
        verify(scanner).wiFiData()
        verify(export).export(mainActivity, wiFiData.wiFiDetails)
        verify(intent).resolveActivity(mainActivity.packageManager)
    }

    @Test
    fun activateWithNoWiFiData() {
        // setup
        val wiFiData = WiFiData(listOf(), WiFiConnection.EMPTY)
        doReturn(wiFiData).whenever(scanner).wiFiData()
        // execute
        fixture.activate(mainActivity, NavigationMenu.EXPORT)
        // validate
        verify(scanner).wiFiData()
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("No Data")
    }

    @Test
    fun activateWithNoExportAvailable() {
        // setup
        val wiFiData: WiFiData = withWiFiData()
        doReturn(wiFiData).whenever(scanner).wiFiData()
        doReturn(intent).whenever(export).export(mainActivity, wiFiData.wiFiDetails)
        doReturn(null).whenever(intent).resolveActivity(any())
        // execute
        fixture.activate(mainActivity, NavigationMenu.EXPORT)
        // validate
        verify(scanner).wiFiData()
        verify(export).export(mainActivity, wiFiData.wiFiDetails)
        verify(intent).resolveActivity(mainActivity.packageManager)
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("Export not available")
    }

    @Test
    fun activateThrowsException() {
        // setup
        val activity = spy(mainActivity)
        val wiFiData: WiFiData = withWiFiData()
        val expected = "error"
        doReturn(wiFiData).whenever(scanner).wiFiData()
        doReturn(intent).whenever(export).export(activity, wiFiData.wiFiDetails)
        doReturn(componentName).whenever(intent).resolveActivity(any())
        doThrow(RuntimeException(expected)).whenever(activity).startActivity(intent)
        // execute
        fixture.activate(activity, NavigationMenu.EXPORT)
        // validate
        verify(scanner).wiFiData()
        verify(export).export(activity, wiFiData.wiFiDetails)
        verify(intent).resolveActivity(activity.packageManager)
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(expected)
    }

    private fun withWiFiData(): WiFiData = WiFiData(listOf(WiFiDetail.EMPTY), WiFiConnection.EMPTY)
}
