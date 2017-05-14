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
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CountryPreference extends CustomPreference {
    public CountryPreference(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs, getData(), getDefault(context));
    }

    @NonNull
    private static List<Data> getData() {
        List<Data> results = new ArrayList<>(CollectionUtils.collect(WiFiChannelCountry.getAll(), new ToData()));
        Collections.sort(results);
        return results;
    }

    @NonNull
    public static String getDefault(@NonNull Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        return getLocale(configuration).getCountry();
    }

    @SuppressWarnings("deprecation")
    @NonNull
    private static Locale getLocale(@NonNull Configuration config) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return config.getLocales().get(0);
        }
        return config.locale;
    }

    private static class ToData implements Transformer<WiFiChannelCountry, Data> {
        @Override
        public Data transform(WiFiChannelCountry input) {
            return new Data(input.getCountryCode(), input.getCountryName());
        }
    }

}
