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

package com.vrem.wifianalyzer.wifi.channelgraph;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.TitleLineGraphSeries;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiChannels;
import com.vrem.wifianalyzer.wifi.graphutils.GraphConstants;
import com.vrem.wifianalyzer.wifi.graphutils.GraphViewWrapper;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

class DataManager {
    @NonNull
    Set<WiFiDetail> getNewSeries(@NonNull List<WiFiDetail> wiFiDetails, @NonNull Pair<WiFiChannel, WiFiChannel> wiFiChannelPair) {
        return new TreeSet<>(CollectionUtils.select(wiFiDetails, new InRangePredicate(wiFiChannelPair)));
    }

    @NonNull
    DataPoint[] getDataPoints(@NonNull WiFiDetail wiFiDetail, int levelMax) {
        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        int frequencyStart = wiFiSignal.getFrequencyStart();
        int frequencyEnd = wiFiSignal.getFrequencyEnd();
        int level = Math.min(wiFiSignal.getLevel(), levelMax);
        return new DataPoint[]{
            new DataPoint(frequencyStart, GraphConstants.MIN_Y),
            new DataPoint(frequencyStart + WiFiChannels.FREQUENCY_SPREAD, level),
            new DataPoint(wiFiSignal.getCenterFrequency(), level),
            new DataPoint(frequencyEnd - WiFiChannels.FREQUENCY_SPREAD, level),
            new DataPoint(frequencyEnd, GraphConstants.MIN_Y)
        };
    }

    void addSeriesData(@NonNull GraphViewWrapper graphViewWrapper, @NonNull Set<WiFiDetail> wiFiDetails, int levelMax) {
        IterableUtils.forEach(wiFiDetails, new SeriesClosure(graphViewWrapper, levelMax));
    }

    private class SeriesClosure implements Closure<WiFiDetail> {
        private final GraphViewWrapper graphViewWrapper;
        private final int levelMax;

        private SeriesClosure(GraphViewWrapper graphViewWrapper, int levelMax) {
            this.graphViewWrapper = graphViewWrapper;
            this.levelMax = levelMax;
        }

        @Override
        public void execute(WiFiDetail wiFiDetail) {
            DataPoint[] dataPoints = getDataPoints(wiFiDetail, levelMax);
            if (graphViewWrapper.isNewSeries(wiFiDetail)) {
                graphViewWrapper.addSeries(wiFiDetail, new TitleLineGraphSeries<>(dataPoints), true);
            } else {
                graphViewWrapper.updateSeries(wiFiDetail, dataPoints, true);
            }
        }
    }

}
