/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.vrem.wifianalyzer.wifi.timegraph;

import com.vrem.wifianalyzer.wifi.graphutils.GraphConstants;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeAxisLabelTest {
    private TimeAxisLabel fixture;

    @Before
    public void setUp() {
        fixture = new TimeAxisLabel();
    }

    @Test
    public void testYAxis() {
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(GraphConstants.MIN_Y, false));
        assertEquals("-99", fixture.formatLabel(GraphConstants.MIN_Y + 1, false));
        assertEquals("0", fixture.formatLabel(GraphConstants.MAX_Y, false));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(GraphConstants.MAX_Y + 1, false));
    }

    @Test
    public void testXAxis() {
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(-2, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(-1, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(0, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(1, true));

        assertEquals("2", fixture.formatLabel(2, true));
        assertEquals("10", fixture.formatLabel(10, true));
    }

}