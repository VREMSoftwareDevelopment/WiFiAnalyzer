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
package com.vrem.wifianalyzer.settings

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.vrem.wifianalyzer.R

enum class ThemeStyle(
    @param:ColorInt val colorGraphText: Int,
    @param:StyleRes private val theme: Int,
    private val nightMode: Int,
) {
    DARK(Color.WHITE, R.style.ThemeSystemNoActionBar, AppCompatDelegate.MODE_NIGHT_YES),
    LIGHT(Color.BLACK, R.style.ThemeSystemNoActionBar, AppCompatDelegate.MODE_NIGHT_NO),
    SYSTEM(Color.GRAY, R.style.ThemeSystemNoActionBar, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM),
    BLACK(Color.WHITE, R.style.ThemeBlackNoActionBar, AppCompatDelegate.MODE_NIGHT_YES),
    ;

    fun setTheme(activity: AppCompatActivity) {
        activity.setTheme(theme)
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}
