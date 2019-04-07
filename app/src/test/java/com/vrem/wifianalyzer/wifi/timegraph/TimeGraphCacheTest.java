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

package com.vrem.wifianalyzer.wifi.timegraph;

import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.graphutils.GraphConstants;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimeGraphCacheTest {

    private TimeGraphCache fixture;

    @Before
    public void setUp() {
        fixture = new TimeGraphCache();
    }

    @Test
    public void testAll() {
        // setup
        List<WiFiDetail> expected = withWiFiDetails();
        // execute
        Set<WiFiDetail> actual = fixture.getWiFiDetails();
        // validate
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void testActive() {
        // setup
        List<WiFiDetail> expected = withWiFiDetails();
        // execute
        Set<WiFiDetail> actual = fixture.active();
        // validate
        assertEquals(expected.size() - 1, actual.size());
        assertFalse(actual.contains(expected.get(0)));
    }

    @Test
    public void testClear() {
        // setup
        List<WiFiDetail> expected = withWiFiDetails();
        // execute
        fixture.clear();
        // validate
        Set<WiFiDetail> actual = fixture.getWiFiDetails();
        assertEquals(expected.size() - 1, actual.size());
        assertFalse(actual.contains(expected.get(0)));
    }

    @Test
    public void testReset() {
        // setup
        List<WiFiDetail> expected = withWiFiDetails();
        // execute
        fixture.reset(expected.get(0));
        // validate
        Set<WiFiDetail> actual = fixture.getWiFiDetails();
        assertEquals(expected.size(), actual.size());
        assertTrue(actual.contains(expected.get(0)));
    }

    private WiFiDetail withWiFiDetail(String SSID) {
        return new WiFiDetail(SSID, "BSSID", StringUtils.EMPTY,
            new WiFiSignal(100, 100, WiFiWidth.MHZ_20, 5, true));
    }

    private List<WiFiDetail> withWiFiDetails() {
        List<WiFiDetail> results = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            WiFiDetail wiFiDetail = withWiFiDetail("SSID" + i);
            results.add(wiFiDetail);
        }
        IterableUtils.forEach(results, new AddClosure());
        for (int i = 0; i < GraphConstants.MAX_NOTSEEN_COUNT; i++) {
            fixture.add(results.get(0));
        }
        return results;
    }

    private class AddClosure implements Closure<WiFiDetail> {
        @Override
        public void execute(WiFiDetail wiFiDetail) {
            fixture.add(wiFiDetail);
        }
    }
}