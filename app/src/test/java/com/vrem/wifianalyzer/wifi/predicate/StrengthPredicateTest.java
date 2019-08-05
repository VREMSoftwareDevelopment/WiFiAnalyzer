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
import com.vrem.wifianalyzer.wifi.model.Strength;
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.junit.Test;

import androidx.annotation.NonNull;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StrengthPredicateTest {

    @Test
    public void testStrengthPredicate() {
        // setup
        WiFiDetail wiFiDetail = makeWiFiDetail(-60);
        // execute & validate
        assertTrue(new StrengthPredicate(Strength.THREE).evaluate(wiFiDetail));
        assertFalse(new StrengthPredicate(Strength.FOUR).evaluate(wiFiDetail));
    }

    @NonNull
    private WiFiDetail makeWiFiDetail(int level) {
        WiFiSignal wiFiSignal = new WiFiSignal(2445, 2445, WiFiWidth.MHZ_20, level, true);
        return new WiFiDetail("ssid", "bssid", "wpa", wiFiSignal, WiFiAdditional.EMPTY);
    }

}