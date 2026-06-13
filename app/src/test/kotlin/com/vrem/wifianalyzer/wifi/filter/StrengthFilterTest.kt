/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2026 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.filter

import android.app.AlertDialog
import android.os.Build
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.filter.adapter.StrengthAdapter
import com.vrem.wifianalyzer.wifi.model.Strength
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class StrengthFilterTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val alertDialog =
        AlertDialog
            .Builder(mainActivity)
            .setView(R.layout.filter_strength)
            .create()
            .also { it.show() }
    private val adapter = StrengthAdapter(emptySet())

    @Test
    fun idsMapEachStrengthToItsView() {
        // Arrange
        val expected =
            mapOf(
                Strength.ZERO to R.id.filterStrength0,
                Strength.ONE to R.id.filterStrength1,
                Strength.TWO to R.id.filterStrength2,
                Strength.THREE to R.id.filterStrength3,
                Strength.FOUR to R.id.filterStrength4,
            )
        // Act
        val fixture = StrengthFilter(adapter, alertDialog)
        // Assert
        assertThat(fixture.ids).containsExactlyInAnyOrderEntriesOf(expected)
    }

    @Test
    fun initMakesFilterContainerVisible() {
        // Act
        StrengthFilter(adapter, alertDialog)
        // Assert
        assertThat(alertDialog.findViewById<View>(R.id.filterStrength).visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun clickTogglesSelection() {
        // Arrange
        StrengthFilter(adapter, alertDialog)
        // Act
        alertDialog.findViewById<View>(R.id.filterStrength2).performClick()
        // Assert
        assertThat(adapter.color(Strength.TWO)).isEqualTo(Strength.TWO.colorResource)
    }
}
