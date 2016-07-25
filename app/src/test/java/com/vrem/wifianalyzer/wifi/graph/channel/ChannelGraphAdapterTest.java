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

import com.jjoe64.graphview.GraphView;
import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphViewNotifier;
import com.vrem.wifianalyzer.wifi.model.WiFiConnection;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChannelGraphAdapterTest {

    private Scanner scanner;
    private ChannelGraphNavigation channelGraphNavigation;
    private ChannelGraphAdapter fixture;

    @Before
    public void setUp() throws Exception {
        RobolectricUtil.INSTANCE.getMainActivity();

        scanner = MainContextHelper.INSTANCE.getScanner();

        channelGraphNavigation = mock(ChannelGraphNavigation.class);

        fixture = new ChannelGraphAdapter(channelGraphNavigation);
    }

    @After
    public void tearDown() throws Exception {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testChannelGraphAdapter() throws Exception {
        verify(scanner).addUpdateNotifier(fixture);
    }

    @Test
    public void testGetGraphViewNotifiers() throws Exception {
        // setup
        int expected = expectedCount();
        // execute
        List<GraphViewNotifier> graphViewNotifiers = fixture.getGraphViewNotifiers();
        // validate
        assertEquals(expected, graphViewNotifiers.size());
    }

    private int expectedCount() {
        int expected = 0;
        for (WiFiBand wiFiBand : WiFiBand.values()) {
            expected += wiFiBand.getWiFiChannels().getWiFiChannelPairs().size();
        }
        return expected;
    }

    @Test
    public void testGetGraphViews() throws Exception {
        // setup
        int expected = expectedCount();
        // execute
        List<GraphView> graphViews = fixture.getGraphViews();
        // validate
        assertEquals(expected, graphViews.size());
    }

    @Test
    public void testUpdate() throws Exception {
        // setup
        WiFiData wiFiData = new WiFiData(new ArrayList<WiFiDetail>(), WiFiConnection.EMPTY, new ArrayList<String>());
        // execute
        fixture.update(wiFiData);
        // validate
        verify(channelGraphNavigation).update();
    }

}