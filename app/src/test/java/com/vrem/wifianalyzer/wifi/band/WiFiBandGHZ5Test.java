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
import static org.junit.Assert.assertTrue;

public class WiFiBandGHZ5Test {

    @Test
    public void testGetBand() throws Exception {
        assertEquals("5 GHz", WiFiBand.GHZ5.getBand());
    }

    @Test
    public void testFindByBand() throws Exception {
        assertEquals(WiFiBand.GHZ5, WiFiBand.find(WiFiBand.GHZ5.ordinal()));
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(WiFiBand.GHZ5, WiFiBand.findByFrequency(4901));
        assertEquals(WiFiBand.GHZ5, WiFiBand.findByFrequency(5899));
    }

    @Test
    public void testToggle() throws Exception {
        assertEquals(WiFiBand.GHZ2, WiFiBand.GHZ5.toggle());
    }

    @Test
    public void testIsGHZ_5() throws Exception {
        assertTrue(WiFiBand.GHZ5.isGHZ5());
    }

}