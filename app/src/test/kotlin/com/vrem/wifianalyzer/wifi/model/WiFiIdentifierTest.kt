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

class WiFiIdentifierTest {
    private val hidden = "*hidden*"
    private val ssid = "xyzSSID"
    private val bssid = "xyzBSSID"
    private val fixture = WiFiIdentifier(ssid, bssid)

    @Test
    fun wiFiIdentifier() {
        // setup
        val expectedTitle = "$ssid ($bssid)"
        // validate
        assertThat(fixture.ssidRaw).isEqualTo(ssid)
        assertThat(fixture.ssid).isEqualTo(ssid)
        assertThat(fixture.bssid).isEqualTo(bssid)
        assertThat(fixture.title).isEqualTo(expectedTitle)
    }

    @Test
    fun titleWithEmptySSID() {
        // setup
        val expectedTitle = "*hidden* ($bssid)"
        val fixture = WiFiIdentifier(String.EMPTY, bssid)
        // validate
        assertThat(fixture.title).isEqualTo(expectedTitle)
    }

    @Test
    fun equalsUsingSameCase() {
        // setup
        val other = WiFiIdentifier(ssid, bssid)
        // execute & validate
        assertThat(other).isEqualTo(fixture)
        assertThat(other).isNotSameAs(fixture)
    }

    @Test
    fun hashCodeUsingSameCase() {
        // setup
        val other = WiFiIdentifier(ssid, bssid)
        // execute & validate
        assertThat(other.hashCode()).isEqualTo(fixture.hashCode())
    }

    @Test
    fun equalsUsingDifferentCase() {
        // setup
        val other = WiFiIdentifier(ssid.lowercase(), bssid.uppercase())
        // execute & validate
        assertThat(fixture.equals(other, true)).isTrue()
    }

    @Test
    fun compareTo() {
        // setup
        val other = WiFiIdentifier(ssid, bssid)
        // execute & validate
        assertThat(fixture.compareTo(other)).isEqualTo(0)
    }

    @Test
    fun rawSSID() {
        // setup
        val fixture = WiFiIdentifier(String.EMPTY, bssid)
        // execute & validate
        assertThat(fixture.ssidRaw).isEqualTo(String.EMPTY)
        assertThat(fixture.ssid).isEqualTo(hidden)
    }

}