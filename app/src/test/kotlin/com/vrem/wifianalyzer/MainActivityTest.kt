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
package com.vrem.wifianalyzer

import android.content.SharedPreferences
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.navigation.NavigationView
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.navigation.NavigationMenuController
import com.vrem.wifianalyzer.navigation.options.OptionMenu
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class MainActivityTest {
    private val fixture = Robolectric.buildActivity(MainActivity::class.java).create().resume().get()

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testMainActivity() {
        assertFalse(MainContext.INSTANCE.scannerService.running())
    }

    @Test
    fun testOnPauseWillPauseScanner() {
        // setup
        val scannerService = MainContextHelper.INSTANCE.scannerService
        // execute
        fixture.onPause()
        // validate
        verify(scannerService).pause()
        verify(scannerService).unregister(fixture.connectionView)
    }

    @Test
    fun testOnResumeWithPermissionGrantedWillResumeScanner() {
        // setup
        val permissionService = MainContextHelper.INSTANCE.permissionService
        val scannerService = MainContextHelper.INSTANCE.scannerService
        whenever(permissionService.permissionGranted()).thenReturn(true)
        whenever(permissionService.locationEnabled()).thenReturn(false)
        // execute
        fixture.onResume()
        // validate
        verify(permissionService).permissionGranted()
        verify(permissionService).locationEnabled()
        verify(scannerService).resume()
        verify(scannerService).register(fixture.connectionView)
    }

    @Test
    fun testOnResumeWithPermissionNotGrantedWillPauseScanner() {
        // setup
        val permissionService = MainContextHelper.INSTANCE.permissionService
        val scannerService = MainContextHelper.INSTANCE.scannerService
        whenever(permissionService.permissionGranted()).thenReturn(false)
        // execute
        fixture.onResume()
        // validate
        verify(scannerService).pause()
        verify(scannerService).register(fixture.connectionView)
        verify(permissionService).permissionGranted()
    }

    @Test
    fun testOnStartWithPermissionGrantedWillResumeScanner() {
        // setup
        val permissionService = MainContextHelper.INSTANCE.permissionService
        val scannerService = MainContextHelper.INSTANCE.scannerService
        whenever(permissionService.permissionGranted()).thenReturn(true)
        // execute
        fixture.onStart()
        // validate
        verify(scannerService).resume()
        verify(permissionService).permissionGranted()
    }

    @Test
    fun testOnStartWithPermissionNotGrantedWillCheckPermission() {
        // setup
        val permissionService = MainContextHelper.INSTANCE.permissionService
        whenever(permissionService.permissionGranted()).thenReturn(false)
        // execute
        fixture.onStart()
        // validate
        verify(permissionService).check()
        verify(permissionService).permissionGranted()
    }

    @Test
    fun testOnCreateOptionsMenu() {
        // setup
        val menu: Menu = mock()
        val optionMenu: OptionMenu = mock()
        fixture.optionMenu = optionMenu
        // execute
        val actual = fixture.onCreateOptionsMenu(menu)
        // validate
        assertTrue(actual)
        verify(optionMenu).create(fixture, menu)
    }

    @Test
    fun testOnOptionsItemSelected() {
        // setup
        val menuItem: MenuItem = mock()
        val optionMenu: OptionMenu = mock()
        fixture.optionMenu = optionMenu
        // execute
        val actual = fixture.onOptionsItemSelected(menuItem)
        // validate
        assertTrue(actual)
        verify(optionMenu).select(menuItem)
    }

    @Test
    fun testOnConfigurationChanged() {
        // setup
        val configuration = fixture.resources.configuration
        val drawerNavigation: DrawerNavigation = mock()
        fixture.drawerNavigation = drawerNavigation
        // execute
        fixture.onConfigurationChanged(configuration)
        // validate
        verify(drawerNavigation).onConfigurationChanged(configuration)
    }

    @Test
    fun testOnPostCreate() {
        // setup
        val drawerNavigation: DrawerNavigation = mock()
        fixture.drawerNavigation = drawerNavigation
        // execute
        fixture.onPostCreate(null)
        // validate
        verify(drawerNavigation).syncState()
    }

    @Test
    fun testOnStop() {
        // setup
        val scannerService = MainContextHelper.INSTANCE.scannerService
        // execute
        fixture.onStop()
        // validate
        verify(scannerService).stop()
    }

    @Test
    fun testUpdateShouldUpdateScanner() {
        // setup
        val scannerService = MainContextHelper.INSTANCE.scannerService
        // execute
        fixture.update()
        // validate
        verify(scannerService).update()
    }

    @Test
    fun testOnSharedPreferenceChangedShouldUpdateScanner() {
        // setup
        val scannerService = MainContextHelper.INSTANCE.scannerService
        val sharedPreferences: SharedPreferences = mock()
        // execute
        fixture.onSharedPreferenceChanged(sharedPreferences, String.EMPTY)
        // validate
        verify(scannerService).update()
    }

    @Test
    fun testOptionMenu() {
        // execute
        val actual = fixture.optionMenu
        // validate
        assertNotNull(actual)
    }

    @Test
    fun testGetCurrentMenuItem() {
        // setup
        val menuItem: MenuItem = mock()
        val navigationMenuController: NavigationMenuController = mock()
        whenever(navigationMenuController.currentMenuItem()).thenReturn(menuItem)
        fixture.navigationMenuController = navigationMenuController
        // execute
        val actual = fixture.currentMenuItem()
        // validate
        assertEquals(menuItem, actual)
        verify(navigationMenuController).currentMenuItem()
    }

    @Test
    fun testGetCurrentNavigationMenu() {
        // setup
        val navigationMenu = NavigationMenu.CHANNEL_GRAPH
        val navigationMenuController: NavigationMenuController = mock()
        whenever(navigationMenuController.currentNavigationMenu()).thenReturn(navigationMenu)
        fixture.navigationMenuController = navigationMenuController
        // execute
        val actual = fixture.currentNavigationMenu()
        // validate
        assertEquals(navigationMenu, actual)
        verify(navigationMenuController).currentNavigationMenu()
    }

    @Test
    fun testSetCurrentNavigationMenu() {
        // setup
        val settings = MainContextHelper.INSTANCE.settings
        val navigationMenu = NavigationMenu.CHANNEL_GRAPH
        val navigationMenuController: NavigationMenuController = mock()
        fixture.navigationMenuController = navigationMenuController
        // execute
        fixture.currentNavigationMenu(navigationMenu)
        // validate
        verify(navigationMenuController).currentNavigationMenu(navigationMenu)
        verify(settings).saveSelectedMenu(navigationMenu)
    }

    @Test
    fun testGetNavigationView() {
        // setup
        val navigationMenuController: NavigationMenuController = mock()
        val navigationView: NavigationView = mock()
        whenever(navigationMenuController.navigationView).thenReturn(navigationView)
        fixture.navigationMenuController = navigationMenuController
        // execute
        val actual = fixture.navigationView()
        // validate
        assertEquals(navigationView, actual)
        verify(navigationMenuController).navigationView
    }
}