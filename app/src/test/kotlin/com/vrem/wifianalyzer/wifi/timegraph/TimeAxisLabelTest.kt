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
package com.vrem.wifianalyzer.wifi.timegraph

import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.wifi.graphutils.MAX_Y
import com.vrem.wifianalyzer.wifi.graphutils.MIN_Y
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TimeAxisLabelTest {
    private val fixture = TimeAxisLabel()

    @Test
    fun yAxis() {
        assertThat(fixture.formatLabel(MIN_Y.toDouble(), false)).isEqualTo(String.EMPTY)
        assertThat(fixture.formatLabel(MIN_Y + 1.toDouble(), false)).isEqualTo("-99")
        assertThat(fixture.formatLabel(MAX_Y.toDouble(), false)).isEqualTo("0")
        assertThat(fixture.formatLabel(MAX_Y + 1.toDouble(), false)).isEqualTo(String.EMPTY)
    }

    @Test
    fun xAxis() {
        assertThat(fixture.formatLabel(-2.0, true)).isEqualTo(String.EMPTY)
        assertThat(fixture.formatLabel(-1.0, true)).isEqualTo(String.EMPTY)
        assertThat(fixture.formatLabel(0.0, true)).isEqualTo(String.EMPTY)
        assertThat(fixture.formatLabel(1.0, true)).isEqualTo(String.EMPTY)
        assertThat(fixture.formatLabel(2.0, true)).isEqualTo("2")
        assertThat(fixture.formatLabel(10.0, true)).isEqualTo("10")
    }
}