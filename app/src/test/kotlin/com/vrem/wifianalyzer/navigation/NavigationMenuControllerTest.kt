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
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class NavigationMenuControllerTest {
    val drawerMenuItems =
        listOf(
            NavigationMenu.ACCESS_POINTS,
            NavigationMenu.CHANNEL_RATING,
            NavigationMenu.CHANNEL_GRAPH,
            NavigationMenu.TIME_GRAPH,
            NavigationMenu.EXPORT,
            NavigationMenu.CHANNEL_AVAILABLE,
            NavigationMenu.VENDORS,
            NavigationMenu.SETTINGS,
            NavigationMenu.ABOUT,
        )

    val bottomMenuItems =
        listOf(
            NavigationMenu.ACCESS_POINTS,
            NavigationMenu.CHANNEL_RATING,
            NavigationMenu.CHANNEL_GRAPH,
            NavigationMenu.TIME_GRAPH,
        )

    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val fixture = mainActivity.navigationMenuController
    private val drawerNavigationView = fixture.drawerNavigationView
    private val bottomNavigationView = fixture.bottomNavigationView

    @After
    fun tearDown() {
        fixture.currentNavigationMenu(NavigationMenu.ACCESS_POINTS)
    }

    @Test
    fun drawerNavigationMenuView() {
        // execute
        val menu: Menu = drawerNavigationView.menu
        // validate
        assertThat(menu.size()).isEqualTo(drawerMenuItems.size)
    }

    @Test
    fun bottomNavigationMenuView() {
        // execute
        val menu: Menu = bottomNavigationView.menu
        // validate
        assertThat(menu.size()).isEqualTo(bottomMenuItems.size)
    }

    @Test
    fun getCurrentDrawerMenuItem() {
        // setup
        val expected = drawerNavigationView.menu.findItem(NavigationMenu.ACCESS_POINTS.idDrawer)
        // execute
        val actual = fixture.currentMenuItem()
        // validate
        assertThat(actual).isEqualTo(expected)
        assertThat(actual.isChecked).isTrue
    }

    @Test
    fun getCurrentNavigationMenu() {
        // execute
        val actual = fixture.currentNavigationMenu()
        // validate
        assertThat(actual).isEqualTo(NavigationMenu.ACCESS_POINTS)
    }

    @Test
    fun setCurrentNavigationMenuWithDrawerNavigationView() {
        // setup
        val expected = NavigationMenu.CHANNEL_GRAPH
        // execute
        fixture.currentNavigationMenu(expected)
        // validate
        assertThat(fixture.currentNavigationMenu()).isEqualTo(expected)
        drawerMenuItems.forEach {
            val menuItem = drawerNavigationView.menu.findItem(it.idDrawer)
            if (it == expected) {
                assertThat(menuItem.isChecked).describedAs(it.toString()).isTrue
            } else {
                assertThat(menuItem.isChecked).describedAs(it.toString()).isFalse
            }
        }
    }

    @Test
    fun setCurrentNavigationMenuWithBottomNavigationView() {
        // setup
        val expected = NavigationMenu.CHANNEL_GRAPH
        // execute
        fixture.currentNavigationMenu(expected)
        // validate
        assertThat(fixture.currentNavigationMenu()).isEqualTo(expected)
        bottomMenuItems.forEach {
            val menuItem = bottomNavigationView.menu.findItem(it.idBottom)
            if (it == expected) {
                assertThat(menuItem.isChecked).describedAs(it.toString()).isTrue
            } else {
                assertThat(menuItem.isChecked).describedAs(it.toString()).isFalse
            }
        }
    }
}
