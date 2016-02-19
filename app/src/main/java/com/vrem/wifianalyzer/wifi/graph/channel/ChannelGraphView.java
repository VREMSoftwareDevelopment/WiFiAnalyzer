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
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.WiFiConstants;
import com.vrem.wifianalyzer.wifi.model.Frequency;

class ChannelGraphView {
    private final View parentView;
    private final Resources resources;
    private final Frequency frequency;

    private GraphView graphView;
    private ChannelGraphAdapter channelGraphAdapter;

    ChannelGraphView(@NonNull View parentView, @NonNull Resources resources, @NonNull Frequency frequency) {
        this.parentView = parentView;
        this.resources = resources;
        this.frequency = frequency;
    }

    static ChannelGraphView channelGraphView2(@NonNull View parentView, @NonNull Resources resources) {
        return new ChannelGraphView(parentView, resources, Frequency.TWO);
    }

    static ChannelGraphView channelGraphView5(@NonNull View parentView, @NonNull Resources resources) {
        return new ChannelGraphView(parentView, resources, Frequency.FIVE);
    }

    ChannelGraphView make() {
        graphView = (GraphView) parentView.findViewById(Frequency.TWO.equals(frequency) ? R.id.channelGraph2 : R.id.channelGraph5);
        graphView.setMinimumWidth(WiFiConstants.CNT_X);

        GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
        gridLabelRenderer.setHighlightZeroLines(false);
        gridLabelRenderer.setLabelFormatter(new ChannelGraphAxisLabel(frequency));
        gridLabelRenderer.setNumVerticalLabels(WiFiConstants.CNT_Y);
        gridLabelRenderer.setNumHorizontalLabels(WiFiConstants.CNT_X);

        gridLabelRenderer.setHorizontalAxisTitle(resources.getString(R.string.graph_wifi_channels));
        gridLabelRenderer.setHorizontalLabelsVisible(true);

        gridLabelRenderer.setVerticalAxisTitle(resources.getString(R.string.graph_signal_strength));
        gridLabelRenderer.setVerticalLabelsVisible(true);

        Viewport viewport = graphView.getViewport();
        viewport.setScrollable(Frequency.FIVE.equals(frequency));
        viewport.setScalable(false);

        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(WiFiConstants.MIN_Y);
        viewport.setMaxY(WiFiConstants.MAX_Y);

        viewport.setXAxisBoundsManual(true);
        int boundsMin = frequency.getChannelFirst() - Frequency.CHANNEL_SPREAD;
        viewport.setMinX(boundsMin);
        viewport.setMaxX(boundsMin + WiFiConstants.CNT_X - 1);

        channelGraphAdapter = new ChannelGraphAdapter(graphView, frequency);

        return this;
    }

    GraphView getGraphView() {
        return graphView;
    }

    ChannelGraphAdapter getChannelGraphAdapter() {
        return channelGraphAdapter;
    }
}
