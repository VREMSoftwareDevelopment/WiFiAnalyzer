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
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import com.vrem.wifianalyzer.R.id.action_scanner
import org.hamcrest.Matchers.allOf

private const val PAUSE = "Pause"
private const val PLAY = "Play"

internal class ScannerInstrumentedTest : Runnable {

    override fun run() {
        onView(allOf(withId(action_scanner), withContentDescription(PAUSE), isDisplayed())).perform(click())
        pauseShort()
        onView(allOf(withId(action_scanner), withContentDescription(PLAY), isDisplayed())).perform(click())
        pauseShort()
        onView(allOf(withId(action_scanner), withContentDescription(PAUSE), isDisplayed()))
        pauseShort()
    }

}