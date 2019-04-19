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

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.TitleLineGraphSeries;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SeriesOptionsTest {
    @Mock
    private GraphColors graphColors;
    @Mock
    private GraphColor graphColor;
    @Mock
    private LineGraphSeries<DataPoint> lineGraphSeries;
    @Mock
    private TitleLineGraphSeries<DataPoint> titleLineGraphSeries;

    private SeriesOptions fixture;

    @Before
    public void setUp() {
        fixture = new SeriesOptions();
        fixture.setGraphColors(graphColors);
    }

    @Test
    public void testRemoveSeries() {
        // setup
        int color = 10;
        when(lineGraphSeries.getColor()).thenReturn(color);
        // execute
        fixture.removeSeriesColor(lineGraphSeries);
        // validate
        verify(graphColors).addColor(color);
    }

    @Test
    public void testHighlightConnectedLineGraphSeriesSetsConnectedThickness() {
        // execute
        fixture.highlightConnected(lineGraphSeries, true);
        // validate
        verify(lineGraphSeries).setThickness(GraphConstants.THICKNESS_CONNECTED);
    }

    @Test
    public void testHighlightConnectedLineGraphSeriesSetsNotConnectedThickness() {
        // execute
        fixture.highlightConnected(lineGraphSeries, false);
        // validate
        verify(lineGraphSeries).setThickness(GraphConstants.THICKNESS_REGULAR);
    }

    @Test
    public void testHighlightConnectedTitleLineGraphSeriesSetsConnectedThickness() {
        // execute
        fixture.highlightConnected(titleLineGraphSeries, true);
        // validate
        verify(titleLineGraphSeries).setThickness(GraphConstants.THICKNESS_CONNECTED);
        verify(titleLineGraphSeries).setTextBold(true);
    }

    @Test
    public void testHighlightConnectedTitleLineGraphSeriesSetsNotConnectedThickness() {
        // execute
        fixture.highlightConnected(titleLineGraphSeries, false);
        // validate
        verify(titleLineGraphSeries).setThickness(GraphConstants.THICKNESS_REGULAR);
        verify(titleLineGraphSeries).setTextBold(false);
    }

    @Test
    public void testSetSeriesColorForLineGraphSeries() {
        // setup
        int primaryColor = 22;
        int backgroundColor = 11;
        when(graphColors.getColor()).thenReturn(graphColor);
        when(graphColor.getPrimary()).thenReturn((long) primaryColor);
        when(graphColor.getBackground()).thenReturn((long) backgroundColor);
        // execute
        fixture.setSeriesColor(lineGraphSeries);
        // validate
        verify(graphColors).getColor();
        verify(graphColor).getPrimary();
        verify(graphColor).getBackground();
        verify(lineGraphSeries).setColor(primaryColor);
        verify(lineGraphSeries).setBackgroundColor(backgroundColor);
    }

    @Test
    public void testSetSeriesColorForTitleLineGraphSeries() {
        // setup
        int primaryColor = 22;
        int backgroundColor = 11;
        when(graphColors.getColor()).thenReturn(graphColor);
        when(graphColor.getPrimary()).thenReturn((long) primaryColor);
        when(graphColor.getBackground()).thenReturn((long) backgroundColor);
        // execute
        fixture.setSeriesColor(titleLineGraphSeries);
        // validate
        verify(graphColors).getColor();
        verify(graphColor).getPrimary();
        verify(graphColor).getBackground();
        verify(titleLineGraphSeries).setColor(primaryColor);
    }

    @Test
    public void testDrawBackgroundForLineGraphSeries() {
        // execute
        fixture.drawBackground(lineGraphSeries, true);
        // validate
        verify(lineGraphSeries).setDrawBackground(true);
    }

}