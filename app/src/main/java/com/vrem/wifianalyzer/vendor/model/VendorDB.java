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
import org.apache.commons.collections4.Transformer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

class VendorDB {
    private static final int MIN_SIZE = 6;
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
        List<String> vendors = readFile(resources, R.raw.vendors);
        if (vendors.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> macs = readFile(resources, R.raw.macs);
        if (macs.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(new TreeSet<>(CollectionUtils.collect(macs, new ToVendorData(vendors))));
    }

    private List<String> readFile(@NonNull Resources resources, @RawRes int id) {
        InputStream inputStream = null;
        try {
            inputStream = resources.openRawResource(id);
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            int count = inputStream.read(bytes);
            if (count != size) {
                return Collections.emptyList();
            }
            return Arrays.asList(new String(bytes).split("\n"));
        } catch (Exception e) {
            // file is corrupted
            return Collections.emptyList();
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

    private class ToVendorData implements Transformer<String, VendorData> {
        private final List<String> vendors;

        private ToVendorData(@NonNull List<String> vendors) {
            this.vendors = vendors;
        }

        @Override
        public VendorData transform(String mac) {
            try {
                if (mac.length() < MIN_SIZE) {
                    return VendorData.EMPTY;
                }
                int index = Integer.parseInt(mac.substring(MIN_SIZE));
                return new VendorData(vendors.get(index), mac.substring(0, MIN_SIZE));
            } catch (Exception e) {
                return VendorData.EMPTY;
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
