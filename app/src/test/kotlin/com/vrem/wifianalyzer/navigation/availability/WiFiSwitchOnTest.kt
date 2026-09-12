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
package com.vrem.wifianalyzer.navigation.availability

import android.view.Menu
import android.view.MenuItem
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.navigation.options.OptionMenu
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class WiFiSwitchOnTest {
    private val mainActivity: MainActivity = mock()
    private val settings = MainContextHelper.INSTANCE.settings
    private val optionMenu: OptionMenu = mock()
    private val menu: Menu = mock()
    private val menuItem: MenuItem = mock()
    private val fixture: NavigationOption = wiFiBandMenuItem(true) { settings }

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(settings)
        verifyNoMoreInteractions(optionMenu)
        verifyNoMoreInteractions(menu)
        verifyNoMoreInteractions(menuItem)
    }

    @Test
    fun navigationOptionWiFiSwitchOnWithMenuWillSetTitleAndVisibility() {
        // Arrange
        val expected = "XYZ\n123"
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(menu)
        whenever(menu.findItem(R.id.action_wifi_band)).thenReturn(menuItem)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ5)
        whenever(mainActivity.getString(WiFiBand.GHZ5.textResource)).thenReturn("XYZ 123")
        // Act
        fixture(mainActivity)
        // Assert
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_wifi_band)
        verify(settings).wiFiBand()
        verify(mainActivity).getString(WiFiBand.GHZ5.textResource)
        verify(menuItem).isVisible = true
        verify(menuItem).title = expected
    }

    @Test
    fun navigationOptionWiFiSwitchOnWithNoMenuWillNotSetTitleAndVisibility() {
        // Arrange
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(null)
        // Act
        fixture(mainActivity)
        // Assert
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu, never()).findItem(R.id.action_wifi_band)
        verify(settings, never()).wiFiBand()
        verify(mainActivity, never()).getString(WiFiBand.GHZ5.textResource)
        verify(menuItem, never()).isVisible = any()
        verify(menuItem, never()).title = any()
    }

    @Test
    fun navigationOptionWiFiSwitchOnIsVisibleAndUsesMainContextSettings() {
        // Arrange
        doReturn(optionMenu).whenever(mainActivity).optionMenu
        doReturn(menu).whenever(optionMenu).menu
        doReturn(menuItem).whenever(menu).findItem(R.id.action_wifi_band)
        doReturn(WiFiBand.GHZ5).whenever(settings).wiFiBand()
        doReturn("XYZ 123").whenever(mainActivity).getString(WiFiBand.GHZ5.textResource)
        // Act
        navigationOptionWiFiSwitchOn(mainActivity)
        // Assert
        verify(menuItem).isVisible = true
        verify(menuItem).title = "XYZ\n123"
        verify(settings).wiFiBand()
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_wifi_band)
        verify(mainActivity).getString(WiFiBand.GHZ5.textResource)
    }
}
