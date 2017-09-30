/*
 * WiFiAnalyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GraphViewBuilderTest {
    private static final int NUM_HORIZONTAL_LABELS = 5;

    @Mock
    private GridLabelRenderer gridLabelRenderer;
    @Mock
    private Context content;
    @Mock
    private GraphView graphView;
    @Mock
    private Viewport viewport;
    @Mock
    private LabelFormatter labelFormatter;

    private GraphViewBuilder fixture;

    @Before
    public void setUp() {
        fixture = new GraphViewBuilder(content, NUM_HORIZONTAL_LABELS, GraphViewBuilder.MAX_Y_DEFAULT);
    }

    @Test
    public void testSetGraphView() throws Exception {
        // setup
        ViewGroup.LayoutParams layoutParams = fixture.getLayoutParams();
        // execute
        fixture.setGraphView(graphView);
        // validate
        verify(graphView).setLayoutParams(layoutParams);
        verify(graphView).setVisibility(View.GONE);
    }

    @Test
    public void testSetViewPortY() throws Exception {
        // setup
        when(graphView.getViewport()).thenReturn(viewport);
        // execute
        fixture.setViewPortY(graphView);
        // validate
        verify(graphView).getViewport();
        verify(viewport).setScrollable(true);
        verify(viewport).setYAxisBoundsManual(true);
        verify(viewport).setMinY(GraphViewBuilder.MIN_Y);
        verify(viewport).setMaxY(GraphViewBuilder.MAX_Y_DEFAULT);
        verify(viewport).setXAxisBoundsManual(true);
    }

    @Test
    public void testSetGridLabelRenderer() throws Exception {
        // setup
        float textSize = 11f;
        int numVerticalLabels = fixture.getNumVerticalLabels();
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        when(gridLabelRenderer.getTextSize()).thenReturn(textSize);
        // execute
        fixture.setGridLabelRenderer(graphView);
        // validate
        verify(graphView).getGridLabelRenderer();
        verify(gridLabelRenderer).setHumanRounding(false);
        verify(gridLabelRenderer).setHighlightZeroLines(false);
        verify(gridLabelRenderer).setNumVerticalLabels(numVerticalLabels);
        verify(gridLabelRenderer).setNumHorizontalLabels(NUM_HORIZONTAL_LABELS);
        verify(gridLabelRenderer).setTextSize(textSize * GraphViewBuilder.TEXT_SIZE_ADJUSTMENT);
        verify(gridLabelRenderer).setVerticalLabelsVisible(true);
        verify(gridLabelRenderer).setHorizontalLabelsVisible(true);
        verify(gridLabelRenderer).reloadStyles();
    }

    @Test
    public void testSetGridLabelRendererWithLabelFormater() throws Exception {
        // setup
        fixture.setLabelFormatter(labelFormatter);
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        // execute
        fixture.setGridLabelRenderer(graphView);
        // validate
        verify(gridLabelRenderer).setLabelFormatter(labelFormatter);
    }

    @Test
    public void testSetGridLabelRendererWithHorizontalLabelsVisible() throws Exception {
        // setup
        fixture.setHorizontalLabelsVisible(false);
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        // execute
        fixture.setGridLabelRenderer(graphView);
        // validate
        verify(gridLabelRenderer).setHorizontalLabelsVisible(false);
    }

    @Test
    public void testSetGridLabelRendererWithNoLabelFormater() throws Exception {
        // setup
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        // execute
        fixture.setGridLabelRenderer(graphView);
        // validate
        verify(gridLabelRenderer, never()).setLabelFormatter(any(LabelFormatter.class));
    }

    @Test
    public void testSetGridLabelRendererWithVerticalAxisTitle() throws Exception {
        // setup
        float textSize = 11f;
        String verticalTitle = "verticalTitle";
        fixture.setVerticalTitle(verticalTitle);
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        when(gridLabelRenderer.getVerticalAxisTitleTextSize()).thenReturn(textSize);
        // execute
        fixture.setGridLabelRenderer(graphView);
        // validate
        verify(gridLabelRenderer).setVerticalAxisTitle(verticalTitle);
        verify(gridLabelRenderer).setVerticalLabelsVisible(true);
        verify(gridLabelRenderer).setVerticalAxisTitleTextSize(textSize * GraphViewBuilder.AXIS_TEXT_SIZE_ADJUSTMENT);
    }

    @Test
    public void testSetGridLabelRendererNoVerticalAxisTitle() throws Exception {
        // setup
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        // execute
        fixture.setGridLabelRenderer(graphView);
        // validate
        verify(gridLabelRenderer, never()).setVerticalAxisTitle(anyString());
        verify(gridLabelRenderer, never()).setVerticalAxisTitleTextSize(anyFloat());
    }

    @Test
    public void testSetGridLabelRendererWithHorizontalAxisTitle() throws Exception {
        // setup
        float textSize = 11f;
        String horizontalTitle = "horizontalTitle";
        fixture.setHorizontalTitle(horizontalTitle);
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        when(gridLabelRenderer.getHorizontalAxisTitleTextSize()).thenReturn(textSize);
        // execute
        fixture.setGridLabelRenderer(graphView);
        // validate
        verify(gridLabelRenderer).setHorizontalAxisTitle(horizontalTitle);
        verify(gridLabelRenderer).setHorizontalLabelsVisible(true);
        verify(gridLabelRenderer).setHorizontalAxisTitleTextSize(textSize * GraphViewBuilder.AXIS_TEXT_SIZE_ADJUSTMENT);
    }

    @Test
    public void testSetGridLabelRendererNoHorizontalAxisTitle() throws Exception {
        // setup
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        // execute
        fixture.setGridLabelRenderer(graphView);
        // validate
        verify(gridLabelRenderer, never()).setHorizontalAxisTitle(anyString());
        verify(gridLabelRenderer, never()).setHorizontalAxisTitleTextSize(anyFloat());
    }

    @Test
    public void testGetNumVerticalLabels() throws Exception {
        // setup
        int expected = 9;
        // execute
        int actual = fixture.getNumVerticalLabels();
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumYLimits() throws Exception {
        validateMaximumY(content, 1, GraphViewBuilder.MAX_Y_DEFAULT);
        validateMaximumY(content, 0, 0);
        validateMaximumY(content, -50, -50);
        validateMaximumY(content, -51, GraphViewBuilder.MAX_Y_DEFAULT);
    }

    private void validateMaximumY(Context content, int maximumY, int expected) {
        fixture = new GraphViewBuilder(content, NUM_HORIZONTAL_LABELS, maximumY);
        assertEquals(expected, fixture.getMaximumY());
    }

}