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

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.AccessPointsDetail;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannels;
import com.vrem.wifianalyzer.wifi.graph.GraphLegend;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import java.util.List;
import java.util.Set;

public class GraphViewWrapper {
    private static final float TEXT_SIZE_ADJUSTMENT = 0.90f;
    private final MainContext mainContext = MainContext.INSTANCE;
    private final GraphView graphView;
    private final SeriesCache seriesCache;
    private final GraphColors graphColors;
    private GraphLegend graphLegend;

    public GraphViewWrapper(@NonNull GraphView graphView, @NonNull GraphLegend graphLegend) {
        this(graphView, graphLegend, null);
    }

    public GraphViewWrapper(@NonNull GraphView graphView, @NonNull GraphLegend graphLegend, WiFiBand wiFiBand) {
        this.graphView = graphView;
        this.graphLegend = graphLegend;
        this.seriesCache = new SeriesCache();
        this.graphColors = new GraphColors();
        setViewPortX(wiFiBand);
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
        return added;
    }

    private boolean isSameSeries(@NonNull BaseSeries<DataPoint> series, BaseSeries<DataPoint> current) {
        return current.equals(series);
    }

    public boolean addSeries(@NonNull WiFiDetail wiFiDetail, @NonNull BaseSeries<DataPoint> series, DataPoint[] data) {
        BaseSeries<DataPoint> current = seriesCache.add(wiFiDetail, series);
        boolean added = isSameSeries(series, current);
        if (added) {
            addNewSeries(wiFiDetail, current);
        } else {
            current.resetData(data);
        }
        return added;
    }

    private void addNewSeries(@NonNull WiFiDetail wiFiDetail, BaseSeries<DataPoint> series) {
        addSeries(series);
        series.setTitle(wiFiDetail.getSSID() + " " + wiFiDetail.getWiFiSignal().getWiFiChannel().getChannel());
        series.setOnDataPointTapListener(new GraphTapListener());
    }

    public void addSeries(@NonNull BaseSeries series) {
        graphView.addSeries(series);
    }

    private void setViewPortX(WiFiBand wiFiBand) {
        Viewport viewport = graphView.getViewport();
        viewport.setXAxisBoundsManual(true);
        int minX = calculateViewPortMinX(wiFiBand);
        viewport.setMinX(minX);
        viewport.setMaxX(calculateViewPortMaxX(wiFiBand, minX));
    }

    private int calculateViewPortMinX(WiFiBand wiFiBand) {
        if (wiFiBand == null) {
            return 0;
        }
        WiFiChannels wiFiChannels = wiFiBand.getWiFiChannels();
        return wiFiChannels.getWiFiChannelFirst().getFrequency() - wiFiChannels.getFrequencyOffset();
    }

    private int calculateViewPortMaxX(WiFiBand wiFiBand, int offset) {
        if (wiFiBand == null) {
            return offset + getViewPortMaxX();
        }
        return offset + (getViewPortMaxX() * wiFiBand.getWiFiChannels().getFrequencySpread());
    }

    private int getViewPortMaxX() {
        return graphView.getGridLabelRenderer().getNumHorizontalLabels() - 1;
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
            LegendRenderer legendRenderer = new LegendRenderer(graphView);
            graphView.setLegendRenderer(legendRenderer);
            this.graphLegend = graphLegend;
        }
    }

    public void setVisibility(@NonNull WiFiBand wiFiBand) {
        graphView.setVisibility(wiFiBand.equals(mainContext.getSettings().getWiFiBand()) ? View.VISIBLE : View.GONE);
    }

    public GraphColor getColor() {
        return graphColors.getColor();
    }

    public GraphView getGraphView() {
        return graphView;
    }

    private class GraphTapListener implements OnDataPointTapListener {
        @Override
        public void onTap(@NonNull Series series, @NonNull DataPointInterface dataPoint) {
            WiFiDetail wiFiDetail = seriesCache.find(series);
            if (wiFiDetail != null) {
                Dialog dialog = new AccessPointsDetail().popupDialog(mainContext.getContext(), mainContext.getLayoutInflater(), wiFiDetail);
                dialog.show();
            }
        }
    }
}
