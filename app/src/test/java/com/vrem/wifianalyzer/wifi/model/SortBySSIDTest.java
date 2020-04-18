/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.vrem.wifianalyzer.wifi.band.WiFiWidth;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SortBySSIDTest {
    private WiFiDetail wiFiDetail1;
    private WiFiDetail wiFiDetail2;

    @Before
    public void setUp() {
        wiFiDetail1 = new WiFiDetail("SSID1", "BSSID1", StringUtils.EMPTY,
            new WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -35, true),
            WiFiAdditional.EMPTY);
        wiFiDetail2 = new WiFiDetail("SSID2", "BSSID2", StringUtils.EMPTY,
            new WiFiSignal(2432, 2432, WiFiWidth.MHZ_20, -55, true),
            WiFiAdditional.EMPTY);
    }


    @Test
    public void testSortBySSID() {
        // setup
        SortBySSID fixture = new SortBySSID();
        // execute & validate
        assertEquals(0, fixture.compare(wiFiDetail1, wiFiDetail1));
        assertEquals(-1, fixture.compare(wiFiDetail1, wiFiDetail2));
        assertEquals(1, fixture.compare(wiFiDetail2, wiFiDetail1));
    }

}