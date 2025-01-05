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
package com.vrem.wifianalyzer.navigation.availability

import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R

internal val navigationOptionWiFiSwitchOff: NavigationOption = {
    updateMenuItem(it, false)
}

internal val navigationOptionWiFiSwitchOn: NavigationOption = {
    updateMenuItem(it, true)
}

private fun updateMenuItem(mainActivity: MainActivity, visible: Boolean) {
    mainActivity.optionMenu.menu?.let {
        val menuItem = it.findItem(R.id.action_wifi_band)
        menuItem.isVisible = visible
        if (visible) {
            val wiFiBand = MainContext.INSTANCE.settings.wiFiBand()
            val title = mainActivity.getString(wiFiBand.textResource)
            menuItem.title = title.replace(' ', '\n')
        }
    }
}

