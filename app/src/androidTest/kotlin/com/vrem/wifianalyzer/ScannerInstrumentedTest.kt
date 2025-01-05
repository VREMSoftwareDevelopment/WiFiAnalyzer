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

internal class ScannerInstrumentedTest : Runnable {
    override fun run() {
        scannerAction(SCANNER_PAUSE_TAG)
        pauseLong()
        scannerAction(SCANNER_RESUME_TAG)
    }

    private fun scannerAction(tag: String) {
        pauseShort()
        val actionMenuItemView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.action_scanner),
                ViewMatchers.withContentDescription(tag),
                ChildAtPosition(
                    ChildAtPosition(ViewMatchers.withId(R.id.toolbar), SCANNER_BUTTON),
                    SCANNER_ACTION
                ),
                ViewMatchers.isDisplayed()
            )
        )
        actionMenuItemView.perform(ViewActions.click())
    }

    companion object {
        private const val SCANNER_BUTTON = 2
        private const val SCANNER_ACTION = 1
        private const val SCANNER_PAUSE_TAG = "Pause"
        private const val SCANNER_RESUME_TAG = "Play"
    }
}