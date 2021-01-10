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
package com.vrem.wifianalyzer.about

import android.content.pm.PackageInfo
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.util.readFile
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog
import java.text.SimpleDateFormat
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class AboutFragmentTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val configuration = INSTANCE.configuration
    private val fixture = AboutFragment()

    @Before
    fun setUp() {
        whenever(configuration.sizeAvailable).thenReturn(true)
        whenever(configuration.largeScreen).thenReturn(true)
        RobolectricUtil.INSTANCE.startFragment(fixture)
        RobolectricUtil.INSTANCE.clearLooper()
    }

    @After
    fun tearDown() {
        INSTANCE.restore()
        verify(configuration, atLeastOnce()).sizeAvailable
        verify(configuration).largeScreen
    }

    @Test
    fun testOnCreateView() {
        assertNotNull(fixture.view)
    }

    @Test
    fun testVersionNumber() {
        // setup
        val expected: String = version() + "SL" + " (" + Build.VERSION.RELEASE + "-" + Build.VERSION.SDK_INT + ")"
        // execute
        val actual = fixture.requireView().findViewById<TextView>(R.id.about_version_info)
        // validate
        assertNotNull(actual)
        assertEquals(expected, actual.text)
    }

    @Test
    fun testPackageName() {
        // execute
        val actual = fixture.requireView().findViewById<TextView>(R.id.about_package_name)
        // validate
        assertNotNull(actual)
        assertEquals(fixture.requireActivity().packageName, actual.text)
    }

    @Test
    fun testApplicationName() {
        // setup
        val expected = fixture.getString(R.string.app_full_name)
        // execute
        val actual = fixture.requireView().findViewById<TextView>(R.id.about_application_name)
        // validate
        assertNotNull(actual)
        assertEquals(expected, actual.text)
    }

    @Test
    fun testCopyright() {
        // setup
        val expected = (fixture.getString(R.string.app_copyright) + SimpleDateFormat("yyyy").format(Date()))
        // execute
        val actual = fixture.requireView().findViewById<TextView>(R.id.about_copyright)
        // validate
        assertNotNull(actual)
        assertEquals(expected, actual.text)
    }

    @Test
    fun testDevice() {
        // setup
        val expected = "unknown - unknown - robolectric"
        // execute
        val actual = fixture.requireView().findViewById<TextView>(R.id.about_device)
        // validate
        assertNotNull(actual)
        assertEquals(expected, actual.text)
    }

    @Test
    fun testWiFi() {
        assertEquals(View.VISIBLE, fixture.requireView().findViewById<TextView>(R.id.about_wifi_band_2ghz_success).visibility)
        assertEquals(View.GONE, fixture.requireView().findViewById<TextView>(R.id.about_wifi_band_5ghz_success).visibility)
        assertEquals(View.VISIBLE, fixture.requireView().findViewById<TextView>(R.id.about_wifi_band_5ghz_fails).visibility)
        assertEquals(View.GONE, fixture.requireView().findViewById<TextView>(R.id.about_wifi_band_6ghz_success).visibility)
        assertEquals(View.VISIBLE, fixture.requireView().findViewById<TextView>(R.id.about_wifi_band_6ghz_fails).visibility)
    }

    @Test
    fun testWriteReview() {
        // setup
        val view = fixture.requireView().findViewById<View>(R.id.writeReview)
        // execute
        val actual = view.performClick()
        //
        assertTrue(actual)
    }

    @Test
    fun testAlertDialogClickListener() {
        validateAlertDialogClickListener(R.id.contributors, R.string.about_contributor_title, R.raw.contributors)
        validateAlertDialogClickListener(R.id.license, R.string.gpl, R.raw.gpl)
        validateAlertDialogClickListener(R.id.graphViewLicense, R.string.al, R.raw.al)
        validateAlertDialogClickListener(R.id.materialDesignIconsLicense, R.string.al, R.raw.al)
    }

    private fun version(): String {
        val packageInfo: PackageInfo = fixture.requireActivity().packageManager.getPackageInfo(mainActivity.packageName, 0)
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
        assertEquals(expectedTitle, shadowAlertDialog.title.toString())
        assertEquals(expectedMessage, shadowAlertDialog.message.toString())
    }
}