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
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GraphViewWrapperTest {
    public static final float VALUE = 10.0f;
    @Mock
    private GraphView graphView;
    @Mock
    private Viewport viewport;
    @Mock
    private GridLabelRenderer gridLabelRenderer;
    @Mock
    private LegendRenderer legendRenderer;
    @Mock
    private Resources resources;
    @Mock
    private SeriesCache seriesCache;
    @Mock
    private GraphColors graphColors;
    @Mock
    private BaseSeries<DataPoint> baseSeries;
    @Mock
    private BaseSeries<DataPoint> currentSeries;

    private DataPoint dataPoint;
    private WiFiDetail wiFiDetail;
    private GraphViewWrapper fixture;

    @Before
    public void setUp() throws Exception {
        wiFiDetail = WiFiDetail.EMPTY;
        dataPoint = new DataPoint(1, 2);

        when(graphView.getLegendRenderer()).thenReturn(legendRenderer);
        when(legendRenderer.getTextSize()).thenReturn(VALUE);

        fixture = new GraphViewWrapper(graphView, GraphLegend.HIDE) {
            @Override
            protected LegendRenderer newLegendRenderer() {
                return legendRenderer;
            }
        };
        fixture.setGraphColors(graphColors);
        fixture.setSeriesCache(seriesCache);

        assertEquals(GraphLegend.HIDE, fixture.getGraphLegend());
    }

    @Test
    public void testRemoveSeries() throws Exception {
        // setup
        Set<WiFiDetail> newSeries = new TreeSet<>();
        List<BaseSeries<DataPoint>> removed = Arrays.asList(baseSeries);
        int color = 10;
        when(seriesCache.remove(newSeries)).thenReturn(removed);
        when(baseSeries.getColor()).thenReturn(color);
        // execute
        fixture.removeSeries(newSeries);
        // validate
        verify(seriesCache).remove(newSeries);
        verify(baseSeries).getColor();
        verify(graphColors).addColor(color);
        verify(graphView).removeSeries(baseSeries);
    }

    @Test
    public void testAppendSeries() throws Exception {
        // setup
        when(seriesCache.add(wiFiDetail, baseSeries)).thenReturn(currentSeries);
        // execute
        boolean actual = fixture.appendSeries(wiFiDetail, baseSeries, dataPoint, 10);
        // validate
        assertFalse(actual);
        verify(seriesCache).add(wiFiDetail, baseSeries);
        verify(currentSeries).appendData(dataPoint, true, 11);
    }

    @Test
    public void testAppendSeriesNew() throws Exception {
        // setup
        when(seriesCache.add(wiFiDetail, baseSeries)).thenReturn(baseSeries);
        // execute
        boolean actual = fixture.appendSeries(wiFiDetail, baseSeries, dataPoint, 10);
        // validate
        assertTrue(actual);
        verify(seriesCache).add(wiFiDetail, baseSeries);
        verify(graphView).addSeries(baseSeries);
        verify(baseSeries).setTitle(wiFiDetail.getSSID() + " " + wiFiDetail.getWiFiSignal().getWiFiChannel().getChannel());
        verify(baseSeries).setOnDataPointTapListener(any(GraphViewWrapper.GraphTapListener.class));
    }

    @Test
    public void testAddSeries() throws Exception {
        // setup
        DataPoint[] dataPoints = {dataPoint};
        when(seriesCache.add(wiFiDetail, baseSeries)).thenReturn(currentSeries);
        // execute
        boolean actual = fixture.addSeries(wiFiDetail, baseSeries, dataPoints);
        // validate
        assertFalse(actual);
        verify(seriesCache).add(wiFiDetail, baseSeries);
        verify(currentSeries).resetData(dataPoints);
    }

    @Test
    public void testAddSeriesNew() throws Exception {
        // setup
        DataPoint[] dataPoints = {dataPoint};
        when(seriesCache.add(wiFiDetail, baseSeries)).thenReturn(baseSeries);
        // execute
        boolean actual = fixture.addSeries(wiFiDetail, baseSeries, dataPoints);
        // validate
        assertTrue(actual);
        verify(seriesCache).add(wiFiDetail, baseSeries);
        verify(graphView).addSeries(baseSeries);
        verify(baseSeries).setTitle(wiFiDetail.getSSID() + " " + wiFiDetail.getWiFiSignal().getWiFiChannel().getChannel());
        verify(baseSeries).setOnDataPointTapListener(any(GraphViewWrapper.GraphTapListener.class));
    }

    @Test
    public void testUpdateLegend() throws Exception {
        // execute
        fixture.updateLegend(GraphLegend.RIGHT);
        // validate
        assertEquals(GraphLegend.RIGHT, fixture.getGraphLegend());
        verify(graphView).setLegendRenderer(legendRenderer);
        verify(legendRenderer).resetStyles();
        verify(legendRenderer).setWidth(0);
        verify(legendRenderer).setTextSize(VALUE * GraphViewWrapper.TEXT_SIZE_ADJUSTMENT);
    }

    @Test
    public void testSetVisibility() throws Exception {
        // execute
        fixture.setVisibility(View.VISIBLE);
        // validate
        verify(graphView).setVisibility(View.VISIBLE);
    }

    @Test
    public void testSetViewport() throws Exception {
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
    public void testSetViewportWithMinAndMax() throws Exception {
        // setup
        when(graphView.getViewport()).thenReturn(viewport);
        // execute
        fixture.setViewport(1, 2);
        // validate
        verify(graphView).getViewport();
        verify(viewport).setMinX(1);
        verify(viewport).setMaxX(2);
    }

}