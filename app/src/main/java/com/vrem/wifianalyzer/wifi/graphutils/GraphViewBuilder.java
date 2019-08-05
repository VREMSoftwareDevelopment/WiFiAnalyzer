/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.graphutils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.vrem.wifianalyzer.settings.ThemeStyle;

import androidx.annotation.NonNull;

public class GraphViewBuilder {
    private final Context context;
    private final int numHorizontalLabels;
    private final int maximumY;
    private final ThemeStyle themeStyle;
    private final LayoutParams layoutParams;
    private LabelFormatter labelFormatter;
    private String verticalTitle;
    private String horizontalTitle;
    private boolean horizontalLabelsVisible;

    public GraphViewBuilder(@NonNull Context context, int numHorizontalLabels, int maximumY, @NonNull ThemeStyle themeStyle) {
        this.context = context;
        this.numHorizontalLabels = numHorizontalLabels;
        this.maximumY = (maximumY > GraphConstants.MAX_Y || maximumY < GraphConstants.MIN_Y_HALF)
            ? GraphConstants.MAX_Y_DEFAULT : maximumY;
        this.themeStyle = themeStyle;
        this.layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.horizontalLabelsVisible = true;
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

    public GraphViewBuilder setHorizontalLabelsVisible(boolean horizontalLabelsVisible) {
        this.horizontalLabelsVisible = horizontalLabelsVisible;
        return this;
    }

    public GraphView build() {
        GraphView graphView = new GraphView(context);
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
        viewport.setMinY(GraphConstants.MIN_Y);
        viewport.setMaxY(getMaximumY());
        viewport.setXAxisBoundsManual(true);
    }

    void setGridLabelRenderer(@NonNull GraphView graphView) {
        GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
        gridLabelRenderer.setHumanRounding(false);
        gridLabelRenderer.setHighlightZeroLines(false);
        gridLabelRenderer.setNumVerticalLabels(getNumVerticalLabels());
        gridLabelRenderer.setNumHorizontalLabels(numHorizontalLabels);
        gridLabelRenderer.setVerticalLabelsVisible(true);
        gridLabelRenderer.setHorizontalLabelsVisible(horizontalLabelsVisible);
        gridLabelRenderer.setTextSize(gridLabelRenderer.getTextSize() * GraphConstants.TEXT_SIZE_ADJUSTMENT);

        gridLabelRenderer.reloadStyles();
        if (labelFormatter != null) {
            gridLabelRenderer.setLabelFormatter(labelFormatter);
        }
        if (verticalTitle != null) {
            gridLabelRenderer.setVerticalAxisTitle(verticalTitle);
            gridLabelRenderer.setVerticalAxisTitleTextSize(
                gridLabelRenderer.getVerticalAxisTitleTextSize() * GraphConstants.AXIS_TEXT_SIZE_ADJUSTMENT);
        }
        if (horizontalTitle != null) {
            gridLabelRenderer.setHorizontalAxisTitle(horizontalTitle);
            gridLabelRenderer.setHorizontalAxisTitleTextSize(
                gridLabelRenderer.getHorizontalAxisTitleTextSize() * GraphConstants.AXIS_TEXT_SIZE_ADJUSTMENT);
        }

        setGridLabelRenderColors(gridLabelRenderer);
    }

    private void setGridLabelRenderColors(@NonNull GridLabelRenderer gridLabelRenderer) {
        int color = ThemeStyle.DARK.equals(themeStyle) ? Color.WHITE : Color.BLACK;
        gridLabelRenderer.setGridColor(Color.GRAY);
        gridLabelRenderer.setVerticalLabelsColor(color);
        gridLabelRenderer.setVerticalAxisTitleColor(color);
        gridLabelRenderer.setHorizontalLabelsColor(color);
        gridLabelRenderer.setHorizontalAxisTitleColor(color);
    }

    int getNumVerticalLabels() {
        return (getMaximumY() - GraphConstants.MIN_Y) / 10 + 1;
    }

    int getMaximumY() {
        return maximumY;
    }

}
