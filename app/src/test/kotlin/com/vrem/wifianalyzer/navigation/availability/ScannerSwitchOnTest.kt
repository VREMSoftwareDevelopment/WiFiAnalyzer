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
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.navigation.options.OptionMenu
import org.junit.After
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

class ScannerSwitchOnTest {
    private val mainActivity = Mockito.mock(MainActivity::class.java)
    private val optionMenu = Mockito.mock(OptionMenu::class.java)
    private val menu = Mockito.mock(Menu::class.java)
    private val menuItem = Mockito.mock(MenuItem::class.java)
    private val scanner = INSTANCE.scannerService

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(mainActivity)
        Mockito.verifyNoMoreInteractions(optionMenu)
        Mockito.verifyNoMoreInteractions(menu)
        Mockito.verifyNoMoreInteractions(menuItem)
        Mockito.verifyNoMoreInteractions(scanner)
        INSTANCE.restore()
    }

    @Test
    fun testNavigationOptionScannerSwitchOn() {
        // setup
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(menu)
        whenever(menu.findItem(R.id.action_scanner)).thenReturn(menuItem)
        // execute
        navigationOptionScannerSwitchOn(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_scanner)
        verify(scanner).isRunning
        verify(menuItem).isVisible = true
        verify(menuItem).setTitle(R.string.scanner_play)
        verify(menuItem).setIcon(R.drawable.ic_play_arrow)
    }

    @Test
    fun testNavigationOptionScannerSwitchOnWithScannerRunningUpdateMenuItemIconAndTitle() {
        // setup
        whenever(scanner.isRunning).thenReturn(true)
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(menu)
        whenever(menu.findItem(R.id.action_scanner)).thenReturn(menuItem)
        // execute
        navigationOptionScannerSwitchOn(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_scanner)
        verify(scanner).isRunning
        verify(menuItem).isVisible = true
        verify(menuItem).setTitle(R.string.scanner_pause)
        verify(menuItem).setIcon(R.drawable.ic_pause)
    }

    @Test
    fun testNavigationOptionScannerSwitchOnWithScannerNotRunningUpdateMenuItemIconAndTitle() {
        // setup
        whenever(scanner.isRunning).thenReturn(false)
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(menu)
        whenever(menu.findItem(R.id.action_scanner)).thenReturn(menuItem)
        // execute
        navigationOptionScannerSwitchOn(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_scanner)
        verify(scanner).isRunning
        verify(menuItem).isVisible = true
        verify(menuItem).setTitle(R.string.scanner_play)
        verify(menuItem).setIcon(R.drawable.ic_play_arrow)
    }

    @Test
    fun testNavigationOptionScannerSwitchOnWithNoMenuDoesNotSetVisibleTrue() {
        // setup
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(null)
        // execute
        navigationOptionScannerSwitchOn(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu, never()).findItem(R.id.action_scanner)
        verify(menuItem, never()).isVisible = true
    }

}