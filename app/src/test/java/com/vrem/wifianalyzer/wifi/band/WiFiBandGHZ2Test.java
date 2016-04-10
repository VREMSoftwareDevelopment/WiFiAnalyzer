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

package com.vrem.wifianalyzer.wifi.band;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class WiFiBandGHZ2Test {

    @Test
    public void testGetBand() throws Exception {
        assertEquals("2.4 GHz", WiFiBand.GHZ2.getBand());
    }

    @Test
    public void testFindByBand() throws Exception {
        assertEquals(WiFiBand.GHZ2, WiFiBand.find(-1));
        assertEquals(WiFiBand.GHZ2, WiFiBand.find(2));

        assertEquals(WiFiBand.GHZ2, WiFiBand.find(WiFiBand.GHZ2.ordinal()));
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(WiFiBand.GHZ2, WiFiBand.findByFrequency(2400));
        assertEquals(WiFiBand.GHZ2, WiFiBand.findByFrequency(2500));
        assertEquals(WiFiBand.GHZ2, WiFiBand.findByFrequency(4899));
        assertEquals(WiFiBand.GHZ2, WiFiBand.findByFrequency(5901));
        assertEquals(WiFiBand.GHZ2, WiFiBand.findByFrequency(2401));
        assertEquals(WiFiBand.GHZ2, WiFiBand.findByFrequency(2499));
    }

    @Test
    public void testToggle() throws Exception {
        assertEquals(WiFiBand.GHZ5, WiFiBand.GHZ2.toggle());
    }

    @Test
    public void testIsGHZ_5() throws Exception {
        assertFalse(WiFiBand.GHZ2.isGHZ5());
    }
}