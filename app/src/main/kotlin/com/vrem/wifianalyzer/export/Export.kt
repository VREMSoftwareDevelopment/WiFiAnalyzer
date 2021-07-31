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

package com.vrem.wifianalyzer.export

import android.content.Intent
import android.content.res.Resources
import android.location.Location
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiSignal.Companion.FREQUENCY_UNITS
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Export(private val exportIntent: ExportIntent = ExportIntent()) {

    private val header = "Time Stamp|" +
            "SSID|" +
            "BSSID|" +
            "Strength|" +
            "Primary Channel|" +
            "Primary Frequency|" +
            "Center Channel|" +
            "Center Frequency|" +
            "Width (Range)|" +
            "Distance|" +
            "802.11mc|" +
            "Security|" +
            "Longitude|" +
            "Latitude|" +
            "Altitude|" +
            "Accuracy|" +
            "Bearing|" +
            "BearingAccuracy" +
            "\n"

    fun export(mainActivity: MainActivity, wiFiDetails: List<WiFiDetail>, location: Location?): Intent =
            export(mainActivity, wiFiDetails, Date(), location)

    fun export(mainActivity: MainActivity, wiFiDetails: List<WiFiDetail>, date: Date, location: Location?): Intent {
        val timestamp: String = timestamp(date)
        val title: String = title(mainActivity, timestamp)
        val data: String = data(wiFiDetails, timestamp, location)
        return exportIntent.intent(title, data)
    }

    internal fun data(wiFiDetails: List<WiFiDetail>, timestamp: String, location: Location?): String =
            header + wiFiDetails.joinToString(separator = String.EMPTY, transform = toExportString(timestamp, location))

    internal fun title(mainActivity: MainActivity, timestamp: String): String {
        val resources: Resources = mainActivity.resources
        val title: String = resources.getString(R.string.action_access_points)
        return "$title-$timestamp"
    }

    internal fun timestamp(date: Date): String = SimpleDateFormat(TIME_STAMP_FORMAT, Locale.US).format(date)

    fun Double.format(fracDigits: Int): String {
        val nf: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
        nf.maximumFractionDigits = fracDigits
        return nf.format(this)
    }

    fun Float.format(fracDigits: Int): String {
        val nf: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
        nf.maximumFractionDigits = fracDigits
        return nf.format(this)
    }

    private fun toExportString(timestamp: String, location: Location?): (WiFiDetail) -> String = {
        with(it) {
            var longitude = ""
            var latitude = ""
            var altitude = ""
            var accuracy = ""
            var bearing = ""
            var bearingAccuracy = ""

            if (location != null) {
                longitude = location.longitude.format(6)
                latitude = location.latitude.format(6)
                if (location.hasAltitude()) altitude = location.altitude.format(1)
                if (location.hasAccuracy()) accuracy = location.accuracy.format(1)
                if (location.hasBearing()) {
                    bearing = location.bearing.format(1)
                    if (location.hasBearingAccuracy()) bearingAccuracy = location.bearingAccuracyDegrees.format(1)
                }
            }

            "$timestamp|" +
                    "${wiFiIdentifier.ssid}|" +
                    "${wiFiIdentifier.bssid}|" +
                    "${wiFiSignal.level}dBm|" +
                    "${wiFiSignal.primaryWiFiChannel.channel}|" +
                    "${wiFiSignal.primaryFrequency}$FREQUENCY_UNITS|" +
                    "${wiFiSignal.centerWiFiChannel.channel}|" +
                    "${wiFiSignal.centerFrequency}$FREQUENCY_UNITS|" +
                    "${wiFiSignal.wiFiWidth.frequencyWidth}$FREQUENCY_UNITS (${wiFiSignal.frequencyStart} - ${wiFiSignal.frequencyEnd})|" +
                    "${wiFiSignal.distance}|" +
                    "${wiFiSignal.is80211mc}|" +
                    capabilities + "|" +
                    "$longitude|" +
                    "$latitude|" +
                    "$altitude|" +
                    "$accuracy|"+
                    "$bearing|"+
                    "$bearingAccuracy"+
                    "\n"
        }
    }

    companion object {
        private const val TIME_STAMP_FORMAT = "yyyy/MM/dd-HH:mm:ss"
    }

}