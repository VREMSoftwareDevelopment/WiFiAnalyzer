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

import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.R

@OpenClass
class NavigationMenuController(
    navigationMenuControl: NavigationMenuControl,
    val navigationView: NavigationView = navigationMenuControl.findViewById(R.id.nav_drawer),
    val bottomNavigationView: BottomNavigationView = navigationMenuControl.findViewById(R.id.nav_bottom)
) {

    private lateinit var currentNavigationMenu: NavigationMenu

    fun currentMenuItem(): MenuItem = navigationView.menu.getItem(currentNavigationMenu.ordinal)

    fun currentNavigationMenu(): NavigationMenu = currentNavigationMenu

    fun currentNavigationMenu(navigationMenu: NavigationMenu) {
        currentNavigationMenu = navigationMenu
        selectCurrentMenuItem(navigationMenu, navigationView.menu)
        selectCurrentMenuItem(navigationMenu, bottomNavigationView.menu)
    }

    private fun selectCurrentMenuItem(navigationMenu: NavigationMenu, menu: Menu) {
        for (i in 0 until menu.size()) {
            val menuItem: MenuItem = menu.getItem(i)
            menuItem.isCheckable = false
            menuItem.isChecked = false
        }
        menu.findItem(navigationMenu.ordinal)?.let {
            it.isCheckable = true
            it.isChecked = true
        }
    }

    init {
        NavigationGroup.entries.forEach { it.populateMenuItems(navigationView.menu) }
        @Suppress("LeakingThis")
        navigationView.setNavigationItemSelectedListener(navigationMenuControl)
        @Suppress("LeakingThis")
        NavigationGroup.GROUP_FEATURE.populateMenuItems(bottomNavigationView.menu)
        @Suppress("LeakingThis")
        bottomNavigationView.setOnItemSelectedListener(navigationMenuControl)
    }
}