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
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelRatingTest {
    private static final int CHANNEL = 1;

    @Mock
    private WiFiDetails wifiDetails1;
    @Mock
    private WiFiDetails wifiDetails2;
    @Mock
    private WiFiDetails wifiDetails3;

    private ChannelRating fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new ChannelRating();
    }

    @Test
    public void testChannelRating() throws Exception {
        assertEquals(0, fixture.getCount(CHANNEL));
        assertEquals(Strength.ZERO, fixture.getStrength(CHANNEL));
    }

    @Test
    public void testGetCount() throws Exception {
        // setup
        Map<Integer, List<WiFiDetails>> details = withDetails();
        fixture.setWiFiChannels(details);
        // execute & validate
        assertEquals(details.get(CHANNEL).size(), fixture.getCount(CHANNEL));
    }

    @Test
    public void testGetStrength() throws Exception {
        // setup
        Map<Integer, List<WiFiDetails>> details = withDetails();
        fixture.setWiFiChannels(details);
        expectedDetails();
        // execute & validate
        assertEquals(wifiDetails3.getStrength(), fixture.getStrength(CHANNEL));
        verifyDetails();
    }

    private void verifyDetails() {
        verify(wifiDetails1).isConnected();
        verify(wifiDetails2).isConnected();
        verify(wifiDetails3).isConnected();

        verify(wifiDetails1).getStrength();
        verify(wifiDetails2, never()).getStrength();
        verify(wifiDetails3, times(2)).getStrength();
    }

    private Map<Integer, List<WiFiDetails>> withDetails() {
        Map<Integer, List<WiFiDetails>> results = new TreeMap<>();
        results.put(CHANNEL, new ArrayList<>(Arrays.asList(new WiFiDetails[]{wifiDetails1, wifiDetails2, wifiDetails3})));
        return results;
    }

    private void expectedDetails() {
        when(wifiDetails1.isConnected()).thenReturn(false);
        when(wifiDetails2.isConnected()).thenReturn(true);
        when(wifiDetails3.isConnected()).thenReturn(false);

        when(wifiDetails1.getStrength()).thenReturn(Strength.ONE);
        when(wifiDetails2.getStrength()).thenReturn(Strength.FOUR);
        when(wifiDetails3.getStrength()).thenReturn(Strength.THREE);
    }

}