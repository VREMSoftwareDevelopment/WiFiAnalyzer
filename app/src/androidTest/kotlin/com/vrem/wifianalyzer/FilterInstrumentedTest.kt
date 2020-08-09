/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers

internal class FilterInstrumentedTest : Runnable {
    override fun run() {
        pauseShort()
        actionOpen().perform(ViewActions.click())
        pauseLong()
        actionClose().perform(ViewActions.scrollTo(), ViewActions.click())
    }

    private fun actionClose(): ViewInteraction {
        val button3 = ViewMatchers.withId(android.R.id.button3)
        val filterClose = ViewMatchers.withText(FILTER_CLOSE_TAG)
        val scrollView = ViewMatchers.withClassName(Matchers.`is`("android.widget.ScrollView"))
        val filterButtonClose = ChildAtPosition(scrollView, FILTER_BUTTON_CLOSE)
        val filterActionClose = ChildAtPosition(filterButtonClose, FILTER_ACTION)
        return Espresso.onView(Matchers.allOf(button3, filterClose, filterActionClose))
    }

    private fun actionOpen(): ViewInteraction {
        val actionFilter = ViewMatchers.withId(R.id.action_filter)
        val filterButtonTag = ViewMatchers.withContentDescription(FILTER_BUTTON_TAG)
        val toolbar = ViewMatchers.withId(R.id.toolbar)
        val filterButtonOpen = ChildAtPosition(toolbar, FILTER_BUTTON_OPEN)
        val filterActionOpen = ChildAtPosition(filterButtonOpen, FILTER_ACTION)
        return Espresso.onView(Matchers.allOf(actionFilter, filterButtonTag, filterActionOpen, ViewMatchers.isDisplayed()))
    }

    companion object {
        private const val FILTER_BUTTON_OPEN = 2
        private const val FILTER_BUTTON_CLOSE = 0
        private const val FILTER_ACTION = 0
        private const val FILTER_BUTTON_TAG = "FilterInstrumentedTest"
        private const val FILTER_CLOSE_TAG = "Close"
    }
}