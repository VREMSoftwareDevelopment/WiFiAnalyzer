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

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.WiFiConstants;
import com.vrem.wifianalyzer.wifi.model.WiFiBand;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelGraphAxisLabelTest {
    @Mock
    private GraphView graphView;
    @Mock
    private LegendRenderer legendRenderer;
    @Mock
    private Context context;
    @Mock
    private Resources resources;
    @Mock
    private Configuration configuration;

    @Before
    public void setUp() throws Exception {
        MainContext.INSTANCE.setContext(context);
        when(context.getResources()).thenReturn(resources);
        when(resources.getConfiguration()).thenReturn(configuration);
    }

    @Test
    public void testFormatLabelYAxis() throws Exception {
        // setup
        Constraints constraints = new Constraints(WiFiBand.TWO);
        ChannelGraphAxisLabel fixture = new ChannelGraphAxisLabel(constraints);
        // execute & validate
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(WiFiConstants.MIN_Y, false));
        assertEquals("-99", fixture.formatLabel(WiFiConstants.MIN_Y + 1, false));
        assertEquals("-10", fixture.formatLabel(WiFiConstants.MAX_Y, false));

    }

    @Test
    public void testFormatLabelXAxis2() throws Exception {
        // setup
        Constraints constraints = new Constraints(WiFiBand.TWO);
        ChannelGraphAxisLabel fixture = new ChannelGraphAxisLabel(constraints);
        // execute & validate
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(constraints.channelFirst() - 1, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(constraints.channelLast() + 1, true));

        assertEquals("1", fixture.formatLabel(constraints.channelFirst(), true));
        assertEquals("14", fixture.formatLabel(constraints.channelLast(), true));
    }

    @Test
    public void testFormatLabelXAxis5() throws Exception {
        // setup
        Constraints constraints = new Constraints(WiFiBand.FIVE);
        ChannelGraphAxisLabel fixture = new ChannelGraphAxisLabel(constraints);
        // execute & validate
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(constraints.channelFirst() - 1, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(constraints.channelLast() + 1, true));

        assertEquals("36", fixture.formatLabel(constraints.channelFirst(), true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(constraints.channelLast(), true));

        assertEquals("164", fixture.formatLabel(164, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(163, true));
    }
}