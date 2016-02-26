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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.model.SortBy;
import com.vrem.wifianalyzer.wifi.model.WiFiBand;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetails;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

class TimeGraphView {
    private final WiFiBand wiFiBand;
    private final GraphView graphView;
    private final Map<String, LineGraphSeries<DataPoint>> seriesMap;
    private final GraphViewUtils graphViewUtils;
    private int scanCount;

    private TimeGraphView(@NonNull GraphViewBuilder graphViewBuilder, @NonNull Resources resources, @NonNull WiFiBand wiFiBand) {
        this.wiFiBand = wiFiBand;
        this.graphView = makeGraphView(graphViewBuilder, resources);
        this.seriesMap = new TreeMap<>();
        this.graphViewUtils = new GraphViewUtils(graphView, seriesMap);
        this.scanCount = 0;
    }

    static TimeGraphView make2(@NonNull GraphViewBuilder graphViewBuilder, @NonNull Resources resources) {
        return new TimeGraphView(graphViewBuilder, resources, WiFiBand.TWO);
    }

    static TimeGraphView make5(@NonNull GraphViewBuilder graphViewBuilder, @NonNull Resources resources) {
        return new TimeGraphView(graphViewBuilder, resources, WiFiBand.FIVE);
    }

    private GraphView makeGraphView(GraphViewBuilder graphViewBuilder, Resources resources) {
        return graphViewBuilder
                .setLabelFormatter(new AxisLabel(0, Integer.MAX_VALUE).setEvenOnly(true))
                .setVerticalTitle(resources.getString(R.string.graph_axis_y))
                .setHorizontalTitle(resources.getString(R.string.graph_time_axis_x))
                .setScrollable(true)
                .build();
    }

    void update(@NonNull WiFiData wiFiData) {
        Set<String> newSeries = new TreeSet<>();
        addConnection(wiFiData, newSeries);
        addWiFiDetails(wiFiData, newSeries);
        graphViewUtils.updateSeries(newSeries);
        graphViewUtils.updateLegend();
        graphViewUtils.setVisibility(wiFiBand);
        scanCount++;
    }

    private void addWiFiDetails(@NonNull WiFiData wiFiData, Set<String> newSeries) {
        for (WiFiDetails wiFiDetails : wiFiData.getWiFiList(wiFiBand, SortBy.STRENGTH)) {
            if (wiFiDetails.isConnected()) {
                continue;
            }
            addData(newSeries, wiFiDetails);
        }
    }

    private void addConnection(@NonNull WiFiData wiFiData, Set<String> newSeries) {
        WiFiDetails connection = wiFiData.getConnection();
        if (connection != null && wiFiBand.equals(connection.getWiFiBand())) {
            addData(newSeries, connection);
        }
    }

    private void addData(@NonNull Set<String> newSeries, @NonNull WiFiDetails wiFiDetails) {
        String key = wiFiDetails.getTitle();
        newSeries.add(key);
        LineGraphSeries<DataPoint> series = seriesMap.get(key);
        if (series == null) {
            series = new LineGraphSeries<>();
            setSeriesOptions(series, wiFiDetails);
            graphView.addSeries(series);
            seriesMap.put(key, series);
        }
        series.appendData(new DataPoint(scanCount, wiFiDetails.getLevel()), true, scanCount + 1);
    }

    private void setSeriesOptions(@NonNull LineGraphSeries<DataPoint> series, @NonNull WiFiDetails wiFiDetails) {
        if (wiFiDetails.isConnected()) {
            series.setColor(GraphColor.BLUE.getPrimary());
            series.setThickness(6);
        } else {
            series.setColor(GraphColor.findColor().getPrimary());
            series.setThickness(2);
        }
        series.setDrawBackground(false);
        series.setTitle(wiFiDetails.getTitle() + " " + wiFiDetails.getChannel());
    }

}
