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

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers

internal class NavigationInstrumentedTest : Runnable {
    override fun run() {
        selectMenuItem(CHANNEL_RATING)
        selectMenuItem(CHANNEL_GRAPH)
        selectMenuItem(TIME_GRAPH)
        pauseLong()
        selectMenuItem(AVAILABLE_CHANNELS)
        selectMenuItem(VENDORS)
        selectMenuItem(ACCESS_POINTS)
        selectMenuItem(SETTINGS)
        pressBackButton()
        selectMenuItem(ABOUT)
        pressBackButton()
    }

    private fun selectMenuItem(menuItem: Int) {
        pauseShort()
        val appCompatImageButton = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription(NAVIGATION_DRAWER_TAG),
                ChildAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.toolbar),
                        ChildAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("com.google.android.material.appbar.AppBarLayout")),
                            NAVIGATION_DRAWER_BUTTON
                        )
                    ),
                    NAVIGATION_DRAWER_ACTION
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatImageButton.perform(ViewActions.click())
        pauseShort()
        val navigationMenuItemView = Espresso.onView(
            Matchers.allOf(
                ChildAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(com.google.android.material.R.id.design_navigation_view),
                        ChildAtPosition(ViewMatchers.withId(R.id.nav_drawer), NAVIGATION_DRAWER_BUTTON)
                    ), menuItem
                ),
                ViewMatchers.isDisplayed()
            )
        )
        navigationMenuItemView.perform(ViewActions.click())
    }

    companion object {
        private const val ACCESS_POINTS = 1
        private const val CHANNEL_RATING = 2
        private const val CHANNEL_GRAPH = 3
        private const val TIME_GRAPH = 4
        private const val AVAILABLE_CHANNELS = 7
        private const val VENDORS = 8
        private const val SETTINGS = 10
        private const val ABOUT = 11
        private const val NAVIGATION_DRAWER_BUTTON = 0
        private const val NAVIGATION_DRAWER_ACTION = 1
        private const val NAVIGATION_DRAWER_TAG = "Open navigation drawer"
    }
}