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

package com.vrem.wifianalyzer.wifi.model;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class WiFiConnectionTest {
    private static final String SSID = "SSID-123";
    private static final String BSSID = "BSSID-123";
    private static final String IP_ADDRESS = "21.205.91.7";
    private static final int LINK_SPEED = 21;

    private WiFiConnection fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new WiFiConnection(SSID, BSSID, IP_ADDRESS, LINK_SPEED);
    }

    @Test
    public void testWiFiConnectionEmpty() throws Exception {
        // validate
        assertEquals(StringUtils.EMPTY, WiFiConnection.EMPTY.getSSID());
        assertEquals(StringUtils.EMPTY, WiFiConnection.EMPTY.getBSSID());
        assertEquals(StringUtils.EMPTY, WiFiConnection.EMPTY.getIpAddress());
        assertEquals(WiFiConnection.LINK_SPEED_INVALID, WiFiConnection.EMPTY.getLinkSpeed());
    }

    @Test
    public void testWiFiConnection() throws Exception {
        // validate
        assertEquals(SSID, fixture.getSSID());
        assertEquals(BSSID, fixture.getBSSID());
        assertEquals(IP_ADDRESS, fixture.getIpAddress());
        assertEquals(LINK_SPEED, fixture.getLinkSpeed());
    }

    @Test
    public void testEquals() throws Exception {
        // setup
        WiFiConnection other = new WiFiConnection(SSID, BSSID);
        // execute & validate
        assertEquals(fixture, other);
        assertNotSame(fixture, other);
    }

    @Test
    public void testHashCode() throws Exception {
        // setup
        WiFiConnection other = new WiFiConnection(SSID, BSSID);
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode());
    }

}