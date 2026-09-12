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

import com.vrem.util.EMPTY
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class WiFiIdentifierTest {
    private val hidden = "*hidden*"
    private val ssid = "xyzSSID"
    private val bssid = "xyzBSSID"
    private val fixture = WiFiIdentifier(ssid, bssid)

    @Test
    fun wiFiIdentifier() {
        // Arrange
        val expectedTitle = "$ssid ($bssid)"
        // Assert
        assertThat(fixture.ssidRaw).isEqualTo(ssid)
        assertThat(fixture.ssid).isEqualTo(ssid)
        assertThat(fixture.bssid).isEqualTo(bssid)
        assertThat(fixture.title).isEqualTo(expectedTitle)
    }

    @Test
    fun titleWithEmptySSID() {
        // Arrange
        val expectedTitle = "*hidden* ($bssid)"
        val fixture = WiFiIdentifier(String.EMPTY, bssid)
        // Assert
        assertThat(fixture.title).isEqualTo(expectedTitle)
    }

    @Test
    fun hashCodeUsingSameCase() {
        // Arrange
        val other = WiFiIdentifier(ssid, bssid)
        // Act & Assert
        assertThat(other.hashCode()).isEqualTo(fixture.hashCode())
    }

    @Test
    fun compareTo() {
        // Arrange
        val other = WiFiIdentifier(ssid, bssid)
        // Act & Assert
        assertThat(fixture.compareTo(other)).isEqualTo(0)
    }

    @Test
    fun rawSSID() {
        // Arrange
        val fixture = WiFiIdentifier(String.EMPTY, bssid)
        // Act & Assert
        assertThat(fixture.ssidRaw).isEqualTo(String.EMPTY)
        assertThat(fixture.ssid).isEqualTo(hidden)
    }

    @Test
    fun equalsWithDifferentCaseAndIgnoreCaseFalse() {
        val other = WiFiIdentifier(ssid.lowercase(), bssid.uppercase())
        assertThat(fixture.equals(other, false)).isFalse
    }

    @Test
    fun equalsWithDifferentSSID() {
        val other = WiFiIdentifier("otherSSID", bssid)
        assertThat(fixture.equals(other)).isFalse
    }

    @Test
    fun equalsWithDifferentBSSID() {
        val other = WiFiIdentifier(ssid, "otherBSSID")
        assertThat(fixture.equals(other)).isFalse
    }

    @Test
    fun equalsWithBothDifferent() {
        val other = WiFiIdentifier("otherSSID", "otherBSSID")
        assertThat(fixture.equals(other)).isFalse
    }

    @Test
    fun equalsUsingDifferentCase() {
        // Arrange
        val other = WiFiIdentifier(ssid.lowercase(), bssid.uppercase())
        // Act & Assert
        assertThat(fixture.equals(other, true)).isTrue
    }

    @Test
    fun equalsUsingSameCase() {
        // Arrange
        val other = WiFiIdentifier(ssid, bssid)
        // Act & Assert
        assertThat(other).isEqualTo(fixture)
        assertThat(other).isNotSameAs(fixture)
    }
}
