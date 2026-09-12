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
package com.vrem.wifianalyzer

import android.content.SharedPreferences
import android.os.Build
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.navigation.NavigationView
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.navigation.NavigationMenuController
import com.vrem.wifianalyzer.navigation.options.OptionMenu
import com.vrem.wifianalyzer.wifi.accesspoint.ConnectionView
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.Robolectric
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class MainActivityTest {
    private val controller =
        Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .resume()
    private val fixture = controller.get()

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun mainActivity() {
        assertThat(MainContext.INSTANCE.scannerService.running()).isFalse
    }

    @Test
    fun collectionWillUpdateConnectionView() {
        // Arrange
        val scannerService = MainContext.INSTANCE.scannerService
        val connectionView: ConnectionView = mock()
        fixture.connectionView = connectionView
        // Act
        controller.stop().start()
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        // Assert
        verify(connectionView).update(scannerService.wiFiData().value)
    }

    @Test
    fun onPermissionResultWithPermissionGrantedWillNotFinishActivity() {
        // Act
        fixture.onPermissionResult(true)
        // Assert
        assertThat(fixture.isFinishing).isFalse
    }

    @Test
    fun onPermissionResultWithPermissionDeniedWillFinishActivity() {
        // Act
        fixture.onPermissionResult(false)
        // Assert
        assertThat(fixture.isFinishing).isTrue
    }

    @Test
    fun onPauseWillPauseScanner() {
        // Arrange
        val scannerService = MainContextHelper.INSTANCE.scannerService
        // Act
        fixture.onPause()
        // Assert
        verify(scannerService).pause()
    }

    @Test
    fun onResumeWithPermissionGrantedAndLocationDisabledWillResumeScanner() {
        // Arrange
        val permissionService = MainContextHelper.INSTANCE.permissionService
        val scannerService = MainContextHelper.INSTANCE.scannerService
        whenever(permissionService.permissionGranted()).thenReturn(true)
        whenever(permissionService.locationEnabled()).thenReturn(false)
        // Act
        fixture.onResume()
        // Assert
        verify(permissionService).permissionGranted()
        verify(permissionService).locationEnabled()
        verify(scannerService).resume()
    }

    @Test
    fun onResumeWithPermissionGrantedAndLocationEnabledWillResumeScanner() {
        // Arrange
        val permissionService = MainContextHelper.INSTANCE.permissionService
        val scannerService = MainContextHelper.INSTANCE.scannerService
        whenever(permissionService.permissionGranted()).thenReturn(true)
        whenever(permissionService.locationEnabled()).thenReturn(true)
        // Act
        fixture.onResume()
        // Assert
        verify(permissionService).permissionGranted()
        verify(permissionService).locationEnabled()
        verify(scannerService).resume()
    }

    @Test
    fun onResumeWithPermissionNotGrantedWillPauseScanner() {
        // Arrange
        val permissionService = MainContextHelper.INSTANCE.permissionService
        val scannerService = MainContextHelper.INSTANCE.scannerService
        whenever(permissionService.permissionGranted()).thenReturn(false)
        // Act
        fixture.onResume()
        // Assert
        verify(scannerService).pause()
        verify(permissionService).permissionGranted()
        verify(permissionService, never()).locationEnabled()
    }

    @Test
    fun onStartWithPermissionGrantedAndLocationDisabledWillResumeScanner() {
        // Arrange
        val permissionService = MainContextHelper.INSTANCE.permissionService
        val scannerService = MainContextHelper.INSTANCE.scannerService
        whenever(permissionService.permissionGranted()).thenReturn(true)
        whenever(permissionService.locationEnabled()).thenReturn(false)
        // Act
        fixture.onStart()
        // Assert
        verify(scannerService).resume()
        verify(permissionService).permissionGranted()
        verify(permissionService).locationEnabled()
    }

    @Test
    fun onStartWithPermissionGrantedAndLocationEnabledWillResumeScanner() {
        // Arrange
        val permissionService = MainContextHelper.INSTANCE.permissionService
        val scannerService = MainContextHelper.INSTANCE.scannerService
        whenever(permissionService.permissionGranted()).thenReturn(true)
        whenever(permissionService.locationEnabled()).thenReturn(true)
        // Act
        fixture.onStart()
        // Assert
        verify(scannerService).resume()
        verify(permissionService).permissionGranted()
        verify(permissionService).locationEnabled()
    }

    @Test
    fun onStartWithPermissionNotGrantedWillCheckPermission() {
        // Arrange
        val permissionService = MainContextHelper.INSTANCE.permissionService
        whenever(permissionService.permissionGranted()).thenReturn(false)
        // Act
        fixture.onStart()
        // Assert
        verify(permissionService).check()
        verify(permissionService).permissionGranted()
        verify(permissionService, never()).locationEnabled()
    }

    @Test
    fun onCreateOptionsMenu() {
        // Arrange
        val menu: Menu = mock()
        val optionMenu: OptionMenu = mock()
        fixture.optionMenu = optionMenu
        // Act
        val actual = fixture.onCreateOptionsMenu(menu)
        // Assert
        assertThat(actual).isTrue
        verify(optionMenu).create(fixture, menu)
    }

    @Test
    fun onOptionsItemSelected() {
        // Arrange
        val menuItem: MenuItem = mock()
        val optionMenu: OptionMenu = mock()
        fixture.optionMenu = optionMenu
        // Act
        val actual = fixture.onOptionsItemSelected(menuItem)
        // Assert
        assertThat(actual).isTrue
        verify(optionMenu).select(menuItem)
    }

    @Test
    fun onConfigurationChanged() {
        // Arrange
        val configuration = fixture.resources.configuration
        val drawerNavigation: DrawerNavigation = mock()
        fixture.drawerNavigation = drawerNavigation
        // Act
        fixture.onConfigurationChanged(configuration)
        // Assert
        verify(drawerNavigation).onConfigurationChanged(configuration)
    }

    @Test
    fun onPostCreate() {
        // Arrange
        val drawerNavigation: DrawerNavigation = mock()
        fixture.drawerNavigation = drawerNavigation
        // Act
        fixture.onPostCreate(null)
        // Assert
        verify(drawerNavigation).syncState()
    }

    @Test
    fun onStop() {
        // Arrange
        val scannerService = MainContextHelper.INSTANCE.scannerService
        // Act
        fixture.onStop()
        // Assert
        verify(scannerService).stop()
    }

    @Test
    fun updateShouldUpdateScanner() {
        // Arrange
        val scannerService = MainContextHelper.INSTANCE.scannerService
        // Act
        fixture.update()
        // Assert
        verify(scannerService).update()
    }

    @Test
    fun onSharedPreferenceChangedShouldUpdateScanner() {
        // Arrange
        val scannerService = MainContextHelper.INSTANCE.scannerService
        val sharedPreferences: SharedPreferences = mock()
        // Act
        fixture.onSharedPreferenceChanged(sharedPreferences, String.EMPTY)
        // Assert
        verify(scannerService).update()
    }

    @Test
    fun optionMenu() {
        // Act
        val actual = fixture.optionMenu
        // Assert
        assertThat(actual).isNotNull()
    }

    @Test
    fun getCurrentMenuItem() {
        // Arrange
        val menuItem: MenuItem = mock()
        val navigationMenuController: NavigationMenuController = mock()
        whenever(navigationMenuController.currentMenuItem()).thenReturn(menuItem)
        fixture.navigationMenuController = navigationMenuController
        // Act
        val actual = fixture.currentMenuItem()
        // Assert
        assertThat(actual).isEqualTo(menuItem)
        verify(navigationMenuController).currentMenuItem()
    }

    @Test
    fun getCurrentNavigationMenu() {
        // Arrange
        val navigationMenu = NavigationMenu.CHANNEL_GRAPH
        val navigationMenuController: NavigationMenuController = mock()
        whenever(navigationMenuController.currentNavigationMenu()).thenReturn(navigationMenu)
        fixture.navigationMenuController = navigationMenuController
        // Act
        val actual = fixture.currentNavigationMenu()
        // Assert
        assertThat(actual).isEqualTo(navigationMenu)
        verify(navigationMenuController).currentNavigationMenu()
    }

    @Test
    fun setCurrentNavigationMenu() {
        // Arrange
        val settings = MainContextHelper.INSTANCE.settings
        val navigationMenu = NavigationMenu.CHANNEL_GRAPH
        val navigationMenuController: NavigationMenuController = mock()
        fixture.navigationMenuController = navigationMenuController
        // Act
        fixture.currentNavigationMenu(navigationMenu)
        // Assert
        verify(navigationMenuController).currentNavigationMenu(navigationMenu)
        verify(settings).saveSelectedMenu(navigationMenu)
    }

    @Test
    fun getNavigationView() {
        // Arrange
        val navigationMenuController: NavigationMenuController = mock()
        val navigationView: NavigationView = mock()
        whenever(navigationMenuController.drawerNavigationView).thenReturn(navigationView)
        fixture.navigationMenuController = navigationMenuController
        // Act
        val actual = fixture.navigationView()
        // Assert
        assertThat(actual).isEqualTo(navigationView)
        verify(navigationMenuController).drawerNavigationView
    }
}
