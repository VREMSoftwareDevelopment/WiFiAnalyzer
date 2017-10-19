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

import java.util.Locale;

public enum LocaleType {
    DEFAULT(Locale.getDefault()),
    ENGLISH(new Locale("en", "US")),
    GERMAN(new Locale("de", "DE")),
    SPANISH(new Locale("es", "ES")),
    FRENCH(new Locale("fr", "FR")),
    ITALIAN(new Locale("it", "IT")),
    PORTUGUESE(new Locale("pt", "PT")),
    RUSSIAN(new Locale("ru", "RU")),
    CHINESE(new Locale("zh", "CN")),
    TAIWANESE(new Locale("zh", "TW"));

    private final Locale locale;

    LocaleType(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public static LocaleType fromString(String countryCode) {
        for (LocaleType localeType : LocaleType.values()) {
            if (localeType.getLocale().getCountry().equals(countryCode)) {
                return localeType;
            }
        }
        return null;
    }
}
