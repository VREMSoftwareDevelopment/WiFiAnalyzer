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

import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.navigation.availability.NavigationOption
import com.vrem.wifianalyzer.navigation.availability.navigationOptionAp
import com.vrem.wifianalyzer.navigation.availability.navigationOptionOff
import com.vrem.wifianalyzer.navigation.availability.navigationOptionOther
import com.vrem.wifianalyzer.navigation.availability.navigationOptionRating
import com.vrem.wifianalyzer.navigation.availability.navigationOptionWiFiSwitchOn
import com.vrem.wifianalyzer.navigation.items.NavigationItem
import com.vrem.wifianalyzer.navigation.items.navigationItemAbout
import com.vrem.wifianalyzer.navigation.items.navigationItemAccessPoints
import com.vrem.wifianalyzer.navigation.items.navigationItemChannelAvailable
import com.vrem.wifianalyzer.navigation.items.navigationItemChannelGraph
import com.vrem.wifianalyzer.navigation.items.navigationItemChannelRating
import com.vrem.wifianalyzer.navigation.items.navigationItemExport
import com.vrem.wifianalyzer.navigation.items.navigationItemSettings
import com.vrem.wifianalyzer.navigation.items.navigationItemTimeGraph
import com.vrem.wifianalyzer.navigation.items.navigationItemVendors

val MAIN_NAVIGATION =
    listOf(
        NavigationMenu.ACCESS_POINTS,
        NavigationMenu.CHANNEL_RATING,
        NavigationMenu.CHANNEL_GRAPH,
        NavigationMenu.TIME_GRAPH,
    )

private const val MENU_ITEM_INVALID_ID = -1

enum class NavigationMenu(
    val idDrawer: Int,
    val idBottom: Int,
    val title: Int,
    val navigationItem: NavigationItem,
    val navigationOptions: List<NavigationOption> = navigationOptionOff,
) {
    ACCESS_POINTS(
        R.id.nav_drawer_access_points,
        R.id.nav_bottom_access_points,
        R.string.action_access_points,
        navigationItemAccessPoints,
        navigationOptionAp,
    ),
    CHANNEL_RATING(
        R.id.nav_drawer_channel_rating,
        R.id.nav_bottom_channel_rating,
        R.string.action_channel_rating,
        navigationItemChannelRating,
        navigationOptionRating,
    ),
    CHANNEL_GRAPH(
        R.id.nav_drawer_channel_graph,
        R.id.nav_bottom_channel_graph,
        R.string.action_channel_graph,
        navigationItemChannelGraph,
        navigationOptionOther,
    ),
    TIME_GRAPH(
        R.id.nav_drawer_time_graph,
        R.id.nav_bottom_time_graph,
        R.string.action_time_graph,
        navigationItemTimeGraph,
        navigationOptionOther,
    ),
    EXPORT(
        R.id.nav_drawer_export,
        MENU_ITEM_INVALID_ID,
        title = R.string.action_export,
        navigationItem = navigationItemExport,
    ),
    CHANNEL_AVAILABLE(
        R.id.nav_drawer_channel_available,
        MENU_ITEM_INVALID_ID,
        title = R.string.action_channel_available,
        navigationItem = navigationItemChannelAvailable,
    ),
    VENDORS(
        R.id.nav_drawer_vendors,
        MENU_ITEM_INVALID_ID,
        title = R.string.action_vendors,
        navigationItem = navigationItemVendors,
    ),
    SETTINGS(
        R.id.nav_drawer_settings,
        MENU_ITEM_INVALID_ID,
        title = R.string.action_settings,
        navigationItem = navigationItemSettings,
    ),
    ABOUT(
        R.id.nav_drawer_about,
        MENU_ITEM_INVALID_ID,
        title = R.string.action_about,
        navigationItem = navigationItemAbout,
    ),
    ;

    fun activateNavigationMenu(mainActivity: MainActivity) = navigationItem.activate(mainActivity, this)

    fun activateOptions(mainActivity: MainActivity) = navigationOptions.forEach { it(mainActivity) }

    fun wiFiBandSwitchable(): Boolean = navigationOptions.contains(navigationOptionWiFiSwitchOn)

    fun registered(): Boolean = navigationItem.registered

    companion object {
        fun find(id: Int): NavigationMenu =
            entries.firstOrNull { it.idDrawer == id || it.idBottom == id } ?: ACCESS_POINTS
    }
}
