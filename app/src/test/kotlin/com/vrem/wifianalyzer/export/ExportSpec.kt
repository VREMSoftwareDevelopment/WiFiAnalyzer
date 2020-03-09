package com.vrem.wifianalyzer.export

import com.vrem.wifianalyzer.wifi.band.WiFiWidth
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import io.kotlintest.specs.StringSpec
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.*

class ExportSpec : StringSpec({

    val timestamp = "timestamp"

    val wiFiDetails = listOf(WiFiDetail("SSID", "BSSID", "capabilities", WiFiSignal(2412, 2422, WiFiWidth.MHZ_40, -40, true)))

    "getData should evaluate to an export string" {

        val expected = String.format(Locale.ENGLISH,
                "Time Stamp|SSID|BSSID|Strength|Primary Channel|Primary Frequency|Center Channel|Center Frequency|Width (Range)|Distance|802.11mc|Security%n"
                        + timestamp + "|SSID|BSSID|-40dBm|1|2412MHz|3|2422MHz|40MHz (2402 - 2442)|~1.0m|true|capabilities%n")

        val actual = getData(wiFiDetails, timestamp)

        assertEquals(expected, actual)
    }
})