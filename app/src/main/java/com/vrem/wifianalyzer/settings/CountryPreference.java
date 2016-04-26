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

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CountryPreference extends ListPreference {
    public CountryPreference(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(@NonNull Context context) {
        List<Data> datas = getSortedDatas();
        setEntries(getNames(datas));
        setEntryValues(getCodes(datas));
        setDefaultValue(context.getResources().getConfiguration().locale.getCountry());
    }

    private CharSequence[] getCodes(List<Data> datas) {
        List<String> entryValues = new ArrayList<>();
        for (Data data: datas) {
            entryValues.add(data.getCode());
        }
        return entryValues.toArray(new CharSequence[]{});
    }

    private CharSequence[] getNames(List<Data> datas) {
        List<String> entries = new ArrayList<>();
        for (Data data: datas) {
            entries.add(data.getName());
        }
        return entries.toArray(new CharSequence[]{});
    }

    private List<Data> getSortedDatas() {
        List<Data> datas = new ArrayList<>();
        for (WiFiChannelCountry wiFiChannelCountry : WiFiChannelCountry.getAll()) {
            datas.add(new Data(wiFiChannelCountry.getCountryCode(), wiFiChannelCountry.getCountryName()));
        }
        Collections.sort(datas);
        return datas;
    }

    class Data implements Comparable<Data> {
        private final String code;
        private final String name;

        public Data(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        @Override
        public int compareTo(@NonNull Data another) {
            return new CompareToBuilder()
                .append(getName(), another.getName())
                .append(getCode(), another.getCode())
                .toComparison();
        }

    }
}
