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

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.vrem.wifianalyzer.R
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@RunWith(Parameterized::class)
class ThemeStyleParameterizedTest(
    private val themeStyle: ThemeStyle,
    private val expectedTheme: Int,
    private val expectedNightMode: Int
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> = listOf(
            arrayOf(ThemeStyle.DARK, R.style.ThemeSystemNoActionBar, AppCompatDelegate.MODE_NIGHT_YES),
            arrayOf(ThemeStyle.LIGHT, R.style.ThemeSystemNoActionBar, AppCompatDelegate.MODE_NIGHT_NO),
            arrayOf(ThemeStyle.SYSTEM, R.style.ThemeSystemNoActionBar, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM),
            arrayOf(ThemeStyle.BLACK, R.style.ThemeBlackNoActionBar, AppCompatDelegate.MODE_NIGHT_YES)
        )
    }

    @Test
    fun setThemeParameterized() {
        // Arrange
        val activity: AppCompatActivity = mock()
        Mockito.mockStatic(AppCompatDelegate::class.java).use { mockedStatic ->
            // Act
            themeStyle.setTheme(activity)
            // Assert
            verify(activity).setTheme(expectedTheme)
            mockedStatic.verify { AppCompatDelegate.setDefaultNightMode(expectedNightMode) }
        }
    }
}
