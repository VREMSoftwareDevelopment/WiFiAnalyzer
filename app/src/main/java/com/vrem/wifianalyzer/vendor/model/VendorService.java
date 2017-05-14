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

import android.content.res.Resources;
import android.support.annotation.NonNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VendorService {
    private static final int MAX_LEN = 6;

    private Set<String> cache;
    private VendorDB vendorDB;

    public VendorService(@NonNull Resources resources) {
        this.cache = new HashSet<>();
        this.vendorDB = new VendorDB(resources);
    }

    public String findVendorName(@NonNull String macAddress) {
        String cleaned = clean(macAddress);
        List<VendorData> results = vendorDB.findByAddress(cleaned);
        String result = StringUtils.EMPTY;
        if (!results.isEmpty()) {
            result = results.get(0).getName();
            cache.add(result);
        }
        return result;
    }

    public List<String> findVendorNames() {
        return new ArrayList<>(cache);
    }

    public List<String> findMacAddresses(String vendorName) {
        return new ArrayList<>(CollectionUtils.collect(vendorDB.findByName(vendorName), new ToMacAddress()));
    }

    Set<String> getCache() {
        return cache;
    }

    String clean(@NonNull String macAddress) {
        String result = macAddress.replace(":", "");
        return result.substring(0, Math.min(result.length(), MAX_LEN)).toUpperCase();
    }

    void setVendorDB(VendorDB vendorDB) {
        this.vendorDB = vendorDB;
    }

    private class ToMacAddress implements Transformer<VendorData, String> {
        @Override
        public String transform(VendorData input) {
            return input.getMac();
        }
    }

}
