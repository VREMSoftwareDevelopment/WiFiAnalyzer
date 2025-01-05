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
package com.vrem.wifianalyzer.wifi.channelrating

import android.os.Build
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.scanner.ScannerService
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class ChannelRatingFragmentTest {
    @Suppress("unused")
    private val mainActivity: MainActivity = RobolectricUtil.INSTANCE.activity
    private val scanner: ScannerService = INSTANCE.scannerService
    private val fixture: ChannelRatingFragment = ChannelRatingFragment()

    @After
    fun tearDown() {
        INSTANCE.restore()
    }

    @Test
    fun onCreateView() {
        // execute
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        verify(scanner).update()
        verify(scanner).register(fixture.channelRatingAdapter)
    }

    @Test
    fun refreshEnabled() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // execute
        val swipeRefreshLayout: SwipeRefreshLayout = fixture.view!!.findViewById(R.id.channelRatingRefresh)
        // validate
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
        verify(scanner, times(2)).register(fixture.channelRatingAdapter)
    }

    @Test
    fun onPause() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // execute
        fixture.onPause()
        // validate
        verify(scanner).unregister(fixture.channelRatingAdapter)
    }

    @Config(sdk = [Build.VERSION_CODES.P])
    @Test
    fun refreshDisabled() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // execute
        val swipeRefreshLayout: SwipeRefreshLayout = fixture.view!!.findViewById(R.id.channelRatingRefresh)
        // validate
        assertThat(swipeRefreshLayout.isRefreshing).isFalse()
        assertThat(swipeRefreshLayout.isEnabled).isFalse()
    }
}