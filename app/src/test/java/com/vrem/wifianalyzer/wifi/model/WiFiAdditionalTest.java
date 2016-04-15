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