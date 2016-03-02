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
    private static final int CHANNEL = 1;

    @Mock
    private WiFiDetails wiFiDetails1;
    @Mock
    private WiFiDetails wiFiDetails2;
    @Mock
    private WiFiDetails wiFiDetails3;

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
        fixture.setWiFiChannels(withDetails());
        expectedChannels();
        // execute & validate
        assertEquals(3, fixture.getCount(CHANNEL));
        verifyChannels();
    }

    @Test
    public void testGetStrength() throws Exception {
        // setup
        fixture.setWiFiChannels(withDetails());
        expectedChannels();
        expectedDetails();
        // execute & validate
        assertEquals(wiFiDetails3.getStrength(), fixture.getStrength(CHANNEL));
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
        verify(wiFiDetails1).getChannelStart();
        verify(wiFiDetails1).getChannelEnd();
        verify(wiFiDetails2).getChannelStart();
        verify(wiFiDetails2).getChannelEnd();
        verify(wiFiDetails3).getChannelStart();
        verify(wiFiDetails3).getChannelEnd();
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
        when(wiFiDetails1.getChannelStart()).thenReturn(CHANNEL);
        when(wiFiDetails1.getChannelEnd()).thenReturn(CHANNEL);

        when(wiFiDetails2.getChannelStart()).thenReturn(CHANNEL);
        when(wiFiDetails2.getChannelEnd()).thenReturn(CHANNEL);

        when(wiFiDetails3.getChannelStart()).thenReturn(CHANNEL);
        when(wiFiDetails3.getChannelEnd()).thenReturn(CHANNEL);
    }

}