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

package com.vrem.wifianalyzer.wifi.timegraph;

import android.content.res.Resources;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.settings.ThemeStyle;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.graphutils.GraphConstants;
import com.vrem.wifianalyzer.wifi.graphutils.GraphViewBuilder;
import com.vrem.wifianalyzer.wifi.graphutils.GraphViewNotifier;
import com.vrem.wifianalyzer.wifi.graphutils.GraphViewWrapper;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.predicate.FilterPredicate;

import org.apache.commons.collections4.Predicate;

import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;

class TimeGraphView implements GraphViewNotifier {
    private final WiFiBand wiFiBand;
    private DataManager dataManager;
    private GraphViewWrapper graphViewWrapper;

    TimeGraphView(@NonNull WiFiBand wiFiBand) {
        this.wiFiBand = wiFiBand;
        this.graphViewWrapper = makeGraphViewWrapper();
        this.dataManager = new DataManager();
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        Settings settings = MainContext.INSTANCE.getSettings();
        Predicate<WiFiDetail> predicate = FilterPredicate.makeOtherPredicate(settings);
        List<WiFiDetail> wiFiDetails = wiFiData.getWiFiDetails(predicate, settings.getSortBy());
        Set<WiFiDetail> newSeries = dataManager.addSeriesData(graphViewWrapper, wiFiDetails, settings.getGraphMaximumY());
        graphViewWrapper.removeSeries(newSeries);
        graphViewWrapper.updateLegend(settings.getTimeGraphLegend());
        graphViewWrapper.setVisibility(isSelected() ? View.VISIBLE : View.GONE);
    }

    private boolean isSelected() {
        return wiFiBand.equals(MainContext.INSTANCE.getSettings().getWiFiBand());
    }

    @Override
    @NonNull
    public GraphView getGraphView() {
        return graphViewWrapper.getGraphView();
    }

    private int getNumX() {
        return GraphConstants.NUM_X_TIME;
    }

    void setGraphViewWrapper(@NonNull GraphViewWrapper graphViewWrapper) {
        this.graphViewWrapper = graphViewWrapper;
    }

    void setDataManager(@NonNull DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @NonNull
    private GraphView makeGraphView(@NonNull MainContext mainContext, int graphMaximumY, @NonNull ThemeStyle themeStyle) {
        Resources resources = mainContext.getResources();
        return new GraphViewBuilder(mainContext.getContext(), getNumX(), graphMaximumY, themeStyle)
            .setLabelFormatter(new TimeAxisLabel())
            .setVerticalTitle(resources.getString(R.string.graph_axis_y))
            .setHorizontalTitle(resources.getString(R.string.graph_time_axis_x))
            .setHorizontalLabelsVisible(false)
            .build();
    }

    @NonNull
    private GraphViewWrapper makeGraphViewWrapper() {
        MainContext mainContext = MainContext.INSTANCE;
        Settings settings = mainContext.getSettings();
        ThemeStyle themeStyle = settings.getThemeStyle();
        int graphMaximumY = settings.getGraphMaximumY();
        Configuration configuration = mainContext.getConfiguration();
        GraphView graphView = makeGraphView(mainContext, graphMaximumY, themeStyle);
        graphViewWrapper = new GraphViewWrapper(graphView, settings.getTimeGraphLegend(), themeStyle);
        configuration.setSize(graphViewWrapper.getSize(graphViewWrapper.calculateGraphType()));
        graphViewWrapper.setViewport();
        return graphViewWrapper;
    }

}
