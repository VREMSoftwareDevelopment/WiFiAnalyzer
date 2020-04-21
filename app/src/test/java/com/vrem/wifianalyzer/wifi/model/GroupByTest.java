/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import org.junit.Test;

import static com.vrem.wifianalyzer.wifi.model.GroupBy.values;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GroupByTest {
    @Test
    public void testGroupByNumber() {
        assertEquals(3, values().length);
    }

    @Test
    public void testGroupBySortByComparator() {
        assertTrue(GroupBy.CHANNEL.comparator().getClass().isInstance(WiFiDetail.sortByChannel()));
        assertTrue(GroupBy.NONE.comparator().getClass().isInstance(WiFiDetail.sortByDefault()));
        assertTrue(GroupBy.SSID.comparator().getClass().isInstance(WiFiDetail.sortBySSID()));

    }

}