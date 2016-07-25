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

import java.util.ArrayList;
import java.util.List;

class CustomPreference extends ListPreference {
    CustomPreference(@NonNull Context context, AttributeSet attrs, List<Data> datas, String defaultValue) {
        super(context, attrs);
        setEntries(getNames(datas));
        setEntryValues(getCodes(datas));
        setDefaultValue(defaultValue);
    }

    private CharSequence[] getCodes(List<Data> datas) {
        List<String> entryValues = new ArrayList<>();
        for (Data data : datas) {
            entryValues.add(data.getCode());
        }
        return entryValues.toArray(new CharSequence[]{});
    }

    private CharSequence[] getNames(List<Data> datas) {
        List<String> entries = new ArrayList<>();
        for (Data data : datas) {
            entries.add(data.getName());
        }
        return entries.toArray(new CharSequence[]{});
    }

}
