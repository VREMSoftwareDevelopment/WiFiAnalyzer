/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.vrem.wifianalyzer.wifi.graph.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;

public class GraphViewBuilder {
    public static final int MIN_Y = -100;
    public static final int MAX_Y = -20;
    static final int NUM_Y = (MAX_Y - MIN_Y) / 10 + 1;

    private final Context content;
    private final int numHorizontalLabels;
    private final LayoutParams layoutParams;
    private LabelFormatter labelFormatter;
    private String verticalTitle;
    private String horizontalTitle;

    public GraphViewBuilder(@NonNull Context content, int numHorizontalLabels) {
        this.content = content;
        this.numHorizontalLabels = numHorizontalLabels;
        this.layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public GraphViewBuilder setLabelFormatter(@NonNull LabelFormatter labelFormatter) {
        this.labelFormatter = labelFormatter;
        return this;
    }

    public GraphViewBuilder setVerticalTitle(@NonNull String verticalTitle) {
        this.verticalTitle = verticalTitle;
        return this;
    }

    public GraphViewBuilder setHorizontalTitle(@NonNull String horizontalTitle) {
        this.horizontalTitle = horizontalTitle;
        return this;
    }

    public GraphView build() {
        GraphView graphView = new GraphView(content);
        setGraphView(graphView);
        setGridLabelRenderer(graphView);
        setViewPortY(graphView);
        return graphView;
    }

    LayoutParams getLayoutParams() {
        return layoutParams;
    }

    void setGraphView(@NonNull GraphView graphView) {
        graphView.setLayoutParams(layoutParams);
        graphView.setVisibility(View.GONE);
    }

    void setViewPortY(@NonNull GraphView graphView) {
        Viewport viewport = graphView.getViewport();
        viewport.setScrollable(true);
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(MIN_Y);
        viewport.setMaxY(MAX_Y);
        viewport.setXAxisBoundsManual(true);
    }

    void setGridLabelRenderer(@NonNull GraphView graphView) {
        GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
        gridLabelRenderer.setHighlightZeroLines(false);
        gridLabelRenderer.setNumVerticalLabels(NUM_Y);
        gridLabelRenderer.setNumHorizontalLabels(numHorizontalLabels);

        if (labelFormatter != null) {
            gridLabelRenderer.setLabelFormatter(labelFormatter);
        }

        if (verticalTitle != null) {
            gridLabelRenderer.setVerticalAxisTitle(verticalTitle);
            gridLabelRenderer.setVerticalLabelsVisible(true);
        } else {
            gridLabelRenderer.setVerticalLabelsVisible(false);
        }

        if (horizontalTitle != null) {
            gridLabelRenderer.setHorizontalAxisTitle(horizontalTitle);
            gridLabelRenderer.setHorizontalLabelsVisible(true);
        } else {
            gridLabelRenderer.setHorizontalLabelsVisible(false);
        }
    }
}
