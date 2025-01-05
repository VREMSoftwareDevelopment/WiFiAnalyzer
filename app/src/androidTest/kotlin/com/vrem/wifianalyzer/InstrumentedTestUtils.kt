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

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

internal class ChildAtPosition(private val parentMatcher: Matcher<View>, private val position: Int) : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("Child at position $position in parent ")
        parentMatcher.describeTo(description)
    }

    override fun matchesSafely(view: View): Boolean {
        val parent = view.parent
        return (parent is ViewGroup && parentMatcher.matches(parent) && view == parent.getChildAt(position))
    }
}

private const val SLEEP_TIME_SHORT = 5000
private const val SLEEP_TIME_LONG = SLEEP_TIME_SHORT * 3

internal fun pressBackButton() {
    pauseShort()
    Espresso.pressBack()
}

internal fun pauseShort() {
    pause(SLEEP_TIME_SHORT)
}

internal fun pauseLong() {
    pause(SLEEP_TIME_LONG)
}

private fun pause(sleepTime: Int) {
    try {
        Thread.sleep(sleepTime.toLong())
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}
