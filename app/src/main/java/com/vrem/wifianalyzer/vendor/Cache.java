/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.vendor;

import android.support.annotation.NonNull;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

enum Cache implements VendorCache {
    INSTANCE;

    private final Set<VendorData> datas = new TreeSet<>();
    private final FastCache fastCache = new FastCache();
    private RemoteCall remoteCall;

    Cache() {
        // Prepopulate Small Cache Set
        datas.add(new VendorData("00026F000000", "00026FFFFFFF", "Senao International Co., Ltd."));
        datas.add(new VendorData("20AA4B000000", "20AA4BFFFFFF", "Cisco-Linksys, LLC"));
        datas.add(new VendorData("20CF30000000", "20CF30FFFFFF", "ASUSTek COMPUTER INC."));
        datas.add(new VendorData("586D8F000000", "586D8FFFFFFF", "Cisco-Linksys, LLC"));
        datas.add(new VendorData("681590000000", "681590FFFFFF", "SAGEMCOM SAS"));
        datas.add(new VendorData("6CB0CE000000", "6CB0CEFFFFFF", "NETGEAR"));
        datas.add(new VendorData("84948C000000", "84948CFFFFFF", "Hitron Technologies. Inc"));
        datas.add(new VendorData("B00594000000", "B00594FFFFFF", "Liteon Technology Corporation"));
        datas.add(new VendorData("E840F2000000", "E840F2FFFFFF", "PEGATRON CORPORATION"));
    }

    @Override
    public VendorData find(@NonNull String macAddress) {
        macAddress = macAddress.toUpperCase();
        VendorData result = fastCache.get(macAddress);
        if (result != null) {
            return result;
        }
        for (VendorData vendorData : datas) {
            if (vendorData.inRange(macAddress)) {
                fastCache.put(macAddress, vendorData);
                return vendorData;
            }
        }
        result = VendorData.EMPTY;
        fastCache.put(macAddress, result);
        getRemoteCall().execute(macAddress);
        return result;
    }

    @Override
    public void add(@NonNull String macAddress, @NonNull VendorData vendorData) {
        datas.add(vendorData);
        fastCache.put(macAddress, vendorData);
    }

    RemoteCall getRemoteCall() {
        return remoteCall == null ? new RemoteCall() : remoteCall;
    }

    void setRemoteCall(@NonNull RemoteCall remoteCall) {
        this.remoteCall = remoteCall;
    }

    void clear() {
        fastCache.clear();
        datas.clear();
    }

    private class FastCache {
        public static final int MAX_KEY_LENGTH = 8;

        private final Map<String, VendorData> cache = new TreeMap<>();

        String key(@NonNull String macAddress) {
            return macAddress.substring(0, Math.min(MAX_KEY_LENGTH, macAddress.length()));
        }

        void put(@NonNull String macAddress, @NonNull VendorData vendorData) {
            cache.put(key(macAddress), vendorData);
        }

        VendorData get(@NonNull String macAddress) {
            return cache.get(key(macAddress));
        }

        void clear() {
            cache.clear();
        }
    }

}
