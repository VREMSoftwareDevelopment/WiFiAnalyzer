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

class SeriesCache {
    private final Map<WiFiDetail, BaseSeries<DataPoint>> cache;

    SeriesCache() {
        this.cache = new TreeMap<>();
    }

    BaseSeries<DataPoint> add(@NonNull WiFiDetail wiFiDetail, @NonNull BaseSeries<DataPoint> series) {
        if (!contains(wiFiDetail)) {
            cache.put(wiFiDetail, series);
        }
        return cache.get(wiFiDetail);
    }

    List<BaseSeries<DataPoint>> remove(@NonNull Set<WiFiDetail> series) {
        List<BaseSeries<DataPoint>> removeSeries = new ArrayList<>();
        List<WiFiDetail> removeFromCache = new ArrayList<>();
        for (WiFiDetail wiFiDetail : cache.keySet()) {
            if (series.contains(wiFiDetail)) {
                continue;
            }
            removeSeries.add(cache.get(wiFiDetail));
            removeFromCache.add(wiFiDetail);
        }
        for (WiFiDetail wiFiDetail : removeFromCache) {
            cache.remove(wiFiDetail);
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
}
