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
import androidx.core.view.forEach
import androidx.core.view.get
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.R

@OpenClass
class NavigationMenuController(
    navigationMenuControl: NavigationMenuControl,
    val drawerNavigationView: NavigationView = navigationMenuControl.findViewById(R.id.nav_drawer),
    val bottomNavigationView: BottomNavigationView = navigationMenuControl.findViewById(R.id.nav_bottom),
) {
    private lateinit var currentNavigationMenu: NavigationMenu

    fun currentMenuItem(): MenuItem = drawerNavigationView.menu[currentNavigationMenu.ordinal]

    fun currentNavigationMenu(): NavigationMenu = currentNavigationMenu

    fun currentNavigationMenu(navigationMenu: NavigationMenu) {
        currentNavigationMenu = navigationMenu
        setChecked(drawerNavigationView.menu, navigationMenu.idDrawer)
        setChecked(bottomNavigationView.menu, navigationMenu.idBottom)
    }

    private fun setChecked(
        menu: Menu,
        id: Int,
    ) {
        if (id != -1) {
            menu.forEach { it.isChecked = false }
            menu.findItem(id)!!.isChecked = true
        }
    }

    init {
        drawerNavigationView.setNavigationItemSelectedListener(navigationMenuControl)
        bottomNavigationView.setOnItemSelectedListener(navigationMenuControl)
    }
}
