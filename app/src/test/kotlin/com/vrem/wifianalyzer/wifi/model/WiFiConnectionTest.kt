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
package com.vrem.wifianalyzer.wifi.model

import com.vrem.util.EMPTY
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class WiFiConnectionTest {
    private val ipAddress = "21.205.91.7"
    private val linkSpeed = 21
    private val wiFiIdentifier = WiFiIdentifier("SSID-123", "BSSID-123")
    private val other = WiFiConnection(WiFiIdentifier("SSID-123", "BSSID-123"))
    private val fixture: WiFiConnection = WiFiConnection(wiFiIdentifier, ipAddress, linkSpeed)

    @Test
    fun wiFiConnectionEmpty() {
        // validate
        assertThat(WiFiConnection.EMPTY.wiFiIdentifier).isEqualTo(WiFiIdentifier.EMPTY)
        assertThat(WiFiConnection.EMPTY.ipAddress).isEqualTo(String.EMPTY)
        assertThat(WiFiConnection.EMPTY.linkSpeed).isEqualTo(WiFiConnection.LINK_SPEED_INVALID)
        assertThat(WiFiConnection.EMPTY.connected).isFalse()
    }

    @Test
    fun wiFiConnection() {
        // validate
        assertThat(fixture.wiFiIdentifier).isEqualTo(wiFiIdentifier)
        assertThat(fixture.ipAddress).isEqualTo(ipAddress)
        assertThat(fixture.linkSpeed).isEqualTo(linkSpeed)
        assertThat(fixture.connected).isTrue()
    }

    @Test
    fun equalsUsingIdentifier() {
        // execute & validate
        assertThat(other).isEqualTo(fixture)
        assertThat(other).isNotSameAs(fixture)
    }

    @Test
    fun hashCodeUsingIdentifier() {
        // execute & validate
        assertThat(other.hashCode()).isEqualTo(fixture.hashCode())
    }

    @Test
    fun compareToUsingIdentifier() {
        // execute & validate
        assertThat(fixture.compareTo(other)).isEqualTo(0)
    }

}