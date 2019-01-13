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

package com.vrem.wifianalyzer.wifi.band;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FrequencyPredicateTest {

    @Test
    public void testWiFiBandPredicate() {
        assertFalse(new FrequencyPredicate(2399).evaluate(WiFiBand.GHZ2));
        assertTrue(new FrequencyPredicate(2400).evaluate(WiFiBand.GHZ2));
        assertTrue(new FrequencyPredicate(2499).evaluate(WiFiBand.GHZ2));
        assertFalse(new FrequencyPredicate(2500).evaluate(WiFiBand.GHZ2));

        assertFalse(new FrequencyPredicate(4899).evaluate(WiFiBand.GHZ5));
        assertTrue(new FrequencyPredicate(4900).evaluate(WiFiBand.GHZ5));
        assertTrue(new FrequencyPredicate(5899).evaluate(WiFiBand.GHZ5));
        assertFalse(new FrequencyPredicate(5900).evaluate(WiFiBand.GHZ5));
    }

}