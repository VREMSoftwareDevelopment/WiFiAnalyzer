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
package com.vrem.wifianalyzer.navigation.options

import android.app.Activity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.vrem.wifianalyzer.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class OptionMenuTest {
    private val menu: Menu = mock()
    private val menuItem: MenuItem = mock()
    private val activity: Activity = mock()
    private val menuInflater: MenuInflater = mock()
    private val fixture = OptionMenu()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(menu)
        verifyNoMoreInteractions(menuItem)
        verifyNoMoreInteractions(activity)
        verifyNoMoreInteractions(menuInflater)
    }

    @Test
    fun create() {
        // Arrange
        whenever(activity.menuInflater).thenReturn(menuInflater)
        // Act
        fixture.create(activity, menu)
        // Assert
        assertThat(fixture.menu).isEqualTo(menu)
        verify(activity).menuInflater
        verify(menuInflater).inflate(R.menu.optionmenu, menu)
    }

    @Test
    fun actions() {
        // Arrange
        val itemId = -1
        whenever(menuItem.itemId).thenReturn(itemId)
        // Act
        fixture.select(menuItem)
        // Assert
        verify(menuItem).itemId
    }
}
