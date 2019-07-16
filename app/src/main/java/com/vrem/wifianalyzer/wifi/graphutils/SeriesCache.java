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

import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.Series;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import androidx.annotation.NonNull;

class SeriesCache {
    private final Map<WiFiDetail, BaseSeries<DataPoint>> cache;

    SeriesCache() {
        this.cache = new TreeMap<>();
    }

    @NonNull
    List<WiFiDetail> difference(@NonNull Set<WiFiDetail> series) {
        Set<WiFiDetail> difference = new TreeSet<>(cache.keySet());
        difference.removeAll(series);
        return new ArrayList<>(difference);
    }

    @NonNull
    List<BaseSeries<DataPoint>> remove(@NonNull List<WiFiDetail> series) {
        List<BaseSeries<DataPoint>> removeSeries = new ArrayList<>();
        IterableUtils.forEach(CollectionUtils.select(series, new RemovePredicate()), new RemoveClosure(removeSeries));
        return removeSeries;
    }

    WiFiDetail find(@NonNull Series series) {
        return IterableUtils.find(cache.keySet(), new FindPredicate(series));
    }

    boolean contains(@NonNull WiFiDetail wiFiDetail) {
        return cache.containsKey(wiFiDetail);
    }

    @NonNull
    BaseSeries<DataPoint> get(@NonNull WiFiDetail wiFiDetail) {
        return cache.get(wiFiDetail);
    }

    @NonNull
    BaseSeries<DataPoint> put(@NonNull WiFiDetail wiFiDetail, @NonNull BaseSeries<DataPoint> series) {
        return cache.put(wiFiDetail, series);
    }

    private class RemoveClosure implements Closure<WiFiDetail> {
        private final List<BaseSeries<DataPoint>> removeSeries;

        private RemoveClosure(List<BaseSeries<DataPoint>> removeSeries) {
            this.removeSeries = removeSeries;
        }

        @Override
        public void execute(WiFiDetail wiFiDetail) {
            removeSeries.add(cache.get(wiFiDetail));
            cache.remove(wiFiDetail);
        }
    }

    private class RemovePredicate implements Predicate<WiFiDetail> {
        @Override
        public boolean evaluate(WiFiDetail wiFiDetail) {
            return cache.containsKey(wiFiDetail);
        }
    }

    private class FindPredicate implements Predicate<WiFiDetail> {
        private final Series series;

        private FindPredicate(@NonNull Series series) {
            this.series = series;
        }

        @Override
        public boolean evaluate(WiFiDetail wiFiDetail) {
            return series.equals(cache.get(wiFiDetail));
        }
    }
}
