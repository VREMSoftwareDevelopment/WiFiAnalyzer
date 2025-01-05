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
package com.vrem.wifianalyzer.wifi.timegraph

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class TimeGraphAdapterTest {

    @Test
    fun graphViewNotifiers() {
        // setup
        RobolectricUtil.INSTANCE.activity
        val fixture = TimeGraphAdapter()
        // execute
        val graphViewNotifiers = fixture.graphViewNotifiers()
        // validate
        assertThat(graphViewNotifiers).hasSize(WiFiBand.entries.size)
    }

    @Test
    fun graphViews() {
        // setup
        RobolectricUtil.INSTANCE.activity
        val fixture = TimeGraphAdapter()
        // execute
        val graphViews = fixture.graphViews()
        // validate
        assertThat(graphViews).hasSize(WiFiBand.entries.size)
    }
}