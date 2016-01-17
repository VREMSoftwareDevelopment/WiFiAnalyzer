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
package com.vrem.wifianalyzer.wifi.channelgraph;

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
public class AxisLabelTest {
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

    @Test(expected = IllegalArgumentException.class)
    public void testAxisLabelNotSupportedWiFiBand() throws Exception {
        Constraints constraints = new Constraints(WiFiBand.ALL);
        AxisLabel fixture = new AxisLabel(constraints);
    }

    @Test
    public void testFormatLabelYAxis() throws Exception {
        // setup
        Constraints constraints = new Constraints(WiFiBand.TWO_POINT_FOUR);
        AxisLabel fixture = new AxisLabel(constraints);
        // execute & validate
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(WiFiConstants.MIN_Y, false));
        assertEquals("-99", fixture.formatLabel(WiFiConstants.MIN_Y + 1, false));
        assertEquals("-10", fixture.formatLabel(WiFiConstants.MAX_Y, false));

    }

    @Test
    public void testFormatLabelXAxis24WithPortrait() throws Exception {
        // setup
        configuration.orientation = Configuration.ORIENTATION_PORTRAIT;
        Constraints constraints = new Constraints(WiFiBand.TWO_POINT_FOUR);
        AxisLabel fixture = new AxisLabel(constraints);
        // execute & validate
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(constraints.minX() - 1, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(constraints.maxX() + 1, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(11, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(13, true));

        assertEquals("1", fixture.formatLabel(constraints.minX(), true));
        assertEquals("14", fixture.formatLabel(constraints.maxX(), true));
        assertEquals("9", fixture.formatLabel(9, true));
        assertEquals("10", fixture.formatLabel(10, true));
        assertEquals("12", fixture.formatLabel(12, true));
    }

    @Test
    public void testFormatLabelXAxis24WithLandscape() throws Exception {
        // setup
        configuration.orientation = Configuration.ORIENTATION_LANDSCAPE;
        Constraints constraints = new Constraints(WiFiBand.TWO_POINT_FOUR);
        AxisLabel fixture = new AxisLabel(constraints);
        // execute & validate
        assertEquals("11", fixture.formatLabel(11, true));
        assertEquals("13", fixture.formatLabel(13, true));
    }

    @Test
    public void testFormatLabelXAxis5WithPortrait() throws Exception {
        // setup
        configuration.orientation = Configuration.ORIENTATION_PORTRAIT;
        Constraints constraints = new Constraints(WiFiBand.FIVE);
        AxisLabel fixture = new AxisLabel(constraints);
        // execute & validate
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(constraints.minX() - 1, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(constraints.maxX() + 1, true));

        assertEquals("34", fixture.formatLabel(constraints.minX(), true));
        assertEquals("36", fixture.formatLabel(36, true));

        assertEquals(StringUtils.EMPTY, fixture.formatLabel(35, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(99, true));

        assertEquals("165", fixture.formatLabel(constraints.maxX(), true));
        assertEquals("162", fixture.formatLabel(162, true));

        assertEquals(StringUtils.EMPTY, fixture.formatLabel(163, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(164, true));
    }

    @Test
    public void testFormatLabelXAxis5WithLandscape() throws Exception {
        // setup
        configuration.orientation = Configuration.ORIENTATION_LANDSCAPE;
        Constraints constraints = new Constraints(WiFiBand.FIVE);
        AxisLabel fixture = new AxisLabel(constraints);
        // execute & validate
        assertEquals("35", fixture.formatLabel(35, true));
        assertEquals("99", fixture.formatLabel(99, true));

        assertEquals("163", fixture.formatLabel(163, true));
        assertEquals("164", fixture.formatLabel(164, true));
    }

}