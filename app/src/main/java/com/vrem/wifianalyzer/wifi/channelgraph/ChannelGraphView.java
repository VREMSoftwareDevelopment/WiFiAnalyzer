/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.channelgraph;

import android.content.res.Resources;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.TitleLineGraphSeries;
import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.settings.ThemeStyle;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiChannels;
import com.vrem.wifianalyzer.wifi.graphutils.GraphColorsKt;
import com.vrem.wifianalyzer.wifi.graphutils.GraphDataPoint;
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
import androidx.core.util.Pair;

import static com.vrem.wifianalyzer.wifi.graphutils.GraphConstantsKt.MIN_Y;
import static com.vrem.wifianalyzer.wifi.graphutils.GraphConstantsKt.THICKNESS_INVISIBLE;

class ChannelGraphView implements GraphViewNotifier {
    private final WiFiBand wiFiBand;
    private final Pair<WiFiChannel, WiFiChannel> wiFiChannelPair;
    private GraphViewWrapper graphViewWrapper;
    private DataManager dataManager;

    ChannelGraphView(@NonNull WiFiBand wiFiBand, @NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
        this.wiFiBand = wiFiBand;
        this.wiFiChannelPair = wiFiChannelPair;
        this.graphViewWrapper = makeGraphViewWrapper();
        this.dataManager = new DataManager();
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        Settings settings = MainContext.INSTANCE.settings;
        Predicate<WiFiDetail> predicate = FilterPredicate.makeOtherPredicate(settings);
        List<WiFiDetail> wiFiDetails = wiFiData.wiFiDetails(predicate, settings.sortBy());
        Set<WiFiDetail> newSeries = dataManager.getNewSeries(wiFiDetails, wiFiChannelPair);
        dataManager.addSeriesData(graphViewWrapper, newSeries, settings.graphMaximumY());
        graphViewWrapper.removeSeries(newSeries);
        graphViewWrapper.updateLegend(settings.channelGraphLegend());
        graphViewWrapper.visibility(isSelected() ? View.VISIBLE : View.GONE);
    }

    private boolean isSelected() {
        Settings settings = MainContext.INSTANCE.settings;
        WiFiBand currentWiFiBand = settings.wiFiBand();
        Configuration configuration = MainContext.INSTANCE.configuration;
        Pair<WiFiChannel, WiFiChannel> currentWiFiChannelPair = configuration.getWiFiChannelPair();
        return wiFiBand.equals(currentWiFiBand) && (WiFiBand.GHZ2.equals(wiFiBand) || wiFiChannelPair.equals(currentWiFiChannelPair));
    }

    @Override
    @NonNull
    public GraphView graphView() {
        return graphViewWrapper.getGraphView();
    }

    private int getNumX() {
        int channelFirst = wiFiChannelPair.first.getChannel() - WiFiChannels.CHANNEL_OFFSET;
        int channelLast = wiFiChannelPair.second.getChannel() + WiFiChannels.CHANNEL_OFFSET;
        return channelLast - channelFirst + 1;
    }

    @NonNull
    private GraphView makeGraphView(@NonNull MainContext mainContext, int graphMaximumY, @NonNull ThemeStyle themeStyle) {
        Resources resources = mainContext.getResources();
        return new GraphViewBuilder(getNumX(), graphMaximumY, themeStyle, true)
            .setLabelFormatter(new ChannelAxisLabel(wiFiBand, wiFiChannelPair))
            .setVerticalTitle(resources.getString(R.string.graph_axis_y))
            .setHorizontalTitle(resources.getString(R.string.graph_channel_axis_x))
            .build(mainContext.getContext());
    }

    @NonNull
    private GraphViewWrapper makeGraphViewWrapper() {
        MainContext mainContext = MainContext.INSTANCE;
        Settings settings = mainContext.settings;
        Configuration configuration = mainContext.configuration;
        ThemeStyle themeStyle = settings.themeStyle();
        int graphMaximumY = settings.graphMaximumY();
        GraphView graphView = makeGraphView(mainContext, graphMaximumY, themeStyle);
        graphViewWrapper = new GraphViewWrapper(graphView, settings.channelGraphLegend(), themeStyle);
        configuration.setSize(graphViewWrapper.size(graphViewWrapper.calculateGraphType()));
        int minX = wiFiChannelPair.first.getFrequency() - WiFiChannels.FREQUENCY_OFFSET;
        int maxX = minX + (graphViewWrapper.getViewportCntX() * WiFiChannels.FREQUENCY_SPREAD);
        graphViewWrapper.setViewport(minX, maxX);
        graphViewWrapper.addSeries(makeDefaultSeries(wiFiChannelPair.second.getFrequency(), minX));
        return graphViewWrapper;
    }

    private TitleLineGraphSeries<GraphDataPoint> makeDefaultSeries(int frequencyEnd, int minX) {
        GraphDataPoint[] dataPoints = new GraphDataPoint[]{
            new GraphDataPoint(minX, MIN_Y),
            new GraphDataPoint(frequencyEnd + WiFiChannels.FREQUENCY_OFFSET, MIN_Y)
        };

        TitleLineGraphSeries<GraphDataPoint> series = new TitleLineGraphSeries<>(dataPoints);
        series.setColor((int) GraphColorsKt.getTransparent().getPrimary());
        series.setThickness(THICKNESS_INVISIBLE);
        return series;
    }

    void setGraphViewWrapper(@NonNull GraphViewWrapper graphViewWrapper) {
        this.graphViewWrapper = graphViewWrapper;
    }

    void setDataManager(@NonNull DataManager dataManager) {
        this.dataManager = dataManager;
    }

}
