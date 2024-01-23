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
package com.vrem.wifianalyzer.wifi.timegraph

import android.os.Build
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class TimeGraphFragmentTest {
    @Suppress("unused")
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val fixture = TimeGraphFragment()
    private val scannerService = MainContextHelper.INSTANCE.scannerService

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testOnCreateView() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        assertNotNull(fixture)
        verify(scannerService).update()
        verify(scannerService).register(fixture.timeGraphAdapter)
    }

    @Test
    fun testRefreshEnabled() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        val swipeRefreshLayout: SwipeRefreshLayout = fixture.view!!.findViewById(R.id.graphRefresh)
        assertTrue(swipeRefreshLayout.isEnabled)
    }

    @Test
    fun testOnResume() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // execute
        fixture.onResume()
        // validate
        verify(scannerService, times(2)).update()
        verify(scannerService, times(2)).register(fixture.timeGraphAdapter)
    }

    @Test
    fun testOnPause() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // execute
        fixture.onPause()
        // validate
        verify(scannerService).unregister(fixture.timeGraphAdapter)
    }

    @Config(sdk = [Build.VERSION_CODES.P])
    @Test
    fun testRefreshDisabled() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        val swipeRefreshLayout: SwipeRefreshLayout = fixture.view!!.findViewById(R.id.graphRefresh)
        assertFalse(swipeRefreshLayout.isRefreshing)
        assertFalse(swipeRefreshLayout.isEnabled)
    }
}