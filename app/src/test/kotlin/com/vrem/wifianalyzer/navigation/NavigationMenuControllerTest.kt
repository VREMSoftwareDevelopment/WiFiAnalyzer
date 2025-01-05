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
package com.vrem.wifianalyzer.navigation

import android.os.Build
import android.view.Menu
import android.view.MenuItem
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
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
    fun navigationMenuView() {
        // execute
        val menu: Menu = navigationView.menu
        // validate
        assertThat(menu.size()).isEqualTo(NavigationMenu.entries.size)
        validateNavigationGroup(menu)
    }

    @Test
    fun getCurrentMenuItem() {
        // setup
        val expected = navigationViewMenuItem(NavigationMenu.ACCESS_POINTS)
        // execute
        val actual = fixture.currentMenuItem()
        // validate
        assertThat(actual).isEqualTo(expected)
        assertThat(actual.isCheckable).isTrue()
        assertThat(actual.isChecked).isTrue()
    }

    @Test
    fun getCurrentNavigationMenu() {
        // execute
        val actual = fixture.currentNavigationMenu()
        // validate
        assertThat(actual).isEqualTo(NavigationMenu.ACCESS_POINTS)
    }

    @Test
    fun setCurrentNavigationMenuWithNavigationView() {
        // setup
        val expected = NavigationMenu.CHANNEL_GRAPH
        // execute
        fixture.currentNavigationMenu(expected)
        // validate
        assertThat(fixture.currentNavigationMenu()).isEqualTo(expected)
        assertThat(navigationViewMenuItem(NavigationMenu.CHANNEL_GRAPH).isCheckable).isTrue()
        assertThat(navigationViewMenuItem(NavigationMenu.CHANNEL_GRAPH).isChecked).isTrue()
        assertThat(navigationViewMenuItem(NavigationMenu.ACCESS_POINTS).isCheckable).isFalse()
        assertThat(navigationViewMenuItem(NavigationMenu.ACCESS_POINTS).isChecked).isFalse()
    }

    @Test
    fun setCurrentNavigationMenuWithBottomNavigationView() {
        // setup
        val expected = NavigationMenu.CHANNEL_GRAPH
        // execute
        fixture.currentNavigationMenu(expected)
        // validate
        assertThat(fixture.currentNavigationMenu()).isEqualTo(expected)
        val menuItem = bottomNavigationMenuItem(NavigationMenu.CHANNEL_GRAPH)
        assertThat(menuItem.isCheckable).isTrue()
        assertThat(menuItem.isChecked).isTrue()
        assertThat(bottomNavigationMenuItem(NavigationMenu.ACCESS_POINTS).isCheckable).isFalse()
    }

    private fun navigationViewMenuItem(navigationMenu: NavigationMenu): MenuItem =
        navigationView.menu.findItem(navigationMenu.ordinal)

    private fun bottomNavigationMenuItem(navigationMenu: NavigationMenu): MenuItem =
        bottomNavigationView.menu.findItem(navigationMenu.ordinal)

    private fun validateNavigationGroup(menu: Menu): Unit =
        NavigationGroup.entries.forEach { validateNavigationMenu(it, menu) }

    private fun validateNavigationMenu(navigationGroup: NavigationGroup, menu: Menu): Unit =
        navigationGroup.navigationMenus.forEach { validateMenuItem(menu, it, navigationGroup) }

    private fun validateMenuItem(menu: Menu, navigationMenu: NavigationMenu, navigationGroup: NavigationGroup) {
        val actual: MenuItem = menu.getItem(navigationMenu.ordinal)
        assertThat(actual.groupId).isEqualTo(navigationGroup.ordinal)
        assertThat(actual.title).isEqualTo(mainActivity.resources.getString(navigationMenu.title))
        assertThat(actual.itemId).isEqualTo(navigationMenu.ordinal)
        assertThat(actual.order).isEqualTo(navigationMenu.ordinal)
    }

}