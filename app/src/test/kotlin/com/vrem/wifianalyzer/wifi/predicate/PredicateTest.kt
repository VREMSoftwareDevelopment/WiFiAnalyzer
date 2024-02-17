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

import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class PredicateTest {
    private val ssid = "SSID"
    private val wpa2 = "WPA2"

    private val settings: Settings = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(settings)
    }

    @Test
    fun makeAccessPointsPredicate() {
        // setup
        whenSettings()
        // execute
        val fixture: Predicate = makeAccessPointsPredicate(settings)
        // validate
        assertThat(fixture).isNotNull()
        verifySettings()
    }

    @Test
    fun makeAccessPointsPredicateIsTrue() {
        // setup
        whenSettings()
        val fixture: Predicate = makeAccessPointsPredicate(settings)
        val wiFiDetail = makeWiFiDetail(ssid, wpa2)
        // execute
        val actual = fixture(wiFiDetail)
        // validate
        assertThat(actual).isTrue()
        verifySettings()
    }

    @Test
    fun makeAccessPointsPredicateWithSecurityToFalse() {
        // setup
        whenSettings()
        val fixture: Predicate = makeAccessPointsPredicate(settings)
        val wiFiDetail = makeWiFiDetail(ssid, "WPA")
        // execute
        val actual = fixture(wiFiDetail)
        // validate
        assertThat(actual).isFalse()
        verifySettings()
    }

    @Test
    fun makeAccessPointsPredicateWithSSIDToFalse() {
        // setup
        whenSettings()
        val fixture: Predicate = makeAccessPointsPredicate(settings)
        val wiFiDetail = makeWiFiDetail("WIFI", wpa2)
        // execute
        val actual = fixture(wiFiDetail)
        // validate
        assertThat(actual).isFalse()
        verifySettings()
    }

    @Test
    fun makeAccessPointsPredicateIsAllPredicate() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        whenSettingsWithFullSets()
        // execute
        val fixture: Predicate = makeAccessPointsPredicate(settings)
        // validate
        assertThat(fixture(wiFiDetail)).isTrue()
        verifySettings()
    }

    @Test
    fun makeAccessPointsPredicateIsTrueWhenFullSet() {
        // setup
        whenSettingsWithFullSets()
        val wiFiDetail = makeWiFiDetail(ssid, wpa2)
        val fixture: Predicate = makeAccessPointsPredicate(settings)
        // execute
        val actual = fixture(wiFiDetail)
        // validate
        assertThat(actual).isTrue()
        verifySettings()
    }

    @Test
    fun makeOtherPredicate() {
        // setup
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ5)
        whenever(settings.findSSIDs()).thenReturn(setOf(ssid, ssid))
        whenever(settings.findStrengths()).thenReturn(setOf(Strength.TWO, Strength.FOUR))
        whenever(settings.findSecurities()).thenReturn(setOf(Security.WEP, Security.WPA2))
        // execute
        val fixture: Predicate = makeOtherPredicate(settings)
        // validate
        assertThat(fixture).isNotNull()
        verify(settings).wiFiBand()
        verify(settings).findSSIDs()
        verify(settings).findStrengths()
        verify(settings).findSecurities()
    }

    private fun whenSettingsWithFullSets() {
        whenever(settings.findSSIDs()).thenReturn(setOf())
        whenever(settings.findWiFiBands()).thenReturn(WiFiBand.entries.toSet())
        whenever(settings.findStrengths()).thenReturn(Strength.entries.toSet())
        whenever(settings.findSecurities()).thenReturn(Security.entries.toSet())
    }

    private fun whenSettings() {
        whenever(settings.findSSIDs()).thenReturn(setOf(ssid, ssid))
        whenever(settings.findWiFiBands()).thenReturn(setOf(WiFiBand.GHZ2))
        whenever(settings.findStrengths()).thenReturn(setOf(Strength.TWO, Strength.FOUR))
        whenever(settings.findSecurities()).thenReturn(setOf(Security.WEP, Security.WPA2))
    }

    private fun verifySettings() {
        verify(settings).findSSIDs()
        verify(settings).findWiFiBands()
        verify(settings).findStrengths()
        verify(settings).findSecurities()
    }

    private fun makeWiFiDetail(ssid: String, capabilities: String): WiFiDetail =
        WiFiDetail(
            WiFiIdentifier(ssid, "bssid"),
            WiFiSecurity(capabilities),
            WiFiSignal(2445, 2445, WiFiWidth.MHZ_20, -40)
        )

}