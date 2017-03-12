/*
 * WiFiAnalyzer
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

package com.vrem.wifianalyzer.wifi.model;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SSIDPredicateTest {

    public static final String SSID = "SSID";

    @Test
    public void testSSIDPredicate() throws Exception {
        // setup
        WiFiDetail wiFiDetail = new WiFiDetail(SSID, "BSSID", "WPA", WiFiSignal.EMPTY, WiFiAdditional.EMPTY);
        // execute & validate
        assertTrue(new SSIDPredicate(SSID).evaluate(wiFiDetail));
        assertTrue(new SSIDPredicate("ID").evaluate(wiFiDetail));
        assertTrue(new SSIDPredicate("SS").evaluate(wiFiDetail));
        assertTrue(new SSIDPredicate("S").evaluate(wiFiDetail));
        assertTrue(new SSIDPredicate(StringUtils.EMPTY).evaluate(wiFiDetail));

        assertFalse(new SSIDPredicate(SSID + "1").evaluate(wiFiDetail));
        assertFalse(new SSIDPredicate("L").evaluate(wiFiDetail));
    }

}