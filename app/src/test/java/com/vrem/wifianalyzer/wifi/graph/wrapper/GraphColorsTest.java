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

package com.vrem.wifianalyzer.wifi.graph.wrapper;

import org.junit.Before;
import org.junit.Test;

import static com.vrem.wifianalyzer.wifi.graph.wrapper.GraphColors.GRAPH_COLORS;
import static org.junit.Assert.assertEquals;

public class GraphColorsTest {
    private GraphColors fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new GraphColors();
    }

    @Test
    public void testGetColorStartsOverWhenEndIsReached() throws Exception {
        for (int i = GRAPH_COLORS.length - 1; i >= 0; i--) {
            assertEquals(GRAPH_COLORS[i], fixture.getColor());
        }
        assertEquals(GRAPH_COLORS[GRAPH_COLORS.length - 1], fixture.getColor());
    }

    @Test
    public void testAddColorAddsColorToAvailablePool() throws Exception {
        GraphColor expected = GRAPH_COLORS[GRAPH_COLORS.length - 1];
        assertEquals(expected, fixture.getColor());
        fixture.addColor(expected.getPrimary());
        assertEquals(expected, fixture.getColor());
    }

    @Test
    public void testAddColorDoesNotAddNonExistingColor() throws Exception {
        GraphColor graphColor = GRAPH_COLORS[GRAPH_COLORS.length - 1];
        assertEquals(graphColor, fixture.getColor());
        fixture.addColor(graphColor.getBackground());
        assertEquals(GRAPH_COLORS[GRAPH_COLORS.length - 2], fixture.getColor());
    }
}