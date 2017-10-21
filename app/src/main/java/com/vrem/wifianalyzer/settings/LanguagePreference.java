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

package com.vrem.wifianalyzer.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.vrem.util.ConfigurationUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class LanguagePreference extends CustomPreference {
    public LanguagePreference(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs, getData(), ConfigurationUtils.getDefaultLanguageTag(context));
    }

    @NonNull
    private static List<Data> getData() {
        TreeSet<LanguageCountry> uniqueLanguageCountries = new TreeSet<LanguageCountry>(new LanguageCountryComparator());
        uniqueLanguageCountries.addAll(LanguageCountry.getAll());
        List<Data> results = new ArrayList<>(CollectionUtils.collect(uniqueLanguageCountries, new ToDataLanguage()));
        Collections.sort(results);
        return results;
    }

    private static class ToDataLanguage implements Transformer<LanguageCountry, Data> {
        @Override
        public Data transform(LanguageCountry input) {
            return new Data(input.getLanguageTag(), input.getLanguageName());
        }
    }

    private static class LanguageCountryComparator implements Comparator<LanguageCountry> {
        @Override
        public int compare(LanguageCountry o1, LanguageCountry o2) {
            return o1.getLanguageTag().compareTo(o2.getLanguageTag());
        }
    }
}
