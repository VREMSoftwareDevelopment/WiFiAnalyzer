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

import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf

private const val NAVIGATION_DRAWER_BUTTON = 0
private const val NAVIGATION_DRAWER_ACTION = 1
private const val NAVIGATION_DRAWER_TAG = "Open navigation drawer"

internal class NavigationInstrumentedTest : Runnable {

    override fun run() {
        listOf(
            2 to "Channel Rating",
            3 to "Channel Graph",
            4 to "Time Graph",
            1 to "Access Points",
            7 to "Available Channels",
            8 to "Vendors"
        ).forEach { (id, title) ->
            selectMenuItem(id, title)
            pauseShort()
        }
        listOf(
            10 to "Settings",
            11 to "About"
        ).forEach { (id, title) ->
            selectMenuItem(id, title)
            pauseShort()
            pressBackButton()
        }
    }

    private fun selectMenuItem(menuItem: Int, expectedTitle: String) {
        onView(
            allOf(
                withContentDescription(NAVIGATION_DRAWER_TAG),
                ChildAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        ChildAtPosition(
                            withClassName(Matchers.`is`("com.google.android.material.appbar.AppBarLayout")),
                            NAVIGATION_DRAWER_BUTTON
                        )
                    ),
                    NAVIGATION_DRAWER_ACTION
                ),
                isDisplayed()
            )
        ).check(matches(isDisplayed())).perform(click())

        onView(
            allOf(
                ChildAtPosition(
                    allOf(
                        withId(com.google.android.material.R.id.design_navigation_view),
                        ChildAtPosition(withId(R.id.nav_drawer), NAVIGATION_DRAWER_BUTTON)
                    ), menuItem
                ),
                isDisplayed()
            )
        ).check(matches(isDisplayed())).perform(click())

        onView(isAssignableFrom(Toolbar::class.java)).check(matches(withToolbarTitle(expectedTitle)))
    }

}