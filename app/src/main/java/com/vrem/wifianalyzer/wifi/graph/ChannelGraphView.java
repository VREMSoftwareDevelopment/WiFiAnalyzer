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
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannels;
import com.vrem.wifianalyzer.wifi.graph.wrapper.GraphColor;
import com.vrem.wifianalyzer.wifi.graph.wrapper.GraphViewBuilder;
import com.vrem.wifianalyzer.wifi.graph.wrapper.GraphViewWrapper;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import java.util.Set;
import java.util.TreeSet;

class ChannelGraphView {
    private final MainContext mainContext = MainContext.INSTANCE;

    private final WiFiBand wiFiBand;
    private final GraphViewWrapper graphViewWrapper;

    ChannelGraphView(@NonNull WiFiBand wiFiBand) {
        this.wiFiBand = wiFiBand;
        this.graphViewWrapper = new GraphViewWrapper(
                makeGraphView(), mainContext.getSettings().getChannelGraphLegend(), wiFiBand);
        initialize();
    }

    private GraphView makeGraphView() {
        Resources resources = mainContext.getContext().getResources();
        return new GraphViewBuilder(mainContext.getContext())
                .setLabelFormatter(new ChannelAxisLabel(wiFiBand, resources))
                .setVerticalTitle(resources.getString(R.string.graph_axis_y))
                .setHorizontalTitle(resources.getString(R.string.graph_channel_axis_x))
                .build();
    }

    void update(@NonNull WiFiData wiFiData) {
        Set<WiFiDetail> newSeries = new TreeSet<>();
        for (WiFiDetail wiFiDetail : wiFiData.getWiFiDetails(wiFiBand, mainContext.getSettings().getSortBy())) {
            newSeries.add(wiFiDetail);
            addData(wiFiDetail);
        }
        graphViewWrapper.removeSeries(newSeries);
        graphViewWrapper.updateLegend(mainContext.getSettings().getChannelGraphLegend());
        graphViewWrapper.setVisibility(wiFiBand);
    }

    private void addData(@NonNull WiFiDetail wiFiDetail) {
        DataPoint[] dataPoints = createDataPoints(wiFiDetail);
        ChannelGraphSeries<DataPoint> series = new ChannelGraphSeries<>(dataPoints);
        if (graphViewWrapper.addSeries(wiFiDetail, series, dataPoints)) {
            GraphColor graphColor = graphViewWrapper.getColor();
            series.setColor(graphColor.getPrimary());
            series.setBackgroundColor(graphColor.getBackground());
        }
    }

    private DataPoint[] createDataPoints(@NonNull WiFiDetail wiFiDetail) {
        int frequencySpread = wiFiBand.getWiFiChannels().getFrequencySpread();
        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        int frequency = wiFiSignal.getFrequency();
        int frequencyStart = wiFiSignal.getFrequencyStart();
        int frequencyEnd = wiFiSignal.getFrequencyEnd();
        int level = wiFiSignal.getLevel();
        return new DataPoint[]{
                new DataPoint(frequencyStart, GraphViewBuilder.MIN_Y),
                new DataPoint(frequencyStart + frequencySpread, level),
                new DataPoint(frequency, level),
                new DataPoint(frequencyEnd - frequencySpread, level),
                new DataPoint(frequencyEnd, GraphViewBuilder.MIN_Y)
        };
    }

    private void initialize() {
        WiFiChannels wiFiChannels = wiFiBand.getWiFiChannels();
        int frequencyOffset = wiFiBand.getWiFiChannels().getFrequencyOffset();
        DataPoint[] dataPoints = new DataPoint[]{
                new DataPoint(wiFiChannels.getWiFiChannelFirst().getFrequency() - frequencyOffset, GraphViewBuilder.MIN_Y),
                new DataPoint(wiFiChannels.getWiFiChannelLast().getFrequency() + frequencyOffset, GraphViewBuilder.MIN_Y)
        };

        ChannelGraphSeries<DataPoint> series = new ChannelGraphSeries<>(dataPoints);
        series.setColor(GraphColor.TRANSPARENT.getPrimary());
        series.zeroThickness();
        graphViewWrapper.addSeries(series);
    }

    GraphView getGraphView() {
        return graphViewWrapper.getGraphView();
    }
}
