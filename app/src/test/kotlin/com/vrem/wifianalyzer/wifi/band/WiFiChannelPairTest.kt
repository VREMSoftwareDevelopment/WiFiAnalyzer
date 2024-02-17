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

package com.vrem.wifianalyzer.wifi.band

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class WiFiChannelPairTest {

    @Test
    fun channelCount() {
        // setup
        val expected = 20 - 10 + 5
        val first = WiFiChannel(10, 10)
        val second = WiFiChannel(20, 20)
        val fixture = WiFiChannelPair(first, second)
        // execute
        val actual = fixture.channelCount()
        // validate
        assertThat(actual).isEqualTo(expected)
    }
}