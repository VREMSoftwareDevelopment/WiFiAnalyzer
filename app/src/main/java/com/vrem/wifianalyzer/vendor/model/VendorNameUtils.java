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

import org.apache.commons.lang3.StringUtils;

class VendorNameUtils {
    private static final int VENDOR_NAME_MAX = 50;

    static String cleanVendorName(String name) {
        if (StringUtils.isBlank(name) || name.contains("<") || name.contains(">")) {
            return StringUtils.EMPTY;
        }
        String result = name
            .replaceAll("[^a-zA-Z0-9]", " ")
            .replaceAll(" +", " ")
            .trim()
            .toUpperCase();

        return result.substring(0, Math.min(result.length(), VENDOR_NAME_MAX));
    }
}
