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

package com.vrem.wifianalyzer.settings;

import android.content.Context;
import android.preference.ListPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

class CustomPreference extends ListPreference {
    CustomPreference(@NonNull Context context, AttributeSet attrs, @NonNull List<Data> datas, @NonNull String defaultValue) {
        super(context, attrs);
        setEntries(getNames(datas));
        setEntryValues(getCodes(datas));
        setDefaultValue(defaultValue);
    }

    @NonNull
    private CharSequence[] getCodes(@NonNull List<Data> datas) {
        List<String> entryValues = new ArrayList<>();
        for (Data data : datas) {
            entryValues.add(data.getCode());
        }
        return entryValues.toArray(new CharSequence[]{});
    }

    @NonNull
    private CharSequence[] getNames(@NonNull List<Data> datas) {
        List<String> entries = new ArrayList<>();
        for (Data data : datas) {
            entries.add(data.getName());
        }
        return entries.toArray(new CharSequence[]{});
    }

}
