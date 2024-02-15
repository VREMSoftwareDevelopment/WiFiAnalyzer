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
package com.vrem.wifianalyzer.about

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class AboutFragment2Test {
    @Suppress("unused")
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val wiFiManagerWrapper = MainContextHelper.INSTANCE.wiFiManagerWrapper
    private val fixture = AboutFragment()

    @Before
    fun setUp() {
        doReturn(true).whenever(wiFiManagerWrapper).isScanThrottleEnabled()
        doReturn(true).whenever(wiFiManagerWrapper).is5GHzBandSupported()
        doReturn(true).whenever(wiFiManagerWrapper).is6GHzBandSupported()
        RobolectricUtil.INSTANCE.startFragment(fixture)
        RobolectricUtil.INSTANCE.clearLooper()
    }

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
        verify(wiFiManagerWrapper).isScanThrottleEnabled()
        verify(wiFiManagerWrapper).is5GHzBandSupported()
        verify(wiFiManagerWrapper).is6GHzBandSupported()
    }

    @Test
    fun testDeviceInformation() {
        assertEquals(View.VISIBLE, fixture.requireView().findViewById<TextView>(R.id.about_wifi_throttling_on).visibility)
        assertEquals(View.GONE, fixture.requireView().findViewById<TextView>(R.id.about_wifi_throttling_off).visibility)

        assertEquals(View.VISIBLE, fixture.requireView().findViewById<TextView>(R.id.about_wifi_band_2ghz_success).visibility)

        assertEquals(View.VISIBLE, fixture.requireView().findViewById<TextView>(R.id.about_wifi_band_5ghz_success).visibility)
        assertEquals(View.GONE, fixture.requireView().findViewById<TextView>(R.id.about_wifi_band_5ghz_fails).visibility)

        assertEquals(View.VISIBLE, fixture.requireView().findViewById<TextView>(R.id.about_wifi_band_6ghz_success).visibility)
        assertEquals(View.GONE, fixture.requireView().findViewById<TextView>(R.id.about_wifi_band_6ghz_fails).visibility)
    }

}