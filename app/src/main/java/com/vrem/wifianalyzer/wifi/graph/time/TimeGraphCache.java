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

import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.wifi.graph.tools.GraphConstants;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class TimeGraphCache implements GraphConstants {
    private final Map<WiFiDetail, Integer> notSeen;

    TimeGraphCache() {
        this.notSeen = new HashMap<>();
    }

    Set<WiFiDetail> active() {
        return new HashSet<>(CollectionUtils.select(notSeen.keySet(), new SeenPredicate()));
    }

    void clear() {
        Set<WiFiDetail> toClear = new HashSet<>(CollectionUtils.select(notSeen.keySet(), new NotSeenPredicate()));
        for (WiFiDetail wiFiDetail : toClear) {
            if (notSeen.containsKey(wiFiDetail)) {
                notSeen.remove(wiFiDetail);
            }
        }
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

    Set<WiFiDetail> getWiFiDetails() {
        return notSeen.keySet();
    }

    private class SeenPredicate implements Predicate<WiFiDetail> {
        @Override
        public boolean evaluate(WiFiDetail object) {
            return notSeen.get(object) <= MAX_NOTSEEN_COUNT;
        }
    }

    private class NotSeenPredicate implements Predicate<WiFiDetail> {
        @Override
        public boolean evaluate(WiFiDetail object) {
            return notSeen.get(object) > MAX_NOTSEEN_COUNT;
        }
    }

}
