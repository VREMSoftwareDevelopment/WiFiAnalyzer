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
package com.vrem.wifianalyzer.export

import android.content.Context
import android.content.Intent
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.wifi.model.FastRoaming
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import com.vrem.wifianalyzer.wifi.model.WiFiSecurity
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import com.vrem.wifianalyzer.wifi.model.WiFiSignalExtra
import com.vrem.wifianalyzer.wifi.model.WiFiStandard
import com.vrem.wifianalyzer.wifi.model.WiFiWidth
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExportTest {
    private val name = "name"
    private val date = Date()

    private val mainActivity: MainActivity = mock()
    private val context: Context = mock()
    private val exportIntent: ExportIntent = mock()
    private val intent: Intent = mock()
    private val fixture = Export(exportIntent)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(exportIntent)
        verifyNoMoreInteractions(intent)
        verifyNoMoreInteractions(context)
    }

    @Test
    fun testExport() {
        // setup
        val wiFiDetails = withWiFiDetails()
        val count = wiFiDetails.size
        val timestamp = timestamp(date)
        val title = title(timestamp)
        val data = data(timestamp)
        doReturn(context).whenever(mainActivity).applicationContext
        doReturn(name).whenever(context).getString(R.string.action_access_points)
        doReturn("802.11AC").whenever(context).getString(WiFiStandard.AC.textResource)
        doReturn("802.11R").whenever(context).getString(FastRoaming.FR_802_11R.textResource)
        whenever(exportIntent.intent(title, data)).thenReturn(intent)
        // execute
        val actual = fixture.export(mainActivity, wiFiDetails, date)
        // validate
        assertEquals(intent, actual)
        verify(mainActivity).applicationContext
        verify(context).getString(R.string.action_access_points)
        verify(context, times(count)).getString(WiFiStandard.AC.textResource)
        verify(context, times(count)).getString(FastRoaming.FR_802_11R.textResource)
        verify(exportIntent).intent(title, data)
    }

    @Test
    fun testTimestamp() {
        // setup
        val expected = timestamp(date)
        // execute
        val actual = fixture.timestamp(date)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testData() {
        // setup
        val wiFiDetails = withWiFiDetails()
        val count = wiFiDetails.size
        val timestamp = timestamp(date)
        val expected = data(timestamp)
        doReturn("802.11AC").whenever(context).getString(WiFiStandard.AC.textResource)
        doReturn("802.11R").whenever(context).getString(FastRoaming.FR_802_11R.textResource)
        // execute
        val actual = fixture.data(context, wiFiDetails, timestamp)
        // validate
        assertEquals(expected, actual)
        verify(context, times(count)).getString(WiFiStandard.AC.textResource)
        verify(context, times(count)).getString(FastRoaming.FR_802_11R.textResource)
    }

    @Test
    fun testTitle() {
        // setup
        val timestamp = timestamp(date)
        val expected = "$name-$timestamp"
        doReturn(name).whenever(context).getString(R.string.action_access_points)
        // execute
        val actual = fixture.title(context, timestamp)
        // validate
        assertEquals(expected, actual)
        verify(context).getString(R.string.action_access_points)
    }

    private fun title(timestamp: String): String = "$name-$timestamp"

    private fun data(timestamp: String): String =
        "Time Stamp|SSID|BSSID|Strength|Primary Channel|Primary Frequency|Center Channel|Center Frequency|Width (Range)|Distance|Timestamp|802.11mc|Security|Standard|FastRoaming\n" +
                timestamp + "|SSID10|BSSID10|-10dBm|3|2422MHz|5|2432MHz|40MHz (2412 - 2452)|~0.0m|123456789|true|capabilities10|802.11AC|802.11R\n" +
                timestamp + "|SSID20|BSSID20|-20dBm|5|2432MHz|7|2442MHz|40MHz (2422 - 2462)|~0.1m|123456789|true|capabilities20|802.11AC|802.11R\n" +
                timestamp + "|SSID30|BSSID30|-30dBm|7|2442MHz|9|2452MHz|40MHz (2432 - 2472)|~0.3m|123456789|true|capabilities30|802.11AC|802.11R\n"

    private fun timestamp(date: Date): String = SimpleDateFormat("yyyy/MM/dd-HH:mm:ss", Locale.US).format(date)

    private fun withWiFiDetails(): List<WiFiDetail> =
        listOf(withWiFiDetail(10), withWiFiDetail(20), withWiFiDetail(30))

    private fun withWiFiDetail(offset: Int): WiFiDetail {
        val wiFiSignalExtra = WiFiSignalExtra(true, WiFiStandard.AC, 123456789, listOf(FastRoaming.FR_802_11R))
        val wiFiSignal = WiFiSignal(2412 + offset, 2422 + offset, WiFiWidth.MHZ_40, -offset, wiFiSignalExtra)
        val wiFiIdentifier = WiFiIdentifier("SSID$offset", "BSSID$offset")
        val wiFiSecurity = WiFiSecurity("capabilities$offset")
        return WiFiDetail(wiFiIdentifier, wiFiSecurity, wiFiSignal)
    }

}