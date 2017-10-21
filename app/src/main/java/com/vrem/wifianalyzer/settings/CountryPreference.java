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

import com.vrem.util.ConfigUtils;
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CountryPreference extends CustomPreference {
    public CountryPreference(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs, getData(), ConfigUtils.getDefaultCountryCode(context));
    }

    @NonNull
    private static List<Data> getData() {
        List<Data> results = new ArrayList<>(CollectionUtils.collect(WiFiChannelCountry.getAll(), new ToData()));
        Collections.sort(results);
        return results;
    }

    private static class ToData implements Transformer<WiFiChannelCountry, Data> {
        @Override
        public Data transform(WiFiChannelCountry input) {
            return new Data(input.getCountryCode(), input.getCountryName());
        }
    }

}
