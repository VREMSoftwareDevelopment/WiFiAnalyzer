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
import android.support.annotation.RawRes;

import com.vrem.wifianalyzer.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class VendorDB {
    private static final int SIZE = 6;
    private final Resources resources;
    private Map<String, List<String>> vendors;
    private Map<String, String> macs;

    VendorDB(@NonNull Resources resources) {
        this.resources = resources;
    }

    String findVendorName(String address) {
        load();
        return macs.get(address);
    }

    List<String> findMacAddresses(String vendorName) {
        load();
        return vendors.get(vendorName);
    }

    Map<String, String> getMacs() {
        load();
        return macs;
    }

    Map<String, List<String>> getVendors() {
        load();
        return vendors;
    }

    private String[] readFile(@NonNull Resources resources, @RawRes int id) {
        InputStream inputStream = null;
        try {
            inputStream = resources.openRawResource(id);
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            int count = inputStream.read(bytes);
            if (count != size) {
                return new String[]{};
            }
            return new String(bytes).split("\n");
        } catch (Exception e) {
            // file is corrupted
            return new String[]{};
        } finally {
            close(inputStream);
        }
    }

    private void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    private void load() {
        if (vendors != null) {
            return;
        }
        vendors = new HashMap<>();
        macs = new HashMap<>();
        String[] lines = readFile(resources, R.raw.data);
        for (String data : lines) {
            if (data != null) {
                String[] parts = data.split("\\|");
                if (parts.length == 2) {
                    List<String> addresses = new ArrayList<>();
                    String name = parts[0];
                    vendors.put(name, addresses);
                    int length = parts[1].length();
                    for (int i = 0; i < length; i += SIZE) {
                        String mac = parts[1].substring(i, i + SIZE);
                        addresses.add(mac);
                        macs.put(mac, name);
                    }
                }
            }
        }
    }

}
