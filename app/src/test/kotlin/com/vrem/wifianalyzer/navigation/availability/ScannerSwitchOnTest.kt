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
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.navigation.options.OptionMenu
import com.vrem.wifianalyzer.wifi.scanner.ScannerService
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class ScannerSwitchOnTest {
    private val mainActivity: MainActivity = mock()
    private val optionMenu: OptionMenu = mock()
    private val menu: Menu = mock()
    private val menuItem: MenuItem = mock()
    private val scanner: ScannerService = mock()
    private val fixture: NavigationOption = scannerMenuItem { scanner }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(optionMenu)
        verifyNoMoreInteractions(menu)
        verifyNoMoreInteractions(menuItem)
        verifyNoMoreInteractions(scanner)
    }

    @Test
    fun navigationOptionScannerSwitchOn() {
        // Arrange
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(menu)
        whenever(menu.findItem(R.id.action_scanner)).thenReturn(menuItem)
        // Act
        fixture(mainActivity)
        // Assert
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_scanner)
        verify(scanner).running()
        verify(menuItem).isVisible = true
        verify(menuItem).setTitle(R.string.scanner_play)
        verify(menuItem).setIcon(R.drawable.ic_play_arrow)
    }

    @Test
    fun navigationOptionScannerSwitchOnWithScannerRunningUpdateMenuItemIconAndTitle() {
        // Arrange
        whenever(scanner.running()).thenReturn(true)
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(menu)
        whenever(menu.findItem(R.id.action_scanner)).thenReturn(menuItem)
        // Act
        fixture(mainActivity)
        // Assert
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_scanner)
        verify(scanner).running()
        verify(menuItem).isVisible = true
        verify(menuItem).setTitle(R.string.scanner_pause)
        verify(menuItem).setIcon(R.drawable.ic_pause)
    }

    @Test
    fun navigationOptionScannerSwitchOnWithScannerNotRunningUpdateMenuItemIconAndTitle() {
        // Arrange
        whenever(scanner.running()).thenReturn(false)
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(menu)
        whenever(menu.findItem(R.id.action_scanner)).thenReturn(menuItem)
        // Act
        fixture(mainActivity)
        // Assert
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_scanner)
        verify(scanner).running()
        verify(menuItem).isVisible = true
        verify(menuItem).setTitle(R.string.scanner_play)
        verify(menuItem).setIcon(R.drawable.ic_play_arrow)
    }

    @Test
    fun navigationOptionScannerSwitchOnWithNoMenuDoesNotSetVisibleTrue() {
        // Arrange
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(null)
        // Act
        fixture(mainActivity)
        // Assert
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu, never()).findItem(R.id.action_scanner)
        verify(menuItem, never()).isVisible = true
    }
}
