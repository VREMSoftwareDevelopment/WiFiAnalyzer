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
package com.vrem.wifianalyzer.navigation.options

import android.annotation.SuppressLint
import android.app.Activity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.menu.MenuBuilder
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.R

@OpenClass
class OptionMenu {
    var menu: Menu? = null

    fun create(activity: Activity, menu: Menu) {
        activity.menuInflater.inflate(R.menu.optionmenu, menu)
        this.menu = menu
        iconsVisible(menu)
    }

    fun select(item: MenuItem): Unit = OptionAction.findOptionAction(item.itemId).action()

    @SuppressLint("RestrictedApi")
    private fun iconsVisible(menu: Menu) {
        try {
            (menu as MenuBuilder).setOptionalIconsVisible(true)
        } catch (e: Exception) {
            // do nothing
        }
    }
}