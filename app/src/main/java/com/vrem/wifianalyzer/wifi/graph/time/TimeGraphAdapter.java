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
package com.vrem.wifianalyzer.wifi.graph.time;

import android.support.annotation.NonNull;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.UpdateNotifier;
import com.vrem.wifianalyzer.wifi.graph.Colors;
import com.vrem.wifianalyzer.wifi.graph.GraphAdapterUtils;
import com.vrem.wifianalyzer.wifi.model.WiFiBand;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetails;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

class TimeGraphAdapter implements UpdateNotifier {
    private final MainContext mainContext = MainContext.INSTANCE;
    private final GraphView graphView;
    private final WiFiBand wifiBand;
    private final Map<String, LineGraphSeries<DataPoint>> seriesMap;
    private int timeIndex;

    TimeGraphAdapter(@NonNull GraphView graphView, @NonNull WiFiBand wifiBand) {
        this.graphView = graphView;
        this.wifiBand = wifiBand;
        this.seriesMap = new TreeMap<>();
        this.timeIndex = 0;
        this.mainContext.getScanner().addUpdateNotifier(this);
    }

    @Override
    public void update(@NonNull WiFiData wifiData) {
        Set<String> newSeries = new TreeSet<>();
        for (WiFiDetails wifiDetails : wifiData.getWiFiList(wifiBand)) {
            String key = wifiDetails.getTitle();
            newSeries.add(key);
            LineGraphSeries<DataPoint> series = seriesMap.get(key);
            if (series == null) {
                series = new LineGraphSeries<>();
                setSeriesOptions(series, wifiDetails);
                graphView.addSeries(series);
                seriesMap.put(key, series);
            }
            series.appendData(new DataPoint(timeIndex, wifiDetails.getLevel()), true, timeIndex + 1);
        }
        GraphAdapterUtils.removeSeries(graphView, seriesMap, newSeries);
        GraphAdapterUtils.updateLegendRenderer(graphView);

        graphView.setVisibility(wifiBand.equals(mainContext.getSettings().getWiFiBand()) ? View.VISIBLE : View.GONE);
        timeIndex++;
    }

    private void setSeriesOptions(@NonNull LineGraphSeries<DataPoint> series, @NonNull WiFiDetails wifiDetails) {
        if (wifiDetails.isConnected()) {
            series.setColor(Colors.BLUE.getPrimary());
            series.setThickness(6);
        } else {
            series.setColor(Colors.findRandomColor().getPrimary());
            series.setThickness(2);
        }
        series.setDrawBackground(false);
        series.setTitle(wifiDetails.getTitle() + " " + wifiDetails.getChannel());
    }

}
