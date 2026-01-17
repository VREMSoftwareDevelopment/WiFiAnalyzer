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
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ThemeStyleTest {
    @Test
    fun themeStyle() {
        assertThat(ThemeStyle.entries)
            .hasSize(4)
            .containsExactly(ThemeStyle.DARK, ThemeStyle.LIGHT, ThemeStyle.SYSTEM, ThemeStyle.BLACK)
    }

    @Test
    fun colorGraphText() {
        assertThat(ThemeStyle.DARK.colorGraphText).isEqualTo(Color.WHITE)
        assertThat(ThemeStyle.LIGHT.colorGraphText).isEqualTo(Color.BLACK)
        assertThat(ThemeStyle.SYSTEM.colorGraphText).isEqualTo(Color.GRAY)
        assertThat(ThemeStyle.BLACK.colorGraphText).isEqualTo(Color.WHITE)
    }

    @Test
    fun themeStyleOrdinal() {
        assertThat(ThemeStyle.DARK.ordinal).isEqualTo(0)
        assertThat(ThemeStyle.LIGHT.ordinal).isEqualTo(1)
        assertThat(ThemeStyle.SYSTEM.ordinal).isEqualTo(2)
        assertThat(ThemeStyle.BLACK.ordinal).isEqualTo(3)
    }
}
