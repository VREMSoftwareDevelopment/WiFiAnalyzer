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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

class Country {
    private final List<Locale> countries;

    public Country() {
        countries = new ArrayList<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            String countryCode = locale.getCountry();
            if (StringUtils.isNotEmpty(countryCode)) {
                countries.add(locale);
            }
        }
        Collections.sort(countries, new LocaleCountryComparator());
    }

    public Locale getCountry(@NonNull String countryCode) {
        Locale country = new Locale("", countryCode);
        int index = Collections.binarySearch(countries, country, new LocaleCountryComparator());
        if (index < 0) {
            return country;
        }
        return countries.get(index);
    }

    public List<Locale> getCountries() {
        return Collections.unmodifiableList(countries);
    }

    private class LocaleCountryComparator implements Comparator<Locale> {
        @Override
        public int compare(Locale lhs, Locale rhs) {
            return lhs.getCountry().compareTo(rhs.getCountry());
        }
    }

}
