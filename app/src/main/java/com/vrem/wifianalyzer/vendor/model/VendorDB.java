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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class VendorDB {
    private final List<VendorData> data;

    VendorDB(@NonNull Resources resources) {
        data = readVendorData(resources);
    }

    List<VendorData> findByAddress(String address) {
        return new ArrayList<>(CollectionUtils.select(data, new AddressPredicate(address)));
    }

    List<VendorData> findByName(String name) {
        return new ArrayList<>(CollectionUtils.select(data, new NamePredicate(name)));
    }

    List<VendorData> findAll() {
        return data;
    }

    private List<VendorData> readVendorData(@NonNull Resources resources) {
        List<VendorData> results = new ArrayList<>();
        String[] vendors = readFile(resources, R.raw.vendors);
        if (vendors.length > 0) {
            String[] macs = readFile(resources, R.raw.macs);
            for (String mac : macs) {
                try {
                    String macAddress = mac.substring(0, 6);
                    int index = Integer.parseInt(mac.substring(6));
                    String vendorName = vendors[index];
                    results.add(new VendorData(vendorName, macAddress));
                } catch (Exception e) {
                    // skip any entry with errors
                }
            }
        }
        return results;
    }

    private String[] readFile(@NonNull Resources resources, @RawRes int id) {
        InputStream inputStream = null;
        try {
            inputStream = resources.openRawResource(id);
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            int count = inputStream.read(bytes);
            if (count != size) {
                // file is corrupted
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

    private class AddressPredicate implements Predicate<VendorData> {
        private final String address;

        private AddressPredicate(@NonNull String address) {
            this.address = address;
        }

        @Override
        public boolean evaluate(VendorData vendorData) {
            return vendorData.getMac().equals(address);
        }
    }

    private class NamePredicate implements Predicate<VendorData> {
        private final String name;

        private NamePredicate(String name) {
            this.name = name;
        }

        @Override
        public boolean evaluate(VendorData vendorData) {
            return vendorData.getName().equals(name);
        }
    }

}
