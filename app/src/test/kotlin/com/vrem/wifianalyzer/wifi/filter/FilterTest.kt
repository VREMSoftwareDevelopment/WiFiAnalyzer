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
package com.vrem.wifianalyzer.wifi.filter

import android.content.DialogInterface
import android.os.Build
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.filter.Filter.Companion.build
import com.vrem.wifianalyzer.wifi.model.Security
import com.vrem.wifianalyzer.wifi.model.Strength
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
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
    fun alertDialog() {
        // execute
        val actual = fixture.alertDialog!!
        // validate
        assertThat(actual.isShowing).isFalse()
    }

    @Test
    fun show() {
        // execute
        fixture.show()
        // validate
        assertThat(fixture.alertDialog!!.isShowing).isTrue()
    }

    @Test
    fun title() {
        // setup
        val expected = mainActivity.resources.getString(R.string.filter_title)
        val shadowAlertDialog = Shadows.shadowOf(fixture.alertDialog!!)
        // execute
        val actual = shadowAlertDialog.title
        // validate
        assertThat(actual.toString()).isEqualTo(expected)
    }

    @Test
    fun positiveButton() {
        // setup
        fixture.show()
        val button = fixture.alertDialog!!.getButton(DialogInterface.BUTTON_POSITIVE)
        val filtersAdapter = INSTANCE.filterAdapter
        val mainActivity = INSTANCE.mainActivity
        // execute
        button.performClick()
        // validate
        RobolectricUtil.INSTANCE.clearLooper()
        assertThat(fixture.alertDialog!!.isShowing).isFalse()
        verify(filtersAdapter).save()
        verify(mainActivity).update()
    }

    @Test
    fun negativeButton() {
        // setup
        fixture.show()
        val button = fixture.alertDialog!!.getButton(DialogInterface.BUTTON_NEGATIVE)
        val filtersAdapter = INSTANCE.filterAdapter
        val mainActivity = INSTANCE.mainActivity
        // execute
        button.performClick()
        // validate
        RobolectricUtil.INSTANCE.clearLooper()
        assertThat(fixture.alertDialog!!.isShowing).isFalse()
        verify(filtersAdapter).reset()
        verify(mainActivity).update()
    }

    @Test
    fun neutralButton() {
        // setup
        fixture.show()
        val button = fixture.alertDialog!!.getButton(DialogInterface.BUTTON_NEUTRAL)
        val filtersAdapter = INSTANCE.filterAdapter
        val mainActivity = INSTANCE.mainActivity
        // execute
        button.performClick()
        // validate
        RobolectricUtil.INSTANCE.clearLooper()
        assertThat(fixture.alertDialog!!.isShowing).isFalse()
        verify(filtersAdapter).reload()
        verify(mainActivity, never()).update()
    }

    @Test
    fun sSIDFilterViewIsVisible() {
        // setup
        fixture.show()
        // execute
        val actual = fixture.alertDialog!!.findViewById<View>(R.id.filterSSID).visibility
        // validate
        assertThat(actual).isEqualTo(View.VISIBLE)
    }

    @Test
    fun wiFiBandFilterViewIsVisible() {
        // setup
        fixture.show()
        // execute
        val actual = fixture.alertDialog!!.findViewById<View>(R.id.filterWiFiBand).visibility
        // validate
        assertThat(actual).isEqualTo(View.VISIBLE)
    }

    @Test
    fun wiFiBandFilterMapping() {
        // setup
        val expected: Set<WiFiBand> = WiFiBand.entries.toSet()
        fixture.show()
        // execute
        val actual: Map<WiFiBand, Int> = fixture.wiFiBandFilter!!.ids
        // validate
        assertThat(actual).hasSize(expected.size)
        expected.forEach { assertThat(actual[it]).isNotNull() }
    }

    @Test
    fun securityFilterViewIsVisible() {
        // setup
        fixture.show()
        // execute
        val actual = fixture.alertDialog!!.findViewById<View>(R.id.filterSecurity).visibility
        // validate
        assertThat(actual).isEqualTo(View.VISIBLE)
    }

    @Test
    fun securityFilterMapping() {
        // setup
        val expected: Set<Security> = Security.entries.toSet()
        fixture.show()
        // execute
        val actual: Map<Security, Int> = fixture.securityFilter!!.ids
        // validate
        assertThat(actual).hasSize(expected.size)
        expected.forEach { assertThat(actual[it]).isNotNull() }
    }

    @Test
    fun strengthFilterViewIsVisible() {
        // setup
        fixture.show()
        // execute
        val actual = fixture.alertDialog!!.findViewById<View>(R.id.filterStrength).visibility
        // validate
        assertThat(actual).isEqualTo(View.VISIBLE)
    }

    @Test
    fun strengthFilterMapping() {
        // setup
        val expected: Set<Strength> = Strength.entries.toSet()
        fixture.show()
        // execute
        val actual: Map<Strength, Int> = fixture.strengthFilter!!.ids
        // validate
        assertThat(actual).hasSize(expected.size)
        expected.forEach { assertThat(actual[it]).isNotNull() }
    }

}