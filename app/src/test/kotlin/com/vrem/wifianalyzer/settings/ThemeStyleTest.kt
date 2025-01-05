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

import com.vrem.wifianalyzer.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ThemeStyleTest {
    @Test
    fun themeStyle() {
        assertThat(ThemeStyle.entries).hasSize(3)
    }

    @Test
    fun theme() {
        assertThat(ThemeStyle.LIGHT.theme).isEqualTo(R.style.ThemeLight)
        assertThat(ThemeStyle.DARK.theme).isEqualTo(R.style.ThemeDark)
        assertThat(ThemeStyle.SYSTEM.theme).isEqualTo(R.style.ThemeSystem)
    }

    @Test
    fun themeNoActionBar() {
        assertThat(ThemeStyle.DARK.themeNoActionBar).isEqualTo(R.style.ThemeDarkNoActionBar)
        assertThat(ThemeStyle.LIGHT.themeNoActionBar).isEqualTo(R.style.ThemeLightNoActionBar)
        assertThat(ThemeStyle.SYSTEM.themeNoActionBar).isEqualTo(R.style.ThemeSystemNoActionBar)
    }

}