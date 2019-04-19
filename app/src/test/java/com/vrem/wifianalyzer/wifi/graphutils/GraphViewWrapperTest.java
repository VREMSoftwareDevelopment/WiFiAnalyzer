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

import android.graphics.Color;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.settings.ThemeStyle;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GraphViewWrapperTest {
    @Mock
    private GraphView graphView;
    @Mock
    private Viewport viewport;
    @Mock
    private GridLabelRenderer gridLabelRenderer;
    @Mock
    private LegendRenderer legendRenderer;
    @Mock
    private SeriesCache seriesCache;
    @Mock
    private SeriesOptions seriesOptions;
    @Mock
    private BaseSeries<DataPoint> baseSeries;

    private DataPoint dataPoint;
    private DataPoint[] dataPoints;
    private WiFiDetail wiFiDetail;
    private GraphViewWrapper fixture;

    @Before
    public void setUp() {
        wiFiDetail = WiFiDetail.EMPTY;
        dataPoint = new DataPoint(1, 2);
        dataPoints = new DataPoint[]{dataPoint};

        when(graphView.getLegendRenderer()).thenReturn(legendRenderer);

        fixture = new GraphViewWrapper(graphView, GraphLegend.HIDE, ThemeStyle.DARK) {
            @Override
            protected LegendRenderer newLegendRenderer() {
                return legendRenderer;
            }
        };
        fixture.setSeriesCache(seriesCache);
        fixture.setSeriesOptions(seriesOptions);

        assertEquals(GraphLegend.HIDE, fixture.getGraphLegend());
    }

    @Test
    public void testRemoveSeries() {
        // setup
        Set<WiFiDetail> newSeries = Collections.emptySet();
        List<WiFiDetail> difference = Collections.emptyList();
        List<BaseSeries<DataPoint>> removed = Collections.singletonList(baseSeries);
        when(seriesCache.difference(newSeries)).thenReturn(difference);
        when(seriesCache.remove(difference)).thenReturn(removed);
        // execute
        fixture.removeSeries(newSeries);
        // validate
        verify(seriesCache).difference(newSeries);
        verify(seriesCache).remove(difference);
        verify(seriesOptions).removeSeriesColor(baseSeries);
        verify(graphView).removeSeries(baseSeries);
    }

    @Test
    public void testDifferenceSeries() {
        // setup
        Set<WiFiDetail> newSeries = Collections.emptySet();
        List<WiFiDetail> expected = Collections.emptyList();
        when(seriesCache.difference(newSeries)).thenReturn(expected);
        // execute
        List<WiFiDetail> actual = fixture.differenceSeries(newSeries);
        // validate
        assertEquals(expected, actual);
        verify(seriesCache).difference(newSeries);
    }

    @Test
    public void testAddSeriesDirectly() {
        // execute
        fixture.addSeries(baseSeries);
        // validate
        verify(graphView).addSeries(baseSeries);
    }

    @Test
    public void testAddSeriesWhenSeriesExistsDoesNotAddSeries() {
        // setup
        when(seriesCache.contains(wiFiDetail)).thenReturn(true);
        // execute
        boolean actual = fixture.addSeries(wiFiDetail, baseSeries, false);
        // validate
        assertFalse(actual);
        verify(seriesCache).contains(wiFiDetail);
        verify(seriesCache, never()).put(wiFiDetail, baseSeries);
    }

    @Test
    public void testAddSeriesAddsSeries() {
        // setup
        String expectedTitle = wiFiDetail.getSSID() + " " + wiFiDetail.getWiFiSignal().getChannelDisplay();
        boolean connected = wiFiDetail.getWiFiAdditional().getWiFiConnection().isConnected();
        when(seriesCache.contains(wiFiDetail)).thenReturn(false);
        // execute
        boolean actual = fixture.addSeries(wiFiDetail, baseSeries, true);
        // validate
        assertTrue(actual);
        verify(seriesCache).contains(wiFiDetail);
        verify(seriesCache).put(wiFiDetail, baseSeries);
        verify(baseSeries).setTitle(expectedTitle);
        verify(baseSeries).setOnDataPointTapListener(any(GraphViewWrapper.GraphTapListener.class));
        verify(seriesOptions).highlightConnected(baseSeries, connected);
        verify(seriesOptions).setSeriesColor(baseSeries);
        verify(seriesOptions).drawBackground(baseSeries, true);
        verify(graphView).addSeries(baseSeries);
    }

    @Test
    public void testUpdateSeriesWhenSeriesDoesNotExistsDoesNotUpdateSeries() {
        // setup
        when(seriesCache.contains(wiFiDetail)).thenReturn(false);
        // execute
        boolean actual = fixture.updateSeries(wiFiDetail, dataPoints, true);
        // validate
        assertFalse(actual);
        verify(seriesCache).contains(wiFiDetail);
        verify(seriesCache, never()).get(wiFiDetail);
    }

    @Test
    public void testUpdateSeriesWhenSeriesDoesExists() {
        // setup
        boolean connected = wiFiDetail.getWiFiAdditional().getWiFiConnection().isConnected();
        when(seriesCache.contains(wiFiDetail)).thenReturn(true);
        when(seriesCache.get(wiFiDetail)).thenReturn(baseSeries);
        // execute
        boolean actual = fixture.updateSeries(wiFiDetail, dataPoints, true);
        // validate
        assertTrue(actual);
        verify(seriesCache).contains(wiFiDetail);
        verify(seriesCache).get(wiFiDetail);
        verify(baseSeries).resetData(dataPoints);
        verify(seriesOptions).highlightConnected(baseSeries, connected);
        verify(seriesOptions).drawBackground(baseSeries, true);
    }

    @Test
    public void testAppendSeriesWhenSeriesDoesNotExistsDoesNotUpdateSeries() {
        // setup
        int count = 10;
        when(seriesCache.contains(wiFiDetail)).thenReturn(false);
        // execute
        boolean actual = fixture.appendToSeries(wiFiDetail, dataPoint, count, true);
        // validate
        assertFalse(actual);
        verify(seriesCache).contains(wiFiDetail);
        verify(seriesCache, never()).get(wiFiDetail);
    }

    @Test
    public void testAppendSeriesWhenSeriesDoesExists() {
        // setup
        int count = 10;
        boolean connected = wiFiDetail.getWiFiAdditional().getWiFiConnection().isConnected();
        when(seriesCache.contains(wiFiDetail)).thenReturn(true);
        when(seriesCache.get(wiFiDetail)).thenReturn(baseSeries);
        // execute
        boolean actual = fixture.appendToSeries(wiFiDetail, dataPoint, count, true);
        // validate
        assertTrue(actual);
        verify(seriesCache).contains(wiFiDetail);
        verify(seriesCache).get(wiFiDetail);
        verify(baseSeries).appendData(dataPoint, true, count + 1);
        verify(seriesOptions).highlightConnected(baseSeries, connected);
        verify(seriesOptions).drawBackground(baseSeries, true);
    }

    @Test
    public void testUpdateLegend() {
        // setup
        float textSize = 10f;
        when(graphView.getTitleTextSize()).thenReturn(textSize);
        // execute
        fixture.updateLegend(GraphLegend.RIGHT);
        // validate
        assertEquals(GraphLegend.RIGHT, fixture.getGraphLegend());
        verify(graphView).setLegendRenderer(legendRenderer);
        verify(legendRenderer).resetStyles();
        verify(legendRenderer).setWidth(0);
        verify(legendRenderer).setTextSize(textSize);
        verify(legendRenderer).setTextColor(Color.WHITE);
    }

    @Test
    public void testSetVisibility() {
        // execute
        fixture.setVisibility(View.VISIBLE);
        // validate
        verify(graphView).setVisibility(View.VISIBLE);
    }

    @Test
    public void testCalculateGraphType() {
        // execute & validate
        assertTrue(fixture.calculateGraphType() > 0);
    }

    @Test
    public void testSetViewport() {
        // setup
        when(graphView.getGridLabelRenderer()).thenReturn(gridLabelRenderer);
        when(gridLabelRenderer.getNumHorizontalLabels()).thenReturn(10);
        when(graphView.getViewport()).thenReturn(viewport);
        // execute
        fixture.setViewport();
        // validate
        verify(graphView).getGridLabelRenderer();
        verify(gridLabelRenderer).getNumHorizontalLabels();
        verify(graphView).getViewport();
        verify(viewport).setMinX(0);
        verify(viewport).setMaxX(9);
    }

    @Test
    public void testGetSize() {
        // execute & validate
        assertEquals(Configuration.SIZE_MAX, fixture.getSize(GraphConstants.TYPE1));
        assertEquals(Configuration.SIZE_MAX, fixture.getSize(GraphConstants.TYPE2));
        assertEquals(Configuration.SIZE_MAX, fixture.getSize(GraphConstants.TYPE3));
        assertEquals(Configuration.SIZE_MIN, fixture.getSize(GraphConstants.TYPE4));
    }

    @Test
    public void testSetViewportWithMinAndMax() {
        // setup
        when(graphView.getViewport()).thenReturn(viewport);
        // execute
        fixture.setViewport(1, 2);
        // validate
        verify(graphView).getViewport();
        verify(viewport).setMinX(1);
        verify(viewport).setMaxX(2);
    }

    @Test
    public void testIsNewSeries() {
        // setup
        when(seriesCache.contains(wiFiDetail)).thenReturn(false);
        // execute
        boolean actual = fixture.isNewSeries(wiFiDetail);
        // validate
        assertTrue(actual);
        verify(seriesCache).contains(wiFiDetail);
    }
}