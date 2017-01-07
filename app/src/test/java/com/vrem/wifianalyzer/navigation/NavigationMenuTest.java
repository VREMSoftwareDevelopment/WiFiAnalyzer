/*
 * WiFi Analyzer
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
import com.vrem.wifianalyzer.about.AboutActivity;
import com.vrem.wifianalyzer.settings.SettingActivity;
import com.vrem.wifianalyzer.vendor.VendorFragment;
import com.vrem.wifianalyzer.wifi.AccessPointsFragment;
import com.vrem.wifianalyzer.wifi.ChannelAvailableFragment;
import com.vrem.wifianalyzer.wifi.ChannelRatingFragment;
import com.vrem.wifianalyzer.wifi.graph.channel.ChannelGraphFragment;
import com.vrem.wifianalyzer.wifi.graph.time.TimeGraphFragment;

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
    public void testFind() throws Exception {
        assertEquals(NavigationMenu.ACCESS_POINTS, NavigationMenu.find(-1));
        assertEquals(NavigationMenu.ACCESS_POINTS, NavigationMenu.find(NavigationMenu.values().length));

        assertEquals(NavigationMenu.ACCESS_POINTS, NavigationMenu.find(NavigationMenu.ACCESS_POINTS.ordinal()));
        assertEquals(NavigationMenu.CHANNEL_RATING, NavigationMenu.find(NavigationMenu.CHANNEL_RATING.ordinal()));
        assertEquals(NavigationMenu.CHANNEL_GRAPH, NavigationMenu.find(NavigationMenu.CHANNEL_GRAPH.ordinal()));
        assertEquals(NavigationMenu.TIME_GRAPH, NavigationMenu.find(NavigationMenu.TIME_GRAPH.ordinal()));
        assertEquals(NavigationMenu.EXPORT, NavigationMenu.find(NavigationMenu.EXPORT.ordinal()));
        assertEquals(NavigationMenu.CHANNEL_AVAILABLE, NavigationMenu.find(NavigationMenu.CHANNEL_AVAILABLE.ordinal()));
        assertEquals(NavigationMenu.VENDOR_LIST, NavigationMenu.find(NavigationMenu.VENDOR_LIST.ordinal()));
        assertEquals(NavigationMenu.SETTINGS, NavigationMenu.find(NavigationMenu.SETTINGS.ordinal()));
        assertEquals(NavigationMenu.ABOUT, NavigationMenu.find(NavigationMenu.ABOUT.ordinal()));
    }

    @Test
    public void testGetItem() throws Exception {
        assertTrue(NavigationMenu.ACCESS_POINTS.getItem() instanceof FragmentItem);
        assertTrue(NavigationMenu.CHANNEL_RATING.getItem() instanceof FragmentItem);
        assertTrue(NavigationMenu.CHANNEL_GRAPH.getItem() instanceof FragmentItem);
        assertTrue(NavigationMenu.TIME_GRAPH.getItem() instanceof FragmentItem);
        assertTrue(NavigationMenu.CHANNEL_AVAILABLE.getItem() instanceof FragmentItem);
        assertTrue(NavigationMenu.VENDOR_LIST.getItem() instanceof FragmentItem);

        assertTrue(NavigationMenu.EXPORT.getItem() instanceof ExportItem);

        assertTrue(NavigationMenu.SETTINGS.getItem() instanceof ActivityItem);
        assertTrue(NavigationMenu.ABOUT.getItem() instanceof ActivityItem);
    }

    @Test
    public void testFragmentItemContainsCorrectFragment() throws Exception {
        assertTrue(((FragmentItem) NavigationMenu.ACCESS_POINTS.getItem()).getFragment() instanceof AccessPointsFragment);
        assertTrue(((FragmentItem) NavigationMenu.CHANNEL_RATING.getItem()).getFragment() instanceof ChannelRatingFragment);
        assertTrue(((FragmentItem) NavigationMenu.CHANNEL_GRAPH.getItem()).getFragment() instanceof ChannelGraphFragment);
        assertTrue(((FragmentItem) NavigationMenu.TIME_GRAPH.getItem()).getFragment() instanceof TimeGraphFragment);
        assertTrue(((FragmentItem) NavigationMenu.CHANNEL_AVAILABLE.getItem()).getFragment() instanceof ChannelAvailableFragment);
        assertTrue(((FragmentItem) NavigationMenu.VENDOR_LIST.getItem()).getFragment() instanceof VendorFragment);
    }

    @Test
    public void testActivityItemContainsCorrectActivity() throws Exception {
        assertEquals(SettingActivity.class, ((ActivityItem) NavigationMenu.SETTINGS.getItem()).getActivity());
        assertEquals(AboutActivity.class, ((ActivityItem) NavigationMenu.ABOUT.getItem()).getActivity());
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
    public void testIsOptionMenuAvailable() throws Exception {
        assertTrue(NavigationMenu.ACCESS_POINTS.isOptionMenu());
        assertTrue(NavigationMenu.CHANNEL_RATING.isOptionMenu());
        assertTrue(NavigationMenu.CHANNEL_GRAPH.isOptionMenu());
        assertTrue(NavigationMenu.TIME_GRAPH.isOptionMenu());
    }

    @Test
    public void testIsOptionMenuNotAvailable() throws Exception {
        assertFalse(NavigationMenu.CHANNEL_AVAILABLE.isOptionMenu());
        assertFalse(NavigationMenu.EXPORT.isOptionMenu());
        assertFalse(NavigationMenu.VENDOR_LIST.isOptionMenu());
        assertFalse(NavigationMenu.SETTINGS.isOptionMenu());
        assertFalse(NavigationMenu.ABOUT.isOptionMenu());
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
}