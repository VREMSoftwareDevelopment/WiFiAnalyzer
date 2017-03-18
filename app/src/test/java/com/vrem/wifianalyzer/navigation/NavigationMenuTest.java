/*
 * WiFiAnalyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.vrem.wifianalyzer.navigation.items.NavigationItemFactory;
import com.vrem.wifianalyzer.navigation.options.NavigationOptionFactory;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NavigationMenuTest {

    @Test
    public void testNavigationMenu() throws Exception {
        assertEquals(9, NavigationMenu.values().length);
    }

    @Test
    public void testGetNavigationItem() throws Exception {
        assertEquals(NavigationItemFactory.ACCESS_POINTS, NavigationMenu.ACCESS_POINTS.getNavigationItem());
        assertEquals(NavigationItemFactory.CHANNEL_RATING, NavigationMenu.CHANNEL_RATING.getNavigationItem());
        assertEquals(NavigationItemFactory.CHANNEL_GRAPH, NavigationMenu.CHANNEL_GRAPH.getNavigationItem());
        assertEquals(NavigationItemFactory.TIME_GRAPH, NavigationMenu.TIME_GRAPH.getNavigationItem());
        assertEquals(NavigationItemFactory.CHANNEL_AVAILABLE, NavigationMenu.CHANNEL_AVAILABLE.getNavigationItem());
        assertEquals(NavigationItemFactory.VENDOR_LIST, NavigationMenu.VENDOR_LIST.getNavigationItem());
        assertEquals(NavigationItemFactory.EXPORT, NavigationMenu.EXPORT.getNavigationItem());
        assertEquals(NavigationItemFactory.SETTINGS, NavigationMenu.SETTINGS.getNavigationItem());
        assertEquals(NavigationItemFactory.ABOUT, NavigationMenu.ABOUT.getNavigationItem());
    }

    @Test
    public void testGetTitle() throws Exception {
        assertEquals(R.string.action_access_points, NavigationMenu.ACCESS_POINTS.getTitle());
        assertEquals(R.string.action_channel_rating, NavigationMenu.CHANNEL_RATING.getTitle());
        assertEquals(R.string.action_channel_graph, NavigationMenu.CHANNEL_GRAPH.getTitle());
        assertEquals(R.string.action_time_graph, NavigationMenu.TIME_GRAPH.getTitle());
        assertEquals(R.string.action_export, NavigationMenu.EXPORT.getTitle());
        assertEquals(R.string.action_channel_available, NavigationMenu.CHANNEL_AVAILABLE.getTitle());
        assertEquals(R.string.action_vendors, NavigationMenu.VENDOR_LIST.getTitle());
        assertEquals(R.string.action_settings, NavigationMenu.SETTINGS.getTitle());
        assertEquals(R.string.action_about, NavigationMenu.ABOUT.getTitle());
    }

    @Test
    public void testIsWiFiBandSwitchableTrue() throws Exception {
        assertTrue(NavigationMenu.CHANNEL_RATING.isWiFiBandSwitchable());
        assertTrue(NavigationMenu.CHANNEL_GRAPH.isWiFiBandSwitchable());
        assertTrue(NavigationMenu.TIME_GRAPH.isWiFiBandSwitchable());
    }

    @Test
    public void testIsWiFiBandSwitchableFalse() throws Exception {
        assertFalse(NavigationMenu.ACCESS_POINTS.isWiFiBandSwitchable());
        assertFalse(NavigationMenu.CHANNEL_AVAILABLE.isWiFiBandSwitchable());
        assertFalse(NavigationMenu.EXPORT.isWiFiBandSwitchable());
        assertFalse(NavigationMenu.VENDOR_LIST.isWiFiBandSwitchable());
        assertFalse(NavigationMenu.SETTINGS.isWiFiBandSwitchable());
        assertFalse(NavigationMenu.ABOUT.isWiFiBandSwitchable());
    }

    @Test
    public void testIsRegisteredTrue() throws Exception {
        assertTrue(NavigationMenu.ACCESS_POINTS.isRegistered());
        assertTrue(NavigationMenu.CHANNEL_RATING.isRegistered());
        assertTrue(NavigationMenu.CHANNEL_GRAPH.isRegistered());
        assertTrue(NavigationMenu.TIME_GRAPH.isRegistered());
    }

    @Test
    public void testIsRegisteredFalse() throws Exception {
        assertFalse(NavigationMenu.CHANNEL_AVAILABLE.isRegistered());
        assertFalse(NavigationMenu.EXPORT.isRegistered());
        assertFalse(NavigationMenu.VENDOR_LIST.isRegistered());
        assertFalse(NavigationMenu.SETTINGS.isRegistered());
        assertFalse(NavigationMenu.ABOUT.isRegistered());
    }

    @Test
    public void testGetIcon() throws Exception {
        assertEquals(R.drawable.ic_network_wifi_grey_500_48dp, NavigationMenu.ACCESS_POINTS.getIcon());
        assertEquals(R.drawable.ic_wifi_tethering_grey_500_48dp, NavigationMenu.CHANNEL_RATING.getIcon());
        assertEquals(R.drawable.ic_insert_chart_grey_500_48dp, NavigationMenu.CHANNEL_GRAPH.getIcon());
        assertEquals(R.drawable.ic_show_chart_grey_500_48dp, NavigationMenu.TIME_GRAPH.getIcon());
        assertEquals(R.drawable.ic_import_export_grey_500_48dp, NavigationMenu.EXPORT.getIcon());
        assertEquals(R.drawable.ic_location_on_grey_500_48dp, NavigationMenu.CHANNEL_AVAILABLE.getIcon());
        assertEquals(R.drawable.ic_list_grey_500_48dp, NavigationMenu.VENDOR_LIST.getIcon());
        assertEquals(R.drawable.ic_settings_grey_500_48dp, NavigationMenu.SETTINGS.getIcon());
        assertEquals(R.drawable.ic_info_outline_grey_500_48dp, NavigationMenu.ABOUT.getIcon());
    }

    @Test
    public void testGetNavigationOptions() throws Exception {
        assertEquals(NavigationOptionFactory.AP, NavigationMenu.ACCESS_POINTS.getNavigationOptions());
        assertEquals(NavigationOptionFactory.RATING, NavigationMenu.CHANNEL_RATING.getNavigationOptions());
        assertEquals(NavigationOptionFactory.OTHER, NavigationMenu.CHANNEL_GRAPH.getNavigationOptions());
        assertEquals(NavigationOptionFactory.OTHER, NavigationMenu.TIME_GRAPH.getNavigationOptions());

        assertEquals(NavigationOptionFactory.OFF, NavigationMenu.CHANNEL_AVAILABLE.getNavigationOptions());
        assertEquals(NavigationOptionFactory.OFF, NavigationMenu.VENDOR_LIST.getNavigationOptions());
        assertEquals(NavigationOptionFactory.OFF, NavigationMenu.EXPORT.getNavigationOptions());
        assertEquals(NavigationOptionFactory.OFF, NavigationMenu.SETTINGS.getNavigationOptions());
        assertEquals(NavigationOptionFactory.OFF, NavigationMenu.ABOUT.getNavigationOptions());
    }
}