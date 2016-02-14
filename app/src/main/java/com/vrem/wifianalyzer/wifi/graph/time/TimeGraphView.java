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

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.WiFiConstants;
import com.vrem.wifianalyzer.wifi.model.WiFiBand;

class TimeGraphView {
    static final int TIME_LABEL_CNT = 10;

    private final View parentView;
    private final Resources resources;
    private final int channelGraphViewId;
    private final WiFiBand wifiBand;

    private GraphView graphView;
    private TimeGraphAdapter timeGraphAdapter;

    TimeGraphView(@NonNull View parentView, @NonNull Resources resources, @NonNull WiFiBand wifiBand) {
        this.parentView = parentView;
        this.resources = resources;
        this.wifiBand = wifiBand;
        this.channelGraphViewId = WiFiBand.TWO.equals(wifiBand) ? R.id.timeGraph2 : R.id.timeGraph5;
    }

    static TimeGraphView timeGraphView2(@NonNull View parentView, @NonNull Resources resources) {
        return new TimeGraphView(parentView, resources, WiFiBand.TWO);
    }

    static TimeGraphView timeGraphView5(@NonNull View parentView, @NonNull Resources resources) {
        return new TimeGraphView(parentView, resources, WiFiBand.FIVE);
    }

    TimeGraphView make() {
        graphView = (GraphView) parentView.findViewById(this.channelGraphViewId);
        graphView.setMinimumWidth(TIME_LABEL_CNT);

        GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
        gridLabelRenderer.setHighlightZeroLines(false);
        gridLabelRenderer.setLabelFormatter(new TimeGraphAxisLabel());
        gridLabelRenderer.setNumVerticalLabels(WiFiConstants.CNT_Y);
        gridLabelRenderer.setNumHorizontalLabels(TIME_LABEL_CNT);

        gridLabelRenderer.setHorizontalAxisTitle(resources.getString(R.string.graph_time));
        gridLabelRenderer.setHorizontalLabelsVisible(true);

        gridLabelRenderer.setVerticalAxisTitle(resources.getString(R.string.graph_signal_strength));
        gridLabelRenderer.setVerticalLabelsVisible(true);

        Viewport viewport = graphView.getViewport();
        viewport.setScrollable(true);
        viewport.setScalable(false);

        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(WiFiConstants.MIN_Y);
        viewport.setMaxY(WiFiConstants.MAX_Y);

        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(TIME_LABEL_CNT);

        timeGraphAdapter = new TimeGraphAdapter(graphView, wifiBand);

        return this;
    }

    GraphView getGraphView() {
        return graphView;
    }

    TimeGraphAdapter getTimeGraphAdapter() {
        return timeGraphAdapter;
    }
}
