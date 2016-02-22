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

class TimeGraphAdapter implements UpdateNotifier {
    private final MainContext mainContext = MainContext.INSTANCE;
    private final GraphView graphView;
    private final WiFiBand wiFiBand;
    private final Map<String, LineGraphSeries<DataPoint>> seriesMap;
    private final GraphViewUtils graphViewUtils;
    private int timeIndex;

    TimeGraphAdapter(@NonNull GraphView graphView, @NonNull WiFiBand wiFiBand) {
        this.graphView = graphView;
        this.wiFiBand = wiFiBand;
        this.seriesMap = new TreeMap<>();
        this.timeIndex = 0;
        this.graphViewUtils = new GraphViewUtils(graphView, seriesMap);
        this.mainContext.getScanner().addUpdateNotifier(this);
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        Set<String> newSeries = new TreeSet<>();
        WiFiDetails connection = wiFiData.getConnection();
        if (connection != null && this.wiFiBand.equals(connection.getWiFiBand())) {
            addData(newSeries, connection);
        }
        for (WiFiDetails wiFiDetails : wiFiData.getWiFiList(wiFiBand, SortBy.STRENGTH)) {
            if (wiFiDetails.isConnected()) {
                continue;
            }
            addData(newSeries, wiFiDetails);
        }
        graphViewUtils.updateSeries(newSeries);
        graphViewUtils.updateLegend();

        graphView.setVisibility(wiFiBand.equals(mainContext.getSettings().getWiFiBand()) ? View.VISIBLE : View.GONE);
        timeIndex++;
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
        series.appendData(new DataPoint(timeIndex, wiFiDetails.getLevel()), true, timeIndex + 1);
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
