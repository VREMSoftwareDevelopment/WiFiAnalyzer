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
package com.vrem.wifianalyzer.wifi.channelgraph

import android.os.Build
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class ChannelGraphFragmentTest {
    @Suppress("unused")
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val scanner = MainContextHelper.INSTANCE.scannerService
    private val fixture = ChannelGraphFragment()

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun onCreateView() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        assertThat(fixture).isNotNull()
        verify(scanner).update()
        verify(scanner).register(fixture.channelGraphAdapter)
    }

    @Test
    fun refreshEnabled() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        val swipeRefreshLayout: SwipeRefreshLayout = fixture.view!!.findViewById(R.id.graphRefresh)
        assertThat(swipeRefreshLayout.isEnabled).isTrue()
    }

    @Test
    fun onResume() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // execute
        fixture.onResume()
        // validate
        verify(scanner, times(2)).update()
        verify(scanner, times(2)).register(fixture.channelGraphAdapter)
    }

    @Test
    fun onPause() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // execute
        fixture.onPause()
        // validate
        verify(scanner).unregister(fixture.channelGraphAdapter)
    }

    @Config(sdk = [Build.VERSION_CODES.P])
    @Test
    fun refreshDisabled() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        val swipeRefreshLayout: SwipeRefreshLayout = fixture.view!!.findViewById(R.id.graphRefresh)
        assertThat(swipeRefreshLayout.isRefreshing).isFalse()
        assertThat(swipeRefreshLayout.isEnabled).isFalse()
    }
}