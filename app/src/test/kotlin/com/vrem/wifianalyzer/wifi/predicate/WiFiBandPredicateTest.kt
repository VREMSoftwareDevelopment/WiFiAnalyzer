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
package com.vrem.wifianalyzer.wifi.predicate

import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import com.vrem.wifianalyzer.wifi.model.WiFiSecurity
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import com.vrem.wifianalyzer.wifi.model.WiFiWidth
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class WiFiBandPredicateTest {
    @Test
    fun wiFiBandPredicateWith2GHzFrequency() {
        // setup
        val wiFiDetail = makeWiFiDetail(2455)
        // execute & validate
        assertThat(WiFiBand.GHZ2.predicate()(wiFiDetail)).isTrue
        assertThat(WiFiBand.GHZ5.predicate()(wiFiDetail)).isFalse
    }

    @Test
    fun wiFiBandPredicateWith5GHzFrequency() {
        // setup
        val wiFiDetail = makeWiFiDetail(5455)
        // execute & validate
        assertThat(WiFiBand.GHZ2.predicate()(wiFiDetail)).isFalse
        assertThat(WiFiBand.GHZ5.predicate()(wiFiDetail)).isTrue
    }

    private fun makeWiFiDetail(frequency: Int): WiFiDetail =
        WiFiDetail(
            WiFiIdentifier("ssid", "bssid"),
            WiFiSecurity("wpa"),
            WiFiSignal(frequency, frequency, WiFiWidth.MHZ_20, 1),
        )
}
