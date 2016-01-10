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

import com.vrem.wifianalyzer.R;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelRatingTest {
    private final static int CHANNEL = 1;

    @Mock
    private DetailsInfo detailsInfo1;
    @Mock
    private DetailsInfo detailsInfo2;
    @Mock
    private DetailsInfo detailsInfo3;

    private ChannelRating fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new ChannelRating();
    }


    @Test
    public void testChannelRating() throws Exception {
        assertEquals(0, fixture.getCount(CHANNEL));
        assertEquals(R.color.success_color, fixture.getColor(CHANNEL));
    }

    @Test
    public void testGetAPCount() throws Exception {
        // setup
        Map<Integer, List<DetailsInfo>> details = withDetails();
        int expected = details.get(CHANNEL).size() - 1;
        fixture.setWiFiChannels(details);
        // execute
        int actual = fixture.getAPCount(CHANNEL);
        // validate
        assertEquals(expected, actual);
        verifyDetails();
    }

    @Test
    public void testGetCount() throws Exception {
        // setup
        Map<Integer, List<DetailsInfo>> details = withDetails();
        int expected = 1;
        fixture.setWiFiChannels(details);
        // execute
        int actual = fixture.getCount(CHANNEL);
        // validate
        assertEquals(expected, actual);
        verifyDetails();
    }

    @Test
    public void testGetColorWarning() throws Exception {
        // setup
        Map<Integer, List<DetailsInfo>> details = withDetails();
        fixture.setWiFiChannels(details);
        // execute
        int actual = fixture.getColor(CHANNEL);
        // validate
        assertEquals(R.color.warning_color, actual);
        verifyDetails();
    }

    @Test
    public void testGetColorError() throws Exception {
        // setup
        Map<Integer, List<DetailsInfo>> details = withDetails();
        withAdditionalDetails(details);
        fixture.setWiFiChannels(details);
        // execute
        int actual = fixture.getColor(CHANNEL);
        // validate
        assertEquals(R.color.error_color, actual);
        verifyDetails();
        verifyAdditionalDetails();
    }

    private void verifyAdditionalDetails() {
        verify(detailsInfo3).isConnected();
        verify(detailsInfo3).getStrength();
    }

    private void withAdditionalDetails(Map<Integer, List<DetailsInfo>> details) {
        List<DetailsInfo> detailsInfos = details.get(CHANNEL);
        detailsInfos.add(detailsInfo3);
        when(detailsInfo3.getStrength()).thenReturn(Strength.TWO);
    }

    private void verifyDetails() {
        verify(detailsInfo1).isConnected();
        verify(detailsInfo2).isConnected();

        verify(detailsInfo1).getStrength();
        verify(detailsInfo2, never()).getStrength();
    }

    private Map<Integer, List<DetailsInfo>> withDetails() {
        when(detailsInfo1.isConnected()).thenReturn(false);
        when(detailsInfo2.isConnected()).thenReturn(true);

        when(detailsInfo1.getStrength()).thenReturn(Strength.TWO);

        Map<Integer, List<DetailsInfo>> results = new TreeMap<>();
        results.put(CHANNEL, new ArrayList<>(Arrays.asList(new DetailsInfo[]{detailsInfo1, detailsInfo2})));
        return results;
    }

}