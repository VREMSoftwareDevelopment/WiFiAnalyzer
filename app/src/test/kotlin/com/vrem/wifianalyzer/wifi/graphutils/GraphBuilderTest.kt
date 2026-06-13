/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2026 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.graphutils

import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.patrykandpatrick.vico.views.cartesian.data.CartesianValueFormatter
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.settings.ThemeStyle
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class GraphBuilderTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity

    @Test
    fun buildCreatesChartView() {
        // setup
        val fixture = GraphBuilder(MAX_Y_DEFAULT, ThemeStyle.DARK)
        // execute
        val actual = fixture.build(mainActivity, false)
        // validate
        assertThat(actual).isNotNull()
        assertThat(actual.visibility).isEqualTo(View.GONE)
        assertThat(actual.layoutParams.width).isEqualTo(ViewGroup.LayoutParams.MATCH_PARENT)
        assertThat(actual.layoutParams.height).isEqualTo(ViewGroup.LayoutParams.MATCH_PARENT)
    }

    @Test
    fun buildWithScalable() {
        // setup
        val fixture = GraphBuilder(MAX_Y_DEFAULT, ThemeStyle.DARK)
        // execute
        val actual = fixture.build(mainActivity, true)
        // validate
        assertThat(actual).isNotNull()
    }

    @Test
    fun buildWithCustomFormatters() {
        // setup
        val xFormatter = CartesianValueFormatter { _, value, _ -> value.toInt().toString() }
        val fixture =
            GraphBuilder(MAX_Y_DEFAULT, ThemeStyle.DARK)
                .setXAxisFormatter(xFormatter)
                .setVerticalTitle("Y Title")
                .setHorizontalTitle("X Title")
        // execute
        val actual = fixture.build(mainActivity, false)
        // validate
        assertThat(actual).isNotNull()
    }

    @Test
    fun getNumVerticalLabels() {
        // setup
        val expected = 9
        val fixture = GraphBuilder(MAX_Y_DEFAULT, ThemeStyle.DARK)
        // execute
        val actual = fixture.numVerticalLabels
        // validate
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun getMaximumYLimits() {
        validateMaximumY(1, MAX_Y_DEFAULT)
        validateMaximumY(0, 0)
        validateMaximumY(-50, -50)
        validateMaximumY(-51, MAX_Y_DEFAULT)
    }

    @Test
    fun getLayoutParamsMatchesParent() {
        // setup
        val fixture = GraphBuilder(MAX_Y_DEFAULT, ThemeStyle.DARK)
        // execute
        val actual = fixture.layoutParams
        // validate
        assertThat(actual.width).isEqualTo(ViewGroup.LayoutParams.MATCH_PARENT)
        assertThat(actual.height).isEqualTo(ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun validateMaximumY(
        maximumY: Int,
        expected: Int,
    ) {
        val fixture = GraphBuilder(maximumY, ThemeStyle.DARK)
        assertThat(fixture.maximumPortY).isEqualTo(expected)
    }
}
