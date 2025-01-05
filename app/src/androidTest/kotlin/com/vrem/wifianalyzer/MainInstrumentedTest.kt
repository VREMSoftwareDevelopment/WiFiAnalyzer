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

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainInstrumentedTest {

    @get:Rule
    val activityTestRule: ActivityScenarioRule<MainActivity> = activityScenarioRule()

    @Test
    fun navigation() {
        pauseShort()
        NavigationInstrumentedTest().run()
        pauseShort()
    }

    @Test
    fun scanner() {
        pauseShort()
        ScannerInstrumentedTest().run()
        pauseShort()
    }

    @Test
    fun filter() {
        pauseShort()
        FilterInstrumentedTest().run()
        pauseShort()
    }
}