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
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.model.WiFiBand;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

class ChannelGraphView {
    private final MainContext mainContext = MainContext.INSTANCE;

    private final WiFiBand wiFiBand;
    private final GraphView graphView;
    private final Map<String, LineGraphSeries<DataPoint>> seriesMap;
    private final GraphViewUtils graphViewUtils;
    private GraphColor currentGraphColor;

    private ChannelGraphView(@NonNull GraphViewBuilder graphViewBuilder, @NonNull Resources resources, @NonNull WiFiBand wiFiBand) {
        boolean options = WiFiBand.GHZ_5.equals(wiFiBand);
        this.wiFiBand = wiFiBand;
        this.graphView = makeGraphView(graphViewBuilder, resources, this.wiFiBand, options);
        this.seriesMap = new TreeMap<>();
        this.graphViewUtils = new GraphViewUtils(graphView, seriesMap);
        this.currentGraphColor = null;
        if (options) {
            addDefaultsSeries(graphView, wiFiBand);
        }
    }

    static ChannelGraphView make2(@NonNull GraphViewBuilder graphViewBuilder, @NonNull Resources resources) {
        return new ChannelGraphView(graphViewBuilder, resources, WiFiBand.GHZ_2);
    }

    static ChannelGraphView make5(@NonNull GraphViewBuilder graphViewBuilder, @NonNull Resources resources) {
        return new ChannelGraphView(graphViewBuilder, resources, WiFiBand.GHZ_5);
    }

    private GraphView makeGraphView(@NonNull GraphViewBuilder graphViewBuilder, @NonNull Resources resources, @NonNull WiFiBand wiFiBand, boolean options) {
        int minX = wiFiBand.getChannelFirstHidden();
        int maxX = minX + GraphViewBuilder.CNT_X - 1;

        return graphViewBuilder
                .setLabelFormatter(new AxisLabel(wiFiBand.getChannelFirst(), wiFiBand.getChannelLast()).setEvenOnly(options))
                .setVerticalTitle(resources.getString(R.string.graph_axis_y))
                .setHorizontalTitle(resources.getString(R.string.graph_channel_axis_x))
                .setScrollable(options)
                .setMinX(minX)
                .setMaxX(maxX)
                .build();
    }

    void update(@NonNull WiFiData wiFiData) {
        Set<String> newSeries = new TreeSet<>();
        for (WiFiDetail wiFiDetail : wiFiData.getWiFiDetails(wiFiBand, mainContext.getSettings().getSortBy())) {
            addData(newSeries, wiFiDetail);
        }
        graphViewUtils.updateSeries(newSeries);
        graphViewUtils.updateLegend();
        graphViewUtils.setVisibility(wiFiBand);
    }

    private void addData(@NonNull Set<String> newSeries, @NonNull WiFiDetail wiFiDetail) {
        String key = wiFiDetail.getTitle();
        newSeries.add(key);
        LineGraphSeries<DataPoint> series = seriesMap.get(key);
        if (series == null) {
            series = new LineGraphSeries<>(createDataPoints(wiFiDetail));
            setSeriesOptions(series, wiFiDetail);
            graphView.addSeries(series);
            seriesMap.put(key, series);
        } else {
            series.resetData(createDataPoints(wiFiDetail));
        }
    }

    private void setSeriesOptions(@NonNull LineGraphSeries<DataPoint> series, @NonNull WiFiDetail wiFiDetail) {
        currentGraphColor = GraphColor.findColor(currentGraphColor);
        series.setColor(currentGraphColor.getPrimary());
        series.setBackgroundColor(currentGraphColor.getBackground());
        series.setDrawBackground(true);
        series.setTitle(wiFiDetail.getTitle() + " " + wiFiDetail.getWiFiSignal().getChannel());
    }

    private DataPoint[] createDataPoints(@NonNull WiFiDetail wiFiDetail) {
        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        int channel = wiFiSignal.getChannel();
        int channelStart = wiFiSignal.getChannelStart();
        int channelEnd = wiFiSignal.getChannelEnd();
        int level = wiFiSignal.getLevel();
        return new DataPoint[]{
                new DataPoint(channelStart, GraphViewBuilder.MIN_Y),
                new DataPoint(channelStart + 1, level),
                new DataPoint(channel, level),
                new DataPoint(channelEnd - 1, level),
                new DataPoint(channelEnd, GraphViewBuilder.MIN_Y)
        };
    }

    private void addDefaultsSeries(@NonNull GraphView graphView, @NonNull WiFiBand wiFiBand) {
        DataPoint[] dataPoints = new DataPoint[]{
                new DataPoint(wiFiBand.getChannelFirstHidden(), GraphViewBuilder.MIN_Y),
                new DataPoint(wiFiBand.getChannelLastHidden(), GraphViewBuilder.MIN_Y)
        };

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        series.setColor(GraphColor.TRANSPARENT.getPrimary());
        series.setDrawBackground(false);
        series.setThickness(0);
        series.setTitle("");
        graphView.addSeries(series);
    }
}
