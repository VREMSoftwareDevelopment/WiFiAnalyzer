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

import android.os.Build
import android.view.MenuItem
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.settings.Settings
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class MainActivityBackPressedTest {
    private val mainActivity: MainActivity = mock()
    private val menuItem: MenuItem = mock()
    private val settings: Settings = MainContextHelper.INSTANCE.settings
    private val fixture = MainActivityBackPressed(mainActivity)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(menuItem)
        verifyNoMoreInteractions(settings)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun handleOnBackPressedWhenCloseDrawerIsClosed() {
        // setup
        whenever(mainActivity.closeDrawer()).thenReturn(true)
        // execute
        fixture.handleOnBackPressed()
        // validate
        verify(mainActivity).closeDrawer()
    }

    @Test
    fun handleOnBackPressedWillFinishMainActivity() {
        // setup
        whenever(mainActivity.closeDrawer()).thenReturn(false)
        whenever(settings.selectedMenu()).thenReturn(NavigationMenu.ACCESS_POINTS)
        whenever(mainActivity.currentNavigationMenu()).thenReturn(NavigationMenu.ACCESS_POINTS)
        // execute
        fixture.handleOnBackPressed()
        // validate
        verify(mainActivity).closeDrawer()
        verify(settings).selectedMenu()
        verify(mainActivity).currentNavigationMenu()
        verify(mainActivity).finish()
    }

    @Test
    fun handleOnBackPressedWillSwitchToPreviousMenu() {
        // setup
        whenever(mainActivity.closeDrawer()).thenReturn(false)
        whenever(settings.selectedMenu()).thenReturn(NavigationMenu.ACCESS_POINTS)
        whenever(mainActivity.currentNavigationMenu()).thenReturn(NavigationMenu.CHANNEL_GRAPH)
        whenever(mainActivity.currentMenuItem()).thenReturn(menuItem)
        // execute
        fixture.handleOnBackPressed()
        // validate
        verify(mainActivity).closeDrawer()
        verify(settings).selectedMenu()
        verify(mainActivity).currentNavigationMenu()
        verify(mainActivity).currentNavigationMenu(NavigationMenu.ACCESS_POINTS)
        verify(mainActivity).currentMenuItem()
        verify(mainActivity).onNavigationItemSelected(menuItem)
    }

}