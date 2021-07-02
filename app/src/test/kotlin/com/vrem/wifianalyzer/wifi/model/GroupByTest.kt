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
package com.vrem.wifianalyzer.wifi.model

import org.junit.Assert.*
import org.junit.Test

class GroupByTest {

    @Test
    fun testGroupByNumber() {
        assertEquals(4, GroupBy.values().size)
    }

    @Test
    fun testGroupBySort() {
        assertTrue(GroupBy.CHANNEL.sort.javaClass.isInstance(sortByChannel()))
        assertTrue(GroupBy.NONE.sort.javaClass.isInstance(sortByDefault()))
        assertTrue(GroupBy.SSID.sort.javaClass.isInstance(sortBySSID()))
        assertTrue(GroupBy.VIRTUAL.sort.javaClass.isInstance(sortBySSID()))
    }

    @Test
    fun testGroupByGroup() {
        assertTrue(GroupBy.CHANNEL.group.javaClass.isInstance(groupByChannel))
        assertTrue(GroupBy.NONE.group.javaClass.isInstance(groupBySSID))
        assertTrue(GroupBy.SSID.group.javaClass.isInstance(groupBySSID))
        assertTrue(GroupBy.VIRTUAL.group.javaClass.isInstance(groupByVirtual))
    }

    @Test
    fun testNone() {
        assertFalse(GroupBy.CHANNEL.none)
        assertTrue(GroupBy.NONE.none)
        assertFalse(GroupBy.SSID.none)
        assertFalse(GroupBy.VIRTUAL.none)
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
        val wiFiDetail = withWiFiDetail()
        val expected = "2435"
        // execute
        val actual: String = GroupBy.CHANNEL.group(wiFiDetail)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testGroupByKeyWithVirtual() {
        // setup
        val wiFiDetail = withWiFiDetail()
        val expected = ":cf:30:ce:1d:7-2435"
        // execute
        val actual: String = GroupBy.VIRTUAL.group(wiFiDetail)
        // validate
        assertEquals(expected, actual)
    }

    private fun withWiFiDetail() = WiFiDetail(
        WiFiIdentifier("SSID1", "20:cf:30:ce:1d:71"),
        "WPA-WPA2",
        WiFiSignal(2435, 2435, WiFiWidth.MHZ_20, -40, true)
    )
}