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

package com.vrem.wifianalyzer.wifi.graph.time;

import com.jjoe64.graphview.GraphView;
import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphViewNotifier;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TimeGraphAdapterTest {

    private Scanner scanner;
    private TimeGraphAdapter fixture;

    @Before
    public void setUp() throws Exception {
        RobolectricUtil.INSTANCE.getMainActivity();

        scanner = MainContextHelper.INSTANCE.getScanner();

        fixture = new TimeGraphAdapter();
    }

    @After
    public void tearDown() throws Exception {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testTimeGraphAdapter() throws Exception {
        verify(scanner).addUpdateNotifier(fixture);
    }

    @Test
    public void testGetGraphViewNotifiers() throws Exception {
        // execute
        List<GraphViewNotifier> graphViewNotifiers = fixture.getGraphViewNotifiers();
        // validate
        assertEquals(WiFiBand.values().length, graphViewNotifiers.size());
    }

    @Test
    public void testGetGraphViews() throws Exception {
        // execute
        List<GraphView> graphViews = fixture.getGraphViews();
        // validate
        assertEquals(WiFiBand.values().length, graphViews.size());
    }

}