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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class VendorService {
    private static final int MAX_LEN = 6;

    private Set<Integer> cache;
    private VendorDB vendorDB;

    public VendorService(@NonNull Resources resources) {
        this.cache = new TreeSet<>();
        this.vendorDB = new VendorDB(resources);
    }

    public String findVendorName(Integer vendorIndex) {
        return vendorDB.findVendorName(vendorIndex);
    }

    public String findVendorName(@NonNull String macAddress) {
        int index = vendorDB.findVendorIndex(clean(macAddress));
        String vendorName = vendorDB.findVendorName(index);
        if (StringUtils.isNotEmpty(vendorName)) {
            cache.add(index);
        }
        return vendorName;
    }

    public List<Integer> findVendorIndexes() {
        return new ArrayList<>(cache);
    }

    public List<String> findMacAddresses(Integer vendorIndex) {
        return vendorDB.findMacAddresses(vendorIndex);
    }

    String clean(@NonNull String macAddress) {
        String result = macAddress.replace(":", "");
        return result.substring(0, Math.min(result.length(), MAX_LEN)).toUpperCase();
    }

    void setVendorDB(VendorDB vendorDB) {
        this.vendorDB = vendorDB;
    }
}
