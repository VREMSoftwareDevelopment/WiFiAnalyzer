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

import android.widget.ListView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.assertj.core.api.Assertions.assertThat

internal class VendorInstrumentedTest : Runnable {
    override fun run() {
        selectMenuItem(R.id.nav_drawer_vendors, "Vendors")
        verifySearchField()
        val totalCount = getListCount()
        verifySearch(totalCount)
        verifyClearSearch(totalCount)
        pressBack()
    }

    private fun verifySearchField() {
        onView(withId(R.id.vendorSearchText)).check(matches(isDisplayed()))
    }

    private fun verifySearch(totalCount: Int) {
        onView(withHint("00:0C:41 CISCO")).perform(replaceText("CISCO"), closeSoftKeyboard())
        pauseShort()
        val filteredCount = getListCount()
        assertThat(filteredCount)
            .withFailMessage("Filtered count ($filteredCount) should be less than initial count ($totalCount)")
            .isLessThan(totalCount)
    }

    private fun verifyClearSearch(totalCount: Int) {
        onView(withHint("00:0C:41 CISCO")).perform(clearText())
        pauseShort()
        val restoredCount = getListCount()
        assertThat(restoredCount)
            .withFailMessage("Restored count ($restoredCount) should equal initial count ($totalCount)")
            .isEqualTo(totalCount)
    }

    private fun getListCount(): Int {
        var count = 0
        onView(withId(android.R.id.list)).check { view, _ ->
            count = (view as ListView).adapter.count
        }
        return count
    }
}
