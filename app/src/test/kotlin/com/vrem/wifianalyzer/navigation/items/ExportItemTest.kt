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
package com.vrem.wifianalyzer.navigation.items

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.MenuItem
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.export.Export
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.wifi.model.WiFiConnection
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class ExportItemTest {
    private val export: Export = mock()
    private val mainActivity: MainActivity = mock()
    private val menuItem: MenuItem = mock()
    private val intent: Intent = mock()
    private val packageManager: PackageManager = mock()
    private val componentName: ComponentName = mock()
    private val scanner = MainContextHelper.INSTANCE.scannerService

    private val fixture = ExportItem(export)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(export)
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(menuItem)
        verifyNoMoreInteractions(intent)
        verifyNoMoreInteractions(packageManager)
        verifyNoMoreInteractions(componentName)
        verifyNoMoreInteractions(scanner)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun activate() {
        // setup
        val wiFiData: WiFiData = withWiFiData()
        doReturn(wiFiData).whenever(scanner).wiFiData()
        doReturn(intent).whenever(export).export(mainActivity, wiFiData.wiFiDetails)
        doNothing().whenever(mainActivity).startActivity(intent)
        doReturn(packageManager).whenever(mainActivity).packageManager
        doReturn(componentName).whenever(intent).resolveActivity(packageManager)
        // execute
        fixture.activate(mainActivity, menuItem, NavigationMenu.EXPORT)
        // validate
        verify(scanner).wiFiData()
        verify(mainActivity).packageManager
        verify(intent).resolveActivity(packageManager)
        verify(mainActivity).startActivity(intent)
        verify(export).export(mainActivity, wiFiData.wiFiDetails)
    }

    @Test
    fun registered() {
        // execute & validate
        assertThat(fixture.registered).isFalse()
    }

    @Test
    fun visibility() {
        // execute & validate
        assertThat(fixture.visibility).isEqualTo(View.GONE)
    }

    private fun withWiFiData(): WiFiData {
        return WiFiData(listOf(WiFiDetail.EMPTY), WiFiConnection.EMPTY)
    }

}