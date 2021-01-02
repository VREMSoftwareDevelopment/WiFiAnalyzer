/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.navigation

import android.os.Build
import android.view.Menu
import android.view.MenuItem
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class NavigationMenuControllerTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val fixture = mainActivity.navigationMenuController
    private val navigationView = fixture.navigationView
    private val bottomNavigationView = fixture.bottomNavigationView

    @After
    fun tearDown() {
        fixture.currentNavigationMenu(NavigationMenu.ACCESS_POINTS)
    }

    @Test
    fun testNavigationMenuView() {
        // execute
        val menu: Menu = navigationView.menu
        // validate
        assertEquals(NavigationMenu.values().size, menu.size())
        validateNavigationGroup(menu)
    }

    @Test
    fun testGetCurrentMenuItem() {
        // setup
        val expected = navigationViewMenuItem(NavigationMenu.ACCESS_POINTS)
        // execute
        val actual = fixture.currentMenuItem()
        // validate
        assertEquals(expected, actual)
        assertTrue(actual.isCheckable)
        assertTrue(actual.isChecked)
    }

    @Test
    fun testGetCurrentNavigationMenu() {
        // execute
        val actual = fixture.currentNavigationMenu()
        // validate
        assertEquals(NavigationMenu.ACCESS_POINTS, actual)
    }

    @Test
    fun testSetCurrentNavigationMenuWithNavigationView() {
        // setup
        val expected = NavigationMenu.CHANNEL_GRAPH
        // execute
        fixture.currentNavigationMenu(expected)
        // validate
        assertEquals(expected, fixture.currentNavigationMenu())
        assertTrue(navigationViewMenuItem(NavigationMenu.CHANNEL_GRAPH).isCheckable)
        assertTrue(navigationViewMenuItem(NavigationMenu.CHANNEL_GRAPH).isChecked)
        assertFalse(navigationViewMenuItem(NavigationMenu.ACCESS_POINTS).isCheckable)
        assertFalse(navigationViewMenuItem(NavigationMenu.ACCESS_POINTS).isChecked)
    }

    @Test
    fun testSetCurrentNavigationMenuWithBottomNavigationView() {
        // setup
        val expected = NavigationMenu.CHANNEL_GRAPH
        // execute
        fixture.currentNavigationMenu(expected)
        // validate
        assertEquals(expected, fixture.currentNavigationMenu())
        val menuItem = bottomNavigationMenuItem(NavigationMenu.CHANNEL_GRAPH)
        assertTrue(menuItem.isCheckable)
        assertTrue(menuItem.isChecked)
        assertFalse(bottomNavigationMenuItem(NavigationMenu.ACCESS_POINTS).isCheckable)
    }

    private fun navigationViewMenuItem(navigationMenu: NavigationMenu): MenuItem =
            navigationView.menu.findItem(navigationMenu.ordinal)

    private fun bottomNavigationMenuItem(navigationMenu: NavigationMenu): MenuItem =
            bottomNavigationView.menu.findItem(navigationMenu.ordinal)

    private fun validateNavigationGroup(menu: Menu): Unit =
            NavigationGroup.values().forEach { validateNavigationMenu(it, menu) }

    private fun validateNavigationMenu(navigationGroup: NavigationGroup, menu: Menu): Unit =
            navigationGroup.navigationMenus.forEach { validateMenuItem(menu, it, navigationGroup) }

    private fun validateMenuItem(menu: Menu, navigationMenu: NavigationMenu, navigationGroup: NavigationGroup) {
        val actual: MenuItem = menu.getItem(navigationMenu.ordinal)
        assertEquals(navigationGroup.ordinal, actual.groupId)
        assertEquals(mainActivity.resources.getString(navigationMenu.title), actual.title)
        assertEquals(navigationMenu.ordinal, actual.itemId)
        assertEquals(navigationMenu.ordinal, actual.order)
    }

}