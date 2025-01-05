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
package com.vrem.wifianalyzer

import android.content.SharedPreferences
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.navigation.NavigationView
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.navigation.NavigationMenuController
import com.vrem.wifianalyzer.navigation.options.OptionMenu
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
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
    fun mainActivity() {
        assertThat(MainContext.INSTANCE.scannerService.running()).isFalse()
    }

    @Test
    fun onPauseWillPauseScanner() {
        // setup
        val scannerService = MainContextHelper.INSTANCE.scannerService
        // execute
        fixture.onPause()
        // validate
        verify(scannerService).pause()
        verify(scannerService).unregister(fixture.connectionView)
    }

    @Test
    fun onResumeWithPermissionGrantedWillResumeScanner() {
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
    fun onResumeWithPermissionNotGrantedWillPauseScanner() {
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
    fun onStartWithPermissionGrantedWillResumeScanner() {
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
    fun onStartWithPermissionNotGrantedWillCheckPermission() {
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
    fun onCreateOptionsMenu() {
        // setup
        val menu: Menu = mock()
        val optionMenu: OptionMenu = mock()
        fixture.optionMenu = optionMenu
        // execute
        val actual = fixture.onCreateOptionsMenu(menu)
        // validate
        assertThat(actual).isTrue()
        verify(optionMenu).create(fixture, menu)
    }

    @Test
    fun onOptionsItemSelected() {
        // setup
        val menuItem: MenuItem = mock()
        val optionMenu: OptionMenu = mock()
        fixture.optionMenu = optionMenu
        // execute
        val actual = fixture.onOptionsItemSelected(menuItem)
        // validate
        assertThat(actual).isTrue()
        verify(optionMenu).select(menuItem)
    }

    @Test
    fun onConfigurationChanged() {
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
    fun onPostCreate() {
        // setup
        val drawerNavigation: DrawerNavigation = mock()
        fixture.drawerNavigation = drawerNavigation
        // execute
        fixture.onPostCreate(null)
        // validate
        verify(drawerNavigation).syncState()
    }

    @Test
    fun onStop() {
        // setup
        val scannerService = MainContextHelper.INSTANCE.scannerService
        // execute
        fixture.onStop()
        // validate
        verify(scannerService).stop()
    }

    @Test
    fun updateShouldUpdateScanner() {
        // setup
        val scannerService = MainContextHelper.INSTANCE.scannerService
        // execute
        fixture.update()
        // validate
        verify(scannerService).update()
    }

    @Test
    fun onSharedPreferenceChangedShouldUpdateScanner() {
        // setup
        val scannerService = MainContextHelper.INSTANCE.scannerService
        val sharedPreferences: SharedPreferences = mock()
        // execute
        fixture.onSharedPreferenceChanged(sharedPreferences, String.EMPTY)
        // validate
        verify(scannerService).update()
    }

    @Test
    fun optionMenu() {
        // execute
        val actual = fixture.optionMenu
        // validate
        assertThat(actual).isNotNull()
    }

    @Test
    fun getCurrentMenuItem() {
        // setup
        val menuItem: MenuItem = mock()
        val navigationMenuController: NavigationMenuController = mock()
        whenever(navigationMenuController.currentMenuItem()).thenReturn(menuItem)
        fixture.navigationMenuController = navigationMenuController
        // execute
        val actual = fixture.currentMenuItem()
        // validate
        assertThat(actual).isEqualTo(menuItem)
        verify(navigationMenuController).currentMenuItem()
    }

    @Test
    fun getCurrentNavigationMenu() {
        // setup
        val navigationMenu = NavigationMenu.CHANNEL_GRAPH
        val navigationMenuController: NavigationMenuController = mock()
        whenever(navigationMenuController.currentNavigationMenu()).thenReturn(navigationMenu)
        fixture.navigationMenuController = navigationMenuController
        // execute
        val actual = fixture.currentNavigationMenu()
        // validate
        assertThat(actual).isEqualTo(navigationMenu)
        verify(navigationMenuController).currentNavigationMenu()
    }

    @Test
    fun setCurrentNavigationMenu() {
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
    fun getNavigationView() {
        // setup
        val navigationMenuController: NavigationMenuController = mock()
        val navigationView: NavigationView = mock()
        whenever(navigationMenuController.navigationView).thenReturn(navigationView)
        fixture.navigationMenuController = navigationMenuController
        // execute
        val actual = fixture.navigationView()
        // validate
        assertThat(actual).isEqualTo(navigationView)
        verify(navigationMenuController).navigationView
    }
}