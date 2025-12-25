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

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

internal class SettingsInstrumentedTest : Runnable {
    override fun run() {
        selectMenuItem(10, "Settings")
        verifyVisibleSettings()
        verifyScrollableSettings()
        pressBack()
    }

    private fun verifyVisibleSettings() {
        onView(withText("Scan Interval")).check(matches(isDisplayed()))
        onView(withText("Sort Access Points By")).check(matches(isDisplayed()))
        onView(withText("Group Access Points By")).check(matches(isDisplayed()))
        onView(withText("Connection Display")).check(matches(isDisplayed()))
        onView(withText("Access Point Display")).check(matches(isDisplayed()))
        onView(withText("Graph Maximum Signal Strength")).check(matches(isDisplayed()))
        onView(withText("Channel Graph Legend Display")).check(matches(isDisplayed()))
        onView(withText("Time Graph Legend Display")).check(matches(isDisplayed()))
    }

    private fun verifyScrollableSettings() {
        scrollToAndVerify("Theme")
        scrollToAndVerify("Keep screen on")
        scrollToAndVerify("Country")
        scrollToAndVerify("Language")
        scrollToAndVerify("Reset")
    }

    private fun scrollToAndVerify(text: String) {
        onView(withId(androidx.preference.R.id.recycler_view))
            .perform(RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText(text))))
        onView(withText(text)).check(matches(isDisplayed()))
    }
}
