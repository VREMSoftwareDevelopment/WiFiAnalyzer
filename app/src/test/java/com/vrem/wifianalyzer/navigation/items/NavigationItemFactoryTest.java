/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.navigation.items;

import com.vrem.wifianalyzer.about.AboutActivity;
import com.vrem.wifianalyzer.settings.SettingActivity;
import com.vrem.wifianalyzer.vendor.VendorFragment;
import com.vrem.wifianalyzer.wifi.accesspoint.AccessPointsFragment;
import com.vrem.wifianalyzer.wifi.channelavailable.ChannelAvailableFragment;
import com.vrem.wifianalyzer.wifi.channelgraph.ChannelGraphFragment;
import com.vrem.wifianalyzer.wifi.channelrating.ChannelRatingFragment;
import com.vrem.wifianalyzer.wifi.timegraph.TimeGraphFragment;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NavigationItemFactoryTest {

    @Test
    public void testFragmentItem() throws Exception {
        assertTrue(((FragmentItem) NavigationItemFactory.ACCESS_POINTS).getFragment() instanceof AccessPointsFragment);
        assertTrue(((FragmentItem) NavigationItemFactory.CHANNEL_RATING).getFragment() instanceof ChannelRatingFragment);
        assertTrue(((FragmentItem) NavigationItemFactory.CHANNEL_GRAPH).getFragment() instanceof ChannelGraphFragment);
        assertTrue(((FragmentItem) NavigationItemFactory.TIME_GRAPH).getFragment() instanceof TimeGraphFragment);
        assertTrue(((FragmentItem) NavigationItemFactory.CHANNEL_AVAILABLE).getFragment() instanceof ChannelAvailableFragment);
        assertTrue(((FragmentItem) NavigationItemFactory.VENDORS).getFragment() instanceof VendorFragment);
    }

    @Test
    public void testIsRegisteredTrue() throws Exception {
        assertTrue(NavigationItemFactory.ACCESS_POINTS.isRegistered());
        assertTrue(NavigationItemFactory.CHANNEL_RATING.isRegistered());
        assertTrue(NavigationItemFactory.CHANNEL_GRAPH.isRegistered());
        assertTrue(NavigationItemFactory.TIME_GRAPH.isRegistered());
    }

    @Test
    public void testIsRegisteredFalse() throws Exception {
        assertFalse(NavigationItemFactory.EXPORT.isRegistered());
        assertFalse(NavigationItemFactory.CHANNEL_AVAILABLE.isRegistered());
        assertFalse(NavigationItemFactory.VENDORS.isRegistered());
        assertFalse(NavigationItemFactory.SETTINGS.isRegistered());
        assertFalse(NavigationItemFactory.ABOUT.isRegistered());
    }

    @Test
    public void testActivityItem() throws Exception {
        assertEquals(SettingActivity.class, ((ActivityItem) NavigationItemFactory.SETTINGS).getActivity());
        assertEquals(AboutActivity.class, ((ActivityItem) NavigationItemFactory.ABOUT).getActivity());
    }

    @Test
    public void testExportItem() throws Exception {
        assertTrue(NavigationItemFactory.EXPORT instanceof ExportItem);
    }

}