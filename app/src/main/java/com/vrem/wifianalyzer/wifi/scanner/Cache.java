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

package com.vrem.wifianalyzer.wifi.scanner;

import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.MainContext;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

class Cache {
    private static final int ADJUST = 10;
    private final Deque<List<ScanResult>> cache = new ArrayDeque<>();

    List<CacheResult> getScanResults() {
        ScanResult current = null;
        int levelTotal = 0;
        int count = 0;
        List<CacheResult> results = new ArrayList<>();
        for (ScanResult scanResult : combineCache()) {
            if (current != null && !scanResult.BSSID.equals(current.BSSID)) {
                CacheResult cacheResult = getCacheResult(current, levelTotal, count);
                results.add(cacheResult);
                count = 0;
                levelTotal = 0;
            }
            current = scanResult;
            count++;
            levelTotal += scanResult.level;
        }
        if (current != null) {
            results.add(getCacheResult(current, levelTotal, count));
        }
        return results;
    }

    @NonNull
    private CacheResult getCacheResult(ScanResult current, int level, int count) {
        CacheResult cacheResult;
        if (isSizeAvailable()) {
            cacheResult = new CacheResult(current, level / count);
        } else {
            cacheResult = new CacheResult(current, (level - ADJUST) / count);
        }
        return cacheResult;
    }

    private List<ScanResult> combineCache() {
        List<ScanResult> scanResults = new ArrayList<>();
        IterableUtils.forEach(cache, new CacheClosure(scanResults));
        Collections.sort(scanResults, new ScanResultComparator());
        return scanResults;
    }

    void add(List<ScanResult> scanResults) {
        int cacheSize = getCacheSize();
        while (cache.size() >= cacheSize) {
            cache.pollLast();
        }
        if (scanResults != null) {
            cache.addFirst(scanResults);
        }
    }

    Deque<List<ScanResult>> getCache() {
        return cache;
    }

    int getCacheSize() {
        if (isSizeAvailable()) {
            int scanInterval = MainContext.INSTANCE.getSettings().getScanInterval();
            if (scanInterval < 5) {
                return 4;
            }
            if (scanInterval < 10) {
                return 3;
            }
            if (scanInterval < 20) {
                return 2;
            }
        }
        return 1;
    }

    private boolean isSizeAvailable() {
        try {
            return MainContext.INSTANCE.getConfiguration().isSizeAvailable();
        } catch (Exception e) {
            return false;
        }
    }

    private static class ScanResultComparator implements Comparator<ScanResult> {
        @Override
        public int compare(ScanResult lhs, ScanResult rhs) {
            return new CompareToBuilder()
                .append(lhs.BSSID, rhs.BSSID)
                .append(lhs.level, rhs.level)
                .toComparison();
        }
    }

    private class CacheClosure implements Closure<List<ScanResult>> {
        private final List<ScanResult> scanResults;

        private CacheClosure(@NonNull List<ScanResult> scanResults) {
            this.scanResults = scanResults;
        }

        @Override
        public void execute(List<ScanResult> cachedScanResults) {
            scanResults.addAll(cachedScanResults);
        }
    }
}
