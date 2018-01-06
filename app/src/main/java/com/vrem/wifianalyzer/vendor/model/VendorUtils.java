/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

class VendorUtils {
    static final int MAX_SIZE = 6;
    private static final String SEPARATOR = ":";

    private VendorUtils() {
        throw new IllegalStateException("Utility class");
    }

    @NonNull
    static String clean(String macAddress) {
        if (macAddress == null) {
            return StringUtils.EMPTY;
        }
        String result = macAddress.replace(SEPARATOR, "");
        return result.substring(0, Math.min(result.length(), MAX_SIZE)).toUpperCase();
    }

    @NonNull
    static String toMacAddress(String source) {
        if (source == null) {
            return StringUtils.EMPTY;
        }
        if (source.length() < MAX_SIZE) {
            return "*" + source + "*";
        }
        return source.substring(0, 2)
            + SEPARATOR + source.substring(2, 4)
            + SEPARATOR + source.substring(4, 6);
    }

    @NonNull
    static String[] readFile(@NonNull Resources resources, @RawRes int id) {
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
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    // do nothing
                }
            }
        }
    }

}
