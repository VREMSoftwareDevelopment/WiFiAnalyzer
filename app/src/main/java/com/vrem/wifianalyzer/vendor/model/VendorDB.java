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

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class VendorDB {
    static final int ID_INVALID = -1;
    private static final int MIN_SIZE = 6;

    private final String[] vendors;
    private final String[] macs;

    VendorDB(@NonNull Resources resources) {
        vendors = readFile(resources, R.raw.vendors);
        macs = readFile(resources, R.raw.macs);
    }

    int findVendorIndex(String address) {
        int index = Arrays.binarySearch(macs, address, new MacAddressComparator());
        if (index < 0) {
            return ID_INVALID;
        }
        return toIndex(macs[index]);
    }

    String findVendorName(Integer vendorIndex) {
        return validVendorIndex(vendorIndex) ? vendors[vendorIndex] : StringUtils.EMPTY;
    }

    List<String> findMacAddresses(Integer vendorIndex) {
        List<String> results = new ArrayList<>();
        if (validVendorIndex(vendorIndex)) {
            String[] macs = this.macs;
            for (String mac : macs) {
                if (vendorIndex == toIndex(mac)) {
                    results.add(toAddress(mac));
                }
            }
        }
        return results;
    }

    String[] getMacs() {
        return macs;
    }

    String[] getVendors() {
        return vendors;
    }

    String toAddress(@NonNull String source) {
        return source.length() < MIN_SIZE ? StringUtils.EMPTY : source.substring(0, MIN_SIZE);
    }

    int toIndex(@NonNull String source) {
        try {
            if (source.length() > MIN_SIZE) {
                return Integer.parseInt(source.substring(MIN_SIZE));
            }
            return ID_INVALID;
        } catch (Exception e) {
            return ID_INVALID;
        }
    }

    private boolean validVendorIndex(Integer vendorIndex) {
        return vendorIndex != null && vendorIndex >= 0 && vendorIndex < vendors.length;
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

    private static class MacAddressComparator implements Comparator<String> {
        @Override
        public int compare(String source, String address) {
            return source.substring(0, MIN_SIZE).compareTo(address);
        }
    }
}
