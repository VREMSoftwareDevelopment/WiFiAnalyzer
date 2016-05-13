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

package com.vrem.wifianalyzer.wifi.scanner;

import android.net.wifi.ScanResult;

import com.vrem.wifianalyzer.MainContext;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

class Cache {
    private final Deque<List<ScanResult>> cache = new ArrayDeque<>();

    protected List<CacheResult> getScanResults() {
        ScanResult current = null;
        int levelTotal = 0;
        int count = 0;
        List<CacheResult> results = new ArrayList<>();
        for (ScanResult scanResult : combineCache()) {
            if (current != null && !scanResult.BSSID.equals(current.BSSID)) {
                results.add(new CacheResult(current, levelTotal / count));
                count = 0;
                levelTotal = 0;
            }
            current = scanResult;
            count++;
            levelTotal += scanResult.level;
        }
        if (current != null) {
            results.add(new CacheResult(current, levelTotal / count));
        }
        return results;
    }

    private List<ScanResult> combineCache() {
        List<ScanResult> scanResults = new ArrayList<>();
        for (List<ScanResult> cachedScanResults : cache) {
            scanResults.addAll(cachedScanResults);
        }
        Collections.sort(scanResults, new ScanResultComparator());
        return scanResults;
    }

    protected void add(List<ScanResult> scanResults) {
        int cacheSize = getCacheSize();
        while (cache.size() >= cacheSize) {
            cache.pollLast();
        }
        if (scanResults != null) {
            cache.addFirst(scanResults);
        }
    }

    protected Deque<List<ScanResult>> getCache() {
        return cache;
    }

    protected int getCacheSize() {
        int scanInterval = MainContext.INSTANCE.getSettings().getScanInterval();
        if (scanInterval < 10) {
            return 3;
        }
        if (scanInterval < 20) {
            return 2;
        }
        return 1;
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
}
