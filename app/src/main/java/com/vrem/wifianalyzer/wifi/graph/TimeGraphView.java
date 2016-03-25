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

package com.vrem.wifianalyzer.wifi.graph;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannels;
import com.vrem.wifianalyzer.wifi.graph.wrapper.GraphColor;
import com.vrem.wifianalyzer.wifi.graph.wrapper.GraphViewBuilder;
import com.vrem.wifianalyzer.wifi.graph.wrapper.GraphViewWrapper;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import java.util.Set;
import java.util.TreeSet;

class TimeGraphView {
    private static final int MAX_SCAN_COUNT = 1000;
    private final MainContext mainContext = MainContext.INSTANCE;

    private final WiFiBand wiFiBand;
    private final GraphViewWrapper graphViewWrapper;
    private int scanCount;
    private int xValue;

    private TimeGraphView(@NonNull View view, int graphViewId, @NonNull Resources resources, @NonNull WiFiBand wiFiBand) {
        this.wiFiBand = wiFiBand;
        this.scanCount = this.xValue = 0;
        GraphView graphView = makeGraphView(view, graphViewId, resources);
        this.graphViewWrapper = new GraphViewWrapper(graphView, mainContext.getSettings().getTimeGraphLegend());
        initialize();
    }

    static TimeGraphView make2(@NonNull View view, Resources resources) {
        return new TimeGraphView(view, R.id.timeGraph2, resources, WiFiBand.GHZ_2);
    }

    static TimeGraphView make5(@NonNull View view, @NonNull Resources resources) {
        return new TimeGraphView(view, R.id.timeGraph5, resources, WiFiBand.GHZ_5);
    }

    private GraphView makeGraphView(@NonNull View view, int graphViewId, @NonNull Resources resources) {
        return new GraphViewBuilder(view, graphViewId)
                .setLabelFormatter(new TimeAxisLabel())
                .setVerticalTitle(resources.getString(R.string.graph_axis_y))
                .setHorizontalTitle(resources.getString(R.string.graph_time_axis_x))
                .build();
    }

    void update(@NonNull WiFiData wiFiData) {
        Set<WiFiDetail> newSeries = new TreeSet<>();
        for (WiFiDetail wiFiDetail : wiFiData.getWiFiDetails(wiFiBand, mainContext.getSettings().getSortBy())) {
            newSeries.add(wiFiDetail);
            addData(wiFiDetail);
        }
        graphViewWrapper.removeSeries(newSeries);
        graphViewWrapper.updateLegend(mainContext.getSettings().getTimeGraphLegend());
        graphViewWrapper.setVisibility(wiFiBand);
        xValue++;
        if (scanCount < MAX_SCAN_COUNT) {
            scanCount++;
        }
    }

    private void addData(@NonNull WiFiDetail wiFiDetail) {
        DataPoint dataPoint = new DataPoint(xValue, wiFiDetail.getWiFiSignal().getLevel());
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{dataPoint});
        if (graphViewWrapper.appendSeries(wiFiDetail, series, dataPoint, scanCount)) {
            series.setColor(graphViewWrapper.getColor().getPrimary());
            series.setDrawBackground(false);
        }
    }

    private void initialize() {
        WiFiChannels wiFiChannels = wiFiBand.getWiFiChannels();
        int frequencyOffset = wiFiBand.getWiFiChannels().getFrequencyOffset();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, GraphViewBuilder.MIN_Y),
                new DataPoint(GraphViewBuilder.CNT_X - 1, GraphViewBuilder.MIN_Y)
        });
        series.setColor(GraphColor.TRANSPARENT.getPrimary());
        series.setThickness(0);
        graphViewWrapper.addSeries(series);
    }

}
