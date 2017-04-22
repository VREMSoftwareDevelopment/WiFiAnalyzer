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

package com.vrem.wifianalyzer.vendor.model;

import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.MainContext;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class VendorService {
    private final Set<String> remoteCalls = new TreeSet<>();
    private final Map<String, String> cache = new HashMap<>();

    private RemoteCall remoteCall;

    public String findVendorName(String macAddress) {
        String key = MacAddress.clean(macAddress);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        Database database = MainContext.INSTANCE.getDatabase();
        String result = database.find(macAddress);
        if (result != null) {
            result = VendorNameUtils.cleanVendorName(result);
            cache.put(key, result);
            return result;
        }
        if (!remoteCalls.contains(key)) {
            remoteCalls.add(key);
            getRemoteCall().execute(macAddress);
        }
        return StringUtils.EMPTY;
    }

    void clear() {
        cache.clear();
        remoteCalls.clear();
    }

    public SortedMap<String, List<String>> findAll() {
        SortedMap<String, List<String>> results = new TreeMap<>();
        IterableUtils.forEach(MainContext.INSTANCE.getDatabase().findAll(), new VendorClosure(results));
        return results;
    }

    // injectors start
    private RemoteCall getRemoteCall() {
        return remoteCall == null ? new RemoteCall() : remoteCall;
    }

    void setRemoteCall(@NonNull RemoteCall remoteCall) {
        this.remoteCall = remoteCall;
    }
    // injectors end

    private class VendorClosure implements Closure<VendorData> {
        private final SortedMap<String, List<String>> vendorDatas;

        private VendorClosure(@NonNull SortedMap<String, List<String>> vendorDatas) {
            this.vendorDatas = vendorDatas;
        }

        @Override
        public void execute(VendorData vendorData) {
            String key = VendorNameUtils.cleanVendorName(vendorData.getName());
            List<String> macs = vendorDatas.get(key);
            if (macs == null) {
                macs = new ArrayList<>();
                vendorDatas.put(key, macs);
            }
            macs.add(vendorData.getMac());
            Collections.sort(macs);
        }
    }
}
