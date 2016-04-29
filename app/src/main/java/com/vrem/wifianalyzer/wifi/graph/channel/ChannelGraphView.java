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

package com.vrem.wifianalyzer.wifi.graph.channel;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.TitleLineGraphSeries;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiChannels;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphColor;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphLegend;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphViewBuilder;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphViewNotifier;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphViewWrapper;
import com.vrem.wifianalyzer.wifi.model.SortBy;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import java.util.Set;
import java.util.TreeSet;

class ChannelGraphView implements GraphViewNotifier {
    private static final int CNT_X_SMALL_2 = 16;
    private static final int CNT_X_SMALL_5 = 18;
    private static final int CNT_X_LARGE = 24;

    private final WiFiBand wiFiBand;
    private final Pair<WiFiChannel, WiFiChannel> wiFiChannelPair;
    private GraphViewWrapper graphViewWrapper;

    ChannelGraphView(@NonNull WiFiBand wiFiBand, @NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
        this.wiFiBand = wiFiBand;
        this.wiFiChannelPair = wiFiChannelPair;
        this.graphViewWrapper = makeGraphViewWrapper();
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        Settings settings = MainContext.INSTANCE.getSettings();
        GraphLegend channelGraphLegend = settings.getChannelGraphLegend();
        SortBy sortBy = settings.getSortBy();
        Set<WiFiDetail> newSeries = new TreeSet<>();
        for (WiFiDetail wiFiDetail : wiFiData.getWiFiDetails(wiFiBand, sortBy)) {
            if (isInRange(wiFiDetail.getWiFiSignal().getFrequency(), wiFiChannelPair)) {
                newSeries.add(wiFiDetail);
                addData(wiFiDetail);
            }
        }
        graphViewWrapper.removeSeries(newSeries);
        graphViewWrapper.updateLegend(channelGraphLegend);
        graphViewWrapper.setVisibility(isSelected() ? View.VISIBLE : View.GONE);
    }

    private boolean isInRange(int frequency, Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
        return frequency >= wiFiChannelPair.first.getFrequency() && frequency <= wiFiChannelPair.second.getFrequency();
    }

    private boolean isSelected() {
        WiFiBand wiFiBand = MainContext.INSTANCE.getSettings().getWiFiBand();
        Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = MainContext.INSTANCE.getConfiguration().getWiFiChannelPair();
        return this.wiFiBand.equals(wiFiBand) && (WiFiBand.GHZ2.equals(this.wiFiBand) || this.wiFiChannelPair.equals(wiFiChannelPair));
    }

    private void addData(@NonNull WiFiDetail wiFiDetail) {
        DataPoint[] dataPoints = createDataPoints(wiFiDetail);
        TitleLineGraphSeries<DataPoint> series = new TitleLineGraphSeries<>(dataPoints);
        if (graphViewWrapper.addSeries(wiFiDetail, series, dataPoints)) {
            GraphColor graphColor = graphViewWrapper.getColor();
            series.setColor((int) graphColor.getPrimary());
            series.setBackgroundColor((int) graphColor.getBackground());
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

    @Override
    public GraphView getGraphView() {
        return graphViewWrapper.getGraphView();
    }

    private int getNumX() {
        int numX = CNT_X_LARGE;
        if (!MainContext.INSTANCE.getConfiguration().isLargeScreenLayout()) {
            numX = WiFiBand.GHZ2.equals(wiFiBand) ? CNT_X_SMALL_2 : CNT_X_SMALL_5;
        }
        int channelFirst = wiFiChannelPair.first.getChannel() - wiFiBand.getWiFiChannels().getChannelOffset();
        int channelLast = wiFiChannelPair.second.getChannel() + wiFiBand.getWiFiChannels().getChannelOffset();
        return Math.min(numX, channelLast - channelFirst + 1);
    }

    private GraphView makeGraphView() {
        Resources resources = MainContext.INSTANCE.getResources();
        return new GraphViewBuilder(MainContext.INSTANCE.getContext(), getNumX())
            .setLabelFormatter(new ChannelAxisLabel(wiFiBand, wiFiChannelPair))
                .setVerticalTitle(resources.getString(R.string.graph_axis_y))
                .setHorizontalTitle(resources.getString(R.string.graph_channel_axis_x))
                .build();
    }

    private GraphViewWrapper makeGraphViewWrapper() {
        graphViewWrapper = new GraphViewWrapper(makeGraphView(), MainContext.INSTANCE.getSettings().getChannelGraphLegend());

        WiFiChannels wiFiChannels = wiFiBand.getWiFiChannels();
        int frequencyOffset = wiFiChannels.getFrequencyOffset();
        int minX = wiFiChannelPair.first.getFrequency() - frequencyOffset;
        int maxX = minX + (graphViewWrapper.getViewportCntX() * wiFiChannels.getFrequencySpread());
        graphViewWrapper.setViewport(minX, maxX);

        DataPoint[] dataPoints = new DataPoint[]{
                new DataPoint(minX, GraphViewBuilder.MIN_Y),
                new DataPoint(wiFiChannelPair.second.getFrequency() + frequencyOffset, GraphViewBuilder.MIN_Y)
        };

        TitleLineGraphSeries<DataPoint> series = new TitleLineGraphSeries<>(dataPoints);
        series.setColor((int) GraphColor.TRANSPARENT.getPrimary());
        series.zeroThickness();
        graphViewWrapper.addSeries(series);
        return graphViewWrapper;
    }

    protected void setGraphViewWrapper(@NonNull GraphViewWrapper graphViewWrapper) {
        this.graphViewWrapper = graphViewWrapper;
    }

}
