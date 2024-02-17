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
package com.vrem.wifianalyzer.wifi.predicate

import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import com.vrem.wifianalyzer.wifi.model.WiFiSecurity
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SSIDPredicateTest {
    @Test
    fun sSIDPredicate() {
        // setup
        val wiFiDetail = WiFiDetail(WiFiIdentifier("ssid", "bssid"), WiFiSecurity("wpa"))
        // execute & validate
        assertThat("ssid".predicate()(wiFiDetail)).isTrue()
        assertThat("id".predicate()(wiFiDetail)).isTrue()
        assertThat("ss".predicate()(wiFiDetail)).isTrue()
        assertThat("s".predicate()(wiFiDetail)).isTrue()
        assertThat("".predicate()(wiFiDetail)).isTrue()
        assertThat("SSID".predicate()(wiFiDetail)).isFalse()
        assertThat("B".predicate()(wiFiDetail)).isFalse()
    }
}