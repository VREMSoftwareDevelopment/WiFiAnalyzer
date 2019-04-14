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

package com.vrem.wifianalyzer.export;

import com.google.common.collect.Lists;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class ExportTest {
    private static final String TIME_STAMP = "time-stamp";
    private Export fixture;

    @Before
    public void setUp() {
        List<WiFiDetail> wiFiDetails = Lists.newArrayList(new WiFiDetail("SSID", "BSSID", "capabilities",
            new WiFiSignal(2412, 2422, WiFiWidth.MHZ_40, -40, true)));
        fixture = new Export(wiFiDetails, TIME_STAMP);
    }

    @Test
    public void testGetData() {
        // setup
        String expected =
            String.format(Locale.ENGLISH,
                "Time Stamp|SSID|BSSID|Strength|Primary Channel|Primary Frequency|Center Channel|Center Frequency|Width (Range)|Distance|802.11mc|Security%n"
                    + TIME_STAMP + "|SSID|BSSID|-40dBm|1|2412MHz|3|2422MHz|40MHz (2402 - 2442)|~1.0m|true|capabilities%n");
        // execute
        String actual = fixture.getData();
        // validate
        assertEquals(expected, actual);
    }

}