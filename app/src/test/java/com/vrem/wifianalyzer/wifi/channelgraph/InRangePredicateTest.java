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

package com.vrem.wifianalyzer.wifi.channelgraph;

import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import androidx.core.util.Pair;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InRangePredicateTest {
    private Pair<WiFiChannel, WiFiChannel> wiFiChannelPair;
    private InRangePredicate fixture;

    @Before
    public void setUp() {
        wiFiChannelPair = WiFiBand.GHZ2.getWiFiChannels().getWiFiChannelPairs().get(0);
        fixture = new InRangePredicate(wiFiChannelPair);
    }

    @Test
    public void testInRangePredicateWithValidFrequency() {
        // execute & validate
        assertTrue(fixture.evaluate(makeWiFiDetail(wiFiChannelPair.first.getFrequency())));
        assertTrue(fixture.evaluate(makeWiFiDetail(wiFiChannelPair.second.getFrequency())));
        assertTrue(fixture.evaluate(makeWiFiDetail((wiFiChannelPair.first.getFrequency() + wiFiChannelPair.second.getFrequency()) / 2)));
    }

    @Test
    public void testInRangePredicateWithInvalidValidFrequency() {
        // execute & validate
        assertFalse(fixture.evaluate(makeWiFiDetail(wiFiChannelPair.first.getFrequency() - 1)));
        assertFalse(fixture.evaluate(makeWiFiDetail(wiFiChannelPair.second.getFrequency() + 1)));
    }

    private WiFiDetail makeWiFiDetail(int frequency) {
        WiFiSignal wiFiSignal = new WiFiSignal(frequency + 20, frequency, WiFiWidth.MHZ_20, -10, true);
        return new WiFiDetail("SSID", "BSSID", StringUtils.EMPTY, wiFiSignal, WiFiAdditional.EMPTY);
    }

}