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

import android.support.annotation.NonNull;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.model.SortBy;
import com.vrem.wifianalyzer.wifi.model.WiFiBand;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetails;
import com.vrem.wifianalyzer.wifi.scanner.UpdateNotifier;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

class ChannelGraphAdapter implements UpdateNotifier {
    private final MainContext mainContext = MainContext.INSTANCE;
    private final GraphView graphView;
    private final WiFiBand wiFiBand;
    private final Map<String, LineGraphSeries<DataPoint>> seriesMap;
    private final GraphViewUtils graphViewUtils;
    private boolean defaultAdded;

    ChannelGraphAdapter(@NonNull GraphView graphView, @NonNull WiFiBand wiFiBand) {
        this.graphView = graphView;
        this.wiFiBand = wiFiBand;
        this.seriesMap = new TreeMap<>();
        this.graphViewUtils = new GraphViewUtils(graphView, seriesMap);
        this.mainContext.getScanner().addUpdateNotifier(this, wiFiBand.name());
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        Set<String> newSeries = new TreeSet<>();
        addConnection(wiFiData, newSeries);
        addDefaultsSeries();
        addWiFiDetails(wiFiData, newSeries);
        graphViewUtils.updateSeries(newSeries);
        graphViewUtils.updateLegend();

        graphView.setVisibility(wiFiBand.equals(mainContext.getSettings().getWiFiBand()) ? View.VISIBLE : View.GONE);
    }

    private void addWiFiDetails(@NonNull WiFiData wiFiData, Set<String> newSeries) {
        for (WiFiDetails wiFiDetails : wiFiData.getWiFiList(wiFiBand, SortBy.CHANNEL)) {
            if (wiFiDetails.isConnected()) {
                continue;
            }
            addData(newSeries, wiFiDetails);
        }
    }

    private void addConnection(@NonNull WiFiData wiFiData, Set<String> newSeries) {
        WiFiDetails connection = wiFiData.getConnection();
        if (connection != null && this.wiFiBand.equals(connection.getWiFiBand())) {
            addData(newSeries, connection);
        }
    }

    private void addData(@NonNull Set<String> newSeries, @NonNull WiFiDetails wiFiDetails) {
        String key = wiFiDetails.getTitle();
        newSeries.add(key);
        LineGraphSeries<DataPoint> series = seriesMap.get(key);
        if (series == null) {
            series = new LineGraphSeries<>(createDataPoints(wiFiDetails));
            setSeriesOptions(series, wiFiDetails);
            graphView.addSeries(series);
            seriesMap.put(key, series);
        } else {
            series.resetData(createDataPoints(wiFiDetails));
        }
    }

    private void setSeriesOptions(@NonNull LineGraphSeries<DataPoint> series, @NonNull WiFiDetails wiFiDetails) {
        if (wiFiDetails.isConnected()) {
            series.setColor(GraphColor.BLUE.getPrimary());
            series.setBackgroundColor(GraphColor.BLUE.getBackground());
            series.setThickness(6);
        } else {
            GraphColor graphColor = GraphColor.findColor();
            series.setColor(graphColor.getPrimary());
            series.setBackgroundColor(graphColor.getBackground());
            series.setThickness(2);
        }
        series.setDrawBackground(true);
        series.setTitle(wiFiDetails.getTitle() + " " + wiFiDetails.getChannel());
    }

    private DataPoint[] createDataPoints(@NonNull WiFiDetails wiFiDetails) {
        int channel = wiFiDetails.getChannel();
        int level = wiFiDetails.getLevel();
        return new DataPoint[]{
                new DataPoint(channel - WiFiBand.CHANNEL_SPREAD, GraphViewBuilder.MIN_Y),
                new DataPoint(channel - WiFiBand.CHANNEL_SPREAD / 2, level),
                new DataPoint(channel, level),
                new DataPoint(channel + WiFiBand.CHANNEL_SPREAD / 2, level),
                new DataPoint(channel + WiFiBand.CHANNEL_SPREAD, GraphViewBuilder.MIN_Y)
        };
    }

    private void addDefaultsSeries() {
        if (WiFiBand.TWO.equals(wiFiBand) || defaultAdded) {
            return;
        }

        int minValue = wiFiBand.getChannelFirst() - WiFiBand.CHANNEL_SPREAD;
        int maxValue = wiFiBand.getChannelLast() + WiFiBand.CHANNEL_SPREAD;
        if (maxValue % 2 != 0) {
            maxValue++;
        }
        DataPoint[] dataPoints = new DataPoint[]{
                new DataPoint(minValue, GraphViewBuilder.MIN_Y),
                new DataPoint(maxValue, GraphViewBuilder.MIN_Y)
        };

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        series.setColor(GraphColor.TRANSPARENT.getPrimary());
        series.setDrawBackground(false);
        series.setThickness(0);
        series.setTitle("");
        graphView.addSeries(series);
        defaultAdded = true;
    }
}
