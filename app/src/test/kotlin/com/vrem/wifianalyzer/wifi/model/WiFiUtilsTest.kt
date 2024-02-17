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
package com.vrem.wifianalyzer.wifi.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.text.DecimalFormat

class WiFiUtilsTest {
    private val decimalFormat = DecimalFormat("#.##")

    @Test
    fun calculateDistance() {
        validate(2437, -36, "0.62")
        validate(2437, -42, "1.23")
        validate(2432, -88, "246.34")
        validate(2412, -91, "350.85")
    }

    private fun validate(frequency: Int, level: Int, expected: String) {
        assertThat(decimalFormat.format(calculateDistance(frequency, level))).isEqualTo(expected)
    }

    @Test
    fun calculateSignalLevel() {
        assertThat(calculateSignalLevel(-110, 5)).isEqualTo(0)
        assertThat(calculateSignalLevel(-89, 5)).isEqualTo(0)
        assertThat(calculateSignalLevel(-88, 5)).isEqualTo(1)
        assertThat(calculateSignalLevel(-78, 5)).isEqualTo(1)
        assertThat(calculateSignalLevel(-77, 5)).isEqualTo(2)
        assertThat(calculateSignalLevel(-67, 5)).isEqualTo(2)
        assertThat(calculateSignalLevel(-66, 5)).isEqualTo(3)
        assertThat(calculateSignalLevel(-56, 5)).isEqualTo(3)
        assertThat(calculateSignalLevel(-55, 5)).isEqualTo(4)
        assertThat(calculateSignalLevel(0, 5)).isEqualTo(4)
    }

    @Test
    fun convertIpAddress() {
        assertThat(convertIpV4Address(123456789)).isEqualTo("21.205.91.7")
        assertThat(convertIpV4Address(1)).isEqualTo("1.0.0.0")
        assertThat(convertIpV4Address(0)).isEmpty()
        assertThat(convertIpV4Address(-1)).isEmpty()
    }

    @Test
    fun convertSSID() {
        assertThat(convertSSID("\"SSID\"")).isEqualTo("SSID")
        assertThat(convertSSID("SSID")).isEqualTo("SSID")
    }
}