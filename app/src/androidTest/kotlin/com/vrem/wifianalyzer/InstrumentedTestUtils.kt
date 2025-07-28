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
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

private const val SLEEP_1_SECOND = 1000
private const val SLEEP_3_SECONDS = 3000

internal class ChildAtPosition(
    val parentMatcher: Matcher<View>,
    val position: Int,
) : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("Child at position $position in parent ")
        parentMatcher.describeTo(description)
    }

    override fun matchesSafely(view: View): Boolean {
        val parent = view.parent
        return (parent is ViewGroup && parentMatcher.matches(parent) && view == parent.getChildAt(position))
    }
}

internal fun withToolbarTitle(expectedTitle: CharSequence): Matcher<View> =
    object : BoundedMatcher<View, Toolbar>(Toolbar::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("with toolbar title: $expectedTitle")
        }

        override fun matchesSafely(toolbar: Toolbar): Boolean = toolbar.title == expectedTitle
    }

internal fun pressBackButton() = pressBack()

internal fun pauseShort() = pause(SLEEP_1_SECOND)

internal fun pauseLong() = pause(SLEEP_3_SECONDS)

private fun pause(sleepTime: Int) = runCatching { Thread.sleep(sleepTime.toLong()) }.getOrElse { it.printStackTrace() }
