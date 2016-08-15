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
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CountryPreference extends CustomPreference {
    public CountryPreference(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs, getData(), getDefault(context));
    }

    private static List<Data> getData() {
        List<Data> result = new ArrayList<>();
        for (WiFiChannelCountry wiFiChannelCountry : WiFiChannelCountry.getAll()) {
            result.add(new Data(wiFiChannelCountry.getCountryCode(), wiFiChannelCountry.getCountryName()));
        }
        Collections.sort(result);
        return result;
    }

    private static String getDefault(@NonNull Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        return configuration.locale.getCountry();
    }
}
