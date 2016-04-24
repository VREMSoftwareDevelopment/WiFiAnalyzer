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

package com.vrem.wifianalyzer.settings;

import android.content.Context;
import android.preference.ListPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry;

import java.util.ArrayList;
import java.util.List;

public class CountryPreference extends ListPreference {
    public CountryPreference(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(@NonNull Context context) {
        List<WiFiChannelCountry> wiFiChannelCountries = WiFiChannelCountry.getAll();
        List<String> entries = new ArrayList<>(wiFiChannelCountries.size());
        List<String> entryValues = new ArrayList<>(wiFiChannelCountries.size());
        for (WiFiChannelCountry wiFiChannelCountry : wiFiChannelCountries) {
            entries.add(wiFiChannelCountry.getCountryName());
            entryValues.add(wiFiChannelCountry.getCountryCode());
        }
        setEntries(entries.toArray(new CharSequence[]{}));
        setEntryValues(entryValues.toArray(new CharSequence[]{}));

        String defaultValue = context.getResources().getConfiguration().locale.getCountry();
        setDefaultValue(defaultValue);
    }

}
