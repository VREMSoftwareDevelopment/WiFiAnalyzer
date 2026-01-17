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

import androidx.appcompat.app.AppCompatDelegate
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Assert.assertEquals

internal class ThemeInstrumentedTest : Runnable {
    override fun run() {
        listOf(
            "Light" to AppCompatDelegate.MODE_NIGHT_NO,
            "System" to AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            "Black" to AppCompatDelegate.MODE_NIGHT_YES,
            "Dark" to AppCompatDelegate.MODE_NIGHT_YES,
        ).forEach { (themeName, expectedNightMode) ->
            changeThemeAndVerify(themeName, expectedNightMode)
        }
    }

    private fun changeThemeAndVerify(
        themeName: String,
        expectedNightMode: Int,
    ) {
        selectMenuItem(R.id.nav_drawer_settings, "Settings")
        scrollToAndVerify("Theme")
        onView(withText("Theme")).perform(click())
        pauseShort()
        onView(withText(themeName)).check(matches(isDisplayed()))
        onView(withText(themeName)).perform(click())
        pauseShort()
        assertEquals(
            "Theme $themeName should set night mode to $expectedNightMode",
            expectedNightMode,
            AppCompatDelegate.getDefaultNightMode(),
        )
    }
}
