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
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.filter.adapter.WiFiBandAdapter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class WiFiBandFilterTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val alertDialog =
        AlertDialog
            .Builder(mainActivity)
            .setView(R.layout.filter_wifi_band)
            .create()
            .also { it.show() }
    private val adapter = WiFiBandAdapter(emptySet())

    @Test
    fun idsMapEachBandToItsView() {
        // Arrange
        val expected =
            mapOf(
                WiFiBand.GHZ2 to R.id.filterWifiBand2,
                WiFiBand.GHZ5 to R.id.filterWifiBand5,
                WiFiBand.GHZ6 to R.id.filterWifiBand6,
            )
        // Act
        val fixture = WiFiBandFilter(adapter, alertDialog)
        // Assert
        assertThat(fixture.ids).containsExactlyInAnyOrderEntriesOf(expected)
    }

    @Test
    fun initMakesFilterContainerVisible() {
        // Act
        WiFiBandFilter(adapter, alertDialog)
        // Assert
        assertThat(alertDialog.findViewById<View>(R.id.filterWiFiBand).visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun clickTogglesSelection() {
        // Arrange
        WiFiBandFilter(adapter, alertDialog)
        // Act
        alertDialog.findViewById<View>(R.id.filterWifiBand5).performClick()
        // Assert
        assertThat(adapter.color(WiFiBand.GHZ5)).isEqualTo(R.color.selected)
    }
}
