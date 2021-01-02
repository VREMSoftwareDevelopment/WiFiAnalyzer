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
package com.vrem.wifianalyzer.wifi.filter

import android.content.DialogInterface
import android.os.Build
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.filter.Filter.Companion.build
import com.vrem.wifianalyzer.wifi.model.Security
import com.vrem.wifianalyzer.wifi.model.Strength
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class FilterTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val fixture = build()

    @Before
    fun setUp() {
        RobolectricUtil.INSTANCE.clearLooper()
    }

    @After
    fun tearDown() {
        INSTANCE.restore()
    }

    @Test
    fun testAlertDialog() {
        // execute
        val actual = fixture.alertDialog!!
        // validate
        assertFalse(actual.isShowing)
    }

    @Test
    fun testShow() {
        // execute
        fixture.show()
        // validate
        assertTrue(fixture.alertDialog!!.isShowing)
    }

    @Test
    fun testTitle() {
        // setup
        val expected = mainActivity.resources.getString(R.string.filter_title)
        val shadowAlertDialog = Shadows.shadowOf(fixture.alertDialog!!)
        // execute
        val actual = shadowAlertDialog.title
        // validate
        assertEquals(expected, actual.toString())
    }

    @Test
    fun testPositiveButton() {
        // setup
        fixture.show()
        val button = fixture.alertDialog!!.getButton(DialogInterface.BUTTON_POSITIVE)
        val filtersAdapter = INSTANCE.filterAdapter
        val mainActivity = INSTANCE.mainActivity
        // execute
        button.performClick()
        // validate
        RobolectricUtil.INSTANCE.clearLooper()
        assertFalse(fixture.alertDialog!!.isShowing)
        verify(filtersAdapter).save()
        verify(mainActivity).update()
    }

    @Test
    fun testNegativeButton() {
        // setup
        fixture.show()
        val button = fixture.alertDialog!!.getButton(DialogInterface.BUTTON_NEGATIVE)
        val filtersAdapter = INSTANCE.filterAdapter
        val mainActivity = INSTANCE.mainActivity
        // execute
        button.performClick()
        // validate
        RobolectricUtil.INSTANCE.clearLooper()
        assertFalse(fixture.alertDialog!!.isShowing)
        verify(filtersAdapter).reset()
        verify(mainActivity).update()
    }

    @Test
    fun testNeutralButton() {
        // setup
        fixture.show()
        val button = fixture.alertDialog!!.getButton(DialogInterface.BUTTON_NEUTRAL)
        val filtersAdapter = INSTANCE.filterAdapter
        val mainActivity = INSTANCE.mainActivity
        // execute
        button.performClick()
        // validate
        RobolectricUtil.INSTANCE.clearLooper()
        assertFalse(fixture.alertDialog!!.isShowing)
        verify(filtersAdapter).reload()
        verify(mainActivity, never()).update()
    }

    @Test
    fun testSSIDFilterViewIsVisible() {
        // setup
        fixture.show()
        // execute
        val actual = fixture.alertDialog!!.findViewById<View>(R.id.filterSSID).visibility
        // validate
        assertEquals(View.VISIBLE, actual)
    }

    @Test
    fun testWiFiBandFilterViewIsVisible() {
        // setup
        fixture.show()
        // execute
        val actual = fixture.alertDialog!!.findViewById<View>(R.id.filterWiFiBand).visibility
        // validate
        assertEquals(View.VISIBLE, actual)
    }

    @Test
    fun testWiFiBandFilterMapping() {
        // setup
        val expected: Set<WiFiBand> = WiFiBand.values().toSet()
        fixture.show()
        // execute
        val actual: Map<WiFiBand, Int> = fixture.wiFiBandFilter!!.ids
        // validate
        assertEquals(expected.size, actual.size)
        expected.forEach { assertNotNull(actual[it]) }
    }

    @Test
    fun testSecurityFilterViewIsVisible() {
        // setup
        fixture.show()
        // execute
        val actual = fixture.alertDialog!!.findViewById<View>(R.id.filterSecurity).visibility
        // validate
        assertEquals(View.VISIBLE, actual)
    }

    @Test
    fun testSecurityFilterMapping() {
        // setup
        val expected: Set<Security> = Security.values().toSet()
        fixture.show()
        // execute
        val actual: Map<Security, Int> = fixture.securityFilter!!.ids
        // validate
        assertEquals(expected.size, actual.size)
        expected.forEach { assertNotNull(actual[it]) }
    }


    @Test
    fun testStrengthFilterViewIsVisible() {
        // setup
        fixture.show()
        // execute
        val actual = fixture.alertDialog!!.findViewById<View>(R.id.filterStrength).visibility
        // validate
        assertEquals(View.VISIBLE, actual)
    }

    @Test
    fun testStrengthFilterMapping() {
        // setup
        val expected: Set<Strength> = Strength.values().toSet()
        fixture.show()
        // execute
        val actual: Map<Strength, Int> = fixture.strengthFilter!!.ids
        // validate
        assertEquals(expected.size, actual.size)
        expected.forEach { assertNotNull(actual[it]) }
    }

}