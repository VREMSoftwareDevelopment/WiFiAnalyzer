/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiWidth
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
@LooperMode(LooperMode.Mode.PAUSED)
class AccessPointPopupTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val fixture = AccessPointPopup()

    @Test
    fun testShowOpensPopup() {
        // setup
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_popup, null)
        // execute
        val actual = fixture.show(view)
        // validate
        assertNotNull(actual)
        assertTrue(actual.isShowing)
    }

    @Test
    fun testPopupIsClosedOnCloseButtonClick() {
        // setup
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_popup, null)
        val dialog = fixture.show(view)
        val closeButton = dialog.findViewById<View>(R.id.popupButtonClose)
        // execute
        closeButton.performClick()
        // validate
        assertFalse(dialog.isShowing)
    }

    @Test
    fun testAttach() {
        // setup
        val wiFiDetail = withWiFiDetail()
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_compact, null)
        // execute
        fixture.attach(view, wiFiDetail)
        // validate
        assertTrue(view.performClick())
    }

    private fun withWiFiDetail(): WiFiDetail =
            WiFiDetail(
                    WiFiIdentifier("SSID", "BSSID"),
                    "capabilities",
                    WiFiSignal(1, 1, WiFiWidth.MHZ_40, 2, true),
                    WiFiAdditional.EMPTY)
}