/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.util.AttributeSet;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;

class CustomPreference extends ListPreference {
    CustomPreference(@NonNull Context context, AttributeSet attrs, @NonNull List<Data> data, @NonNull String defaultValue) {
        super(context, attrs);
        setEntries(getNames(data));
        setEntryValues(getCodes(data));
        setDefaultValue(defaultValue);
    }

    @NonNull
    private CharSequence[] getCodes(@NonNull List<Data> data) {
        return new ArrayList<>(CollectionUtils.collect(data, new ToCode())).toArray(new CharSequence[]{});
    }

    @NonNull
    private CharSequence[] getNames(@NonNull List<Data> data) {
        return new ArrayList<>(CollectionUtils.collect(data, new ToName())).toArray(new CharSequence[]{});
    }

    private static class ToCode implements Transformer<Data, String> {
        @Override
        public String transform(Data input) {
            return input.getCode();
        }
    }

    private static class ToName implements Transformer<Data, String> {
        @Override
        public String transform(Data input) {
            return input.getName();
        }
    }
}
