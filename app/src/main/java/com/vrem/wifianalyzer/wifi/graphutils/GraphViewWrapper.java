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

import android.graphics.Color;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.settings.ThemeStyle;
import com.vrem.wifianalyzer.wifi.accesspoint.AccessPointDetail;
import com.vrem.wifianalyzer.wifi.accesspoint.AccessPointPopup;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;

public class GraphViewWrapper {
    private final GraphView graphView;
    private final ThemeStyle themeStyle;
    private GraphLegend graphLegend;
    private SeriesCache seriesCache;
    private SeriesOptions seriesOptions;

    public GraphViewWrapper(@NonNull GraphView graphView, @NonNull GraphLegend graphLegend, @NonNull ThemeStyle themeStyle) {
        this.graphView = graphView;
        this.graphLegend = graphLegend;
        this.themeStyle = themeStyle;
        setSeriesCache(new SeriesCache());
        setSeriesOptions(new SeriesOptions());
    }

    void setSeriesCache(@NonNull SeriesCache seriesCache) {
        this.seriesCache = seriesCache;
    }

    void setSeriesOptions(@NonNull SeriesOptions seriesOptions) {
        this.seriesOptions = seriesOptions;
    }

    public void removeSeries(@NonNull Set<WiFiDetail> newSeries) {
        IterableUtils.forEach(seriesCache.remove(differenceSeries(newSeries)), new RemoveClouser());
    }

    @NonNull
    public List<WiFiDetail> differenceSeries(@NonNull Set<WiFiDetail> newSeries) {
        return seriesCache.difference(newSeries);
    }

    public boolean addSeries(@NonNull WiFiDetail wiFiDetail, @NonNull BaseSeries<DataPoint> series, @NonNull Boolean drawBackground) {
        if (seriesCache.contains(wiFiDetail)) {
            return false;
        }
        seriesCache.put(wiFiDetail, series);
        series.setTitle(wiFiDetail.getSSID() + " " + wiFiDetail.getWiFiSignal().getChannelDisplay());
        series.setOnDataPointTapListener(new GraphTapListener());
        seriesOptions.highlightConnected(series, wiFiDetail.getWiFiAdditional().getWiFiConnection().isConnected());
        seriesOptions.setSeriesColor(series);
        seriesOptions.drawBackground(series, drawBackground);
        graphView.addSeries(series);
        return true;
    }

    public boolean updateSeries(@NonNull WiFiDetail wiFiDetail, @NonNull DataPoint[] data, @NonNull Boolean drawBackground) {
        if (!seriesCache.contains(wiFiDetail)) {
            return false;
        }
        BaseSeries<DataPoint> series = seriesCache.get(wiFiDetail);
        series.resetData(data);
        seriesOptions.highlightConnected(series, wiFiDetail.getWiFiAdditional().getWiFiConnection().isConnected());
        seriesOptions.drawBackground(series, drawBackground);
        return true;
    }

    public boolean appendToSeries(@NonNull WiFiDetail wiFiDetail, @NonNull DataPoint data, @NonNull Integer count, @NonNull Boolean drawBackground) {
        if (!seriesCache.contains(wiFiDetail)) {
            return false;
        }
        BaseSeries<DataPoint> series = seriesCache.get(wiFiDetail);
        series.appendData(data, true, count + 1);
        seriesOptions.highlightConnected(series, wiFiDetail.getWiFiAdditional().getWiFiConnection().isConnected());
        seriesOptions.drawBackground(series, drawBackground);
        return true;
    }

    public boolean isNewSeries(@NonNull WiFiDetail wiFiDetail) {
        return !seriesCache.contains(wiFiDetail);
    }

    public void setViewport() {
        Viewport viewport = graphView.getViewport();
        viewport.setMinX(0);
        viewport.setMaxX(getViewportCntX());
    }

    public void setViewport(int minX, int maxX) {
        Viewport viewport = graphView.getViewport();
        viewport.setMinX(minX);
        viewport.setMaxX(maxX);
    }

    public int getViewportCntX() {
        return graphView.getGridLabelRenderer().getNumHorizontalLabels() - 1;
    }

    public void addSeries(@NonNull BaseSeries series) {
        graphView.addSeries(series);
    }

    public void updateLegend(@NonNull GraphLegend graphLegend) {
        resetLegendRenderer(graphLegend);
        LegendRenderer legendRenderer = graphView.getLegendRenderer();
        legendRenderer.resetStyles();
        legendRenderer.setWidth(0);
        legendRenderer.setTextSize(graphView.getTitleTextSize());
        legendRenderer.setTextColor(ThemeStyle.DARK.equals(themeStyle) ? Color.WHITE : Color.BLACK);
        graphLegend.display(legendRenderer);
    }

    private void resetLegendRenderer(@NonNull GraphLegend graphLegend) {
        if (!this.graphLegend.equals(graphLegend)) {
            graphView.setLegendRenderer(newLegendRenderer());
            this.graphLegend = graphLegend;
        }
    }

    public int calculateGraphType() {
        try {
            String graphType = BuildConfig.APPLICATION_ID;
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(graphType.getBytes());
            return Arrays.hashCode(messageDigest.digest());
        } catch (Exception e) {
            return GraphConstants.TYPE1;
        }
    }

    public void setHorizontalLabelsVisible(boolean horizontalLabelsVisible) {
        getGraphView().getGridLabelRenderer().setHorizontalLabelsVisible(horizontalLabelsVisible);
    }

    public void setVisibility(int visibility) {
        graphView.setVisibility(visibility);
    }

    public GraphView getGraphView() {
        return graphView;
    }

    public int getSize(int value) {
        return value == GraphConstants.TYPE1 || value == GraphConstants.TYPE2 || value == GraphConstants.TYPE3
            ? Configuration.SIZE_MAX : Configuration.SIZE_MIN;
    }

    LegendRenderer newLegendRenderer() {
        return new LegendRenderer(graphView);
    }

    GraphLegend getGraphLegend() {
        return graphLegend;
    }

    private class RemoveClouser implements Closure<BaseSeries<DataPoint>> {
        @Override
        public void execute(BaseSeries<DataPoint> series) {
            seriesOptions.removeSeriesColor(series);
            graphView.removeSeries(series);
        }
    }

    class GraphTapListener implements OnDataPointTapListener {
        @Override
        public void onTap(@NonNull Series series, @NonNull DataPointInterface dataPoint) {
            WiFiDetail wiFiDetail = seriesCache.find(series);
            if (wiFiDetail != null) {
                View popupView = getAccessPointDetail().makeViewDetailed(wiFiDetail);
                getAccessPointPopup().show(popupView);
            }
        }

        @NonNull
        private AccessPointPopup getAccessPointPopup() {
            return new AccessPointPopup();
        }

        @NonNull
        private AccessPointDetail getAccessPointDetail() {
            return new AccessPointDetail();
        }
    }
}
