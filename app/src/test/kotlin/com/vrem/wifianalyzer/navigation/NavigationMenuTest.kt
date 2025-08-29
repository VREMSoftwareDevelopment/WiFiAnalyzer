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
package com.vrem.wifianalyzer.navigation

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.navigation.availability.navigationOptionAp
import com.vrem.wifianalyzer.navigation.availability.navigationOptionOff
import com.vrem.wifianalyzer.navigation.availability.navigationOptionOther
import com.vrem.wifianalyzer.navigation.availability.navigationOptionRating
import com.vrem.wifianalyzer.navigation.items.navigationItemAbout
import com.vrem.wifianalyzer.navigation.items.navigationItemAccessPoints
import com.vrem.wifianalyzer.navigation.items.navigationItemChannelAvailable
import com.vrem.wifianalyzer.navigation.items.navigationItemChannelGraph
import com.vrem.wifianalyzer.navigation.items.navigationItemChannelRating
import com.vrem.wifianalyzer.navigation.items.navigationItemExport
import com.vrem.wifianalyzer.navigation.items.navigationItemSettings
import com.vrem.wifianalyzer.navigation.items.navigationItemTimeGraph
import com.vrem.wifianalyzer.navigation.items.navigationItemVendors
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class NavigationMenuTest {
    @Test
    fun groupFeature() {
        assertThat(MAIN_NAVIGATION).containsExactly(
            NavigationMenu.ACCESS_POINTS,
            NavigationMenu.CHANNEL_RATING,
            NavigationMenu.CHANNEL_GRAPH,
            NavigationMenu.TIME_GRAPH,
        )
    }

    @Test
    fun navigationMenu() {
        assertThat(NavigationMenu.entries).hasSize(9)
    }

    @Test
    fun navigationItem() {
        assertThat(NavigationMenu.ACCESS_POINTS.navigationItem).isEqualTo(navigationItemAccessPoints)
        assertThat(NavigationMenu.CHANNEL_RATING.navigationItem).isEqualTo(navigationItemChannelRating)
        assertThat(NavigationMenu.CHANNEL_GRAPH.navigationItem).isEqualTo(navigationItemChannelGraph)
        assertThat(NavigationMenu.TIME_GRAPH.navigationItem).isEqualTo(navigationItemTimeGraph)
        assertThat(NavigationMenu.CHANNEL_AVAILABLE.navigationItem).isEqualTo(navigationItemChannelAvailable)
        assertThat(NavigationMenu.VENDORS.navigationItem).isEqualTo(navigationItemVendors)
        assertThat(NavigationMenu.EXPORT.navigationItem).isEqualTo(navigationItemExport)
        assertThat(NavigationMenu.SETTINGS.navigationItem).isEqualTo(navigationItemSettings)
        assertThat(NavigationMenu.ABOUT.navigationItem).isEqualTo(navigationItemAbout)
    }

    @Test
    fun title() {
        assertThat(NavigationMenu.ACCESS_POINTS.title).isEqualTo(R.string.action_access_points)
        assertThat(NavigationMenu.CHANNEL_RATING.title).isEqualTo(R.string.action_channel_rating)
        assertThat(NavigationMenu.CHANNEL_GRAPH.title).isEqualTo(R.string.action_channel_graph)
        assertThat(NavigationMenu.TIME_GRAPH.title).isEqualTo(R.string.action_time_graph)
        assertThat(NavigationMenu.EXPORT.title).isEqualTo(R.string.action_export)
        assertThat(NavigationMenu.CHANNEL_AVAILABLE.title).isEqualTo(R.string.action_channel_available)
        assertThat(NavigationMenu.VENDORS.title).isEqualTo(R.string.action_vendors)
        assertThat(NavigationMenu.SETTINGS.title).isEqualTo(R.string.action_settings)
        assertThat(NavigationMenu.ABOUT.title).isEqualTo(R.string.action_about)
    }

    @Test
    fun wiFiBandSwitchableTrue() {
        assertThat(NavigationMenu.CHANNEL_RATING.wiFiBandSwitchable()).isTrue
        assertThat(NavigationMenu.CHANNEL_GRAPH.wiFiBandSwitchable()).isTrue
        assertThat(NavigationMenu.TIME_GRAPH.wiFiBandSwitchable()).isTrue
    }

    @Test
    fun wiFiBandSwitchableFalse() {
        assertThat(NavigationMenu.ACCESS_POINTS.wiFiBandSwitchable()).isFalse
        assertThat(NavigationMenu.CHANNEL_AVAILABLE.wiFiBandSwitchable()).isFalse
        assertThat(NavigationMenu.EXPORT.wiFiBandSwitchable()).isFalse
        assertThat(NavigationMenu.VENDORS.wiFiBandSwitchable()).isFalse
        assertThat(NavigationMenu.SETTINGS.wiFiBandSwitchable()).isFalse
        assertThat(NavigationMenu.ABOUT.wiFiBandSwitchable()).isFalse
    }

    @Test
    fun registeredTrue() {
        assertThat(NavigationMenu.ACCESS_POINTS.registered()).isTrue
        assertThat(NavigationMenu.CHANNEL_RATING.registered()).isTrue
        assertThat(NavigationMenu.CHANNEL_GRAPH.registered()).isTrue
        assertThat(NavigationMenu.TIME_GRAPH.registered()).isTrue
    }

    @Test
    fun registeredFalse() {
        assertThat(NavigationMenu.CHANNEL_AVAILABLE.registered()).isFalse
        assertThat(NavigationMenu.EXPORT.registered()).isFalse
        assertThat(NavigationMenu.VENDORS.registered()).isFalse
        assertThat(NavigationMenu.SETTINGS.registered()).isFalse
        assertThat(NavigationMenu.ABOUT.registered()).isFalse
    }

    @Test
    fun idDrawer() {
        assertThat(NavigationMenu.ACCESS_POINTS.idDrawer).isEqualTo(R.id.nav_drawer_access_points)
        assertThat(NavigationMenu.CHANNEL_RATING.idDrawer).isEqualTo(R.id.nav_drawer_channel_rating)
        assertThat(NavigationMenu.CHANNEL_GRAPH.idDrawer).isEqualTo(R.id.nav_drawer_channel_graph)
        assertThat(NavigationMenu.TIME_GRAPH.idDrawer).isEqualTo(R.id.nav_drawer_time_graph)
        assertThat(NavigationMenu.EXPORT.idDrawer).isEqualTo(R.id.nav_drawer_export)
        assertThat(NavigationMenu.CHANNEL_AVAILABLE.idDrawer).isEqualTo(R.id.nav_drawer_channel_available)
        assertThat(NavigationMenu.VENDORS.idDrawer).isEqualTo(R.id.nav_drawer_vendors)
        assertThat(NavigationMenu.SETTINGS.idDrawer).isEqualTo(R.id.nav_drawer_settings)
        assertThat(NavigationMenu.ABOUT.idDrawer).isEqualTo(R.id.nav_drawer_about)
    }

    @Test
    fun idBottom() {
        assertThat(NavigationMenu.ACCESS_POINTS.idBottom).isEqualTo(R.id.nav_bottom_access_points)
        assertThat(NavigationMenu.CHANNEL_RATING.idBottom).isEqualTo(R.id.nav_bottom_channel_rating)
        assertThat(NavigationMenu.CHANNEL_GRAPH.idBottom).isEqualTo(R.id.nav_bottom_channel_graph)
        assertThat(NavigationMenu.TIME_GRAPH.idBottom).isEqualTo(R.id.nav_bottom_time_graph)
        assertThat(NavigationMenu.EXPORT.idBottom).isEqualTo(-1)
        assertThat(NavigationMenu.CHANNEL_AVAILABLE.idBottom).isEqualTo(-1)
        assertThat(NavigationMenu.VENDORS.idBottom).isEqualTo(-1)
        assertThat(NavigationMenu.SETTINGS.idBottom).isEqualTo(-1)
        assertThat(NavigationMenu.ABOUT.idBottom).isEqualTo(-1)
    }

    @Test
    fun navigationOptions() {
        assertThat(NavigationMenu.ACCESS_POINTS.navigationOptions).isEqualTo(navigationOptionAp)
        assertThat(NavigationMenu.CHANNEL_RATING.navigationOptions).isEqualTo(navigationOptionRating)
        assertThat(NavigationMenu.CHANNEL_GRAPH.navigationOptions).isEqualTo(navigationOptionOther)
        assertThat(NavigationMenu.TIME_GRAPH.navigationOptions).isEqualTo(navigationOptionOther)
        assertThat(NavigationMenu.CHANNEL_AVAILABLE.navigationOptions).isEqualTo(navigationOptionOff)
        assertThat(NavigationMenu.VENDORS.navigationOptions).isEqualTo(navigationOptionOff)
        assertThat(NavigationMenu.EXPORT.navigationOptions).isEqualTo(navigationOptionOff)
        assertThat(NavigationMenu.SETTINGS.navigationOptions).isEqualTo(navigationOptionOff)
        assertThat(NavigationMenu.ABOUT.navigationOptions).isEqualTo(navigationOptionOff)
    }

    @Test
    fun findByDrawerId() {
        assertThat(NavigationMenu.find(R.id.nav_drawer_access_points)).isEqualTo(NavigationMenu.ACCESS_POINTS)
        assertThat(NavigationMenu.find(R.id.nav_drawer_channel_rating)).isEqualTo(NavigationMenu.CHANNEL_RATING)
        assertThat(NavigationMenu.find(R.id.nav_drawer_channel_graph)).isEqualTo(NavigationMenu.CHANNEL_GRAPH)
        assertThat(NavigationMenu.find(R.id.nav_drawer_time_graph)).isEqualTo(NavigationMenu.TIME_GRAPH)
        assertThat(NavigationMenu.find(R.id.nav_drawer_export)).isEqualTo(NavigationMenu.EXPORT)
        assertThat(NavigationMenu.find(R.id.nav_drawer_channel_available)).isEqualTo(NavigationMenu.CHANNEL_AVAILABLE)
        assertThat(NavigationMenu.find(R.id.nav_drawer_vendors)).isEqualTo(NavigationMenu.VENDORS)
        assertThat(NavigationMenu.find(R.id.nav_drawer_settings)).isEqualTo(NavigationMenu.SETTINGS)
        assertThat(NavigationMenu.find(R.id.nav_drawer_about)).isEqualTo(NavigationMenu.ABOUT)
    }

    @Test
    fun findByBottomId() {
        assertThat(NavigationMenu.find(R.id.nav_bottom_access_points)).isEqualTo(NavigationMenu.ACCESS_POINTS)
        assertThat(NavigationMenu.find(R.id.nav_bottom_channel_rating)).isEqualTo(NavigationMenu.CHANNEL_RATING)
        assertThat(NavigationMenu.find(R.id.nav_bottom_channel_graph)).isEqualTo(NavigationMenu.CHANNEL_GRAPH)
        assertThat(NavigationMenu.find(R.id.nav_bottom_time_graph)).isEqualTo(NavigationMenu.TIME_GRAPH)
    }

    @Test
    fun findDefault() {
        assertThat(NavigationMenu.find(0)).isEqualTo(NavigationMenu.ACCESS_POINTS)
    }
}
