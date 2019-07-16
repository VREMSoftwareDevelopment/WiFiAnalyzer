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

import com.vrem.wifianalyzer.wifi.graphutils.GraphConstants;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;

class TimeGraphCache {
    private final Map<WiFiDetail, Integer> notSeen;

    TimeGraphCache() {
        this.notSeen = new HashMap<>();
    }

    @NonNull
    Set<WiFiDetail> active() {
        return new HashSet<>(CollectionUtils.select(notSeen.keySet(), new SeenPredicate()));
    }

    void clear() {
        IterableUtils.forEach(CollectionUtils.select(notSeen.keySet(), new NotSeenPredicate()), new RemoveClosure());
    }

    void add(@NonNull WiFiDetail wiFiDetail) {
        Integer currentCount = notSeen.get(wiFiDetail);
        if (currentCount == null) {
            currentCount = 0;
        }
        currentCount++;
        notSeen.put(wiFiDetail, currentCount);
    }

    void reset(@NonNull WiFiDetail wiFiDetail) {
        Integer currentCount = notSeen.get(wiFiDetail);
        if (currentCount != null) {
            notSeen.put(wiFiDetail, 0);
        }
    }

    @NonNull
    Set<WiFiDetail> getWiFiDetails() {
        return notSeen.keySet();
    }

    private class RemoveClosure implements Closure<WiFiDetail> {
        @Override
        public void execute(WiFiDetail wiFiDetail) {
            notSeen.remove(wiFiDetail);
        }
    }

    private class SeenPredicate implements Predicate<WiFiDetail> {
        @Override
        public boolean evaluate(WiFiDetail object) {
            return notSeen.get(object) <= GraphConstants.MAX_NOTSEEN_COUNT;
        }
    }

    private class NotSeenPredicate implements Predicate<WiFiDetail> {
        @Override
        public boolean evaluate(WiFiDetail object) {
            return notSeen.get(object) > GraphConstants.MAX_NOTSEEN_COUNT;
        }
    }

}
