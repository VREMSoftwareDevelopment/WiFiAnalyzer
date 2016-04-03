/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
    public void testNavigationGroupMenuItems() throws Exception {
        assertArrayEquals(new NavigationMenu[]{
                        NavigationMenu.ACCESS_POINTS,
                        NavigationMenu.CHANNEL_RATING,
                        NavigationMenu.CHANNEL_GRAPH,
                        NavigationMenu.TIME_GRAPH
                },
                NavigationGroup.GROUP_FEATURE.navigationMenu());
        assertArrayEquals(new NavigationMenu[]{
                        NavigationMenu.CHANNEL_AVAILABLE,
                        NavigationMenu.VENDOR_LIST
                },
                NavigationGroup.GROUP_OTHER.navigationMenu());
        assertArrayEquals(new NavigationMenu[]{
                        NavigationMenu.SETTINGS,
                        NavigationMenu.ABOUT
                },
                NavigationGroup.GROUP_SETTINGS.navigationMenu());
    }
}