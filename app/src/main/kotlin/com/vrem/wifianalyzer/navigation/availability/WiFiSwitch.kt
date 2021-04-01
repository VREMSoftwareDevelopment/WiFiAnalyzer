/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import androidx.core.text.parseAsHtml
import com.vrem.util.EMPTY
import com.vrem.util.compatColor
import com.vrem.util.toHtml
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.navigation.options.OptionMenu
import com.vrem.wifianalyzer.wifi.band.WiFiBand

internal val navigationOptionWiFiSwitchOff: NavigationOption = {
    it.supportActionBar?.let { actionBar ->
        actionBar.subtitle = String.EMPTY
    }
    menu(it.optionMenu, false)
}

internal val navigationOptionWiFiSwitchOn: NavigationOption = {
    actionBarOn(it)
    menu(it.optionMenu, true)
}

private fun menu(optionMenu: OptionMenu, visible: Boolean) {
    optionMenu.menu?.let {
        it.findItem(R.id.action_wifi_band).isVisible = visible
    }
}

private fun actionBarOn(mainActivity: MainActivity) {
    mainActivity.supportActionBar?.let {
        val colorSelected = mainActivity.compatColor(R.color.selected)
        val colorNotSelected = mainActivity.compatColor(R.color.regular)
        val resources = mainActivity.resources
        val wiFiBand2 = resources.getString(WiFiBand.GHZ2.textResource)
        val wiFiBand5 = resources.getString(WiFiBand.GHZ5.textResource)
        val wiFiBand = MainContext.INSTANCE.settings.wiFiBand()
        val subtitle = makeSubtitle(WiFiBand.GHZ2 == wiFiBand, wiFiBand2, wiFiBand5, colorSelected, colorNotSelected)
        it.subtitle = subtitle.parseAsHtml()
    }
}

private const val SPACER = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"

internal fun makeSubtitle(wiFiBand2Selected: Boolean, wiFiBand2: String, wiFiBand5: String, colorSelected: Int, colorNotSelected: Int): String =
        if (wiFiBand2Selected) {
            wiFiBand2.toHtml(colorSelected, false) + SPACER + wiFiBand5.toHtml(colorNotSelected, true)
        } else {
            wiFiBand2.toHtml(colorNotSelected, true) + SPACER + wiFiBand5.toHtml(colorSelected, false)
        }

