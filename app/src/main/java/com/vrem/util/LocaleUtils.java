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

package com.vrem.util;

import android.support.annotation.NonNull;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class LocaleUtils {
    private static final Locale SPANISH = new Locale("es");
    private static final Locale PORTUGUESE = new Locale("pt");
    private static final Locale RUSSIAN = new Locale("ru");
    public static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(
        Locale.GERMAN, Locale.ENGLISH, SPANISH, Locale.FRENCH, Locale.ITALIAN, PORTUGUESE, RUSSIAN,
        Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE);
    private static final String SEPARATOR = "_";

    public static Locale findByCountryCode(@NonNull String countryCode) {
        return find(SyncAvoid.AVAILABLE_LOCALES, new CountryCodePredicate(countryCode));
    }

    public static Locale findByLanguageTag(@NonNull String languageTag) {
        return find(SUPPORTED_LOCALES, new LanguageTagPredicate(fromLanguageTag(languageTag)));
    }

    public static List<Locale> getAllCountries() {
        return new ArrayList<>(SyncAvoid.COUNTRIES_LOCALES.values());
    }

    private static Locale find(List<Locale> locales, Predicate<Locale> predicate) {
        Locale result = IterableUtils.find(locales, predicate);
        return result == null ? Locale.getDefault() : result;
    }

    public static String toLanguageTag(@NonNull Locale locale) {
        return locale.getLanguage() + SEPARATOR + locale.getCountry();
    }

    private static Locale fromLanguageTag(@NonNull String languageTag) {
        String[] codes = languageTag.split("_");
        if (codes.length == 1) {
            return new Locale(codes[0]);
        }
        if (codes.length == 2) {
            return new Locale(codes[0], StringUtils.capitalize(codes[1]));
        }
        return Locale.getDefault();
    }

    private static class CountryCodePredicate implements Predicate<Locale> {
        private final String countryCode;

        private CountryCodePredicate(@NonNull String countryCode) {
            this.countryCode = StringUtils.capitalize(countryCode);
        }

        @Override
        public boolean evaluate(Locale locale) {
            return countryCode.equals(locale.getCountry());
        }
    }

    private static class CountriesPredicate implements Predicate<Locale> {
        private final Set<String> countryCodes;

        private CountriesPredicate(@NonNull Set<String> countryCodes) {
            this.countryCodes = countryCodes;
        }

        @Override
        public boolean evaluate(Locale locale) {
            return countryCodes.contains(locale.getCountry());
        }
    }

    private static class LanguageTagPredicate implements Predicate<Locale> {
        private final Locale locale;

        private LanguageTagPredicate(@NonNull Locale locale) {
            this.locale = locale;
        }

        @Override
        public boolean evaluate(Locale object) {
            return object.getLanguage().equals(locale.getLanguage()) && object.getCountry().equals(locale.getCountry());
        }
    }

    private static class SyncAvoid {
        private static final SortedMap<String, Locale> COUNTRIES_LOCALES;
        private static final List<Locale> AVAILABLE_LOCALES;

        static {
            Set<String> countryCodes = new TreeSet<>(Arrays.asList(Locale.getISOCountries()));
            List<Locale> availableLocales = Arrays.asList(Locale.getAvailableLocales());
            AVAILABLE_LOCALES = new ArrayList<>(CollectionUtils.select(availableLocales, new CountriesPredicate(countryCodes)));
            COUNTRIES_LOCALES = new TreeMap<>();
            IterableUtils.forEach(AVAILABLE_LOCALES, new CountryClosure());
        }

        private static class CountryClosure implements Closure<Locale> {
            @Override
            public void execute(Locale locale) {
                String countryCode = locale.getCountry();
                COUNTRIES_LOCALES.put(StringUtils.capitalize(countryCode), locale);
            }
        }
    }

}
