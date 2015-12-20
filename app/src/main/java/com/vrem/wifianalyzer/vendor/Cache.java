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

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.TreeMap;

enum Cache implements VendorCache {
    INSTANCE;

    private final Map<String, String> cache = new TreeMap<>();

    private RemoteCall remoteCall;

    Cache() {
        // Prepopulate Small Cache Set
        cache.put("00026F", "Senao International Co., Ltd.");
        cache.put("10FEED", "TP-LINK TECHNOLOGIES CO., LTD.");
        cache.put("20AA4B", "Cisco-Linksys, LLC");
        cache.put("20CF30", "ASUSTek COMPUTER INC.");
        cache.put("44E9DD", "SAGEMCOM SAS");
        cache.put("54B80A", "D-Link International");
        cache.put("586D8F", "Cisco-Linksys, LLC");
        cache.put("681590", "SAGEMCOM SAS");
        cache.put("6C3BE5", "Hewlett Packard");
        cache.put("6CB0CE", "NETGEAR");
        cache.put("6CCA08", "ARRIS Group, Inc.");
        cache.put("744401", "NETGEAR");
        cache.put("788DF7", "Hitron Technologies. Inc");
        cache.put("84948C", "Hitron Technologies. Inc");
        cache.put("B00594", "Liteon Technology Corporation");
        cache.put("BC1401", "Hitron Technologies. Inc");
        cache.put("BC4DFB", "Hitron Technologies. Inc");
        cache.put("E840F2", "PEGATRON CORPORATION");
    }

    @Override
    public String find(@NonNull String macAddress) {
        String request = macAddress.toUpperCase();
        String result = cache.get(MacAddress.clean(macAddress));
        if (result != null) {
            return result;
        }
        add(macAddress, StringUtils.EMPTY);
        getRemoteCall().execute(request);
        return StringUtils.EMPTY;
    }

    @Override
    public void add(@NonNull String macAddress, @NonNull String vendorName) {
        cache.put(MacAddress.clean(macAddress), vendorName);
    }

    RemoteCall getRemoteCall() {
        return remoteCall == null ? new RemoteCall() : remoteCall;
    }

    void setRemoteCall(@NonNull RemoteCall remoteCall) {
        this.remoteCall = remoteCall;
    }

    void clear() {
        cache.clear();
    }
}
