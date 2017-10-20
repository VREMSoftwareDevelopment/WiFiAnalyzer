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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanguageCountry {

    private final LocaleType localeType;

    private LanguageCountry(LocaleType localeType) {
        this.localeType = localeType;
    }

    public static List<LanguageCountry> getAll(){
        return new ArrayList<LanguageCountry>(CollectionUtils.collect(Arrays.asList(LocaleType.values()), new ToLanguageCountry()));
    }

    public String getLanguageCode() {
        return localeType.getLocale().toString();
    }

    public String getLanguageName() {
        String languageName = localeType.getLocale().getDisplayName(localeType.getLocale());
        return StringUtils.capitalize(languageName);
    }

    private static class ToLanguageCountry implements Transformer<LocaleType, LanguageCountry> {
        @Override
        public LanguageCountry transform(LocaleType input) {
            return new LanguageCountry(input);
        }
    }
}
