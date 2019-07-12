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

package com.vrem.wifianalyzer.navigation.availability;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NavigationOptionFactoryTest {

    @Test
    public void testRating() {
        List<NavigationOption> options = NavigationOptionFactory.RATING;
        assertEquals(4, options.size());

        assertTrue(options.contains(NavigationOptionFactory.WIFI_SWITCH_ON));
        assertTrue(options.contains(NavigationOptionFactory.SCANNER_SWITCH_ON));
        assertTrue(options.contains(NavigationOptionFactory.FILTER_OFF));
        assertTrue(options.contains(NavigationOptionFactory.BOTTOM_NAV_ON));

        assertFalse(options.contains(NavigationOptionFactory.WIFI_SWITCH_OFF));
        assertFalse(options.contains(NavigationOptionFactory.SCANNER_SWITCH_OFF));
        assertFalse(options.contains(NavigationOptionFactory.FILTER_ON));
        assertFalse(options.contains(NavigationOptionFactory.BOTTOM_NAV_OFF));
    }

    @Test
    public void testOther() {
        List<NavigationOption> options = NavigationOptionFactory.OTHER;
        assertEquals(4, options.size());

        assertTrue(options.contains(NavigationOptionFactory.WIFI_SWITCH_ON));
        assertTrue(options.contains(NavigationOptionFactory.SCANNER_SWITCH_ON));
        assertTrue(options.contains(NavigationOptionFactory.FILTER_ON));
        assertTrue(options.contains(NavigationOptionFactory.BOTTOM_NAV_ON));

        assertFalse(options.contains(NavigationOptionFactory.WIFI_SWITCH_OFF));
        assertFalse(options.contains(NavigationOptionFactory.SCANNER_SWITCH_OFF));
        assertFalse(options.contains(NavigationOptionFactory.FILTER_OFF));
        assertFalse(options.contains(NavigationOptionFactory.BOTTOM_NAV_OFF));
    }

    @Test
    public void testOff() {
        List<NavigationOption> options = NavigationOptionFactory.OFF;
        assertEquals(4, options.size());

        assertTrue(options.contains(NavigationOptionFactory.WIFI_SWITCH_OFF));
        assertTrue(options.contains(NavigationOptionFactory.SCANNER_SWITCH_OFF));
        assertTrue(options.contains(NavigationOptionFactory.FILTER_OFF));
        assertTrue(options.contains(NavigationOptionFactory.BOTTOM_NAV_OFF));

        assertFalse(options.contains(NavigationOptionFactory.WIFI_SWITCH_ON));
        assertFalse(options.contains(NavigationOptionFactory.SCANNER_SWITCH_ON));
        assertFalse(options.contains(NavigationOptionFactory.FILTER_ON));
        assertFalse(options.contains(NavigationOptionFactory.BOTTOM_NAV_ON));
    }

    @Test
    public void testAccessPoints() {
        List<NavigationOption> options = NavigationOptionFactory.AP;
        assertEquals(4, options.size());

        assertTrue(options.contains(NavigationOptionFactory.WIFI_SWITCH_OFF));
        assertTrue(options.contains(NavigationOptionFactory.SCANNER_SWITCH_ON));
        assertTrue(options.contains(NavigationOptionFactory.FILTER_ON));
        assertTrue(options.contains(NavigationOptionFactory.BOTTOM_NAV_ON));

        assertFalse(options.contains(NavigationOptionFactory.WIFI_SWITCH_ON));
        assertFalse(options.contains(NavigationOptionFactory.SCANNER_SWITCH_OFF));
        assertFalse(options.contains(NavigationOptionFactory.FILTER_OFF));
        assertFalse(options.contains(NavigationOptionFactory.BOTTOM_NAV_OFF));
    }

}