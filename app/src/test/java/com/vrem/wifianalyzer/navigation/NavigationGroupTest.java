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

package com.vrem.wifianalyzer.navigation;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class NavigationGroupTest {

    @Test
    public void testNavigationGroup() throws Exception {
        assertEquals(3, NavigationGroup.values().length);
    }

    @Test
    public void testNavigationGroupOrder() throws Exception {
        assertArrayEquals(new NavigationGroup[]{
                NavigationGroup.GROUP_FEATURE,
                NavigationGroup.GROUP_OTHER,
                NavigationGroup.GROUP_SETTINGS,
            },
            NavigationGroup.values());
    }

    @Test
    public void testGetNavigationMenus() throws Exception {
        assertArrayEquals(new NavigationMenu[]{
                NavigationMenu.ACCESS_POINTS,
                NavigationMenu.CHANNEL_RATING,
                NavigationMenu.CHANNEL_GRAPH,
                NavigationMenu.TIME_GRAPH
            },
            NavigationGroup.GROUP_FEATURE.getNavigationMenus().toArray());
        assertArrayEquals(new NavigationMenu[]{
                NavigationMenu.EXPORT,
                NavigationMenu.CHANNEL_AVAILABLE,
                NavigationMenu.VENDORS
            },
            NavigationGroup.GROUP_OTHER.getNavigationMenus().toArray());
        assertArrayEquals(new NavigationMenu[]{
                NavigationMenu.SETTINGS,
                NavigationMenu.ABOUT
            },
            NavigationGroup.GROUP_SETTINGS.getNavigationMenus().toArray());
    }

    @Test
    public void testNext() throws Exception {
        assertEquals(NavigationMenu.CHANNEL_GRAPH, NavigationGroup.GROUP_FEATURE.next(NavigationMenu.CHANNEL_RATING));
        assertEquals(NavigationMenu.ACCESS_POINTS, NavigationGroup.GROUP_FEATURE.next(NavigationMenu.TIME_GRAPH));
        assertEquals(NavigationMenu.EXPORT, NavigationGroup.GROUP_FEATURE.next(NavigationMenu.EXPORT));
    }

    @Test
    public void testPrevious() throws Exception {
        assertEquals(NavigationMenu.ACCESS_POINTS, NavigationGroup.GROUP_FEATURE.previous(NavigationMenu.CHANNEL_RATING));
        assertEquals(NavigationMenu.TIME_GRAPH, NavigationGroup.GROUP_FEATURE.previous(NavigationMenu.ACCESS_POINTS));
        assertEquals(NavigationMenu.EXPORT, NavigationGroup.GROUP_FEATURE.next(NavigationMenu.EXPORT));
    }

}