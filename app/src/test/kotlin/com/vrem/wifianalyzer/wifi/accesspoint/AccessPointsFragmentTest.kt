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
package com.vrem.wifianalyzer.wifi.accesspoint

import android.os.Build
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.model.WiFiData
import kotlinx.coroutines.flow.MutableStateFlow
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class AccessPointsFragmentTest {
    private val mainActivity = RobolectricUtil.INSTANCE.mainActivity
    private val scanner = MainContextHelper.INSTANCE.scannerService
    private val fixture = AccessPointsFragment()
    private val wiFiData = MutableStateFlow(WiFiData.EMPTY)

    @Before
    fun setUp() {
        doReturn(wiFiData).whenever(scanner).wiFiData()
    }

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun onCreateView() {
        // Act
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // Assert
        assertThat(fixture).isNotNull()
        assertThat(wiFiData.subscriptionCount.value).isEqualTo(1)
        verify(scanner).update()
    }

    @Test
    fun refreshEnabled() {
        // Arrange
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // Assert
        val swipeRefreshLayout: SwipeRefreshLayout = fixture.view!!.findViewById(R.id.accessPointsRefresh)
        assertThat(swipeRefreshLayout.isEnabled).isTrue
    }

    @Test
    fun onResume() {
        // Arrange
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // Act
        fixture.onResume()
        // Assert
        assertThat(wiFiData.subscriptionCount.value).isEqualTo(1)
        verify(scanner, times(2)).update()
    }

    @Test
    fun onDestroyView() {
        // Arrange
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // Act
        RobolectricUtil.INSTANCE.removeFragment(fixture)
        // Assert
        assertThat(wiFiData.subscriptionCount.value).isEqualTo(0)
    }

    @Config(sdk = [Build.VERSION_CODES.P])
    @Test
    fun refreshDisabled() {
        // Arrange
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // Assert
        val swipeRefreshLayout: SwipeRefreshLayout = fixture.view!!.findViewById(R.id.accessPointsRefresh)
        assertThat(swipeRefreshLayout.isRefreshing).isFalse
        assertThat(swipeRefreshLayout.isEnabled).isFalse
    }
}
