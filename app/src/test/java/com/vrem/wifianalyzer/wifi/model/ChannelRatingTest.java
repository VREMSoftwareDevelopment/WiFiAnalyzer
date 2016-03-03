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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelRatingTest {
    private static final int CHANNEL1 = 1;
    private static final int CHANNEL2 = 2;
    private static final int CHANNEL3 = 3;

    @Mock private WiFiDetails wiFiDetails1;
    @Mock private WiFiDetails wiFiDetails2;
    @Mock private WiFiDetails wiFiDetails3;

    @Mock private WiFiFrequency wiFiFrequency1;
    @Mock private WiFiFrequency wiFiFrequency2;
    @Mock private WiFiFrequency wiFiFrequency3;

    private ChannelRating fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new ChannelRating();
    }

    @Test
    public void testChannelRating() throws Exception {
        assertEquals(0, fixture.getCount(CHANNEL1));
        assertEquals(Strength.ZERO, fixture.getStrength(CHANNEL1));
    }

    @Test
    public void testGetCountChannel1() throws Exception {
        // setup
        fixture.setWiFiChannels(withDetails());
        expectedChannels();
        // execute & validate
        assertEquals(1, fixture.getCount(CHANNEL1));
        verifyChannels();
    }

    @Test
    public void testGetCountChannel2() throws Exception {
        // setup
        fixture.setWiFiChannels(withDetails());
        expectedChannels();
        // execute & validate
        assertEquals(2, fixture.getCount(CHANNEL2));
        verifyChannels();
    }

    @Test
    public void testGetCountChannel3() throws Exception {
        // setup
        fixture.setWiFiChannels(withDetails());
        expectedChannels();
        // execute & validate
        assertEquals(3, fixture.getCount(CHANNEL3));
        verifyChannels();
    }

    @Test
    public void testGetStrength() throws Exception {
        // setup
        fixture.setWiFiChannels(withDetails());
        expectedChannels();
        expectedDetails();
        // execute & validate
        assertEquals(wiFiDetails3.getStrength(), fixture.getStrength(CHANNEL3));
        verifyChannels();
        verifyDetails();
    }

    private void verifyDetails() {
        verify(wiFiDetails1).isConnected();
        verify(wiFiDetails2).isConnected();
        verify(wiFiDetails3).isConnected();

        verify(wiFiDetails1).getStrength();
        verify(wiFiDetails2, never()).getStrength();
        verify(wiFiDetails3, times(2)).getStrength();
    }

    private void verifyChannels() {
        verify(wiFiDetails1).getWiFiFrequency();
        verify(wiFiDetails2).getWiFiFrequency();
        verify(wiFiDetails3).getWiFiFrequency();
    }

    private List<WiFiDetails> withDetails() {
        return new ArrayList<>(Arrays.asList(new WiFiDetails[]{wiFiDetails1, wiFiDetails2, wiFiDetails3}));
    }

    private void expectedDetails() {
        when(wiFiDetails1.isConnected()).thenReturn(false);
        when(wiFiDetails2.isConnected()).thenReturn(true);
        when(wiFiDetails3.isConnected()).thenReturn(false);

        when(wiFiDetails1.getStrength()).thenReturn(Strength.ONE);
        when(wiFiDetails2.getStrength()).thenReturn(Strength.FOUR);
        when(wiFiDetails3.getStrength()).thenReturn(Strength.THREE);
    }

    private void expectedChannels() {
        when(wiFiDetails1.getWiFiFrequency()).thenReturn(wiFiFrequency1);
        when(wiFiDetails2.getWiFiFrequency()).thenReturn(wiFiFrequency2);
        when(wiFiDetails3.getWiFiFrequency()).thenReturn(wiFiFrequency3);

        when(wiFiFrequency1.getChannelStart()).thenReturn(CHANNEL1);
        when(wiFiFrequency1.getChannelEnd()).thenReturn(CHANNEL3);

        when(wiFiFrequency2.getChannelStart()).thenReturn(CHANNEL2);
        when(wiFiFrequency2.getChannelEnd()).thenReturn(CHANNEL3);

        when(wiFiFrequency3.getChannelStart()).thenReturn(CHANNEL3);
        when(wiFiFrequency3.getChannelEnd()).thenReturn(CHANNEL3);
    }

}