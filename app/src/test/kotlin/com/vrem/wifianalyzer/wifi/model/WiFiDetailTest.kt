/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class WiFiDetailTest {
    private val frequency = 2435
    private val level = -40
    private val vendorName = "VendorName"
    private val wiFiSecurity = WiFiSecurity("WPA-WPA2")
    private val ssid = "xyzSSID"
    private val bssid = "xyzBSSID"
    private val wiFiSignal = WiFiSignal(frequency, frequency, WiFiWidth.MHZ_20, level)
    private val wiFiAdditional = WiFiAdditional(vendorName, WiFiConnection.EMPTY)
    private val wiFiIdentifier = WiFiIdentifier(ssid, bssid)
    private val other = WiFiDetail(WiFiIdentifier(ssid, bssid), WiFiSecurity("WPA-WPA3"))
    private val fixture = WiFiDetail(wiFiIdentifier, wiFiSecurity, wiFiSignal, wiFiAdditional)

    @Test
    fun wiFiDetail() {
        // validate
        assertThat(fixture.wiFiSignal).isEqualTo(wiFiSignal)
        assertThat(fixture.wiFiAdditional).isEqualTo(wiFiAdditional)
        assertThat(fixture.wiFiIdentifier).isEqualTo(wiFiIdentifier)
        assertThat(fixture.wiFiSecurity).isEqualTo(wiFiSecurity)
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
    fun compareTo() {
        // execute & validate
        assertThat(fixture.compareTo(other)).isEqualTo(0)
    }

    @Test
    fun wiFiDetailCopyConstructor() {
        // setup
        val expected = WiFiDetail(wiFiIdentifier, wiFiSecurity, wiFiSignal)
        // execute
        val actual = WiFiDetail(expected, expected.wiFiAdditional)
        // validate
        assertThat(actual).isEqualTo(expected)
        assertThat(actual.wiFiIdentifier).isEqualTo(expected.wiFiIdentifier)
        assertThat(actual.wiFiSecurity).isEqualTo(expected.wiFiSecurity)
        assertThat(actual.wiFiSecurity.security).isEqualTo(expected.wiFiSecurity.security)
        assertThat(actual.wiFiSecurity.securities).isEqualTo(expected.wiFiSecurity.securities)
        assertThat(actual.wiFiAdditional).isEqualTo(expected.wiFiAdditional)
        assertThat(actual.wiFiSignal).isEqualTo(expected.wiFiSignal)
    }

}