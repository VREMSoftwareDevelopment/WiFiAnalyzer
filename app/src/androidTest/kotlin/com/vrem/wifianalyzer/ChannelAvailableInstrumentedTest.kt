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
package com.vrem.wifianalyzer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf

internal class ChannelAvailableInstrumentedTest : Runnable {
    override fun run() {
        selectMenuItem(7, "Available Channels")
        verify2GHzSection()
        verify5GHzSection()
        verify6GHzSection()
        pressBack()
    }

    private fun verify2GHzSection() {
        onView(allOf(withText("2.4 GHz"), isDisplayed())).check(matches(isDisplayed()))
    }

    private fun verify5GHzSection() {
        onView(allOf(withText("5 GHz"), isDisplayed())).check(matches(isDisplayed()))
    }

    private fun verify6GHzSection() {
        onView(allOf(withText("6 GHz"), isDisplayed())).check(matches(isDisplayed()))
    }
}
