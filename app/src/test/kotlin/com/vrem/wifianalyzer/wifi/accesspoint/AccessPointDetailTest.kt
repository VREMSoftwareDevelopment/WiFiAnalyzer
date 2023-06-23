/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.accesspoint

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.model.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class AccessPointDetailTest {
    private val vendorName = "VendorName-VendorName-VendorName-VendorName-VendorName-VendorName"
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val settings = INSTANCE.settings
    private val fixture = AccessPointDetail()

    @Before
    fun setUp() {
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPLETE)
    }

    @After
    fun tearDown() {
        INSTANCE.restore()
    }

    @Test
    fun testMakeViewShouldCreateNewView() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertNotNull(actual)
    }

    @Test
    fun testMakeViewShouldUseGivenView() {
        // setup
        val expected = mainActivity.layoutInflater.inflate(AccessPointViewType.COMPLETE.layout, null, false)
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeView(expected, null, wiFiDetail)
        // validate
        if (expected != actual) System.out.println("MISMATCH: makeView() = $actual ≠ $expected")
        assertEquals(expected, actual)
    }

    @Test
    fun testMakeViewCompleteWithTabGone() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateViewVisibility(actual, View.GONE, R.id.tab)
    }

    @Test
    fun testMakeViewCompleteWithGroupIndicatorGone() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateViewVisibility(actual, View.GONE, R.id.groupIndicator)
    }

    @Test
    fun testMakeViewCompleteWithVendorShortInvisible() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertNull(actual.findViewById(R.id.vendorLong))
        validateViewVisibility(actual, View.GONE, R.id.vendorShort)
    }

    @Test
    fun testMakeViewCompleteWithVendorShortVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(wiFiAdditional = WiFiAdditional(vendorName, WiFiConnection.EMPTY))
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertNull(actual.findViewById(R.id.vendorLong))
        validateViewVisibility(actual, View.VISIBLE, R.id.vendorShort)
    }

    @Test
    fun testMakeViewCompleteWithVendorShortMaximumSize() {
        // setup
        val wiFiDetail = withWiFiDetail(wiFiAdditional = WiFiAdditional(vendorName, WiFiConnection.EMPTY))
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateTextViewValue(actual, vendorName.substring(0, 12), R.id.vendorShort)
    }

    @Test
    fun testMakeViewCompleteWithTabVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(String.EMPTY)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail, true)
        // validate
        validateViewVisibility(actual, View.VISIBLE, R.id.tab)
    }

    @Test
    fun testMakeViewCompleteWithWiFiDetailAndEmptySSID() {
        // setup
        val wiFiDetail = withWiFiDetail(String.EMPTY)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateTextViewValuesCompleteView(actual, wiFiDetail)
    }

    @Test
    fun testMakeViewCompleteWithWiFiDetail() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateTextViewValuesCompleteView(actual, wiFiDetail)
    }

    @Test
    fun testMakeViewCompleteWithTextNotSelectable() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertFalse(actual.findViewById<TextView>(R.id.ssid).isTextSelectable)
    }

    @Test
    fun testMakeViewCompactWithTabGone() {
        // setup
        val wiFiDetail = withWiFiDetail()
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateViewVisibility(actual, View.GONE, R.id.tab)
    }

    @Test
    fun testMakeViewCompactWithGroupIndicatorGone() {
        // setup
        val wiFiDetail = withWiFiDetail()
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateViewVisibility(actual, View.GONE, R.id.groupIndicator)
    }

    @Test
    fun testMakeViewCompactWithTabVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(String.EMPTY)
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail, true)
        // validate
        validateViewVisibility(actual, View.VISIBLE, R.id.tab)
    }

    @Test
    fun testMakeViewCompactWithWiFiDetailAndEmptySSID() {
        // setup
        val wiFiDetail = withWiFiDetail(String.EMPTY)
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateTextViewValuesCompactView(actual, wiFiDetail)
    }

    @Test
    fun testMakeViewCompactWithWiFiDetail() {
        // setup
        val wiFiDetail = withWiFiDetail()
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateTextViewValuesCompactView(actual, wiFiDetail)
    }

    @Test
    fun testMakeViewCompactWithAttachPopup() {
        // setup
        val wiFiDetail = withWiFiDetail()
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertNotNull(actual.findViewById(R.id.attachPopup))
    }

    @Test
    fun testMakeViewCompactDoesNotHaveFullDetails() {
        // setup
        val wiFiDetail = withWiFiDetail()
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertNull(actual.findViewById(R.id.levelImage))
        assertNull(actual.findViewById(R.id.wiFiStandardImage))
        assertNull(actual.findViewById(R.id.channel_frequency_range))
        assertNull(actual.findViewById(R.id.width))
        assertNull(actual.findViewById(R.id.capabilities))
        assertNull(actual.findViewById(R.id.vendorShort))
    }

    @Test
    fun testMakeViewCompactWithTextNotSelectable() {
        // setup
        val wiFiDetail = withWiFiDetail()
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertFalse(actual.findViewById<TextView>(R.id.ssid).isTextSelectable)
    }

    @Test
    fun testMakeViewPopupWithWiFiDetail() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        validateTextViewValuesPopupView(actual, wiFiDetail)
    }

    @Test
    fun testMakeViewDetailedWithVendorInvisible() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        validateViewVisibility(actual, View.GONE, R.id.vendorShort)
        validateViewVisibility(actual, View.GONE, R.id.vendorLong)
    }

    @Test
    fun testMakeViewDetailedWithVendorVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(wiFiAdditional = WiFiAdditional(vendorName, WiFiConnection.EMPTY))
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        validateViewVisibility(actual, View.GONE, R.id.vendorShort)
        validateViewVisibility(actual, View.VISIBLE, R.id.vendorLong)
    }

    @Test
    fun testMakeViewDetailedWithTextSelectable() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        assertTrue(actual.findViewById<TextView>(R.id.ssid).isTextSelectable)
        assertTrue(actual.findViewById<TextView>(R.id.vendorLong).isTextSelectable)
    }

    @Test
    fun testMakeViewDetailedWith80211mcInvisible() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        validateViewVisibility(actual, View.GONE, R.id.flag80211mc)
    }

    @Test
    fun testMakeViewDetailedWith80211mcVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(is80211mc = true)
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        validateViewVisibility(actual, View.VISIBLE, R.id.flag80211mc)
    }

    @Test
    fun testMakeViewDetailedWithTimestampInvisible() {
        validateInvisibleTimestamp(timestamp =              0)
        validateInvisibleTimestamp(timestamp =       -1000000)  // 1 second in the future
        validateInvisibleTimestamp(timestamp = 31536000000000)  // 1 year ago
    }

    @Test
    fun testMakeViewDetailedWithTimestampVisible() {
        validateVisibleTimestamp(timestamp =              1, expected = "1μs")
        validateVisibleTimestamp(timestamp =           9999, expected = "9999μs")
        validateVisibleTimestamp(timestamp =          10000, expected = "10ms")
        validateVisibleTimestamp(timestamp =          10001, expected = "10.001ms")
        validateVisibleTimestamp(timestamp =          99999, expected = "99.999ms")
        validateVisibleTimestamp(timestamp =         100000, expected = "100ms")
        validateVisibleTimestamp(timestamp =         100006, expected = "100.01ms")
        validateVisibleTimestamp(timestamp =         999994, expected = "999.99ms")
        validateVisibleTimestamp(timestamp =         999996, expected = "1s")
        validateVisibleTimestamp(timestamp =        1000049, expected = "1s")
        validateVisibleTimestamp(timestamp =        1000051, expected = "1.0001s")
        validateVisibleTimestamp(timestamp =        1234567, expected = "1.2346s")
        validateVisibleTimestamp(timestamp =        9999949, expected = "9.9999s")
        validateVisibleTimestamp(timestamp =        9999951, expected = "10s")
        validateVisibleTimestamp(timestamp =       10000051, expected = "10.001s")
        validateVisibleTimestamp(timestamp =       59999499, expected = "59.999s")
        validateVisibleTimestamp(timestamp =       59999501, expected = "1m")
        validateVisibleTimestamp(timestamp =       60004999, expected = "1m")
        validateVisibleTimestamp(timestamp =       60005001, expected = "1m1s")
        validateVisibleTimestamp(timestamp =      123456789, expected = "2m3.46s")
        validateVisibleTimestamp(timestamp =     1234567890, expected = "20m34.6s")
        validateVisibleTimestamp(timestamp =    12345678901, expected = "3h25m46s")
        validateVisibleTimestamp(timestamp =   123456789012, expected = "1d10h18m")
        validateVisibleTimestamp(timestamp =  1234567890123, expected = "14d7h")
        validateVisibleTimestamp(timestamp = 12345678901234, expected = "142d21h")
        validateVisibleTimestamp(timestamp = 86398199999999, expected = "999d23h")
        validateVisibleTimestamp(timestamp = 86398200000001, expected = "1000d")
    }

    private fun validateInvisibleTimestamp(timestamp: Long) {
        val viewResult = fixture.makeViewDetailed(withWiFiDetail(timestamp = timestamp))
        validateViewVisibility(viewResult, View.GONE, R.id.timestamp)
        validateTextViewValue(viewResult, String.EMPTY, R.id.timestamp)
    }

    private fun validateVisibleTimestamp(timestamp: Long, expected: String) {
        val viewResult = fixture.makeViewDetailed(withWiFiDetail(timestamp = timestamp))
        validateViewVisibility(viewResult, View.VISIBLE, R.id.timestamp)
        validateTextViewValue(viewResult, expected, R.id.timestamp)
    }

    private fun withWiFiDetail(
        ssid: String = "SSID",
        wiFiAdditional: WiFiAdditional = WiFiAdditional.EMPTY,
        is80211mc: Boolean = false,
        timestamp: Long = 1000000
    ): WiFiDetail =
        WiFiDetail(
            WiFiIdentifier(ssid, "BSSID"),
            "[WPS-capabilities][WPA2-XYZ][XYZ-FT/SAE-XYZ-abc]",
            WiFiSignal(1, 1, WiFiWidth.MHZ_40, 2, is80211mc, WiFiStandard.AC, timestamp),
            wiFiAdditional
        )

    private fun validateTextViewValuesCompleteView(view: View, wiFiDetail: WiFiDetail) {
        validateTextViewValuesCompactView(view, wiFiDetail)
        val wiFiSignal = wiFiDetail.wiFiSignal
        validateTextViewValue(view, "${wiFiSignal.frequencyStart} - ${wiFiSignal.frequencyEnd}", R.id.channel_frequency_range)
        validateTextViewValue(view, "(${wiFiSignal.wiFiWidth.frequencyWidth}${WiFiSignal.FREQUENCY_UNITS})", R.id.width)
        validateTextViewValue(view, "[WPS WPA2 WPA3]", R.id.capabilities)
        validateImageViewValue(view, wiFiSignal.strength.imageResource, R.id.levelImage)
        validateImageViewValue(view, wiFiDetail.security.imageResource, R.id.securityImage)
        validateImageViewValue(view, wiFiSignal.wiFiStandard.imageResource, R.id.wiFiStandardImage)
    }

    private fun validateTextViewValuesPopupView(view: View, wiFiDetail: WiFiDetail) {
        validateTextViewValuesCompleteView(view, wiFiDetail)
        validateTextViewValue(view, wiFiDetail.capabilities, R.id.capabilitiesLong)
        val expectedWiFiStandard =
            view.context.getString(wiFiDetail.wiFiSignal.wiFiStandard.textResource)
        validateTextViewValue(view, expectedWiFiStandard, R.id.wiFiStandard)
        val expectedWiFiBand = view.context.getString(wiFiDetail.wiFiSignal.wiFiBand.textResource)
        validateTextViewValue(view, expectedWiFiBand, R.id.wiFiBand)
    }

    private fun validateTextViewValuesCompactView(view: View, wiFiDetail: WiFiDetail) {
        val wiFiSignal = wiFiDetail.wiFiSignal
        validateTextViewValue(view, wiFiDetail.wiFiIdentifier.title, R.id.ssid)
        validateTextViewValue(view, "${wiFiSignal.level}dBm", R.id.level)
        validateTextViewValue(view, wiFiSignal.channelDisplay(), R.id.channel)
        validateTextViewValue(view, "${wiFiSignal.primaryFrequency}${WiFiSignal.FREQUENCY_UNITS}", R.id.primaryFrequency)
        validateTextViewValue(view, wiFiSignal.distance, R.id.distance)
    }

    private fun validateViewVisibility(view: View, /*@VisibilityRes*/ expected: Int, id: Int) {
        val actual = view.findViewById<View>(id).visibility
        if (expected != actual) System.out.println("MISMATCH: View[$id].visibility = $actual ≠ $expected")
        assertEquals(expected, actual)
    }

    private fun validateTextViewValue(view: View, expected: String, id: Int) {
        val actual = view.findViewById<TextView>(id).text.toString()
        if (expected != actual) System.out.println("MISMATCH: TextView[$id].text = $actual ≠ $expected")
        assertEquals(expected, actual)
    }

    private fun validateImageViewValue(view: View, @DrawableRes expected: Int, id: Int) {
        val actual = view.findViewById<ImageView>(id).tag
        if (expected != actual) System.out.println("MISMATCH: ImageView[$id].tag = $actual ≠ $expected")
        assertEquals(expected, actual)
    }

}
