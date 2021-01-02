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

import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import org.junit.Assert.assertTrue
import org.junit.Test

class MakePredicateTest {

    @Test
    fun testMakePredicateExpectsTruePredicate() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        val toPredicate: ToPredicate<TestObject> = { truePredicate }
        val filters: Set<TestObject> = TestObject.values().toSet()
        // execute
        val actual: Predicate = makePredicate(TestObject.values(), filters, toPredicate)
        // validate
        assertTrue(actual(wiFiDetail))
    }

    @Test
    fun testMakePredicateExpectsAnyPredicate() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        val toPredicate: ToPredicate<TestObject> = { truePredicate }
        val filters: Set<TestObject> = setOf(TestObject.VALUE1, TestObject.VALUE3)
        // execute
        val actual: Predicate = makePredicate(TestObject.values(), filters, toPredicate)
        // validate
        assertTrue(actual(wiFiDetail))
    }

}