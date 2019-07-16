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

import com.vrem.util.LocaleUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

public class LanguagePreference extends CustomPreference {

    public LanguagePreference(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs, getData(), LocaleUtils.getDefaultLanguageTag());
    }

    @NonNull
    private static List<Data> getData() {
        List<Data> results = new ArrayList<>(CollectionUtils.collect(LocaleUtils.getSupportedLanguages(), new ToData()));
        Collections.sort(results);
        return results;
    }

    private static class ToData implements Transformer<Locale, Data> {
        @Override
        public Data transform(Locale input) {
            return new Data(LocaleUtils.toLanguageTag(input), StringUtils.capitalize(input.getDisplayName(input)));
        }
    }
}
