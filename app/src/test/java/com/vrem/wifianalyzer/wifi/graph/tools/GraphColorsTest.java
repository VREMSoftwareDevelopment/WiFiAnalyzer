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

import android.content.res.Resources;

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GraphColorsTest {
    private final String[] colors = new String[]{"#FB1554", "#33FB1554", "#74FF89", "#3374FF89", "#8B1EFC", "#338B1EFC"};
    private final GraphColor[] graphColors = new GraphColor[]{
            new GraphColor(0xFB1554, 0x33FB1554),
            new GraphColor(0x74FF89, 0x3374FF89),
            new GraphColor(0x8B1EFC, 0x338B1EFC)
    };
    private Resources resources;
    private GraphColors fixture;

    @Before
    public void setUp() throws Exception {
        resources = MainContextHelper.INSTANCE.getResources();
        when(resources.getStringArray(R.array.graph_colors)).thenReturn(colors);
        fixture = new GraphColors();
    }

    @After
    public void tearDown() throws Exception {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testGetColorStartsOverWhenEndIsReached() throws Exception {
        assertEquals(graphColors[2], fixture.getColor());
        assertEquals(graphColors[1], fixture.getColor());
        assertEquals(graphColors[0], fixture.getColor());
        assertEquals(graphColors[2], fixture.getColor());
    }

    @Test
    public void testAddColorAddsColorToAvailablePool() throws Exception {
        GraphColor expected = graphColors[2];
        assertEquals(expected, fixture.getColor());
        fixture.addColor(expected.getPrimary());
        assertEquals(expected, fixture.getColor());
    }

    @Test
    public void testAddColorDoesNotAddNonExistingColor() throws Exception {
        GraphColor expected = graphColors[1];
        GraphColor graphColor = graphColors[2];
        assertEquals(graphColor, fixture.getColor());
        fixture.addColor(graphColor.getBackground());
        assertEquals(expected, fixture.getColor());
    }
}
