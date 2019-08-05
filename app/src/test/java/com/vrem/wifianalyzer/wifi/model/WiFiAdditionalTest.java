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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WiFiAdditionalTest {
    private static final String VENDOR_NAME = "VendorName";

    @Test
    public void testWiFiAdditionalWithWiFiConnection() {
        // setup
        WiFiConnection wiFiConnection = new WiFiConnection("SSID", "BSSID", "192.168.1.10", 22);
        // execute
        WiFiAdditional fixture = new WiFiAdditional(VENDOR_NAME, wiFiConnection);
        // validate
        assertEquals(VENDOR_NAME, fixture.getVendorName());
        assertEquals(wiFiConnection, fixture.getWiFiConnection());
    }

    @Test
    public void testWiFiAdditional() {
        // execute
        WiFiAdditional fixture = new WiFiAdditional(VENDOR_NAME);
        // validate
        assertEquals(VENDOR_NAME, fixture.getVendorName());
    }

    @Test
    public void testWiFiAdditionalEmpty() {
        // validate
        assertEquals(StringUtils.EMPTY, WiFiAdditional.EMPTY.getVendorName());
    }

}