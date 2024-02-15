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

import android.view.Menu

enum class NavigationGroup(val navigationMenus: List<NavigationMenu>) {
    GROUP_FEATURE(
        listOf(
            NavigationMenu.ACCESS_POINTS,
            NavigationMenu.CHANNEL_RATING,
            NavigationMenu.CHANNEL_GRAPH,
            NavigationMenu.TIME_GRAPH
        )
    ),
    GROUP_OTHER(listOf(NavigationMenu.EXPORT, NavigationMenu.CHANNEL_AVAILABLE, NavigationMenu.VENDORS)),
    GROUP_SETTINGS(listOf(NavigationMenu.SETTINGS, NavigationMenu.ABOUT));

    fun next(navigationMenu: NavigationMenu): NavigationMenu {
        var index = navigationMenus.indexOf(navigationMenu)
        if (index < 0) {
            return navigationMenu
        }
        index++
        if (index >= navigationMenus.size) {
            index = 0
        }
        return navigationMenus[index]
    }

    fun previous(navigationMenu: NavigationMenu): NavigationMenu {
        var index = navigationMenus.indexOf(navigationMenu)
        if (index < 0) {
            return navigationMenu
        }
        index--
        if (index < 0) {
            index = navigationMenus.size - 1
        }
        return navigationMenus[index]
    }

    fun populateMenuItems(menu: Menu): Unit =
        navigationMenus.forEach {
            val menuItem = menu.add(ordinal, it.ordinal, it.ordinal, it.title)
            menuItem.setIcon(it.icon)
        }

}