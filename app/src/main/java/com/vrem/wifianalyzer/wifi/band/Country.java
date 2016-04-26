/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.vrem.wifianalyzer.wifi.band;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class Country {
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
