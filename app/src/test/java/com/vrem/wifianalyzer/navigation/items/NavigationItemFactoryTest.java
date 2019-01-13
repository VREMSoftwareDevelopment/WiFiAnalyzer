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

package com.vrem.wifianalyzer.navigation.items;

import android.view.View;

import com.vrem.wifianalyzer.about.AboutFragment;
import com.vrem.wifianalyzer.settings.SettingsFragment;
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
    public void testFragmentItem() {
        assertTrue(((FragmentItem) NavigationItemFactory.ACCESS_POINTS).getFragment() instanceof AccessPointsFragment);
        assertTrue(((FragmentItem) NavigationItemFactory.CHANNEL_RATING).getFragment() instanceof ChannelRatingFragment);
        assertTrue(((FragmentItem) NavigationItemFactory.CHANNEL_GRAPH).getFragment() instanceof ChannelGraphFragment);
        assertTrue(((FragmentItem) NavigationItemFactory.TIME_GRAPH).getFragment() instanceof TimeGraphFragment);
        assertTrue(((FragmentItem) NavigationItemFactory.CHANNEL_AVAILABLE).getFragment() instanceof ChannelAvailableFragment);
        assertTrue(((FragmentItem) NavigationItemFactory.VENDORS).getFragment() instanceof VendorFragment);
        assertTrue(((FragmentItem) NavigationItemFactory.SETTINGS).getFragment() instanceof SettingsFragment);
        assertTrue(((FragmentItem) NavigationItemFactory.ABOUT).getFragment() instanceof AboutFragment);
    }

    @Test
    public void testIsRegisteredTrue() {
        assertTrue(NavigationItemFactory.ACCESS_POINTS.isRegistered());
        assertTrue(NavigationItemFactory.CHANNEL_RATING.isRegistered());
        assertTrue(NavigationItemFactory.CHANNEL_GRAPH.isRegistered());
        assertTrue(NavigationItemFactory.TIME_GRAPH.isRegistered());
    }

    @Test
    public void testIsRegisteredFalse() {
        assertFalse(NavigationItemFactory.EXPORT.isRegistered());
        assertFalse(NavigationItemFactory.CHANNEL_AVAILABLE.isRegistered());
        assertFalse(NavigationItemFactory.VENDORS.isRegistered());
        assertFalse(NavigationItemFactory.SETTINGS.isRegistered());
        assertFalse(NavigationItemFactory.ABOUT.isRegistered());
    }

    @Test
    public void testGetVisibility() {
        assertEquals(View.VISIBLE, NavigationItemFactory.ACCESS_POINTS.getVisibility());
        assertEquals(View.VISIBLE, NavigationItemFactory.CHANNEL_RATING.getVisibility());
        assertEquals(View.VISIBLE, NavigationItemFactory.CHANNEL_GRAPH.getVisibility());
        assertEquals(View.VISIBLE, NavigationItemFactory.TIME_GRAPH.getVisibility());
        assertEquals(View.VISIBLE, NavigationItemFactory.CHANNEL_AVAILABLE.getVisibility());
        assertEquals(View.GONE, NavigationItemFactory.VENDORS.getVisibility());
        assertEquals(View.GONE, NavigationItemFactory.EXPORT.getVisibility());
        assertEquals(View.GONE, NavigationItemFactory.SETTINGS.getVisibility());
        assertEquals(View.GONE, NavigationItemFactory.ABOUT.getVisibility());
    }

    @Test
    public void testExportItem() {
        assertTrue(NavigationItemFactory.EXPORT instanceof ExportItem);
    }

}