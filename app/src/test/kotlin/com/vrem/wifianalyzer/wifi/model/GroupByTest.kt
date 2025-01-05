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
package com.vrem.wifianalyzer.wifi.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class GroupByTest {

    @Test
    fun groupByNumber() {
        assertThat(GroupBy.entries).hasSize(4)
    }

    @Test
    fun groupBySort() {
        assertThat(GroupBy.CHANNEL.sort.javaClass.isInstance(sortByChannel())).isTrue()
        assertThat(GroupBy.NONE.sort.javaClass.isInstance(sortByDefault())).isTrue()
        assertThat(GroupBy.SSID.sort.javaClass.isInstance(sortBySSID())).isTrue()
        assertThat(GroupBy.VIRTUAL.sort.javaClass.isInstance(sortBySSID())).isTrue()
    }

    @Test
    fun groupByGroup() {
        assertThat(GroupBy.CHANNEL.group.javaClass.isInstance(groupByChannel)).isTrue()
        assertThat(GroupBy.NONE.group.javaClass.isInstance(groupBySSID)).isTrue()
        assertThat(GroupBy.SSID.group.javaClass.isInstance(groupBySSID)).isTrue()
        assertThat(GroupBy.VIRTUAL.group.javaClass.isInstance(groupByVirtual)).isTrue()
    }

    @Test
    fun none() {
        assertThat(GroupBy.CHANNEL.none).isFalse()
        assertThat(GroupBy.NONE.none).isTrue()
        assertThat(GroupBy.SSID.none).isFalse()
        assertThat(GroupBy.VIRTUAL.none).isFalse()
    }

    @Test
    fun groupByKeyWithNone() {
        // setup
        val expected = "SSID_TO_TEST"
        val wiFiDetail = WiFiDetail(WiFiIdentifier(expected))
        // execute
        val actual: String = GroupBy.NONE.group(wiFiDetail)
        // validate
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun groupByKeyWithSSID() {
        // setup
        val expected = "SSID_TO_TEST"
        val wiFiDetail = WiFiDetail(WiFiIdentifier(expected))
        // execute
        val actual: String = GroupBy.SSID.group(wiFiDetail)
        // validate
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun groupByKeyWithChannel() {
        // setup
        val wiFiDetail = withWiFiDetail()
        val expected = "2435"
        // execute
        val actual: String = GroupBy.CHANNEL.group(wiFiDetail)
        // validate
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun groupByKeyWithVirtual() {
        // setup
        val wiFiDetail = withWiFiDetail()
        val expected = ":cf:30:ce:1d:7-2435"
        // execute
        val actual: String = GroupBy.VIRTUAL.group(wiFiDetail)
        // validate
        assertThat(actual).isEqualTo(expected)
    }

    private fun withWiFiDetail() = WiFiDetail(
        WiFiIdentifier("SSID1", "20:cf:30:ce:1d:71"),
        WiFiSecurity("WPA-WPA2"),
        WiFiSignal(2435, 2435, WiFiWidth.MHZ_20, -40)
    )
}