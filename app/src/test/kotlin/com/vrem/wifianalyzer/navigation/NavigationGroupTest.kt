/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class NavigationGroupTest {
    @Test
    fun navigationGroup() {
        assertThat(NavigationGroup.entries).hasSize(3)
    }

    @Test
    fun navigationGroupOrder() {
        // setup
        val expected = arrayOf(
            NavigationGroup.GROUP_FEATURE,
            NavigationGroup.GROUP_OTHER,
            NavigationGroup.GROUP_SETTINGS
        )
        // validate
        assertThat(NavigationGroup.entries.toTypedArray()).isEqualTo(expected)
    }

    @Test
    fun settingsNavigationMenus() {
        // setup
        val expected: List<NavigationMenu> = listOf(NavigationMenu.SETTINGS, NavigationMenu.ABOUT)
        // validate
        assertThat(NavigationGroup.GROUP_SETTINGS.navigationMenus).isEqualTo(expected)
    }

    @Test
    fun featureNavigationMenus() {
        // setup
        val expected: List<NavigationMenu> = listOf(
            NavigationMenu.ACCESS_POINTS,
            NavigationMenu.CHANNEL_RATING,
            NavigationMenu.CHANNEL_GRAPH,
            NavigationMenu.TIME_GRAPH
        )
        // validate
        assertThat(NavigationGroup.GROUP_FEATURE.navigationMenus).isEqualTo(expected)
    }

    @Test
    fun otherNavigationMenus() {
        // setup
        val expected: List<NavigationMenu> = listOf(
            NavigationMenu.EXPORT,
            NavigationMenu.CHANNEL_AVAILABLE,
            NavigationMenu.VENDORS
        )
        // validate
        assertThat(NavigationGroup.GROUP_OTHER.navigationMenus).isEqualTo(expected)
    }

    @Test
    fun next() {
        assertThat(NavigationGroup.GROUP_FEATURE.next(NavigationMenu.CHANNEL_RATING)).isEqualTo(NavigationMenu.CHANNEL_GRAPH)
        assertThat(NavigationGroup.GROUP_FEATURE.next(NavigationMenu.TIME_GRAPH)).isEqualTo(NavigationMenu.ACCESS_POINTS)
        assertThat(NavigationGroup.GROUP_FEATURE.next(NavigationMenu.EXPORT)).isEqualTo(NavigationMenu.EXPORT)
    }

    @Test
    fun previous() {
        assertThat(NavigationGroup.GROUP_FEATURE.previous(NavigationMenu.CHANNEL_RATING)).isEqualTo(NavigationMenu.ACCESS_POINTS)
        assertThat(NavigationGroup.GROUP_FEATURE.previous(NavigationMenu.ACCESS_POINTS)).isEqualTo(NavigationMenu.TIME_GRAPH)
        assertThat(NavigationGroup.GROUP_FEATURE.next(NavigationMenu.EXPORT)).isEqualTo(NavigationMenu.EXPORT)
    }
}