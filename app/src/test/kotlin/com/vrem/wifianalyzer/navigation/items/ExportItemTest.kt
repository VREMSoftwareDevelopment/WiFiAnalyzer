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
package com.vrem.wifianalyzer.navigation.items

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.view.MenuItem
import android.view.View
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContextMockkHelper
import com.vrem.wifianalyzer.export.Export
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.wifi.model.WiFiConnection
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ExportItemTest {
    private val export: Export = mockk()
    private val mainActivity: MainActivity = mockk()
    private val menuItem: MenuItem = mockk()
    private val intent: Intent = mockk()
    private val packageManager: PackageManager = mockk()
    private val componentName: ComponentName = mockk()
    private val scanner = MainContextMockkHelper.INSTANCE.scannerService

    private val fixture = ExportItem(export)

    @After
    fun tearDown() {
        confirmVerified(export)
        confirmVerified(mainActivity)
        confirmVerified(menuItem)
        confirmVerified(intent)
        confirmVerified(packageManager)
        confirmVerified(componentName)
        confirmVerified(scanner)
        MainContextMockkHelper.INSTANCE.restore()
    }

    @Test
    fun testActivate() {
        // setup
        val wiFiData: WiFiData = withWiFiData()
        every { scanner.wiFiData() } returns wiFiData
        every { export.export(mainActivity, wiFiData.wiFiDetails) } returns intent
        every { mainActivity.startActivity(intent) } just runs
        every { mainActivity.packageManager } returns packageManager
        every { intent.resolveActivity(packageManager) } returns componentName
        // execute
        fixture.activate(mainActivity, menuItem, NavigationMenu.EXPORT)
        // validate
        verify { scanner.wiFiData() }
        verify { mainActivity.packageManager }
        verify { intent.resolveActivity(packageManager) }
        verify { mainActivity.startActivity(intent) }
        verify { export.export(mainActivity, wiFiData.wiFiDetails) }
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