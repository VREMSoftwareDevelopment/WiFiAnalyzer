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

import com.jjoe64.graphview.LegendRenderer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GraphLegendTest {
    @Mock
    private LegendRenderer legendRenderer;

    @Test
    public void testSortByNumber() throws Exception {
        assertEquals(3, GraphLegend.values().length);
    }

    @Test
    public void testGetDisplay() throws Exception {
        assertTrue(GraphLegend.HIDE.getDisplay() instanceof GraphLegend.DisplayNone);
        assertTrue(GraphLegend.LEFT.getDisplay() instanceof GraphLegend.DisplayLeft);
        assertTrue(GraphLegend.RIGHT.getDisplay() instanceof GraphLegend.DisplayRight);
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(GraphLegend.HIDE, GraphLegend.find(GraphLegend.HIDE.ordinal(), GraphLegend.LEFT));
        assertEquals(GraphLegend.LEFT, GraphLegend.find(GraphLegend.LEFT.ordinal(), GraphLegend.RIGHT));
        assertEquals(GraphLegend.RIGHT, GraphLegend.find(GraphLegend.RIGHT.ordinal(), GraphLegend.LEFT));
    }

    @Test
    public void testFindWithInvalidIndex() throws Exception {
        assertEquals(GraphLegend.HIDE, GraphLegend.find(-1, GraphLegend.HIDE));
        assertEquals(GraphLegend.RIGHT, GraphLegend.find(-1, GraphLegend.RIGHT));
        assertEquals(GraphLegend.LEFT, GraphLegend.find(-1, GraphLegend.LEFT));
    }

    @Test
    public void testDisplayHide() throws Exception {
        // execute
        GraphLegend.HIDE.display(legendRenderer);
        // validate
        verify(legendRenderer).setVisible(false);
    }

    @Test
    public void testDisplayLeft() throws Exception {
        // execute
        GraphLegend.LEFT.display(legendRenderer);
        // validate
        verify(legendRenderer).setVisible(true);
        verify(legendRenderer).setFixedPosition(0, 0);
    }

    @Test
    public void testDisplayRight() throws Exception {
        // execute
        GraphLegend.RIGHT.display(legendRenderer);
        // validate
        verify(legendRenderer).setVisible(true);
        verify(legendRenderer).setAlign(LegendRenderer.LegendAlign.TOP);
    }

}