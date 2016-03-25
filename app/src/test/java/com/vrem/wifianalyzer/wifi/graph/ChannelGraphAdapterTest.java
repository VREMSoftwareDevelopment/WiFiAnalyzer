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

import android.support.annotation.NonNull;

import com.jjoe64.graphview.GraphView;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelGraphAdapterTest {
    @Mock private Scanner scanner;
    @Mock private ChannelGraphView channelGraphView2;
    @Mock
    private ChannelGraphView channelGraphView5;
    @Mock
    private GraphView graphView2;
    @Mock
    private GraphView graphView5;
    @Mock private WiFiData wifiData;

    private ChannelGraphAdapter fixture;

    @Before
    public void setUp() throws Exception {
        MainContext.INSTANCE.setScanner(scanner);

        fixture = new ChannelGraphAdapter() {
            @Override
            protected ChannelGraphView makeChannelGraphView(@NonNull WiFiBand wiFiBand) {
                if (WiFiBand.GHZ_5.equals(wiFiBand)) {
                    return channelGraphView5;
                }
                if (WiFiBand.GHZ_2.equals(wiFiBand)) {
                    return channelGraphView2;
                }
                throw new RuntimeException("Unknown WiFiBand: " + wiFiBand);
            }
        };
    }

    @Test
    public void testChannelGraphAdapterIsScannerConsumer() throws Exception {
        verify(scanner).addUpdateNotifier(fixture);
    }

    @Test
    public void testUpdate() throws Exception {
        // execute
        fixture.update(wifiData);
        // validate
        verify(channelGraphView2).update(wifiData);
        verify(channelGraphView5).update(wifiData);
    }

    @Test
    public void testGetGraphViews() throws Exception {
        // setup
        when(channelGraphView2.getGraphView()).thenReturn(graphView2);
        when(channelGraphView5.getGraphView()).thenReturn(graphView5);
        // execute
        List<GraphView> actual = fixture.getGraphViews();
        // validate
        assertEquals(WiFiBand.values().length, actual.size());
        assertEquals(graphView2, actual.get(0));
        assertEquals(graphView5, actual.get(1));
        verify(channelGraphView2).getGraphView();
        verify(channelGraphView5).getGraphView();
    }
}