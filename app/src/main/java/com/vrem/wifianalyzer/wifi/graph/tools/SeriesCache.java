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

    protected SeriesCache() {
        this.cache = new TreeMap<>();
    }

    protected BaseSeries<DataPoint> add(@NonNull WiFiDetail wiFiDetail, @NonNull BaseSeries<DataPoint> series) {
        if (!contains(wiFiDetail)) {
            cache.put(wiFiDetail, series);
        }
        return cache.get(wiFiDetail);
    }

    protected List<BaseSeries<DataPoint>> remove(@NonNull Set<WiFiDetail> series) {
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

    protected WiFiDetail find(@NonNull Series series) {
        for (WiFiDetail wiFiDetail : cache.keySet()) {
            if (series.equals(cache.get(wiFiDetail))) {
                return wiFiDetail;
            }
        }
        return null;
    }

    protected boolean contains(@NonNull WiFiDetail wiFiDetail) {
        return cache.containsKey(wiFiDetail);
    }
}
