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

import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.Security;
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.junit.Test;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SecurityPredicateTest {

    @Test
    public void testSecurityPredicateWithFoundWPAValue() {
        // setup
        WiFiDetail wiFiDetail = wiFiDetail();
        SecurityPredicate fixture = new SecurityPredicate(Security.WPA);
        // execute
        boolean actual = fixture.evaluate(wiFiDetail);
        // validate
        assertTrue(actual);
    }

    @Test
    public void testSecurityPredicateWithFoundWEPValue() {
        // setup
        WiFiDetail wiFiDetail = wiFiDetail();
        SecurityPredicate fixture = new SecurityPredicate(Security.WEP);
        // execute
        boolean actual = fixture.evaluate(wiFiDetail);
        // validate
        assertTrue(actual);
    }

    @Test
    public void testSecurityPredicateWithFoundNoneValue() {
        // setup
        WiFiDetail wiFiDetail = wiFiDetailWithNoSecurity();
        SecurityPredicate fixture = new SecurityPredicate(Security.NONE);
        // execute
        boolean actual = fixture.evaluate(wiFiDetail);
        // validate
        assertTrue(actual);
    }

    @Test
    public void testSecurityPredicateWithNotFoundValue() {
        // setup
        WiFiDetail wiFiDetail = wiFiDetail();
        SecurityPredicate fixture = new SecurityPredicate(Security.WPA2);
        // execute
        boolean actual = fixture.evaluate(wiFiDetail);
        // validate
        assertFalse(actual);
    }

    @NonNull
    private WiFiDetail wiFiDetail() {
        WiFiSignal wiFiSignal = new WiFiSignal(2455, 2455, WiFiWidth.MHZ_20, 1, true);
        WiFiIdentifier wiFiIdentifier = new WiFiIdentifier("ssid", "bssid");
        return new WiFiDetail(wiFiIdentifier, "ess-wep-wpa", wiFiSignal, WiFiAdditional.EMPTY, new ArrayList<>());
    }

    @NonNull
    private WiFiDetail wiFiDetailWithNoSecurity() {
        WiFiSignal wiFiSignal = new WiFiSignal(2455, 2455, WiFiWidth.MHZ_20, 1, true);
        WiFiIdentifier wiFiIdentifier = new WiFiIdentifier("ssid", "bssid");
        return new WiFiDetail(wiFiIdentifier, "ess", wiFiSignal, WiFiAdditional.EMPTY, new ArrayList<>());
    }


}