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

package com.vrem.wifianalyzer.wifi.model;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class WiFiConnectionTest {
    private static final String SSID = "SSID-123";
    private static final String BSSID = "BSSID-123";
    private static final String IP_ADDRESS = "21.205.91.7";
    private static final int LINK_SPEED = 21;

    private WiFiConnection fixture;

    @Before
    public void setUp() {
        fixture = new WiFiConnection(SSID, BSSID, IP_ADDRESS, LINK_SPEED);
    }

    @Test
    public void testWiFiConnectionEmpty() {
        // validate
        assertEquals(StringUtils.EMPTY, WiFiConnection.EMPTY.getSSID());
        assertEquals(StringUtils.EMPTY, WiFiConnection.EMPTY.getBSSID());
        assertEquals(StringUtils.EMPTY, WiFiConnection.EMPTY.getIpAddress());
        assertEquals(WiFiConnection.LINK_SPEED_INVALID, WiFiConnection.EMPTY.getLinkSpeed());
        assertFalse(WiFiConnection.EMPTY.isConnected());
    }

    @Test
    public void testWiFiConnection() {
        // validate
        assertEquals(SSID, fixture.getSSID());
        assertEquals(BSSID, fixture.getBSSID());
        assertEquals(IP_ADDRESS, fixture.getIpAddress());
        assertEquals(LINK_SPEED, fixture.getLinkSpeed());
        assertTrue(fixture.isConnected());
    }

    @Test
    public void testEquals() {
        // setup
        WiFiConnection other = new WiFiConnection(SSID, BSSID, StringUtils.EMPTY, WiFiConnection.LINK_SPEED_INVALID);
        // execute & validate
        assertEquals(fixture, other);
        assertNotSame(fixture, other);
    }

    @Test
    public void testHashCode() {
        // setup
        WiFiConnection other = new WiFiConnection(SSID, BSSID, StringUtils.EMPTY, WiFiConnection.LINK_SPEED_INVALID);
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode());
    }

}