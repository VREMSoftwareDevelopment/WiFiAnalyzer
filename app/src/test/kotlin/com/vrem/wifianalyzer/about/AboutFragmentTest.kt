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
package com.vrem.wifianalyzer.about

import android.content.pm.PackageInfo
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.util.packageInfo
import com.vrem.util.readFile
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog
import java.text.SimpleDateFormat
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class AboutFragmentTest {
    @Suppress("unused")
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val configuration = MainContextHelper.INSTANCE.configuration
    private val wiFiManagerWrapper = MainContextHelper.INSTANCE.wiFiManagerWrapper
    private val fixture = AboutFragment()

    @Before
    fun setUp() {
        doReturn(false).whenever(wiFiManagerWrapper).isScanThrottleEnabled()
        doReturn(false).whenever(wiFiManagerWrapper).is5GHzBandSupported()
        doReturn(false).whenever(wiFiManagerWrapper).is6GHzBandSupported()
        doReturn(true).whenever(configuration).sizeAvailable
        doReturn(true).whenever(configuration).largeScreen
        RobolectricUtil.INSTANCE.startFragment(fixture)
        RobolectricUtil.INSTANCE.clearLooper()
    }

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
        verify(wiFiManagerWrapper).isScanThrottleEnabled()
        verify(wiFiManagerWrapper).is5GHzBandSupported()
        verify(wiFiManagerWrapper).is6GHzBandSupported()
        verify(configuration, atLeastOnce()).sizeAvailable
        verify(configuration).largeScreen
    }

    @Test
    fun onCreateView() {
        assertThat(fixture.view).isNotNull()
    }

    @Test
    fun versionNumber() {
        // setup
        val expected: String = version() + "SL" + " (" + Build.VERSION.RELEASE + "-" + Build.VERSION.SDK_INT + ")"
        // execute
        val actual = fixture.requireView().findViewById<TextView>(R.id.about_version_info)
        // validate
        assertThat(actual).isNotNull()
        assertThat(actual.text).isEqualTo(expected)
    }

    @Test
    fun packageName() {
        // execute
        val actual = fixture.requireView().findViewById<TextView>(R.id.about_package_name)
        // validate
        assertThat(actual).isNotNull()
        assertThat(actual.text).isEqualTo(fixture.requireActivity().packageName)
    }

    @Test
    fun applicationName() {
        // setup
        val expected = fixture.getString(R.string.app_full_name)
        // execute
        val actual = fixture.requireView().findViewById<TextView>(R.id.about_application_name)
        // validate
        assertThat(actual).isNotNull()
        assertThat(actual.text).isEqualTo(expected)
    }

    @Test
    fun copyright() {
        // setup
        val expected = (fixture.getString(R.string.app_copyright) + SimpleDateFormat("yyyy").format(Date()))
        // execute
        val actual = fixture.requireView().findViewById<TextView>(R.id.about_copyright)
        // validate
        assertThat(actual).isNotNull()
        assertThat(actual.text).isEqualTo(expected)
    }

    @Test
    fun device() {
        // setup
        val expected = "robolectric - robolectric - robolectric"
        // execute
        val actual = fixture.requireView().findViewById<TextView>(R.id.about_device)
        // validate
        assertThat(actual).isNotNull()
        assertThat(actual.text).isEqualTo(expected)
    }

    @Test
    fun deviceInformation() {
        assertThat(fixture.requireView().findViewById<TextView>(R.id.about_wifi_throttling_on).visibility).isEqualTo(View.GONE)
        assertThat(fixture.requireView().findViewById<TextView>(R.id.about_wifi_throttling_off).visibility).isEqualTo(View.VISIBLE)

        assertThat(fixture.requireView().findViewById<TextView>(R.id.about_wifi_band_2ghz_success).visibility).isEqualTo(View.VISIBLE)

        assertThat(fixture.requireView().findViewById<TextView>(R.id.about_wifi_band_5ghz_success).visibility).isEqualTo(View.GONE)
        assertThat(fixture.requireView().findViewById<TextView>(R.id.about_wifi_band_5ghz_fails).visibility).isEqualTo(View.VISIBLE)

        assertThat(fixture.requireView().findViewById<TextView>(R.id.about_wifi_band_6ghz_success).visibility).isEqualTo(View.GONE)
        assertThat(fixture.requireView().findViewById<TextView>(R.id.about_wifi_band_6ghz_fails).visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun writeReview() {
        // setup
        val view = fixture.requireView().findViewById<View>(R.id.writeReview)
        // execute
        val actual = view.performClick()
        //
        assertThat(actual).isTrue()
    }

    @Test
    fun alertDialogClickListener() {
        validateAlertDialogClickListener(R.id.contributors, R.string.about_contributor_title, R.raw.contributors)
        validateAlertDialogClickListener(R.id.license, R.string.gpl, R.raw.gpl)
        validateAlertDialogClickListener(R.id.graphViewLicense, R.string.al, R.raw.al)
        validateAlertDialogClickListener(R.id.materialDesignIconsLicense, R.string.al, R.raw.al)
    }

    private fun version(): String {
        val packageInfo: PackageInfo = fixture.requireActivity().packageInfo()
        return packageInfo.versionName + " - " + packageInfo.longVersionCode
    }

    private fun validateAlertDialogClickListener(viewId: Int, titleId: Int, messageId: Int) {
        // setup
        val view = fixture.view!!.findViewById<View>(viewId)
        val expectedTitle = fixture.requireActivity().applicationContext.getString(titleId)
        val expectedMessage = readFile(fixture.requireActivity().resources, messageId)
        // execute
        view.performClick()
        // validate
        val alertDialog = ShadowAlertDialog.getLatestAlertDialog()
        val shadowAlertDialog = shadowOf(alertDialog)
        assertThat(shadowAlertDialog.title.toString()).isEqualTo(expectedTitle)
        assertThat(shadowAlertDialog.message.toString()).isEqualTo(expectedMessage)
    }
}