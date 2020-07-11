/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiWidth
import com.vrem.wifianalyzer.wifi.model.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

internal enum class TestObject {
    VALUE1, VALUE3, VALUE2
}

class FilterPredicateTest {
    private val ssid = "SSID"
    private val wpa2 = "WPA2"

    private val settings: Settings = mock()

    @Before
    fun setUp() {
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ5)
        whenever(settings.findSSIDs()).thenReturn(setOf(ssid, ssid))
        whenever(settings.findWiFiBands()).thenReturn(setOf(WiFiBand.GHZ2))
        whenever(settings.findStrengths()).thenReturn(setOf(Strength.TWO, Strength.FOUR))
        whenever(settings.findSecurities()).thenReturn(setOf(Security.WEP, Security.WPA2))
    }

    @Test
    fun testMakeAccessPointsPredicate() {
        // execute
        val fixture: Predicate = makeAccessPointsPredicate(settings)
        // validate
        assertNotNull(fixture)
        verify(settings).findSSIDs()
        verify(settings).findWiFiBands()
        verify(settings).findStrengths()
        verify(settings).findSecurities()
    }

    @Test
    fun testMakeOtherPredicate() {
        // execute
        val fixture: Predicate = makeOtherPredicate(settings)
        // validate
        assertNotNull(fixture)
        verify(settings).findSSIDs()
        verify(settings).wiFiBand()
        verify(settings).findStrengths()
        verify(settings).findSecurities()
    }

    @Test
    fun testMakeAccessPointsPredicateIsTrue() {
        // setup
        val fixture: Predicate = makeAccessPointsPredicate(settings)
        val wiFiDetail = makeWiFiDetail(ssid, wpa2)
        // execute
        val actual = fixture.test(wiFiDetail)
        // validate
        assertTrue(actual)
    }

    @Test
    fun testMakeAccessPointsPredicateWithSecurityToFalse() {
        // setup
        val fixture: Predicate = makeAccessPointsPredicate(settings)
        val wiFiDetail = makeWiFiDetail(ssid, "WPA")
        // execute
        val actual = fixture.test(wiFiDetail)
        // validate
        assertFalse(actual)
    }

    @Test
    fun testMakeAccessPointsPredicateWithSSIDToFalse() {
        // setup
        val fixture: Predicate = makeAccessPointsPredicate(settings)
        val wiFiDetail = makeWiFiDetail("WIFI", wpa2)
        // execute
        val actual = fixture.test(wiFiDetail)
        // validate
        assertFalse(actual)
    }

    @Test
    fun testMakeAccessPointsPredicateIsAllPredicate() {
        // execute
        val fixture: Predicate = makeAccessPointsPredicate(settings)
        // validate
        assertTrue(fixture is AllPredicate)
    }

    @Test
    fun testMakeAccessPointsPredicateIsTrueWhenFullSet() {
        // setup
        val wiFiDetail = makeWiFiDetail(ssid, wpa2)
        whenever(settings.findSSIDs()).thenReturn(setOf())
        whenever(settings.findWiFiBands()).thenReturn(WiFiBand.values().toSet())
        whenever(settings.findStrengths()).thenReturn(Strength.values().toSet())
        whenever(settings.findSecurities()).thenReturn(Security.values().toSet())
        val fixture: Predicate = makeAccessPointsPredicate(settings)
        // execute
        val actual = fixture.test(wiFiDetail)
        // validate
        assertTrue(actual)
    }

    @Test
    fun testPredicateExpectsTruePredicateWithAllValues() {
        // setup
        val toPredicate: ToPredicate<TestObject> = { TruePredicate() }
        val filters: Set<TestObject> = TestObject.values().toSet()
        // execute
        val actual: Predicate = predicate(TestObject.values(), filters, toPredicate)
        // validate
        assertTrue(actual is TruePredicate)
    }

    @Test
    fun testPredicateExpectsAnyPredicateWithSomeValues() {
        // setup
        val toPredicate: ToPredicate<TestObject> = { TruePredicate() }
        val filters: Set<TestObject> = setOf(TestObject.VALUE1, TestObject.VALUE3)
        // execute
        val actual: Predicate = predicate(TestObject.values(), filters, toPredicate)
        // validate
        assertTrue(actual is AnyPredicate)
    }

    @Test
    fun testAnyPredicateIsTrue() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        val predicates = listOf<Predicate>(FalsePredicate(), TruePredicate(), FalsePredicate())
        val fixture = AnyPredicate(predicates)
        // execute
        val actual = fixture.test(wiFiDetail)
        // validate
        assertTrue(actual)
    }

    @Test
    fun testAnyPredicateIsFalse() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        val predicates = listOf<Predicate>(FalsePredicate(), FalsePredicate(), FalsePredicate())
        val fixture = AnyPredicate(predicates)
        // execute
        val actual = fixture.test(wiFiDetail)
        // validate
        assertFalse(actual)
    }

    @Test
    fun testAllPredicateIsTrue() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        val predicates = listOf<Predicate>(TruePredicate(), TruePredicate(), TruePredicate())
        val fixture = AllPredicate(predicates)
        // execute
        val actual = fixture.test(wiFiDetail)
        // validate
        assertTrue(actual)
    }

    @Test
    fun testAllPredicateIsFalse() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        val predicates = listOf<Predicate>(FalsePredicate(), TruePredicate(), FalsePredicate())
        val fixture = AllPredicate(predicates)
        // execute
        val actual = fixture.test(wiFiDetail)
        // validate
        assertFalse(actual)
    }

    private fun makeWiFiDetail(ssid: String, security: String): WiFiDetail =
            WiFiDetail(
                    WiFiIdentifier(ssid, "bssid"),
                    security,
                    WiFiSignal(2445, 2445, WiFiWidth.MHZ_20, -40, true))

}