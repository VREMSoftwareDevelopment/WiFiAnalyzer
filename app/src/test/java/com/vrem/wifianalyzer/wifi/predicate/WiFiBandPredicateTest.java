/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WiFiBandPredicateTest {

    @Test
    public void testWiFiBandPredicateWith2GHzFrequency() {
        // setup
        WiFiDetail wiFiDetail = makeWiFiDetail(2455);
        // execute & validate
        assertTrue(new WiFiBandPredicate(WiFiBand.GHZ2).evaluate(wiFiDetail));
        assertFalse(new WiFiBandPredicate(WiFiBand.GHZ5).evaluate(wiFiDetail));
    }

    @Test
    public void testWiFiBandPredicateWith5GHzFrequency() {
        // setup
        WiFiDetail wiFiDetail = makeWiFiDetail(5455);
        // execute & validate
        assertFalse(new WiFiBandPredicate(WiFiBand.GHZ2).evaluate(wiFiDetail));
        assertTrue(new WiFiBandPredicate(WiFiBand.GHZ5).evaluate(wiFiDetail));
    }

    @NonNull
    private WiFiDetail makeWiFiDetail(int frequency) {
        WiFiSignal wiFiSignal = new WiFiSignal(frequency, frequency, WiFiWidth.MHZ_20, 1);
        return new WiFiDetail("ssid", "bssid", "wpa", wiFiSignal, WiFiAdditional.EMPTY);
    }

}