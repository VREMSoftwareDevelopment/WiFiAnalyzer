/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.channelgraph;

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiChannels;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Locale;

import static com.vrem.wifianalyzer.wifi.graphutils.GraphConstantsKt.MAX_Y;
import static com.vrem.wifianalyzer.wifi.graphutils.GraphConstantsKt.MIN_Y;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelAxisLabelTest {
    private Settings settings;
    private ChannelAxisLabel fixture;

    @Before
    public void setUp() {
        settings = MainContextHelper.INSTANCE.getSettings();
        when(this.settings.countryCode()).thenReturn(Locale.US.getCountry());

        fixture = new ChannelAxisLabel(WiFiBand.GHZ2, WiFiBand.GHZ2.getWiFiChannels().wiFiChannelPairs().get(0));
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testYAxis() {
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(MIN_Y, false));
        assertEquals("-99", fixture.formatLabel(MIN_Y + 1, false));
        assertEquals("0", fixture.formatLabel(MAX_Y, false));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(MAX_Y + 1, false));
    }

    @Test
    public void testXAxis() {
        // setup
        WiFiChannel wiFiChannel = WiFiBand.GHZ2.getWiFiChannels().wiFiChannelFirst();
        // execute
        String actual = fixture.formatLabel(wiFiChannel.getFrequency(), true);
        // validate
        assertEquals("" + wiFiChannel.getChannel(), actual);
        verify(settings).countryCode();
    }

    @Test
    public void testXAxisWithFrequencyInRange() {
        // setup
        WiFiChannel wiFiChannel = WiFiBand.GHZ2.getWiFiChannels().wiFiChannelFirst();
        // execute & validate
        assertEquals("" + wiFiChannel.getChannel(), fixture.formatLabel(wiFiChannel.getFrequency() - 2, true));
        assertEquals("" + wiFiChannel.getChannel(), fixture.formatLabel(wiFiChannel.getFrequency() + 2, true));
        verify(settings, times(2)).countryCode();
    }

    @Test
    public void testXAxisWithFrequencyNotAllowedInLocale() {
        // setup
        WiFiChannel wiFiChannel = WiFiBand.GHZ2.getWiFiChannels().wiFiChannelLast();
        // execute
        String actual = fixture.formatLabel(wiFiChannel.getFrequency(), true);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testXAxisWithUnknownFrequencyReturnEmptyString() {
        // setup
        WiFiChannels wiFiChannels = WiFiBand.GHZ2.getWiFiChannels();
        WiFiChannel wiFiChannel = wiFiChannels.wiFiChannelFirst();
        // execute
        String actual = fixture.formatLabel(wiFiChannel.getFrequency() - WiFiChannels.FREQUENCY_OFFSET, true);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

}