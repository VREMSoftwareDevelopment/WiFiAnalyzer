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

package com.vrem.wifianalyzer.wifi.graphutils;

import android.content.Context;
import android.content.res.Resources;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

    @Mock
    private Resources resources;
    @Mock
    private Context context;

    private GraphColors fixture;

    @Before
    public void setUp() {
        MainActivity mainActivity = MainContextHelper.INSTANCE.getMainActivity();
        when(mainActivity.getApplicationContext()).thenReturn(context);
        when(context.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.graph_colors)).thenReturn(colors);

        fixture = new GraphColors();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testGetColorStartsOverWhenEndIsReached() {
        assertEquals(graphColors[2], fixture.getColor());
        assertEquals(graphColors[1], fixture.getColor());
        assertEquals(graphColors[0], fixture.getColor());
        assertEquals(graphColors[2], fixture.getColor());
    }

    @Test
    public void testAddColorAddsColorToAvailablePool() {
        GraphColor expected = graphColors[2];
        assertEquals(expected, fixture.getColor());
        fixture.addColor(expected.getPrimary());
        assertEquals(expected, fixture.getColor());
    }

    @Test
    public void testAddColorDoesNotAddNonExistingColor() {
        GraphColor expected = graphColors[1];
        GraphColor graphColor = graphColors[2];
        assertEquals(graphColor, fixture.getColor());
        fixture.addColor(graphColor.getBackground());
        assertEquals(expected, fixture.getColor());
    }
}
