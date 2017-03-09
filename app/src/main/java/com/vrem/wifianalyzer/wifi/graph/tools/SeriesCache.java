/*
 * WiFiAnalyzer
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

package com.vrem.wifianalyzer.wifi.graph.tools;

import android.support.annotation.NonNull;

import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.Series;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

class SeriesCache {
    private final Map<WiFiDetail, BaseSeries<DataPoint>> cache;

    SeriesCache() {
        this.cache = new TreeMap<>();
    }

    List<WiFiDetail> difference(@NonNull Set<WiFiDetail> series) {
        Set<WiFiDetail> difference = new TreeSet<>(cache.keySet());
        difference.removeAll(series);
        return new ArrayList<>(difference);
    }

    List<BaseSeries<DataPoint>> remove(@NonNull List<WiFiDetail> series) {
        List<BaseSeries<DataPoint>> removeSeries = new ArrayList<>();
        for (WiFiDetail wiFiDetail : series) {
            if (cache.containsKey(wiFiDetail)) {
                removeSeries.add(cache.get(wiFiDetail));
                cache.remove(wiFiDetail);
            }
        }
        return removeSeries;
    }

    WiFiDetail find(@NonNull Series series) {
        for (WiFiDetail wiFiDetail : cache.keySet()) {
            if (series.equals(cache.get(wiFiDetail))) {
                return wiFiDetail;
            }
        }
        return null;
    }

    boolean contains(@NonNull WiFiDetail wiFiDetail) {
        return cache.containsKey(wiFiDetail);
    }

    BaseSeries<DataPoint> get(@NonNull WiFiDetail wiFiDetail) {
        return cache.get(wiFiDetail);
    }

    BaseSeries<DataPoint> put(WiFiDetail wiFiDetail, BaseSeries<DataPoint> series) {
        return cache.put(wiFiDetail, series);
    }
}
