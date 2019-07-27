/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

import androidx.annotation.NonNull;

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
        Locale locale = Locale.getDefault();
        return result.substring(0, Math.min(result.length(), MAX_SIZE)).toUpperCase(locale);
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

}
