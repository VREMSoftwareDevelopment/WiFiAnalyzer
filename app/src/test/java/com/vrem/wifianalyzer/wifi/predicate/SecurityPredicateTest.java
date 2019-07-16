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

package com.vrem.wifianalyzer.wifi.predicate;

import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.Security;
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.junit.Test;

import androidx.annotation.NonNull;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SecurityPredicateTest {

    @Test
    public void testSecurityPredicateWithFoundValue() {
        // setup
        WiFiDetail wiFiDetail = makeWiFiDetail("wpa");
        SecurityPredicate fixture = new SecurityPredicate(Security.WPA);
        // execute
        boolean actual = fixture.evaluate(wiFiDetail);
        // validate
        assertTrue(actual);
        assertFalse(new SecurityPredicate(Security.WEP).evaluate(wiFiDetail));
    }

    @Test
    public void testSecurityPredicateWithNotFoundValue() {
        // setup
        WiFiDetail wiFiDetail = makeWiFiDetail("wep");
        SecurityPredicate fixture = new SecurityPredicate(Security.WPA);
        // execute
        boolean actual = fixture.evaluate(wiFiDetail);
        // validate
        assertFalse(actual);
    }

    @NonNull
    private WiFiDetail makeWiFiDetail(String security) {
        WiFiSignal wiFiSignal = new WiFiSignal(2455, 2455, WiFiWidth.MHZ_20, 1, true);
        return new WiFiDetail("ssid", "bssid", security, wiFiSignal, WiFiAdditional.EMPTY);
    }

}