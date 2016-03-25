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

import com.vrem.wifianalyzer.wifi.band.WiFiWidth;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class WiFiDetailTest {
    private static final int FREQUENCY = 2435;
    private static final int LEVEL = -40;
    private static final String VENDOR_NAME = "VendorName";
    private static final String WPA = "WPA";
    private static final String SSID = "xyzSSID";
    private static final String BSSID = "xyzBSSID";

    private WiFiSignal wiFiSignal;
    private WiFiAdditional wiFiAdditional;
    private WiFiDetail fixture;

    @Before
    public void setUp() throws Exception {
        wiFiAdditional = new WiFiAdditional(VENDOR_NAME, false);
        wiFiSignal = new WiFiSignal(FREQUENCY, WiFiWidth.MHZ_20, LEVEL);
        fixture = new WiFiDetail(SSID, BSSID, WPA, wiFiSignal, wiFiAdditional);
    }

    @Test
    public void testWiFiDetail() throws Exception {
        // validate
        assertEquals(wiFiSignal, fixture.getWiFiSignal());
        assertEquals(wiFiAdditional, fixture.getWiFiAdditional());
        assertEquals(SSID, fixture.getSSID());
        assertEquals(BSSID, fixture.getBSSID());
        assertEquals(WPA, fixture.getCapabilities());
        assertEquals(SSID + " (" + BSSID + ")", fixture.getTitle());
        assertEquals(Security.WPA, fixture.getSecurity());
    }

    @Test
    public void testGetTitleWithEmptySSID() throws Exception {
        // setup
        fixture = new WiFiDetail(StringUtils.EMPTY, BSSID, WPA, wiFiSignal);
        // validate
        assertEquals("*** (" + BSSID + ")", fixture.getTitle());
    }

    @Test
    public void testEquals() throws Exception {
        // setup
        WiFiDetail other = new WiFiDetail(SSID, BSSID, WPA, wiFiSignal);
        // execute & validate
        assertEquals(fixture, other);
        assertNotSame(fixture, other);
    }

    @Test
    public void testHashCode() throws Exception {
        // setup
        WiFiDetail other = new WiFiDetail(SSID, BSSID, WPA, wiFiSignal);
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode());
    }

    @Test
    public void testCompareTo() throws Exception {
        // setup
        WiFiDetail other = new WiFiDetail(SSID, BSSID, WPA, wiFiSignal);
        // execute & validate
        assertEquals(0, fixture.compareTo(other));
    }

}