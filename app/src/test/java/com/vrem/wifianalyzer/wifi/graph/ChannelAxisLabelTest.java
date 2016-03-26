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

import android.content.res.Configuration;
import android.content.res.Resources;

import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiChannels;
import com.vrem.wifianalyzer.wifi.graph.wrapper.GraphViewBuilder;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelAxisLabelTest {
    @Mock
    private Resources resources;
    @Mock
    private Configuration configuration;

    private ChannelAxisLabel fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new ChannelAxisLabel(WiFiBand.GHZ_2, WiFiBand.GHZ_2.getWiFiChannels().getChannelsSet().get(0), resources);

        when(resources.getConfiguration()).thenReturn(configuration);
        configuration.locale = Locale.US;
    }

    @Test
    public void testYAxis() throws Exception {
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(GraphViewBuilder.MIN_Y, false));
        assertEquals("-99", fixture.formatLabel(GraphViewBuilder.MIN_Y + 1, false));

        assertEquals("-20", fixture.formatLabel(GraphViewBuilder.MAX_Y, false));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(GraphViewBuilder.MAX_Y + 1, false));
    }

    @Test
    public void testXAxis() throws Exception {
        WiFiChannel wiFiChannel = WiFiBand.GHZ_2.getWiFiChannels().getWiFiChannelFirst();
        assertEquals("" + wiFiChannel.getChannel(), fixture.formatLabel(wiFiChannel.getFrequency(), true));
    }

    @Test
    public void testXAxisWithFrequencyInRange() throws Exception {
        WiFiChannel wiFiChannel = WiFiBand.GHZ_2.getWiFiChannels().getWiFiChannelFirst();
        assertEquals("" + wiFiChannel.getChannel(), fixture.formatLabel(wiFiChannel.getFrequency() - 2, true));
        assertEquals("" + wiFiChannel.getChannel(), fixture.formatLabel(wiFiChannel.getFrequency() + 2, true));
    }

    @Test
    public void testXAxisWithFrequencyNotAllowedInLocale() throws Exception {
        WiFiChannel wiFiChannel = WiFiBand.GHZ_2.getWiFiChannels().getWiFiChannelLast();
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(wiFiChannel.getFrequency(), true));
    }

    @Test
    public void testXAxisWithUnknownFrequencyReturnEmptyString() throws Exception {
        WiFiChannels wiFiChannels = WiFiBand.GHZ_2.getWiFiChannels();
        WiFiChannel wiFiChannel = wiFiChannels.getWiFiChannelFirst();
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(wiFiChannel.getFrequency() - wiFiChannels.getFrequencyOffset(), true));
    }

}