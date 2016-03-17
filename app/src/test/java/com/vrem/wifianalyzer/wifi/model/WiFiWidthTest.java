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

public class WiFiWidthTest {

    @Test
    public void testWiFiWidth() throws Exception {
        assertEquals(5, WiFiWidth.values().length);
    }

    @Test
    public void testGetFrequencyWidth() throws Exception {
        assertEquals(20, WiFiWidth.MHZ_20.getFrequencyWidth());
        assertEquals(40, WiFiWidth.MHZ_40.getFrequencyWidth());
        assertEquals(80, WiFiWidth.MHZ_80.getFrequencyWidth());
        assertEquals(160, WiFiWidth.MHZ_160.getFrequencyWidth());
        assertEquals(160, WiFiWidth.MHZ_80_80.getFrequencyWidth());
    }

    @Test
    public void testGetFrequencyHalfWidth() throws Exception {
        assertEquals(10, WiFiWidth.MHZ_20.getFrequencyWidthHalf());
        assertEquals(20, WiFiWidth.MHZ_40.getFrequencyWidthHalf());
        assertEquals(40, WiFiWidth.MHZ_80.getFrequencyWidthHalf());
        assertEquals(80, WiFiWidth.MHZ_160.getFrequencyWidthHalf());
        assertEquals(80, WiFiWidth.MHZ_80_80.getFrequencyWidthHalf());
    }

    @Test
    public void testGetChannelWidth() throws Exception {
        assertEquals(4, WiFiWidth.MHZ_20.getChannelWidth());
        assertEquals(8, WiFiWidth.MHZ_40.getChannelWidth());
        assertEquals(16, WiFiWidth.MHZ_80.getChannelWidth());
        assertEquals(32, WiFiWidth.MHZ_160.getChannelWidth());
        assertEquals(32, WiFiWidth.MHZ_80_80.getChannelWidth());
    }

    @Test
    public void testGetChannelHalfWidth() throws Exception {
        assertEquals(2, WiFiWidth.MHZ_20.getChannelWidthHalf());
        assertEquals(4, WiFiWidth.MHZ_40.getChannelWidthHalf());
        assertEquals(8, WiFiWidth.MHZ_80.getChannelWidthHalf());
        assertEquals(16, WiFiWidth.MHZ_160.getChannelWidthHalf());
        assertEquals(16, WiFiWidth.MHZ_80_80.getChannelWidthHalf());
    }

    @Test
    public void testFind() throws Exception {
        for (WiFiWidth wiFiWidth : WiFiWidth.values()) {
            assertEquals(wiFiWidth, WiFiWidth.find(wiFiWidth.ordinal()));
        }
        assertEquals(WiFiWidth.MHZ_20, WiFiWidth.find(-1));
        assertEquals(WiFiWidth.MHZ_20, WiFiWidth.find(WiFiWidth.values().length));
    }

}