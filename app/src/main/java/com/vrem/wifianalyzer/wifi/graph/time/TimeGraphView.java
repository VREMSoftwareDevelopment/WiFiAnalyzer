/*
 * WiFi Analyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.graph.time;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphColor;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphViewBuilder;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphViewNotifier;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphViewWrapper;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import java.util.Set;
import java.util.TreeSet;

class TimeGraphView implements GraphViewNotifier {
    private static final int MAX_SCAN_COUNT = 400;
    private static final int NUM_X_SMALL = 18;
    private static final int NUM_X_LARGE = 24;
    private static final int THICKNESS_INVISIBLE = 0;

    private final WiFiBand wiFiBand;
    private GraphViewWrapper graphViewWrapper;
    private int scanCount;
    private int xValue;

    TimeGraphView(@NonNull WiFiBand wiFiBand) {
        this.wiFiBand = wiFiBand;
        this.scanCount = this.xValue = 0;
        this.graphViewWrapper = makeGraphViewWrapper();
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        Settings settings = MainContext.INSTANCE.getSettings();
        Set<WiFiDetail> newSeries = new TreeSet<>();
        for (WiFiDetail wiFiDetail : wiFiData.getWiFiDetails(wiFiBand, settings.getSortBy())) {
            newSeries.add(wiFiDetail);
            addData(wiFiDetail);
        }
        graphViewWrapper.removeSeries(newSeries);
        graphViewWrapper.updateLegend(settings.getTimeGraphLegend());
        graphViewWrapper.setVisibility(isSelected() ? View.VISIBLE : View.GONE);
        xValue++;
        if (scanCount < MAX_SCAN_COUNT) {
            scanCount++;
        }
    }

    private boolean isSelected() {
        return wiFiBand.equals(MainContext.INSTANCE.getSettings().getWiFiBand());
    }

    private void addData(@NonNull WiFiDetail wiFiDetail) {
        DataPoint dataPoint = new DataPoint(xValue, wiFiDetail.getWiFiSignal().getLevel());
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{dataPoint});
        if (graphViewWrapper.appendSeries(wiFiDetail, series, dataPoint, scanCount)) {
            series.setColor((int) graphViewWrapper.getColor().getPrimary());
            series.setDrawBackground(false);
        }
    }

    @Override
    public GraphView getGraphView() {
        return graphViewWrapper.getGraphView();
    }

    private int getNumX() {
        Configuration configuration = MainContext.INSTANCE.getConfiguration();
        return configuration.isLargeScreen() ? NUM_X_LARGE : NUM_X_SMALL;
    }

    void setGraphViewWrapper(@NonNull GraphViewWrapper graphViewWrapper) {
        this.graphViewWrapper = graphViewWrapper;
    }

    private GraphView makeGraphView(@NonNull MainActivity mainActivity, int graphMaximumY) {
        Resources resources = mainActivity.getResources();
        return new GraphViewBuilder(mainActivity, getNumX(), graphMaximumY)
            .setLabelFormatter(new TimeAxisLabel())
            .setVerticalTitle(resources.getString(R.string.graph_axis_y))
            .setHorizontalTitle(resources.getString(R.string.graph_time_axis_x))
            .build();
    }

    private GraphViewWrapper makeGraphViewWrapper() {
        MainContext mainContext = MainContext.INSTANCE;
        MainActivity mainActivity = mainContext.getMainActivity();
        Settings settings = mainContext.getSettings();
        GraphView graphView = makeGraphView(mainActivity, settings.getGraphMaximumY());
        graphViewWrapper = new GraphViewWrapper(graphView, settings.getTimeGraphLegend());

        graphViewWrapper.setViewport();

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
            new DataPoint(0, GraphViewBuilder.MIN_Y),
            new DataPoint(getNumX() - 1, GraphViewBuilder.MIN_Y)
        });
        series.setColor((int) GraphColor.TRANSPARENT.getPrimary());
        series.setThickness(THICKNESS_INVISIBLE);
        graphViewWrapper.addSeries(series);

        return graphViewWrapper;
    }

}
