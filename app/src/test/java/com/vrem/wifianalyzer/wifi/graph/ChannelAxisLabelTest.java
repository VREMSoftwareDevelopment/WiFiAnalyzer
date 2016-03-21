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

package com.vrem.wifianalyzer.wifi.graph;

import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ChannelAxisLabelTest {
    private ChannelAxisLabel fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new ChannelAxisLabel(WiFiBand.GHZ_2);
    }

    @Test
    public void testYAxis() throws Exception {
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(GraphViewBuilder.MIN_Y, false));
        assertEquals("-99", fixture.formatLabel(GraphViewBuilder.MIN_Y + 1, false));

        assertEquals("-10", fixture.formatLabel(GraphViewBuilder.MAX_Y, false));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(GraphViewBuilder.MAX_Y + 1, false));
    }

    @Test
    public void testXAxis() throws Exception {
        List<WiFiChannel> wiFiChannels = WiFiBand.GHZ_2.getWiFiChannels();
        assertEquals("" + wiFiChannels.get(0).getChannel(),
                fixture.formatLabel(wiFiChannels.get(0).getFrequency(), true));
        assertEquals("" + wiFiChannels.get(wiFiChannels.size() - 1).getChannel(),
                fixture.formatLabel(wiFiChannels.get(wiFiChannels.size() - 1).getFrequency(), true));
    }

    @Test
    public void testXAxisWithInvalidFrequencies() throws Exception {
        List<WiFiChannel> wiFiChannels = WiFiBand.GHZ_5.getWiFiChannels();
        assertEquals(StringUtils.EMPTY,
                fixture.formatLabel(wiFiChannels.get(0).getFrequency(), true));
        assertEquals(StringUtils.EMPTY,
                fixture.formatLabel(wiFiChannels.get(wiFiChannels.size() - 1).getFrequency(), true));
    }

}