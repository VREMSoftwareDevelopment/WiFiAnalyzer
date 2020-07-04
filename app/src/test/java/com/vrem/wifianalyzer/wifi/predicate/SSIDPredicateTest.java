/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.predicate;

import com.vrem.wifianalyzer.wifi.model.WiFiAdditional;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SSIDPredicateTest {

    private static final String SSID = "ssid";

    @Test
    public void testSSIDPredicate() {
        // setup
        WiFiIdentifier wiFiIdentifier = new WiFiIdentifier(SSID, "bssid");
        WiFiDetail wiFiDetail = new WiFiDetail(wiFiIdentifier, "wpa", WiFiSignal.EMPTY, WiFiAdditional.EMPTY, new ArrayList<>());
        // execute & validate
        assertTrue(new SSIDPredicate(SSID).evaluate(wiFiDetail));
        assertTrue(new SSIDPredicate("id").evaluate(wiFiDetail));
        assertTrue(new SSIDPredicate("ss").evaluate(wiFiDetail));
        assertTrue(new SSIDPredicate("s").evaluate(wiFiDetail));
        assertTrue(new SSIDPredicate("").evaluate(wiFiDetail));

        assertFalse(new SSIDPredicate("SSID").evaluate(wiFiDetail));
        assertFalse(new SSIDPredicate("B").evaluate(wiFiDetail));
    }

}