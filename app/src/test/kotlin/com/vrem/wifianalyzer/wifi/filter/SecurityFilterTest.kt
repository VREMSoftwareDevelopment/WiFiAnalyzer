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
import com.vrem.wifianalyzer.wifi.filter.adapter.SecurityAdapter
import com.vrem.wifianalyzer.wifi.model.Security
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class SecurityFilterTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val alertDialog =
        AlertDialog
            .Builder(mainActivity)
            .setView(R.layout.filter_security)
            .create()
            .also { it.show() }
    private val adapter = SecurityAdapter(emptySet())

    @Test
    fun idsMapEachSecurityToItsView() {
        // Arrange
        val expected =
            mapOf(
                Security.NONE to R.id.filterSecurityNone,
                Security.WPS to R.id.filterSecurityWPS,
                Security.WEP to R.id.filterSecurityWEP,
                Security.WPA to R.id.filterSecurityWPA,
                Security.WPA2 to R.id.filterSecurityWPA2,
                Security.WPA3 to R.id.filterSecurityWPA3,
            )
        // Act
        val fixture = SecurityFilter(adapter, alertDialog)
        // Assert
        assertThat(fixture.ids).containsExactlyInAnyOrderEntriesOf(expected)
    }

    @Test
    fun initMakesFilterContainerVisible() {
        // Act
        SecurityFilter(adapter, alertDialog)
        // Assert
        assertThat(alertDialog.findViewById<View>(R.id.filterSecurity).visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun clickTogglesSelection() {
        // Arrange
        SecurityFilter(adapter, alertDialog)
        // Act
        alertDialog.findViewById<View>(R.id.filterSecurityWPA2).performClick()
        // Assert
        assertThat(adapter.color(Security.WPA2)).isEqualTo(R.color.selected)
    }
}
