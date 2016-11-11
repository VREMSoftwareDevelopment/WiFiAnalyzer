/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.band;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

class Country {
    private final SortedMap<String, Locale> countries;

    Country() {
        countries = new TreeMap<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            String countryCode = locale.getCountry();
            if (StringUtils.isNotEmpty(countryCode) && StringUtils.isAlpha(countryCode) && countryCode.length() == 2) {
                countries.put(StringUtils.capitalize(countryCode), locale);
            }
        }
    }

    Locale getCountry(@NonNull String countryCode) {
        String code = StringUtils.capitalize(countryCode);
        Locale country = countries.get(code);
        if (country == null) {
            country = new Locale("", code);
        }
        return country;
    }

    List<Locale> getCountries() {
        return new ArrayList<>(countries.values());
    }

}
