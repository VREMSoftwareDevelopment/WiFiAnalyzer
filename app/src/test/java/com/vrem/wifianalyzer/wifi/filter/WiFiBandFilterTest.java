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

package com.vrem.wifianalyzer.wifi.filter;

import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WiFiBandFilterTest {

    @Test
    public void testMapping() {
        Set<WiFiBand> wiFiBands = EnumUtils.values(WiFiBand.class);
        assertEquals(wiFiBands.size(), WiFiBandFilter.ids.size());
        IterableUtils.forEach(wiFiBands, new MappingClosure());
    }

    private static class MappingClosure implements Closure<WiFiBand> {
        @Override
        public void execute(WiFiBand wiFiBand) {
            assertNotNull(WiFiBandFilter.ids.get(wiFiBand));
        }
    }
}