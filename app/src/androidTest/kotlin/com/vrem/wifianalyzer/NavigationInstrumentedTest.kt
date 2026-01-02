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
package com.vrem.wifianalyzer

import androidx.test.espresso.Espresso.pressBack

internal class NavigationInstrumentedTest : Runnable {
    override fun run() {
        listOf(
            R.id.nav_drawer_channel_rating to "Channel Rating",
            R.id.nav_drawer_channel_graph to "Channel Graph",
            R.id.nav_drawer_time_graph to "Time Graph",
            R.id.nav_drawer_access_points to "Access Points",
            R.id.nav_drawer_channel_available to "Available Channels",
            R.id.nav_drawer_vendors to "Vendors",
        ).forEach { (menuItemId, title) ->
            selectMenuItem(menuItemId, title)
        }
        listOf(
            R.id.nav_drawer_settings to "Settings",
            R.id.nav_drawer_about to "About",
        ).forEach { (menuItemId, title) ->
            selectMenuItem(menuItemId, title)
            pressBack()
        }
    }
}
