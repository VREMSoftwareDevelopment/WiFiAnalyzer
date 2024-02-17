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

class WiFiVirtualTest {

    @Test
    fun wiFiVirtualWithVirtualBSSID() {
        // setup
        val wiFiDetail = WiFiDetail(
            WiFiIdentifier("SSID1", "20:cf:30:ce:1d:71"),
            WiFiSecurity.EMPTY,
            WiFiSignal(2432, 2432, WiFiWidth.MHZ_20, -50),
            WiFiAdditional.EMPTY
        )
        // execute
        val actual = wiFiDetail.wiFiVirtual
        // validate
        assertThat(actual.bssid).isEqualTo(":cf:30:ce:1d:7")
        assertThat(actual.frequency).isEqualTo(2432)
        assertThat(actual.key).isEqualTo(":cf:30:ce:1d:7-" + 2432)
    }

    @Test
    fun wiFiVirtualWithRegularBSSIDWhenBSSIDShort() {
        // setup
        val wiFiDetail = WiFiDetail(
            WiFiIdentifier("SSID1", "20:cf:30:ce:1d:7"),
            WiFiSecurity.EMPTY,
            WiFiSignal(2432, 2432, WiFiWidth.MHZ_20, -50),
            WiFiAdditional.EMPTY
        )
        // execute
        val actual = wiFiDetail.wiFiVirtual
        // validate
        assertThat(actual.bssid).isEqualTo("20:cf:30:ce:1d:7")
        assertThat(actual.frequency).isEqualTo(2432)
        assertThat(actual.key).isEqualTo("20:cf:30:ce:1d:7-" + 2432)
    }

    @Test
    fun wiFiVirtualWithRegularBSSIDWhenBSSIDLong() {
        // setup
        val wiFiDetail = WiFiDetail(
            WiFiIdentifier("SSID1", "20:cf:30:ce:1d:71:"),
            WiFiSecurity.EMPTY,
            WiFiSignal(2432, 2432, WiFiWidth.MHZ_20, -50),
            WiFiAdditional.EMPTY
        )
        // execute
        val actual = wiFiDetail.wiFiVirtual
        // validate
        assertThat(actual.bssid).isEqualTo("20:cf:30:ce:1d:71:")
        assertThat(actual.frequency).isEqualTo(2432)
        assertThat(actual.key).isEqualTo("20:cf:30:ce:1d:71:-" + 2432)
    }

}