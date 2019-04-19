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
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.vrem.wifianalyzer.settings.ThemeStyle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;
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
        fixture = new GraphViewBuilder(content, NUM_HORIZONTAL_LABELS, GraphConstants.MAX_Y_DEFAULT, ThemeStyle.DARK);
    }

    @Test
    public void testSetGraphView() {
        // setup
        ViewGroup.LayoutParams layoutParams = fixture.getLayoutParams();
        // execute
        fixture.setGraphView(graphView);
        // validate
        verify(graphView).setLayoutParams(layoutParams);
        verify(graphView).setVisibility(View.GONE);
    }

    @Test
    public void testSetViewPortY() {
        // setup
        when(graphView.getViewport()).thenReturn(viewport);
        // execute
        fixture.setViewPortY(graphView);
        // validate
        verify(graphView).getViewport();
        verify(viewport).setScrollable(true);
        verify(viewport).setYAxisBoundsManual(true);
        verify(viewport).setMinY(GraphConstants.MIN_Y);
        verify(viewport).setMaxY(GraphConstants.MAX_Y_DEFAULT);
        verify(viewport).setXAxisBoundsManual(true);
    }

    @Test
    public void testSetGridLabelRenderer() {
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
        verify(gridLabelRenderer).setTextSize(textSize * GraphConstants.TEXT_SIZE_ADJUSTMENT);
        verify(gridLabelRenderer).setVerticalLabelsVisible(true);
        verify(gridLabelRenderer).setHorizontalLabelsVisible(true);
        verify(gridLabelRenderer).reloadStyles();
    }

    @Test
    public void testSetGridLabelRendererWithLabelFormater() {
        // setup
        fixture.setLabelFormatter(labelFormatter);
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        // execute
        fixture.setGridLabelRenderer(graphView);
        // validate
        verify(gridLabelRenderer).setLabelFormatter(labelFormatter);
    }

    @Test
    public void testSetGridLabelRendererWithHorizontalLabelsVisible() {
        // setup
        fixture.setHorizontalLabelsVisible(false);
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        // execute
        fixture.setGridLabelRenderer(graphView);
        // validate
        verify(gridLabelRenderer).setHorizontalLabelsVisible(false);
    }

    @Test
    public void testSetGridLabelRendererWithNoLabelFormater() {
        // setup
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        // execute
        fixture.setGridLabelRenderer(graphView);
        // validate
        verify(gridLabelRenderer, never()).setLabelFormatter(any(LabelFormatter.class));
    }

    @Test
    public void testSetGridLabelRendererWithVerticalAxisTitle() {
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
        verify(gridLabelRenderer).setVerticalAxisTitleTextSize(textSize * GraphConstants.AXIS_TEXT_SIZE_ADJUSTMENT);
    }

    @Test
    public void testSetGridLabelRendererNoVerticalAxisTitle() {
        // setup
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        // execute
        fixture.setGridLabelRenderer(graphView);
        // validate
        verify(gridLabelRenderer, never()).setVerticalAxisTitle(anyString());
        verify(gridLabelRenderer, never()).setVerticalAxisTitleTextSize(anyFloat());
    }

    @Test
    public void testSetGridLabelRendererWithHorizontalAxisTitle() {
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
        verify(gridLabelRenderer).setHorizontalAxisTitleTextSize(textSize * GraphConstants.AXIS_TEXT_SIZE_ADJUSTMENT);
    }

    @Test
    public void testSetGridLabelRendererNoHorizontalAxisTitle() {
        // setup
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        // execute
        fixture.setGridLabelRenderer(graphView);
        // validate
        verify(gridLabelRenderer, never()).setHorizontalAxisTitle(anyString());
        verify(gridLabelRenderer, never()).setHorizontalAxisTitleTextSize(anyFloat());
    }

    @Test
    public void testGetNumVerticalLabels() {
        // setup
        int expected = 9;
        // execute
        int actual = fixture.getNumVerticalLabels();
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumYLimits() {
        validateMaximumY(content, 1, GraphConstants.MAX_Y_DEFAULT);
        validateMaximumY(content, 0, 0);
        validateMaximumY(content, -50, -50);
        validateMaximumY(content, -51, GraphConstants.MAX_Y_DEFAULT);
    }

    private void validateMaximumY(Context content, int maximumY, int expected) {
        fixture = new GraphViewBuilder(content, NUM_HORIZONTAL_LABELS, maximumY, ThemeStyle.DARK);
        assertEquals(expected, fixture.getMaximumY());
    }

    @Test
    public void testSetGridLabelRenderColorsWithDarkTheme() {
        // setup
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        // execute
        fixture.setGridLabelRenderer(graphView);
        // validate
        verify(gridLabelRenderer).setGridColor(Color.GRAY);
        verify(gridLabelRenderer).setVerticalLabelsColor(Color.WHITE);
        verify(gridLabelRenderer).setVerticalAxisTitleColor(Color.WHITE);
        verify(gridLabelRenderer).setHorizontalLabelsColor(Color.WHITE);
        verify(gridLabelRenderer).setHorizontalAxisTitleColor(Color.WHITE);
    }

    @Test
    public void testSetGridLabelRenderColorsWithLightTheme() {
        // setup
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        fixture = new GraphViewBuilder(content, NUM_HORIZONTAL_LABELS, GraphConstants.MAX_Y_DEFAULT, ThemeStyle.LIGHT);
        // execute
        fixture.setGridLabelRenderer(graphView);
        // validate
        verify(gridLabelRenderer).setGridColor(Color.GRAY);
        verify(gridLabelRenderer).setVerticalLabelsColor(Color.BLACK);
        verify(gridLabelRenderer).setVerticalAxisTitleColor(Color.BLACK);
        verify(gridLabelRenderer).setHorizontalLabelsColor(Color.BLACK);
        verify(gridLabelRenderer).setHorizontalAxisTitleColor(Color.BLACK);
    }
}