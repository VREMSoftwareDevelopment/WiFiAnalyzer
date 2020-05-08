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
package com.vrem.wifianalyzer.wifi.model

import com.vrem.wifianalyzer.wifi.band.WiFiWidth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GroupByTest {
    @Test
    fun testGroupByNumber() {
        assertEquals(3, GroupBy.values().size)
    }

    @Test
    fun testGroupBySortByComparator() {
        assertTrue(GroupBy.CHANNEL.sort.javaClass.isInstance(sortByChannel()))
        assertTrue(GroupBy.NONE.sort.javaClass.isInstance(sortByDefault()))
        assertTrue(GroupBy.SSID.sort.javaClass.isInstance(sortBySSID()))
    }

    @Test
    fun testGroupByKeyWithNone() {
        // setup
        val expected = "SSID_TO_TEST"
        val wiFiDetail = WiFiDetail(WiFiIdentifier(expected))
        // execute
        val actual: String = GroupBy.NONE.group(wiFiDetail)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testGroupByKeyWithSSID() {
        // setup
        val expected = "SSID_TO_TEST"
        val wiFiDetail = WiFiDetail(WiFiIdentifier(expected))
        // execute
        val actual: String = GroupBy.SSID.group(wiFiDetail)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testGroupByKeyWithChannel() {
        // setup
        val expected = "6"
        val wiFiDetail = WiFiDetail(
                WiFiIdentifier("xyzSSID", "xyzBSSID"),
                "WPA-WPA2",
                WiFiSignal(2435, 2435, WiFiWidth.MHZ_20, -40, true))
        // execute
        val actual: String = GroupBy.CHANNEL.group(wiFiDetail)
        // validate
        assertEquals(expected, actual)
    }
}