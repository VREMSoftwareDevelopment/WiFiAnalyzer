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

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AxisLabelTest {
    private final static int MIN_X = 2;
    private final static int MAX_X = 5;
    private AxisLabel fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AxisLabel(MIN_X, MAX_X);
    }

    @Test
    public void testYAxis() throws Exception {
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(GraphViewBuilder.MIN_Y, false));
        assertEquals("-99", fixture.formatLabel(GraphViewBuilder.MIN_Y + 1, false));

        assertEquals("-10", fixture.formatLabel(GraphViewBuilder.MAX_Y, false));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(GraphViewBuilder.MAX_Y + 1, false));
    }

    @Test
    public void testXAxis() throws Exception {
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(MIN_X - 1, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(MAX_X + 1, true));

        assertEquals(""+ MIN_X, fixture.formatLabel(MIN_X, true));
        assertEquals(""+((MIN_X + MAX_X)/2), fixture.formatLabel(((MIN_X + MAX_X)/2), true));
        assertEquals(""+ MAX_X, fixture.formatLabel(MAX_X, true));
    }

    @Test
    public void testXAxisEven() throws Exception {
        fixture.setEvenOnly(true);

        assertEquals("" + MIN_X, fixture.formatLabel(MIN_X, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(MAX_X, true));
    }
}