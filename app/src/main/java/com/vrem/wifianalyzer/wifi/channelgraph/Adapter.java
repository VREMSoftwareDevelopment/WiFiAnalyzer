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
package com.vrem.wifianalyzer.wifi.channelgraph;

import android.support.annotation.NonNull;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.UpdateNotifier;
import com.vrem.wifianalyzer.wifi.WiFiConstants;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

class Adapter implements UpdateNotifier {
    private final MainContext mainContext = MainContext.INSTANCE;
    private final GraphView graphView;
    private final Constraints constraints;
    private final Map<String, LineGraphSeries<DataPoint>> seriesMap;
    private final Random random;

    Adapter(@NonNull GraphView graphView, @NonNull Constraints constraints) {
        this.graphView = graphView;
        this.constraints = constraints;
        this.seriesMap = new TreeMap<>();
        this.random = new Random();
        mainContext.getScanner().addUpdateNotifier(this);
    }

    @Override
    public void update(@NonNull WiFiData wifiData) {
        Set<String> newSeries = new TreeSet<>();
        for (WiFiDetails wifiDetails : wifiData.getWiFiList(mainContext.getSettings().getWiFiBand())) {
            String key = wifiDetails.getTitle();
            newSeries.add(key);
            LineGraphSeries<DataPoint> series = seriesMap.get(key);
            if (series == null) {
                series = new LineGraphSeries<>(createDataPoints(wifiDetails));
                setSeriesOptions(series, wifiDetails);
                graphView.addSeries(series);
                seriesMap.put(key, series);
            } else {
                series.resetData(createDataPoints(wifiDetails));
            }
        }
        removeSeries(newSeries);
        updateLegendRenderer();
    }

    private void removeSeries(Set<String> newSeries) {
        List<String> remove = new ArrayList<>();
        for (String title : seriesMap.keySet()) {
            if (!newSeries.contains(title)) {
                graphView.removeSeries(seriesMap.get(title));
                remove.add(title);
            }
        }
        for (String title : remove) {
            seriesMap.remove(title);
        }
    }

    private void setSeriesOptions(@NonNull LineGraphSeries<DataPoint> series, @NonNull WiFiDetails wifiDetails) {
        int colorIndex = getColorIndex(wifiDetails);
        series.setColor(Colors.values()[colorIndex].getPrimary());
        series.setBackgroundColor(Colors.values()[colorIndex].getBackground());
        series.setDrawBackground(true);
        series.setThickness(wifiDetails.isConnected() ? 6 : 2);
        series.setTitle(wifiDetails.getTitle() + " " + wifiDetails.getChannel());
    }

    private int getColorIndex(@NonNull WiFiDetails wifiDetails) {
        if (wifiDetails.isConnected()) {
            return Colors.BLUE.ordinal();
        }
        int colorIndex = random.nextInt(Colors.values().length - 1);
        if (colorIndex == Colors.BLUE.ordinal()) {
            colorIndex++;
        }
        return colorIndex;
    }

    @NonNull
    private DataPoint[] createDataPoints(@NonNull WiFiDetails wifiDetails) {
        int channel = wifiDetails.getChannel();
        int level = wifiDetails.getLevel();
        return new DataPoint[]{
                new DataPoint(channel - WiFiConstants.CHANNEL_OFFSET, WiFiConstants.MIN_Y),
                new DataPoint(channel - WiFiConstants.CHANNEL_OFFSET / 2, level),
                new DataPoint(channel, level),
                new DataPoint(channel + WiFiConstants.CHANNEL_OFFSET / 2, level),
                new DataPoint(channel + WiFiConstants.CHANNEL_OFFSET, WiFiConstants.MIN_Y)
        };
    }

    private void updateLegendRenderer() {
        LegendRenderer legendRenderer = graphView.getLegendRenderer();
        legendRenderer.resetStyles();
        legendRenderer.setVisible(true);
        legendRenderer.setWidth(0);
        legendRenderer.setFixedPosition(0, 0);
        legendRenderer.setTextSize(legendRenderer.getTextSize() * 0.50f);
    }
}
