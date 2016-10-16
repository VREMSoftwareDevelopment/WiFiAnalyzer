/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.graph.tools;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.jjoe64.graphview.series.TitleLineGraphSeries;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.AccessPointsDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import java.util.List;
import java.util.Set;

public class GraphViewWrapper {
    static final float TEXT_SIZE_ADJUSTMENT = 0.9f;
    private static final int THICKNESS_REGULAR = 5;
    private static final int THICKNESS_CONNECTED = THICKNESS_REGULAR * 2;

    private final GraphView graphView;
    private SeriesCache seriesCache;
    private GraphColors graphColors;
    private GraphLegend graphLegend;

    public GraphViewWrapper(@NonNull GraphView graphView, @NonNull GraphLegend graphLegend) {
        this.graphView = graphView;
        this.graphLegend = graphLegend;
        setSeriesCache(new SeriesCache());
        setGraphColors(new GraphColors());
    }

    void setSeriesCache(@NonNull SeriesCache seriesCache) {
        this.seriesCache = seriesCache;
    }

    void setGraphColors(@NonNull GraphColors graphColors) {
        this.graphColors = graphColors;
    }

    public void removeSeries(@NonNull Set<WiFiDetail> newSeries) {
        List<BaseSeries<DataPoint>> removed = seriesCache.remove(newSeries);
        for (Series series : removed) {
            graphColors.addColor(series.getColor());
            graphView.removeSeries(series);
        }
    }

    public boolean appendSeries(@NonNull WiFiDetail wiFiDetail, @NonNull BaseSeries<DataPoint> series, DataPoint data, int count) {
        BaseSeries<DataPoint> current = seriesCache.add(wiFiDetail, series);
        boolean added = isSameSeries(series, current);
        if (added) {
            addNewSeries(wiFiDetail, current);
        } else {
            current.appendData(data, true, count + 1);
        }
        highlightConnected(wiFiDetail.getWiFiAdditional().isConnected(), current);
        return added;
    }

    private void highlightConnected(boolean isConnected, @NonNull BaseSeries<DataPoint> series) {
        if (series instanceof LineGraphSeries) {
            ((LineGraphSeries) series).setThickness(isConnected ? THICKNESS_CONNECTED : THICKNESS_REGULAR);
        } else if (series instanceof TitleLineGraphSeries) {
            TitleLineGraphSeries titleLineGraphSeries = (TitleLineGraphSeries) series;
            titleLineGraphSeries.setThickness(isConnected ? THICKNESS_CONNECTED : THICKNESS_REGULAR);
            titleLineGraphSeries.setTextBold(isConnected);
        }
    }

    private boolean isSameSeries(@NonNull BaseSeries<DataPoint> series, BaseSeries<DataPoint> current) {
        return current.equals(series);
    }

    public void setViewport() {
        Viewport viewport = graphView.getViewport();
        viewport.setMinX(0);
        viewport.setMaxX(getViewportCntX());
    }

    public void setViewport(int minX, int maxX) {
        Viewport viewport = graphView.getViewport();
        viewport.setMinX(minX);
        viewport.setMaxX(maxX);
    }

    public int getViewportCntX() {
        return graphView.getGridLabelRenderer().getNumHorizontalLabels() - 1;
    }

    public boolean addSeries(@NonNull WiFiDetail wiFiDetail, @NonNull BaseSeries<DataPoint> series, DataPoint[] data) {
        BaseSeries<DataPoint> current = seriesCache.add(wiFiDetail, series);
        boolean added = isSameSeries(series, current);
        if (added) {
            addNewSeries(wiFiDetail, current);
        } else {
            current.resetData(data);
        }
        highlightConnected(wiFiDetail.getWiFiAdditional().isConnected(), current);
        return added;
    }

    private void addNewSeries(@NonNull WiFiDetail wiFiDetail, BaseSeries<DataPoint> series) {
        addSeries(series);
        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        series.setTitle(wiFiDetail.getSSID() + " " + wiFiSignal.getPrimaryWiFiChannel().getChannel());
        series.setOnDataPointTapListener(new GraphTapListener());
    }

    public void addSeries(@NonNull BaseSeries series) {
        graphView.addSeries(series);
    }

    public void updateLegend(@NonNull GraphLegend graphLegend) {
        resetLegendRenderer(graphLegend);
        LegendRenderer legendRenderer = graphView.getLegendRenderer();
        legendRenderer.resetStyles();
        legendRenderer.setWidth(0);
        legendRenderer.setTextSize(legendRenderer.getTextSize() * TEXT_SIZE_ADJUSTMENT);
        graphLegend.display(legendRenderer);
    }

    private void resetLegendRenderer(@NonNull GraphLegend graphLegend) {
        if (!this.graphLegend.equals(graphLegend)) {
            graphView.setLegendRenderer(newLegendRenderer());
            this.graphLegend = graphLegend;
        }
    }

    LegendRenderer newLegendRenderer() {
        return new LegendRenderer(graphView);
    }

    public void setVisibility(int visibility) {
        graphView.setVisibility(visibility);
    }

    public GraphColor getColor() {
        return graphColors.getColor();
    }

    public GraphView getGraphView() {
        return graphView;
    }

    GraphLegend getGraphLegend() {
        return graphLegend;
    }

    class GraphTapListener implements OnDataPointTapListener {
        @Override
        public void onTap(@NonNull Series series, @NonNull DataPointInterface dataPoint) {
            WiFiDetail wiFiDetail = seriesCache.find(series);
            if (wiFiDetail != null) {
                MainContext mainContext = MainContext.INSTANCE;
                LayoutInflater layoutInflater = mainContext.getLayoutInflater();
                MainActivity mainActivity = mainContext.getMainActivity();
                Dialog dialog = new AccessPointsDetail().popupDialog(mainActivity, layoutInflater, wiFiDetail);
                dialog.show();
            }
        }
    }

}
