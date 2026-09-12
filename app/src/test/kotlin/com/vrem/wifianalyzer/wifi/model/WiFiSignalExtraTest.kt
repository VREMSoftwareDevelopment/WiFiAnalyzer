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
package com.vrem.wifianalyzer.wifi.model

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class WiFiSignalExtraTest {
    private val mainActivity = RobolectricUtil.INSTANCE.mainActivity
    private val fixture = WiFiSignalExtra(true, WiFiStandard.AC, FastRoaming.entries.toList())

    @Test
    fun wiFiSignalExtra() {
        // Assert
        assertThat(fixture.is80211mc).isTrue
        assertThat(fixture.wiFiStandard).isEqualTo(WiFiStandard.AC)
        assertThat(fixture.fastRoaming).isEqualTo(FastRoaming.entries.toList())
    }

    @Test
    fun wiFiStandardDisplay() {
        // Arrange
        val expected = "802.11ac"
        // Act
        val actual = fixture.wiFiStandardDisplay(mainActivity.applicationContext)
        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun fastRoamingDisplay() {
        // Arrange
        val expected = "802.11k 802.11r 802.11v OKC"
        // Act
        val actual = fixture.fastRoamingDisplay(mainActivity.applicationContext)
        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun fastRoamingDisplayWithOneFastRoaming() {
        // Arrange
        val fixture = WiFiSignalExtra(fastRoaming = listOf(FastRoaming.FR_802_11R))
        val expected = "802.11r"
        // Act
        val actual = fixture.fastRoamingDisplay(mainActivity.applicationContext)
        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun fastRoamingDisplayWithEmptyList() {
        // Arrange
        val fixture = WiFiSignalExtra(fastRoaming = emptyList())
        val expected = ""
        // Act
        val actual = fixture.fastRoamingDisplay(mainActivity.applicationContext)
        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun fastRoamingDisplayWithDuplicateValues() {
        // Arrange
        val fixture = WiFiSignalExtra(fastRoaming = listOf(FastRoaming.FR_802_11R, FastRoaming.FR_802_11R))
        val expected = "802.11r 802.11r"
        // Act
        val actual = fixture.fastRoamingDisplay(mainActivity.applicationContext)
        // Assert
        assertThat(actual).isEqualTo(expected)
    }
}
