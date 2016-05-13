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

package com.vrem.wifianalyzer.wifi.graph.tools;

import com.jjoe64.graphview.GraphView;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GraphAdapterTest {
    private Scanner scanner;
    @Mock
    private GraphViewNotifier graphViewNotifier;
    @Mock
    private GraphView graphView;
    @Mock
    private WiFiData wifiData;

    private GraphAdapter fixture;

    @Before
    public void setUp() throws Exception {
        scanner = MainContextHelper.INSTANCE.getScanner();

        fixture = new GraphAdapter(Arrays.asList(graphViewNotifier));
    }

    @After
    public void tearDown() throws Exception {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testTimeGraphAdapterIsScannerConsumer() throws Exception {
        verify(scanner).addUpdateNotifier(fixture);
    }

    @Test
    public void testUpdate() throws Exception {
        // execute
        fixture.update(wifiData);
        // validate
        verify(graphViewNotifier).update(wifiData);
    }

    @Test
    public void testGetGraphViews() throws Exception {
        // setup
        when(graphViewNotifier.getGraphView()).thenReturn(graphView);
        // execute
        List<GraphView> actual = fixture.getGraphViews();
        // validate
        assertEquals(1, actual.size());
        assertEquals(graphView, actual.get(0));
        verify(graphViewNotifier).getGraphView();
    }
    
}