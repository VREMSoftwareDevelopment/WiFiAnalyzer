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

package com.vrem.wifianalyzer.wifi.graph.channel;

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiChannels;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphViewBuilder;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelAxisLabelTest {
    private Settings settings;
    private ChannelAxisLabel fixture;

    @Before
    public void setUp() throws Exception {
        settings = MainContextHelper.INSTANCE.getSettings();
        when(this.settings.getCountryCode()).thenReturn(Locale.US.getCountry());

        fixture = new ChannelAxisLabel(WiFiBand.GHZ2, WiFiBand.GHZ2.getWiFiChannels().getWiFiChannelPairs().get(0));
    }

    @After
    public void tearDown() throws Exception {
        MainContextHelper.INSTANCE.restore();
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
        // setup
        WiFiChannel wiFiChannel = WiFiBand.GHZ2.getWiFiChannels().getWiFiChannelFirst();
        // execute
        String actual = fixture.formatLabel(wiFiChannel.getFrequency(), true);
        // validate
        assertEquals("" + wiFiChannel.getChannel(), actual);
        verify(settings).getCountryCode();
    }

    @Test
    public void testXAxisWithFrequencyInRange() throws Exception {
        // setup
        WiFiChannel wiFiChannel = WiFiBand.GHZ2.getWiFiChannels().getWiFiChannelFirst();
        // execute & validate
        assertEquals("" + wiFiChannel.getChannel(), fixture.formatLabel(wiFiChannel.getFrequency() - 2, true));
        assertEquals("" + wiFiChannel.getChannel(), fixture.formatLabel(wiFiChannel.getFrequency() + 2, true));
        verify(settings, times(2)).getCountryCode();
    }

    @Test
    public void testXAxisWithFrequencyNotAllowedInLocale() throws Exception {
        // setup
        WiFiChannel wiFiChannel = WiFiBand.GHZ2.getWiFiChannels().getWiFiChannelLast();
        // execute
        String actual = fixture.formatLabel(wiFiChannel.getFrequency(), true);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testXAxisWithUnknownFrequencyReturnEmptyString() throws Exception {
        // setup
        WiFiChannels wiFiChannels = WiFiBand.GHZ2.getWiFiChannels();
        WiFiChannel wiFiChannel = wiFiChannels.getWiFiChannelFirst();
        // execute
        String actual = fixture.formatLabel(wiFiChannel.getFrequency() - wiFiChannels.getFrequencyOffset(), true);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

}