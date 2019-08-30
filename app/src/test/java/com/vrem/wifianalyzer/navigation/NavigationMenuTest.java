/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.navigation;

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.navigation.availability.NavigationOptionFactory;
import com.vrem.wifianalyzer.navigation.items.NavigationItemFactory;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NavigationMenuTest {

    @Test
    public void testNavigationMenu() {
        assertEquals(10, NavigationMenu.values().length);
    }

    @Test
    public void testGetNavigationItem() {
        assertEquals(NavigationItemFactory.ACCESS_POINTS, NavigationMenu.ACCESS_POINTS.getNavigationItem());
        assertEquals(NavigationItemFactory.CHANNEL_RATING, NavigationMenu.CHANNEL_RATING.getNavigationItem());
        assertEquals(NavigationItemFactory.CHANNEL_GRAPH, NavigationMenu.CHANNEL_GRAPH.getNavigationItem());
        assertEquals(NavigationItemFactory.TIME_GRAPH, NavigationMenu.TIME_GRAPH.getNavigationItem());
        assertEquals(NavigationItemFactory.CHANNEL_AVAILABLE, NavigationMenu.CHANNEL_AVAILABLE.getNavigationItem());
        assertEquals(NavigationItemFactory.VENDORS, NavigationMenu.VENDORS.getNavigationItem());
        assertEquals(NavigationItemFactory.EXPORT, NavigationMenu.EXPORT.getNavigationItem());
        assertEquals(NavigationItemFactory.SETTINGS, NavigationMenu.SETTINGS.getNavigationItem());
        assertEquals(NavigationItemFactory.ABOUT, NavigationMenu.ABOUT.getNavigationItem());
        assertEquals(NavigationItemFactory.PORT_AUTHORITY, NavigationMenu.PORT_AUTHORITY.getNavigationItem());
    }

    @Test
    public void testGetTitle() {
        assertEquals(R.string.action_access_points, NavigationMenu.ACCESS_POINTS.getTitle());
        assertEquals(R.string.action_channel_rating, NavigationMenu.CHANNEL_RATING.getTitle());
        assertEquals(R.string.action_channel_graph, NavigationMenu.CHANNEL_GRAPH.getTitle());
        assertEquals(R.string.action_time_graph, NavigationMenu.TIME_GRAPH.getTitle());
        assertEquals(R.string.action_export, NavigationMenu.EXPORT.getTitle());
        assertEquals(R.string.action_channel_available, NavigationMenu.CHANNEL_AVAILABLE.getTitle());
        assertEquals(R.string.action_vendors, NavigationMenu.VENDORS.getTitle());
        assertEquals(R.string.action_settings, NavigationMenu.SETTINGS.getTitle());
        assertEquals(R.string.action_about, NavigationMenu.ABOUT.getTitle());
        assertEquals(R.string.action_port_authority, NavigationMenu.PORT_AUTHORITY.getTitle());
    }

    @Test
    public void testIsWiFiBandSwitchableTrue() {
        assertTrue(NavigationMenu.CHANNEL_RATING.isWiFiBandSwitchable());
        assertTrue(NavigationMenu.CHANNEL_GRAPH.isWiFiBandSwitchable());
        assertTrue(NavigationMenu.TIME_GRAPH.isWiFiBandSwitchable());
    }

    @Test
    public void testIsWiFiBandSwitchableFalse() {
        assertFalse(NavigationMenu.ACCESS_POINTS.isWiFiBandSwitchable());
        assertFalse(NavigationMenu.CHANNEL_AVAILABLE.isWiFiBandSwitchable());
        assertFalse(NavigationMenu.EXPORT.isWiFiBandSwitchable());
        assertFalse(NavigationMenu.VENDORS.isWiFiBandSwitchable());
        assertFalse(NavigationMenu.SETTINGS.isWiFiBandSwitchable());
        assertFalse(NavigationMenu.ABOUT.isWiFiBandSwitchable());
        assertFalse(NavigationMenu.PORT_AUTHORITY.isWiFiBandSwitchable());
    }

    @Test
    public void testIsRegisteredTrue() {
        assertTrue(NavigationMenu.ACCESS_POINTS.isRegistered());
        assertTrue(NavigationMenu.CHANNEL_RATING.isRegistered());
        assertTrue(NavigationMenu.CHANNEL_GRAPH.isRegistered());
        assertTrue(NavigationMenu.TIME_GRAPH.isRegistered());
    }

    @Test
    public void testIsRegisteredFalse() {
        assertFalse(NavigationMenu.CHANNEL_AVAILABLE.isRegistered());
        assertFalse(NavigationMenu.EXPORT.isRegistered());
        assertFalse(NavigationMenu.VENDORS.isRegistered());
        assertFalse(NavigationMenu.SETTINGS.isRegistered());
        assertFalse(NavigationMenu.ABOUT.isRegistered());
        assertFalse(NavigationMenu.PORT_AUTHORITY.isRegistered());
    }

    @Test
    public void testGetIcon() {
        assertEquals(R.drawable.ic_network_wifi, NavigationMenu.ACCESS_POINTS.getIcon());
        assertEquals(R.drawable.ic_wifi_tethering, NavigationMenu.CHANNEL_RATING.getIcon());
        assertEquals(R.drawable.ic_insert_chart, NavigationMenu.CHANNEL_GRAPH.getIcon());
        assertEquals(R.drawable.ic_show_chart, NavigationMenu.TIME_GRAPH.getIcon());
        assertEquals(R.drawable.ic_import_export, NavigationMenu.EXPORT.getIcon());
        assertEquals(R.drawable.ic_location_on, NavigationMenu.CHANNEL_AVAILABLE.getIcon());
        assertEquals(R.drawable.ic_list_grey, NavigationMenu.VENDORS.getIcon());
        assertEquals(R.drawable.ic_settings, NavigationMenu.SETTINGS.getIcon());
        assertEquals(R.drawable.ic_info_outline, NavigationMenu.ABOUT.getIcon());
        assertEquals(R.drawable.ic_lan, NavigationMenu.PORT_AUTHORITY.getIcon());
    }

    @Test
    public void testGetNavigationOptions() {
        assertEquals(NavigationOptionFactory.AP, NavigationMenu.ACCESS_POINTS.getNavigationOptions());
        assertEquals(NavigationOptionFactory.RATING, NavigationMenu.CHANNEL_RATING.getNavigationOptions());
        assertEquals(NavigationOptionFactory.OTHER, NavigationMenu.CHANNEL_GRAPH.getNavigationOptions());
        assertEquals(NavigationOptionFactory.OTHER, NavigationMenu.TIME_GRAPH.getNavigationOptions());

        assertEquals(NavigationOptionFactory.OFF, NavigationMenu.CHANNEL_AVAILABLE.getNavigationOptions());
        assertEquals(NavigationOptionFactory.OFF, NavigationMenu.VENDORS.getNavigationOptions());
        assertEquals(NavigationOptionFactory.OFF, NavigationMenu.EXPORT.getNavigationOptions());
        assertEquals(NavigationOptionFactory.OFF, NavigationMenu.SETTINGS.getNavigationOptions());
        assertEquals(NavigationOptionFactory.OFF, NavigationMenu.ABOUT.getNavigationOptions());
        assertEquals(NavigationOptionFactory.OFF, NavigationMenu.PORT_AUTHORITY.getNavigationOptions());
    }
}