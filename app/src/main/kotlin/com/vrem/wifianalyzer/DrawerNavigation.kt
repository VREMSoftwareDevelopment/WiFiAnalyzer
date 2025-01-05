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
package com.vrem.wifianalyzer

import android.content.res.Configuration
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.vrem.annotation.OpenClass

@OpenClass
class DrawerNavigation(private val mainActivity: MainActivity, private val toolbar: Toolbar) {
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    fun syncState() = actionBarDrawerToggle.syncState()

    fun onConfigurationChanged(newConfig: Configuration?) =
        actionBarDrawerToggle.onConfigurationChanged(newConfig)

    fun create() {
        val drawer = mainActivity.findViewById<DrawerLayout>(R.id.drawer_layout)
        actionBarDrawerToggle = createDrawerToggle(drawer)
        drawer.addDrawerListener(actionBarDrawerToggle)
        syncState()
    }

    fun createDrawerToggle(drawer: DrawerLayout): ActionBarDrawerToggle =
        ActionBarDrawerToggle(mainActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

}