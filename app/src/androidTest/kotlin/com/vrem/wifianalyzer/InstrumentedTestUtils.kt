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
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

private const val PAUSE_1_SECOND = 1000L
private const val PAUSE_20_SECONDS = 20000L

/**
 * Returns a matcher that matches a Toolbar with the given title.
 * @param expectedTitle The expected toolbar title.
 * @return Matcher for Toolbar with the specified title.
 */
internal fun withToolbarTitle(expectedTitle: CharSequence): Matcher<View> =
    object : BoundedMatcher<View, Toolbar>(Toolbar::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("with toolbar title: $expectedTitle")
        }

        override fun matchesSafely(toolbar: Toolbar): Boolean = toolbar.title == expectedTitle
    }

/**
 * Custom Espresso ViewAction to wait for a given duration.
 */
fun waitFor(delay: Long): ViewAction =
    object : ViewAction {
        override fun getConstraints() = isRoot()

        override fun getDescription() = "Wait for $delay milliseconds."

        override fun perform(
            uiController: UiController,
            view: View?,
        ) {
            uiController.loopMainThreadForAtLeast(delay)
        }
    }

/**
 * Pauses the test execution for a short duration (1 second) using Espresso ViewAction.
 */
internal fun pauseShort() {
    onView(isRoot()).perform(waitFor(PAUSE_1_SECOND))
}

/**
 * Pauses the test execution for a long duration (20 seconds) using Espresso ViewAction.
 */
internal fun pauseLong() {
    onView(isRoot()).perform(waitFor(PAUSE_20_SECONDS))
}

/**
 * Verifies that the current connection view is displayed.
 */
internal fun verifyCurrentConnectionDisplayed() {
    onView(withId(R.id.connection)).check(matches(isDisplayed()))
}

/**
 * Navigates to a bottom navigation item by its ID and pauses briefly.
 * @param navId The resource ID of the navigation item.
 */
internal fun navigateToBottomNav(navId: Int) {
    onView(allOf(withId(navId), isDisplayed())).perform(click())
    pauseShort()
}

/**
 * Verifies that the toolbar displays the expected title.
 * @param expectedTitle The expected toolbar title.
 */
internal fun verifyToolbarTitle(expectedTitle: String) {
    onView(isAssignableFrom(Toolbar::class.java)).check(matches(withToolbarTitle(expectedTitle)))
}

/**
 * Dismisses a popup dialog with an "OK" button and verifies it is no longer displayed.
 */
internal fun dismissPopup() {
    onView(withText("OK")).check(matches(isDisplayed()))
    onView(withText("OK")).perform(click())
    pauseShort()
    onView(withText("OK")).check(doesNotExist())
}

/**
 * Returns a ViewAction that clicks at a specific percentage position within a view.
 * @param xPercent The X position as a percentage (0.0 to 1.0).
 * @param yPercent The Y position as a percentage (0.0 to 1.0).
 * @return ViewAction that performs the click.
 */
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

/**
 * Selects a navigation drawer menu item by its resource ID and verifies the toolbar title.
 * @param menuItemId The resource ID of the menu item in the drawer.
 * @param expectedTitle The expected toolbar title after selection.
 */
internal fun selectMenuItem(
    menuItemId: Int,
    expectedTitle: String,
) {
    onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
    onView(allOf(withId(menuItemId), isDisplayed())).check(matches(isDisplayed())).perform(click())
    onView(isAssignableFrom(Toolbar::class.java)).check(matches(withToolbarTitle(expectedTitle)))
}

/**
 * Scrolls to a specific item in a RecyclerView and verifies it is displayed.
 * @param text The text of the item to scroll to.
 * @param recyclerViewId The resource ID of the RecyclerView (default is preference recycler view).
 */
internal fun scrollToAndVerify(
    text: String,
    recyclerViewId: Int = androidx.preference.R.id.recycler_view,
) {
    onView(withId(recyclerViewId))
        .perform(RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText(text))))
    onView(withText(text)).check(matches(isDisplayed()))
}

/**
 * Resets the WiFiAnalyzer filters to default.
 */
internal fun resetFilters() {
    onView(allOf(withId(R.id.action_filter), isDisplayed())).perform(click())
    onView(allOf(withId(android.R.id.button2), isDisplayed())).perform(click())
}

/**
 * Ensures the scanner is running (pause and resume to reset state).
 */
internal fun resetScannerState() {
    onView(withId(R.id.action_scanner)).check(matches(isDisplayed()))
    onView(withId(R.id.action_scanner)).perform(click()) // Pause if running
    onView(withId(R.id.action_scanner)).perform(click()) // Resume
}

/**
 * Resets the app settings to default using the Settings screen.
 */
internal fun resetSettings() {
    selectMenuItem(R.id.nav_drawer_settings, "Settings")
    scrollToAndVerify("Reset")
    onView(withText("Reset")).perform(click())
    pressBack()
}

/**
 * Navigates to the Access Points (home) screen.
 */
internal fun returnToHome() {
    navigateToBottomNav(R.id.nav_bottom_access_points)
}
