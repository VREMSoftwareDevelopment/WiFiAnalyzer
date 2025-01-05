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

import android.content.DialogInterface
import android.os.Build
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class AccessPointPopupTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val fixture = AccessPointPopup()

    @Test
    fun showOpensPopup() {
        // setup
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_popup, null)
        // execute
        val actual = fixture.show(view)
        // validate
        assertThat(actual).isNotNull()
        assertThat(actual.isShowing).isTrue()
    }

    @Test
    fun popupIsClosedOnPositiveButtonClick() {
        // setup
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_popup, null)
        val alertDialog = fixture.show(view)
        val button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        // execute
        button.performClick()
        // validate
        RobolectricUtil.INSTANCE.clearLooper()
        assertThat(alertDialog.isShowing).isFalse()
    }

    @Test
    fun popupPositiveButtonIsNotVisible() {
        // setup
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_popup, null)
        val alertDialog = fixture.show(view)
        // execute
        val actual = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        // validate
        assertThat(actual.visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun popupNegativeButtonIsNotVisible() {
        // setup
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_popup, null)
        val alertDialog = fixture.show(view)
        // execute
        val actual = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        // validate
        assertThat(actual.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun popupNeutralButtonIsNotVisible() {
        // setup
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_popup, null)
        val alertDialog = fixture.show(view)
        // execute
        val actual = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL)
        // validate
        assertThat(actual.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun attach() {
        // setup
        val wiFiDetail = withWiFiDetail()
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_compact, null)
        // execute
        fixture.attach(view, wiFiDetail)
        // validate
        assertThat(view.performClick()).isTrue()
    }

    private fun withWiFiDetail(): WiFiDetail =
        WiFiDetail(
            WiFiIdentifier("SSID", "BSSID"),
            WiFiSecurity("capabilities"),
            WiFiSignal(1, 1, WiFiWidth.MHZ_40, 2)
        )
}