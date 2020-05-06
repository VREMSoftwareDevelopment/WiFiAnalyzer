/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class ExportSpec {

    private val timestamp = "timestamp"

    @Test
    fun `getData should evaluate to an export string`() {

        val wiFiDetails: List<WiFiDetail> = withWiFiDetails()
        val expected: String = String.format(Locale.ENGLISH,
                "Time Stamp|SSID|BSSID|Strength|Primary Channel|Primary Frequency|Center Channel|Center Frequency|Width (Range)|Distance|802.11mc|Security%n"
                        + timestamp + "|SSID10|BSSID10|-10dBm|3|2422MHz|5|2432MHz|40MHz (2412 - 2452)|~0.0m|true|capabilities10%n"
                        + timestamp + "|SSID20|BSSID20|-20dBm|5|2432MHz|7|2442MHz|40MHz (2422 - 2462)|~0.1m|true|capabilities20%n"
                        + timestamp + "|SSID30|BSSID30|-30dBm|7|2442MHz|9|2452MHz|40MHz (2432 - 2472)|~0.3m|true|capabilities30%n"
        )

        val actual: String = getData(wiFiDetails, timestamp)

        assertEquals(expected, actual)
    }

    private fun withWiFiDetails(): List<WiFiDetail> {
        return listOf(withWiFiDetail(10), withWiFiDetail(20), withWiFiDetail(30))
    }

    private fun withWiFiDetail(offset: Int): WiFiDetail {
        val wiFiSignal = WiFiSignal(2412 + offset, 2422 + offset, WiFiWidth.MHZ_40, -offset, true)
        val wiFiIdentifier = WiFiIdentifier("SSID$offset", "BSSID$offset")
        return WiFiDetail(wiFiIdentifier, "capabilities$offset", wiFiSignal)
    }
}