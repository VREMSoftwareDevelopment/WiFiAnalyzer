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

package com.vrem.wifianalyzer.navigation.options;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NavigationOptionFactoryTest {

    @Test
    public void testAllOn() throws Exception {
        assertTrue(NavigationOptionFactory.ALL_ON.contains(NavigationOptionFactory.WIFI_SWITCH_ON));
        assertTrue(NavigationOptionFactory.ALL_ON.contains(NavigationOptionFactory.SCANNER_SWITCH_ON));

        assertFalse(NavigationOptionFactory.ALL_ON.contains(NavigationOptionFactory.WIFI_SWITCH_OFF));
        assertFalse(NavigationOptionFactory.ALL_ON.contains(NavigationOptionFactory.SCANNER_SWITCH_OFF));
    }

    @Test
    public void testAllOff() throws Exception {
        assertTrue(NavigationOptionFactory.ALL_OFF.contains(NavigationOptionFactory.WIFI_SWITCH_OFF));
        assertTrue(NavigationOptionFactory.ALL_OFF.contains(NavigationOptionFactory.SCANNER_SWITCH_OFF));

        assertFalse(NavigationOptionFactory.ALL_OFF.contains(NavigationOptionFactory.WIFI_SWITCH_ON));
        assertFalse(NavigationOptionFactory.ALL_OFF.contains(NavigationOptionFactory.SCANNER_SWITCH_ON));
    }
}