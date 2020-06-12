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
package com.vrem.wifianalyzer.navigation.availability

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.navigation.options.OptionMenu
import org.junit.After
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

class WiFiSwitchOffTest {
    private val mainActivity = mock(MainActivity::class.java)
    private val optionMenu = mock(OptionMenu::class.java)
    private val menu = mock(Menu::class.java)
    private val menuItem = mock(MenuItem::class.java)
    private val actionBar = mock(ActionBar::class.java)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(optionMenu)
        verifyNoMoreInteractions(menu)
        verifyNoMoreInteractions(menuItem)
        verifyNoMoreInteractions(actionBar)
        INSTANCE.restore()
    }

    @Test
    fun testNavigationOptionWiFiSwitchOffWithActionBarSetEmptySubtitle() {
        // setup
        whenever(mainActivity.supportActionBar).thenReturn(actionBar)
        whenever(mainActivity.optionMenu).thenReturn(null)
        // execute
        navigationOptionWiFiSwitchOff(mainActivity)
        // validate
        verify(mainActivity).supportActionBar
        verify(actionBar).subtitle = String.EMPTY
        verify(mainActivity).optionMenu
    }

    @Test
    fun testNavigationOptionWiFiSwitchOffWithNoActionBarDoesNotSetSubtitle() {
        // setup
        whenever(mainActivity.supportActionBar).thenReturn(null)
        whenever(mainActivity.optionMenu).thenReturn(null)
        // execute
        navigationOptionWiFiSwitchOff(mainActivity)
        // validate
        verify(mainActivity).supportActionBar
        verify(actionBar, never()).subtitle = ArgumentMatchers.anyString()
        verify(mainActivity).optionMenu
    }

    @Test
    fun testNavigationOptionWiFiSwitchOffWithOptionMenuSetVisibleFalse() {
        // setup
        whenever(mainActivity.supportActionBar).thenReturn(null)
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(menu)
        whenever(menu.findItem(R.id.action_wifi_band)).thenReturn(menuItem)
        // execute
        navigationOptionWiFiSwitchOff(mainActivity)
        // validate
        verify(mainActivity).supportActionBar
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_wifi_band)
        verify(menuItem).isVisible = false
    }

    @Test
    fun testNavigationOptionWiFiSwitchOffWithNoOptionMenuDoesNotSetWiFiBandVisible() {
        // setup
        whenever(mainActivity.supportActionBar).thenReturn(null)
        whenever(mainActivity.optionMenu).thenReturn(null)
        // execute
        navigationOptionWiFiSwitchOff(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(menu, never()).findItem(R.id.action_wifi_band)
        verify(menuItem, never()).isVisible = ArgumentMatchers.anyBoolean()
        verify(mainActivity).supportActionBar
    }

    @Test
    fun testNavigationOptionWiFiSwitchOffWithNoMenuDoesNotSetWiFiBandVisible() {
        // setup
        whenever(mainActivity.supportActionBar).thenReturn(null)
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(null)
        // execute
        navigationOptionWiFiSwitchOff(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu, never()).findItem(R.id.action_wifi_band)
        verify(menuItem, never()).isVisible = ArgumentMatchers.anyBoolean()
        verify(mainActivity).supportActionBar
    }

}