/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.graphics.drawable.Drawable
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R

@OpenClass
internal class FilterOn : NavigationOption {
    override fun apply(mainActivity: MainActivity) {
        val menu = mainActivity.optionMenu.menu
        if (menu != null) {
            val menuItem = menu.findItem(R.id.action_filter)
            menuItem.isVisible = true
            setIconColor(mainActivity, menuItem, MainContext.INSTANCE.filterAdapter.isActive)
        }
    }

    private fun setIconColor(mainActivity: MainActivity, menuItem: MenuItem, active: Boolean) {
        val color = getColor(mainActivity, if (active) R.color.selected else R.color.regular)
        setTint(menuItem.icon, color)
    }

    fun getColor(mainActivity: MainActivity?, color: Int): Int {
        return ContextCompat.getColor(mainActivity!!, color)
    }

    fun setTint(drawable: Drawable?, color: Int) {
        DrawableCompat.setTint(drawable!!, color)
    }
}