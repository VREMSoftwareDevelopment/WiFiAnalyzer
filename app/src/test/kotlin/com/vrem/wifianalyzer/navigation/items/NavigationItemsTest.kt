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
package com.vrem.wifianalyzer.navigation.items

import android.os.Build
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.about.AboutFragment
import com.vrem.wifianalyzer.settings.SettingsFragment
import com.vrem.wifianalyzer.vendor.VendorFragment
import com.vrem.wifianalyzer.wifi.accesspoint.AccessPointsFragment
import com.vrem.wifianalyzer.wifi.channelavailable.ChannelAvailableFragment
import com.vrem.wifianalyzer.wifi.channelgraph.ChannelGraphFragment
import com.vrem.wifianalyzer.wifi.channelrating.ChannelRatingFragment
import com.vrem.wifianalyzer.wifi.timegraph.TimeGraphFragment
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class NavigationItemsTest {
    @Test
    fun fragmentItem() {
        assertThat((navigationItemAccessPoints as FragmentItem).fragment is AccessPointsFragment).isTrue()
        assertThat((navigationItemChannelRating as FragmentItem).fragment is ChannelRatingFragment).isTrue()
        assertThat((navigationItemChannelGraph as FragmentItem).fragment is ChannelGraphFragment).isTrue()
        assertThat((navigationItemTimeGraph as FragmentItem).fragment is TimeGraphFragment).isTrue()
        assertThat((navigationItemChannelAvailable as FragmentItem).fragment is ChannelAvailableFragment).isTrue()
        assertThat((navigationItemVendors as FragmentItem).fragment is VendorFragment).isTrue()
        assertThat((navigationItemSettings as FragmentItem).fragment is SettingsFragment).isTrue()
        assertThat((navigationItemAbout as FragmentItem).fragment is AboutFragment).isTrue()
    }

    @Test
    fun registeredTrue() {
        assertThat(navigationItemAccessPoints.registered).isTrue()
        assertThat(navigationItemChannelRating.registered).isTrue()
        assertThat(navigationItemChannelGraph.registered).isTrue()
        assertThat(navigationItemTimeGraph.registered).isTrue()
    }

    @Test
    fun registeredFalse() {
        assertThat(navigationItemExport.registered).isFalse()
        assertThat(navigationItemChannelAvailable.registered).isFalse()
        assertThat(navigationItemVendors.registered).isFalse()
        assertThat(navigationItemSettings.registered).isFalse()
        assertThat(navigationItemAbout.registered).isFalse()
    }

    @Test
    fun visibility() {
        assertThat(navigationItemAccessPoints.visibility).isEqualTo(View.VISIBLE)
        assertThat(navigationItemChannelRating.visibility).isEqualTo(View.VISIBLE)
        assertThat(navigationItemChannelGraph.visibility).isEqualTo(View.VISIBLE)
        assertThat(navigationItemTimeGraph.visibility).isEqualTo(View.VISIBLE)
        assertThat(navigationItemChannelAvailable.visibility).isEqualTo(View.VISIBLE)
        assertThat(navigationItemVendors.visibility).isEqualTo(View.GONE)
        assertThat(navigationItemExport.visibility).isEqualTo(View.GONE)
        assertThat(navigationItemSettings.visibility).isEqualTo(View.GONE)
        assertThat(navigationItemAbout.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun exportItem() {
        assertThat(navigationItemExport is ExportItem).isTrue()
    }
}