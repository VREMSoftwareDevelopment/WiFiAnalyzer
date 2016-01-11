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

public class WiFiBandTest {
    @Test
    public void testWiFiBand() throws Exception {
        assertEquals(3, WiFiBand.values().length);
    }

    @Test
    public void testGetBand() throws Exception {
        assertEquals("2.4 & 5 GHz", WiFiBand.ALL.getBand());
        assertEquals("2.4 GHz", WiFiBand.TWO_POINT_FOUR.getBand());
        assertEquals("5 GHz", WiFiBand.FIVE.getBand());
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(WiFiBand.ALL, WiFiBand.find(null));
        assertEquals(WiFiBand.ALL, WiFiBand.find("XYZ"));

        assertEquals(WiFiBand.ALL, WiFiBand.find(WiFiBand.ALL.getBand()));
        assertEquals(WiFiBand.TWO_POINT_FOUR, WiFiBand.find(WiFiBand.TWO_POINT_FOUR.getBand()));
        assertEquals(WiFiBand.FIVE, WiFiBand.find(WiFiBand.FIVE.getBand()));
    }

}