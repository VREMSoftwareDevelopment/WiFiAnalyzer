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
package com.vrem.wifianalyzer.navigation

import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.navigation.availability.navigationOptionAp
import com.vrem.wifianalyzer.navigation.availability.navigationOptionOff
import com.vrem.wifianalyzer.navigation.availability.navigationOptionOther
import com.vrem.wifianalyzer.navigation.availability.navigationOptionRating
import com.vrem.wifianalyzer.navigation.items.*
import org.junit.Assert.*
import org.junit.Test

class NavigationMenuTest {
    @Test
    fun testNavigationMenu() {
        assertEquals(10, NavigationMenu.values().size)
    }

    @Test
    fun testNavigationItem() {
        assertEquals(navigationItemAccessPoints, NavigationMenu.ACCESS_POINTS.navigationItem)
        assertEquals(navigationItemChannelRating, NavigationMenu.CHANNEL_RATING.navigationItem)
        assertEquals(navigationItemChannelGraph, NavigationMenu.CHANNEL_GRAPH.navigationItem)
        assertEquals(navigationItemTimeGraph, NavigationMenu.TIME_GRAPH.navigationItem)
        assertEquals(navigationItemChannelAvailable, NavigationMenu.CHANNEL_AVAILABLE.navigationItem)
        assertEquals(navigationItemVendors, NavigationMenu.VENDORS.navigationItem)
        assertEquals(navigationItemExport, NavigationMenu.EXPORT.navigationItem)
        assertEquals(navigationItemSettings, NavigationMenu.SETTINGS.navigationItem)
        assertEquals(navigationItemAbout, NavigationMenu.ABOUT.navigationItem)
        assertEquals(navigationItemPortAuthority, NavigationMenu.PORT_AUTHORITY.navigationItem)
    }

    @Test
    fun testTitle() {
        assertEquals(R.string.action_access_points, NavigationMenu.ACCESS_POINTS.title)
        assertEquals(R.string.action_channel_rating, NavigationMenu.CHANNEL_RATING.title)
        assertEquals(R.string.action_channel_graph, NavigationMenu.CHANNEL_GRAPH.title)
        assertEquals(R.string.action_time_graph, NavigationMenu.TIME_GRAPH.title)
        assertEquals(R.string.action_export, NavigationMenu.EXPORT.title)
        assertEquals(R.string.action_channel_available, NavigationMenu.CHANNEL_AVAILABLE.title)
        assertEquals(R.string.action_vendors, NavigationMenu.VENDORS.title)
        assertEquals(R.string.action_settings, NavigationMenu.SETTINGS.title)
        assertEquals(R.string.action_about, NavigationMenu.ABOUT.title)
        assertEquals(R.string.action_port_authority, NavigationMenu.PORT_AUTHORITY.title)
    }

    @Test
    fun testWiFiBandSwitchableTrue() {
        assertTrue(NavigationMenu.CHANNEL_RATING.wiFiBandSwitchable())
        assertTrue(NavigationMenu.CHANNEL_GRAPH.wiFiBandSwitchable())
        assertTrue(NavigationMenu.TIME_GRAPH.wiFiBandSwitchable())
    }

    @Test
    fun testWiFiBandSwitchableFalse() {
        assertFalse(NavigationMenu.ACCESS_POINTS.wiFiBandSwitchable())
        assertFalse(NavigationMenu.CHANNEL_AVAILABLE.wiFiBandSwitchable())
        assertFalse(NavigationMenu.EXPORT.wiFiBandSwitchable())
        assertFalse(NavigationMenu.VENDORS.wiFiBandSwitchable())
        assertFalse(NavigationMenu.SETTINGS.wiFiBandSwitchable())
        assertFalse(NavigationMenu.ABOUT.wiFiBandSwitchable())
        assertFalse(NavigationMenu.PORT_AUTHORITY.wiFiBandSwitchable())
    }

    @Test
    fun testRegisteredTrue() {
        assertTrue(NavigationMenu.ACCESS_POINTS.registered())
        assertTrue(NavigationMenu.CHANNEL_RATING.registered())
        assertTrue(NavigationMenu.CHANNEL_GRAPH.registered())
        assertTrue(NavigationMenu.TIME_GRAPH.registered())
    }

    @Test
    fun testRegisteredFalse() {
        assertFalse(NavigationMenu.CHANNEL_AVAILABLE.registered())
        assertFalse(NavigationMenu.EXPORT.registered())
        assertFalse(NavigationMenu.VENDORS.registered())
        assertFalse(NavigationMenu.SETTINGS.registered())
        assertFalse(NavigationMenu.ABOUT.registered())
        assertFalse(NavigationMenu.PORT_AUTHORITY.registered())
    }

    @Test
    fun testIcon() {
        assertEquals(R.drawable.ic_network_wifi, NavigationMenu.ACCESS_POINTS.icon)
        assertEquals(R.drawable.ic_wifi_tethering, NavigationMenu.CHANNEL_RATING.icon)
        assertEquals(R.drawable.ic_insert_chart, NavigationMenu.CHANNEL_GRAPH.icon)
        assertEquals(R.drawable.ic_show_chart, NavigationMenu.TIME_GRAPH.icon)
        assertEquals(R.drawable.ic_import_export, NavigationMenu.EXPORT.icon)
        assertEquals(R.drawable.ic_location_on, NavigationMenu.CHANNEL_AVAILABLE.icon)
        assertEquals(R.drawable.ic_list, NavigationMenu.VENDORS.icon)
        assertEquals(R.drawable.ic_settings, NavigationMenu.SETTINGS.icon)
        assertEquals(R.drawable.ic_info_outline, NavigationMenu.ABOUT.icon)
        assertEquals(R.drawable.ic_lan, NavigationMenu.PORT_AUTHORITY.icon)
    }

    @Test
    fun testNavigationOptions() {
        assertEquals(navigationOptionAp, NavigationMenu.ACCESS_POINTS.navigationOptions)
        assertEquals(navigationOptionRating, NavigationMenu.CHANNEL_RATING.navigationOptions)
        assertEquals(navigationOptionOther, NavigationMenu.CHANNEL_GRAPH.navigationOptions)
        assertEquals(navigationOptionOther, NavigationMenu.TIME_GRAPH.navigationOptions)
        assertEquals(navigationOptionOff, NavigationMenu.CHANNEL_AVAILABLE.navigationOptions)
        assertEquals(navigationOptionOff, NavigationMenu.VENDORS.navigationOptions)
        assertEquals(navigationOptionOff, NavigationMenu.EXPORT.navigationOptions)
        assertEquals(navigationOptionOff, NavigationMenu.SETTINGS.navigationOptions)
        assertEquals(navigationOptionOff, NavigationMenu.ABOUT.navigationOptions)
        assertEquals(navigationOptionOff, NavigationMenu.PORT_AUTHORITY.navigationOptions)
    }
}