/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WiFiAdditionalTest {
    private static final String VENDOR_NAME = "VendorName";
    private static final String IP_ADDRESS = "IPAddress";
    private static final int LINK_SPEED = 135;

    @Test
    public void testWiFiAdditionalWithIpAddress() throws Exception {
        WiFiAdditional fixture = new WiFiAdditional(VENDOR_NAME, IP_ADDRESS, LINK_SPEED);
        assertEquals(VENDOR_NAME, fixture.getVendorName());
        assertEquals(IP_ADDRESS, fixture.getIPAddress());
        assertEquals(LINK_SPEED, fixture.getLinkSpeed());
        assertTrue(fixture.isConnected());
        assertTrue(fixture.isConfiguredNetwork());
    }

    @Test
    public void testWiFiAdditionalWithConfiguredNetwork() throws Exception {
        WiFiAdditional fixture = new WiFiAdditional(VENDOR_NAME, true);
        assertEquals(VENDOR_NAME, fixture.getVendorName());
        assertEquals(StringUtils.EMPTY, fixture.getIPAddress());
        assertEquals(WiFiConnection.LINK_SPEED_INVALID, fixture.getLinkSpeed());
        assertFalse(fixture.isConnected());
        assertTrue(fixture.isConfiguredNetwork());
    }

    @Test
    public void testWiFiAdditionalEmpty() throws Exception {
        assertEquals(StringUtils.EMPTY, WiFiAdditional.EMPTY.getVendorName());
        assertEquals(StringUtils.EMPTY, WiFiAdditional.EMPTY.getIPAddress());
        assertEquals(WiFiConnection.LINK_SPEED_INVALID, WiFiAdditional.EMPTY.getLinkSpeed());
        assertFalse(WiFiAdditional.EMPTY.isConnected());
        assertFalse(WiFiAdditional.EMPTY.isConfiguredNetwork());
    }

}