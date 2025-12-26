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

import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

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

private const val SLEEP_1_SECOND = 1000

internal fun pauseShort() = pause(SLEEP_1_SECOND)

private const val SLEEP_20_SECONDS = 20000

internal fun pauseLong() = pause(SLEEP_20_SECONDS)

private fun pause(sleepTime: Int) = runCatching { Thread.sleep(sleepTime.toLong()) }.getOrElse { it.printStackTrace() }

internal fun verifyCurrentConnectionDisplayed() {
    onView(withId(R.id.connection)).check(matches(isDisplayed()))
}

internal fun navigateToBottomNav(navId: Int) {
    onView(allOf(withId(navId), isDisplayed())).perform(click())
    pauseShort()
}

internal fun verifyToolbarTitle(expectedTitle: String) {
    onView(isAssignableFrom(Toolbar::class.java)).check(matches(withToolbarTitle(expectedTitle)))
}

internal fun dismissPopup() {
    onView(withText("OK")).check(matches(isDisplayed()))
    onView(withText("OK")).perform(click())
    pauseShort()
    onView(withText("OK")).check(doesNotExist())
}

internal fun clickAtPosition(
    xPercent: Float,
    yPercent: Float,
): ViewAction =
    GeneralClickAction(
        Tap.SINGLE,
        { view ->
            val screenPos = IntArray(2)
            view.getLocationOnScreen(screenPos)
            val x = screenPos[0] + view.width * xPercent
            val y = screenPos[1] + view.height * yPercent
            floatArrayOf(x, y)
        },
        Press.FINGER,
        InputDevice.SOURCE_UNKNOWN,
        MotionEvent.BUTTON_PRIMARY,
    )

private const val NAVIGATION_DRAWER_BUTTON = 0
private const val NAVIGATION_DRAWER_ACTION = 1
private const val NAVIGATION_DRAWER_TAG = "Open navigation drawer"

internal fun selectMenuItem(
    menuItem: Int,
    expectedTitle: String,
) {
    onView(
        allOf(
            withContentDescription(NAVIGATION_DRAWER_TAG),
            ChildAtPosition(
                allOf(
                    withId(R.id.toolbar),
                    ChildAtPosition(
                        withClassName(Matchers.`is`("com.google.android.material.appbar.AppBarLayout")),
                        NAVIGATION_DRAWER_BUTTON,
                    ),
                ),
                NAVIGATION_DRAWER_ACTION,
            ),
            isDisplayed(),
        ),
    ).check(matches(isDisplayed())).perform(click())

    onView(
        allOf(
            ChildAtPosition(
                allOf(
                    withId(com.google.android.material.R.id.design_navigation_view),
                    ChildAtPosition(withId(R.id.nav_drawer), NAVIGATION_DRAWER_BUTTON),
                ),
                menuItem,
            ),
            isDisplayed(),
        ),
    ).check(matches(isDisplayed())).perform(click())

    onView(isAssignableFrom(Toolbar::class.java)).check(matches(withToolbarTitle(expectedTitle)))
}

internal fun scrollToAndVerify(text: String, recyclerViewId: Int = androidx.preference.R.id.recycler_view) {
    onView(withId(recyclerViewId))
        .perform(RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText(text))))
    onView(withText(text)).check(matches(isDisplayed()))
}
