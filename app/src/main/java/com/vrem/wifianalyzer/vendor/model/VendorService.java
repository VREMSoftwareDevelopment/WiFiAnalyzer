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

package com.vrem.wifianalyzer.vendor.model;

import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.MainContext;

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
        String result = MainContext.INSTANCE.getDatabase().find(macAddress);
        if (result != null) {
            result = cleanVendorName(result);
            cache.put(key, result);
            return result;
        }
        if (!remoteCalls.contains(key)) {
            remoteCalls.add(key);
            getRemoteCall().execute(macAddress);
        }
        return StringUtils.EMPTY;
    }

    protected void clear() {
        cache.clear();
        remoteCalls.clear();
    }

    public SortedMap<String, List<String>> findAll() {
        SortedMap<String, List<String>> results = new TreeMap<>();
        List<VendorData> vendorDatas = MainContext.INSTANCE.getDatabase().findAll();
        for (VendorData vendorData : vendorDatas) {
            String key = cleanVendorName(vendorData.getName());
            List<String> macs = results.get(key);
            if (macs == null) {
                macs = new ArrayList<>();
                results.put(key, macs);
            }
            macs.add(vendorData.getMac());
            Collections.sort(macs);
        }
        return results;
    }

    private String cleanVendorName(String name) {
        if (StringUtils.isEmpty(name)) {
            return StringUtils.EMPTY;
        }
        return name
                .replace(".", " ")
                .replace(",", " ")
                .replace("   ", " ")
                .replace("  ", " ")
                .trim()
                .toUpperCase();
    }

    // injectors start
    private RemoteCall getRemoteCall() {
        return remoteCall == null ? new RemoteCall() : remoteCall;
    }

    protected void setRemoteCall(@NonNull RemoteCall remoteCall) {
        this.remoteCall = remoteCall;
    }
    // injectors end
}
