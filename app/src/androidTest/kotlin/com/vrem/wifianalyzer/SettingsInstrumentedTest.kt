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

import android.widget.EditText
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf

private const val COUNTRY = "Country"
private const val MATCHING_COUNTRY = "Canada"

internal class SettingsInstrumentedTest : Runnable {
    override fun run() {
        selectMenuItem(R.id.nav_drawer_settings, "Settings")
        verifySettings()
        verifyCountrySearch()
        pressBack()
    }

    private fun verifySettings() {
        listOf(
            "Scan Interval",
            "Sort Access Points By",
            "Group Access Points By",
            "Connection Display",
            "Access Point Display",
            "Graph Maximum Signal Strength",
            "Theme",
            "Keep screen on",
            COUNTRY,
            "Language",
            "Reset",
        ).forEach { scrollToAndVerify(it) }
    }

    private fun verifyCountrySearch() {
        scrollToAndVerify(COUNTRY)
        onView(withText(COUNTRY)).perform(click())
        onView(allOf(isAssignableFrom(EditText::class.java), isDescendantOfA(withId(R.id.customPreferenceSearchText))))
            .perform(typeText(MATCHING_COUNTRY))
        closeSoftKeyboard()
        onView(countryRow(MATCHING_COUNTRY)).check(matches(isDisplayed()))
        onView(withId(android.R.id.list)).check(matches(hasChildCount(1)))
        pressBack()
    }

    private fun countryRow(name: String) = allOf(withText(name), isDescendantOfA(withId(android.R.id.list)))
}
