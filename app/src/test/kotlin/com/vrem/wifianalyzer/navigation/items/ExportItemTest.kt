/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.view.MenuItem
import android.view.View
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.export.Export
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.wifi.model.WiFiConnection
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.mockito.Mockito.*

class ExportItemTest {
    private val export = mock(Export::class.java)
    private val mainActivity = mock(MainActivity::class.java)
    private val menuItem = mock(MenuItem::class.java)
    private val intent = mock(Intent::class.java)
    private val packageManager = mock(PackageManager::class.java)
    private val componentName = mock(ComponentName::class.java)
    private val scanner = INSTANCE.scannerService

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
        INSTANCE.restore()
    }

    @Test
    fun testActivate() {
        // setup
        val wiFiData: WiFiData = withWiFiData()
        whenever(scanner.wiFiData).thenReturn(wiFiData)
        whenever(export.export(mainActivity, wiFiData.wiFiDetails)).thenReturn(intent)
        whenever(mainActivity.packageManager).thenReturn(packageManager)
        whenever(intent.resolveActivity(packageManager)).thenReturn(componentName)
        // execute
        fixture.activate(mainActivity, menuItem, NavigationMenu.EXPORT)
        // validate
        verify(scanner).wiFiData
        verify(mainActivity).packageManager
        verify(intent).resolveActivity(packageManager)
        verify(mainActivity).startActivity(intent)
        verify(export).export(mainActivity, wiFiData.wiFiDetails)
    }

    @Test
    fun testRegistered() {
        // execute & validate
        assertFalse(fixture.registered)
    }

    @Test
    fun testVisibility() {
        // execute & validate
        assertEquals(View.GONE, fixture.visibility)
    }

    private fun withWiFiData(): WiFiData {
        return WiFiData(listOf(WiFiDetail.EMPTY), WiFiConnection.EMPTY)
    }

}