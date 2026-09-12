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

import android.graphics.drawable.Drawable
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.navigation.options.OptionMenu
import com.vrem.wifianalyzer.wifi.filter.adapter.FiltersAdapter
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class FilterOnTest {
    private val mainActivity: MainActivity = mock()
    private val optionMenu: OptionMenu = mock()
    private val menu: Menu = mock()
    private val menuItem: MenuItem = mock()
    private val drawable: Drawable = mock()
    private val filterAdapter: FiltersAdapter = mock()
    private val fixture: NavigationOption = filterMenuItem { filterAdapter }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(filterAdapter)
        verifyNoMoreInteractions(drawable)
        verifyNoMoreInteractions(menuItem)
        verifyNoMoreInteractions(menu)
        verifyNoMoreInteractions(optionMenu)
        verifyNoMoreInteractions(mainActivity)
    }

    @Test
    fun navigationOptionFilterOnWithFilterInactive() {
        // Arrange
        val colorResult = 200
        whenever(filterAdapter.isActive()).thenReturn(false)
        whenever(ContextCompat.getColor(mainActivity, R.color.regular)).thenReturn(colorResult)
        withMenuItem()
        // Act
        fixture(mainActivity)
        // Assert
        verifyMenuItem()
        ContextCompat.getColor(verify(mainActivity), R.color.regular)
        verify(drawable).setTint(colorResult)
    }

    @Test
    fun navigationOptionFilterOnWithFilterActive() {
        // Arrange
        val colorResult = 100
        whenever(filterAdapter.isActive()).thenReturn(true)
        whenever(ContextCompat.getColor(mainActivity, R.color.selected)).thenReturn(colorResult)
        withMenuItem()
        // Act
        fixture(mainActivity)
        // Assert
        verifyMenuItem()
        ContextCompat.getColor(verify(mainActivity), R.color.selected)
        verify(drawable).setTint(colorResult)
    }

    @Test
    fun navigationOptionFilterOnWithNoMenuDoesNotSetVisibleTrue() {
        // Arrange
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(null)
        // Act
        fixture(mainActivity)
        // Assert
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
    }

    @Test
    fun navigationOptionFilterOnWithNoIconDoesNotSetTint() {
        // Arrange
        whenever(filterAdapter.isActive()).thenReturn(false)
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(menu)
        whenever(menu.findItem(R.id.action_filter)).thenReturn(menuItem)
        whenever(menuItem.icon).thenReturn(null)
        // Act
        fixture(mainActivity)
        // Assert
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_filter)
        verify(menuItem).icon
        verify(filterAdapter).isActive()
        verify(menuItem).isVisible = true
    }

    private fun verifyMenuItem() {
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_filter)
        verify(menuItem).icon
        verify(filterAdapter).isActive()
        verify(menuItem).isVisible = true
    }

    private fun withMenuItem() {
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(menu)
        whenever(menu.findItem(R.id.action_filter)).thenReturn(menuItem)
        whenever(menuItem.icon).thenReturn(drawable)
    }
}
