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
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

internal class ChannelRatingInstrumentedTest : Runnable {
    override fun run() {
        navigateToBottomNav(R.id.nav_bottom_channel_rating)
        verifyToolbarTitle("Channel Rating")
        verifyChannelListDisplayed()
        verifyBestChannels()
        verifyCurrentConnectionDisplayed()
    }

    private fun verifyChannelListDisplayed() {
        onView(withId(R.id.channelRatingView)).check(matches(isDisplayed()))
    }

    private fun verifyBestChannels() {
        onView(withText("Best Channels:")).check(matches(isDisplayed()))
    }
}
