/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.vrem.wifianalyzer.wifi.model.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SecurityPredicateTest {
    @Test
    fun testSecurityPredicateWithFoundWPAValue() {
        // setup
        val wiFiDetail = wiFiDetail()
        val fixture = Security.WPA.predicate()
        // execute
        val actual = fixture(wiFiDetail)
        // validate
        assertTrue(actual)
    }

    @Test
    fun testSecurityPredicateWithFoundWEPValue() {
        // setup
        val wiFiDetail = wiFiDetail()
        val fixture = Security.WEP.predicate()
        // execute
        val actual = fixture(wiFiDetail)
        // validate
        assertTrue(actual)
    }

    @Test
    fun testSecurityPredicateWithFoundNoneValue() {
        // setup
        val wiFiDetail = wiFiDetailWithNoSecurity()
        val fixture = Security.NONE.predicate()
        // execute
        val actual = fixture(wiFiDetail)
        // validate
        assertTrue(actual)
    }

    @Test
    fun testSecurityPredicateWithNotFoundValue() {
        // setup
        val wiFiDetail = wiFiDetail()
        val fixture = Security.WPA2.predicate()
        // execute
        val actual = fixture(wiFiDetail)
        // validate
        assertFalse(actual)
    }

    private fun wiFiDetail(): WiFiDetail =
            WiFiDetail(
                    WiFiIdentifier("ssid", "bssid"),
                    "ess-wep-wpa",
                    WiFiSignal(2455, 2455, WiFiWidth.MHZ_20, 1, true))

    private fun wiFiDetailWithNoSecurity(): WiFiDetail =
            WiFiDetail(
                    WiFiIdentifier("ssid", "bssid"),
                    "ess",
                    WiFiSignal(2455, 2455, WiFiWidth.MHZ_20, 1, true))

}