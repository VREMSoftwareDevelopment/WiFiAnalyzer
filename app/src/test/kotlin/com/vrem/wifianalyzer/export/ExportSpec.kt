/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.export

import com.vrem.wifianalyzer.wifi.band.WiFiWidth
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class ExportSpec {

    private val timestamp = "timestamp"

    private val wiFiDetails = listOf(WiFiDetail("SSID", "BSSID", "capabilities", WiFiSignal(2412, 2422, WiFiWidth.MHZ_40, -40, true)))

    @Test
    fun `getData should evaluate to an export string`() {

        val expected = String.format(Locale.ENGLISH,
                "Time Stamp|SSID|BSSID|Strength|Primary Channel|Primary Frequency|Center Channel|Center Frequency|Width (Range)|Distance|802.11mc|Security%n"
                        + timestamp + "|SSID|BSSID|-40dBm|1|2412MHz|3|2422MHz|40MHz (2402 - 2442)|~1.0m|true|capabilities%n")

        val actual = getData(wiFiDetails, timestamp)

        assertEquals(expected, actual)
    }
}