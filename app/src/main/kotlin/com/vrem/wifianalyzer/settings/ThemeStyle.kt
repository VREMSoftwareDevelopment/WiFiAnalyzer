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
package com.vrem.wifianalyzer.settings

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import com.vrem.wifianalyzer.R

enum class ThemeStyle(@param:StyleRes val theme: Int, @param:StyleRes val themeNoActionBar: Int, @param:ColorInt val colorGraphText: Int) {
    DARK(R.style.ThemeDark, R.style.ThemeDarkNoActionBar, Color.WHITE),
    LIGHT(R.style.ThemeLight, R.style.ThemeLightNoActionBar, Color.BLACK),
    SYSTEM(R.style.ThemeSystem, R.style.ThemeSystemNoActionBar, Color.GRAY);
}
