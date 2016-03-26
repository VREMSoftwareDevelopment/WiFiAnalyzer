/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.vrem.wifianalyzer.wifi.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SortByTest {

    @Test
    public void testSortByNumber() throws Exception {
        assertEquals(3, SortBy.values().length);
    }

    @Test
    public void testComparator() throws Exception {
        assertTrue(SortBy.STRENGTH.comparator() instanceof SortBy.StrengthComparator);
        assertTrue(SortBy.SSID.comparator() instanceof SortBy.SSIDComparator);
        assertTrue(SortBy.CHANNEL.comparator() instanceof SortBy.ChannelComparator);
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(SortBy.STRENGTH, SortBy.find(-1));
        assertEquals(SortBy.STRENGTH, SortBy.find(3));

        assertEquals(SortBy.STRENGTH, SortBy.find(SortBy.STRENGTH.ordinal()));
        assertEquals(SortBy.SSID, SortBy.find(SortBy.SSID.ordinal()));
        assertEquals(SortBy.CHANNEL, SortBy.find(SortBy.CHANNEL.ordinal()));
    }
}