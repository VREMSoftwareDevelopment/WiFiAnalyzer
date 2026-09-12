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
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
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

    private val mainActivity = RobolectricUtil.INSTANCE.mainActivity
    private val fixture = mainActivity.navigationMenuController
    private val drawerNavigationView = fixture.drawerNavigationView
    private val bottomNavigationView = fixture.bottomNavigationView

    @After
    fun tearDown() {
        fixture.currentNavigationMenu(NavigationMenu.ACCESS_POINTS)
    }

    @Test
    fun drawerNavigationMenuView() {
        // Act
        val menu: Menu = drawerNavigationView.menu
        // Assert
        assertThat(menu.size()).isEqualTo(drawerMenuItems.size)
    }

    @Test
    fun bottomNavigationMenuView() {
        // Act
        val menu: Menu = bottomNavigationView.menu
        // Assert
        assertThat(menu.size()).isEqualTo(bottomMenuItems.size)
    }

    @Test
    fun getCurrentDrawerMenuItem() {
        // Arrange
        val expected = drawerNavigationView.menu.findItem(NavigationMenu.ACCESS_POINTS.idDrawer)
        // Act
        val actual = fixture.currentMenuItem()
        // Assert
        assertThat(actual).isEqualTo(expected)
        assertThat(actual.isChecked).isTrue
    }

    @Test
    fun getCurrentNavigationMenu() {
        // Act
        val actual = fixture.currentNavigationMenu()
        // Assert
        assertThat(actual).isEqualTo(NavigationMenu.ACCESS_POINTS)
    }

    @Test
    fun setCurrentNavigationMenuWithDrawerNavigationView() {
        // Arrange
        val expected = NavigationMenu.CHANNEL_GRAPH
        // Act
        fixture.currentNavigationMenu(expected)
        // Assert
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
        // Arrange
        val expected = NavigationMenu.CHANNEL_GRAPH
        // Act
        fixture.currentNavigationMenu(expected)
        // Assert
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
