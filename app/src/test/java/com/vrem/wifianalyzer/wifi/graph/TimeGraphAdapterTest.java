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

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TimeGraphAdapterTest {
    @Mock private Scanner scanner;
    @Mock private TimeGraphView timeGraphView1;
    @Mock private TimeGraphView timeGraphView2;
    @Mock private WiFiData wifiData;

    private TimeGraphAdapter fixture;

    @Before
    public void setUp() throws Exception {
        MainContext.INSTANCE.setScanner(scanner);

        fixture = new TimeGraphAdapter(timeGraphView1, timeGraphView2);
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
        verify(timeGraphView1).update(wifiData);
        verify(timeGraphView2).update(wifiData);
    }
}