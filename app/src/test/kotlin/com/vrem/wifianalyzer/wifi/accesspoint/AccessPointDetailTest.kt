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
package com.vrem.wifianalyzer.wifi.accesspoint

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class AccessPointDetailTest {
    private val vendorName = "1VendorName-2VendorName-3VendorName-4VendorName-5VendorName-6VendorName"
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val settings = INSTANCE.settings
    private val fixture = AccessPointDetail()
    private val expectedSecurities = "[WPS WEP WPA2 WPA3]"
    private val expectedSecurityTypes =
        "[DPP EAP OPEN OSEN PASSPOINT_R1_R2 PASSPOINT_R3 PSK WAPI_CERT WAPI_PSK WEP EAP_WPA3_ENTERPRISE EAP_WPA3_ENTERPRISE_192_BIT OWE SAE]"

    @Before
    fun setUp() {
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPLETE)
    }

    @After
    fun tearDown() {
        INSTANCE.restore()
    }

    @Test
    fun makeViewShouldCreateNewView() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertThat(actual).isNotNull
    }

    @Test
    fun makeViewShouldUseGivenView() {
        // setup
        val expected = mainActivity.layoutInflater.inflate(AccessPointViewType.COMPLETE.layout, null, false)
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeView(expected, null, wiFiDetail)
        // validate
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun makeViewCompleteWithTabGone() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertThat(actual.findViewById<View>(R.id.tab).visibility).isEqualTo(View.GONE)
    }

    @Test
    fun makeViewCompleteWithGroupIndicatorGone() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertThat(actual.findViewById<View>(R.id.groupIndicator).visibility).isEqualTo(View.GONE)
    }

    @Test
    fun makeViewCompleteWithVendorShortNotVisible() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertThat(actual.findViewById<View>(R.id.vendorLong)).isNull()
        assertThat(actual.findViewById<View>(R.id.vendorShort).visibility).isEqualTo(View.GONE)
    }

    @Test
    fun makeViewCompleteWithVendorShortVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(wiFiAdditional = WiFiAdditional(vendorName, WiFiConnection.EMPTY))
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertThat(actual.findViewById<View>(R.id.vendorLong)).isNull()
        assertThat(actual.findViewById<View>(R.id.vendorShort).visibility).isEqualTo(View.VISIBLE)
        validateTextViewValue(actual, vendorName, R.id.vendorShort)
    }

    @Test
    fun makeViewCompleteWithTabVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(String.EMPTY)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail, true)
        // validate
        assertThat(actual.findViewById<View>(R.id.tab).visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun makeViewCompleteWithWiFiDetailAndEmptySSID() {
        // setup
        val wiFiDetail = withWiFiDetail(String.EMPTY)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateTextViewValuesCompleteView(actual, wiFiDetail)
    }

    @Test
    fun makeViewCompleteWithWiFiDetail() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateTextViewValuesCompleteView(actual, wiFiDetail)
    }

    @Test
    fun makeViewCompleteWithTextNotSelectable() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertThat(actual.findViewById<TextView>(R.id.ssid).isTextSelectable).isFalse()
    }

    @Test
    fun makeViewCompactWithTabGone() {
        // setup
        val wiFiDetail = withWiFiDetail()
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertThat(actual.findViewById<View>(R.id.tab).visibility).isEqualTo(View.GONE)
    }

    @Test
    fun makeViewCompactWithGroupIndicatorGone() {
        // setup
        val wiFiDetail = withWiFiDetail()
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertThat(actual.findViewById<View>(R.id.groupIndicator).visibility).isEqualTo(View.GONE)
    }

    @Test
    fun makeViewCompactWithTabVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(String.EMPTY)
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail, true)
        // validate
        assertThat(actual.findViewById<View>(R.id.tab).visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun makeViewCompactWithWiFiDetailAndEmptySSID() {
        // setup
        val wiFiDetail = withWiFiDetail(String.EMPTY)
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateTextViewValuesCompactView(actual, wiFiDetail)
    }

    @Test
    fun makeViewCompactWithWiFiDetail() {
        // setup
        val wiFiDetail = withWiFiDetail()
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateTextViewValuesCompactView(actual, wiFiDetail)
    }

    @Test
    fun makeViewCompactWithAttachPopup() {
        // setup
        val wiFiDetail = withWiFiDetail()
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertThat(actual.findViewById<View>(R.id.attachPopup)).isNotNull()
    }

    @Test
    fun makeViewCompactDoesNotHaveFullDetails() {
        // setup
        val wiFiDetail = withWiFiDetail()
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertThat(actual.findViewById<View>(R.id.levelImage)).isNull()
        assertThat(actual.findViewById<View>(R.id.wiFiStandardValue)).isNull()
        assertThat(actual.findViewById<View>(R.id.channel_frequency_range)).isNull()
        assertThat(actual.findViewById<View>(R.id.width)).isNull()
        assertThat(actual.findViewById<View>(R.id.capabilities)).isNull()
        assertThat(actual.findViewById<View>(R.id.vendorShort)).isNull()
    }

    @Test
    fun makeViewCompactWithTextNotSelectable() {
        // setup
        val wiFiDetail = withWiFiDetail()
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertThat(actual.findViewById<TextView>(R.id.ssid).isTextSelectable).isFalse()
    }

    @Test
    fun makeViewPopupWithWiFiDetail() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        validateTextViewValuesPopupView(actual, wiFiDetail)
    }

    @Test
    fun makeViewDetailedWithVendorNotVisible() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        assertThat(actual.findViewById<View>(R.id.vendorShort).visibility).isEqualTo(View.GONE)
        assertThat(actual.findViewById<View>(R.id.vendorLong).visibility).isEqualTo(View.GONE)
    }

    @Test
    fun makeViewDetailedWithVendorVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(wiFiAdditional = WiFiAdditional(vendorName, WiFiConnection.EMPTY))
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        assertThat(actual.findViewById<View>(R.id.vendorShort).visibility).isEqualTo(View.GONE)
        assertThat(actual.findViewById<View>(R.id.vendorLong).visibility).isEqualTo(View.VISIBLE)
        validateTextViewValue(actual, vendorName, R.id.vendorLong)
    }

    @Test
    fun makeViewDetailedWithTextSelectable() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        assertThat(actual.findViewById<TextView>(R.id.ssid).isTextSelectable).isTrue()
        assertThat(actual.findViewById<TextView>(R.id.vendorLong).isTextSelectable).isTrue()
    }

    @Test
    fun makeViewDetailedWith80211mcNotVisible() {
        // setup
        val wiFiDetail = withWiFiDetail()
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        assertThat(actual.findViewById<View>(R.id.flag80211mc).visibility).isEqualTo(View.GONE)
    }

    @Test
    fun makeViewDetailedWith80211mcVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(is80211mc = true)
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        assertThat(actual.findViewById<View>(R.id.flag80211mc).visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun makeViewDetailedWithFastRoaming() {
        // setup
        val wiFiDetail = withWiFiDetail()
        val expectedFastRoaming = "802.11k 802.11r 802.11v"
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        validateTextViewValue(actual, expectedFastRoaming, R.id.fastRoaming)
    }


    private fun withWiFiDetail(
        ssid: String = "SSID",
        wiFiAdditional: WiFiAdditional = WiFiAdditional.EMPTY,
        is80211mc: Boolean = false
    ): WiFiDetail =
        WiFiDetail(
            WiFiIdentifier(ssid, "BSSID"),
            WiFiSecurity("[WPS-capabilities][WPA2-XYZ][XYZ-FT]", WiFiSecurityTypeTest.All),
            WiFiSignal(
                1, 1, WiFiWidth.MHZ_40, 2,
                WiFiSignalExtra(is80211mc, WiFiStandard.AC, FastRoaming.entries.toList())
            ),
            wiFiAdditional
        )

    private fun validateTextViewValuesCompleteView(view: View, wiFiDetail: WiFiDetail) {
        validateTextViewValuesCompactView(view, wiFiDetail)
        val wiFiSignal = wiFiDetail.wiFiSignal
        validateTextViewValue(view, "${wiFiSignal.frequencyStart} - ${wiFiSignal.frequencyEnd}", R.id.channel_frequency_range)
        validateTextViewValue(view, "(${wiFiSignal.wiFiWidth.frequencyWidth}${WiFiSignal.FREQUENCY_UNITS})", R.id.width)
        validateTextViewValue(view, expectedSecurities, R.id.capabilities)
        validateImageViewValue(view, wiFiSignal.strength.imageResource, R.id.levelImage)
        validateImageViewValue(view, wiFiDetail.wiFiSecurity.security.imageResource, R.id.securityImage)
        val expectedWiFiStandard = view.context.getString(wiFiSignal.extra.wiFiStandard.valueResource)
        validateTextViewValue(view, expectedWiFiStandard, R.id.wiFiStandardValue)
    }

    private fun validateTextViewValuesPopupView(view: View, wiFiDetail: WiFiDetail) {
        validateTextViewValuesCompleteView(view, wiFiDetail)
        with(wiFiDetail) {
            validateTextViewValue(view, wiFiSecurity.capabilities, R.id.capabilitiesLong)
            validateTextViewValue(view, expectedSecurityTypes, R.id.securityTypes)
            val expectedWiFiStandard = view.context.getString(wiFiSignal.extra.wiFiStandard.fullResource)
            validateTextViewValue(view, expectedWiFiStandard, R.id.wiFiStandardFull)
            val expectedWiFiBand = view.context.getString(wiFiSignal.wiFiBand.textResource)
            validateTextViewValue(view, expectedWiFiBand, R.id.wiFiBand)
        }
    }

    private fun validateTextViewValuesCompactView(view: View, wiFiDetail: WiFiDetail) {
        val wiFiSignal = wiFiDetail.wiFiSignal
        validateTextViewValue(view, wiFiDetail.wiFiIdentifier.title, R.id.ssid)
        validateTextViewValue(view, "${wiFiSignal.level}dBm", R.id.level)
        validateTextViewValue(view, wiFiSignal.channelDisplay(), R.id.channel)
        validateTextViewValue(view, "${wiFiSignal.primaryFrequency}${WiFiSignal.FREQUENCY_UNITS}", R.id.primaryFrequency)
        validateTextViewValue(view, wiFiSignal.distance, R.id.distance)
    }

    private fun validateTextViewValue(view: View, expected: String, id: Int) {
        assertThat(view.findViewById<TextView>(id).text.toString()).isEqualTo(expected)
    }

    private fun validateImageViewValue(view: View, @DrawableRes expected: Int, id: Int) {
        assertThat(view.findViewById<ImageView>(id).tag).isEqualTo(expected)
    }

}