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

package com.vrem.util;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import androidx.annotation.NonNull;

public class LocaleUtils {
    static final Locale SPANISH = new Locale("es");
    static final Locale POLISH = new Locale("pl");
    static final Locale PORTUGUESE = new Locale("pt");
    static final Locale RUSSIAN = new Locale("ru");
    private static final String SEPARATOR = "_";

    private LocaleUtils() {
        throw new IllegalStateException("Utility class");
    }

    @NonNull
    public static Locale findByCountryCode(@NonNull String countryCode) {
        return find(SyncAvoid.AVAILABLE_LOCALES, new CountryCodePredicate(countryCode));
    }

    @NonNull
    public static List<Locale> getAllCountries() {
        return new ArrayList<>(SyncAvoid.COUNTRIES_LOCALES.values());
    }

    @NonNull
    public static Locale findByLanguageTag(@NonNull String languageTag) {
        return find(SyncAvoid.SUPPORTED_LOCALES, new LanguageTagPredicate(fromLanguageTag(languageTag)));
    }

    @NonNull
    public static List<Locale> getSupportedLanguages() {
        return SyncAvoid.SUPPORTED_LOCALES;
    }

    @NonNull
    public static String getDefaultCountryCode() {
        return SyncAvoid.DEFAULT.getCountry();
    }

    @NonNull
    public static String getDefaultLanguageTag() {
        return LocaleUtils.toLanguageTag(SyncAvoid.DEFAULT);
    }

    @NonNull
    private static Locale find(List<Locale> locales, Predicate<Locale> predicate) {
        Locale result = IterableUtils.find(locales, predicate);
        return result == null ? SyncAvoid.DEFAULT : result;
    }

    @NonNull
    public static String toLanguageTag(@NonNull Locale locale) {
        return locale.getLanguage() + SEPARATOR + locale.getCountry();
    }

    @NonNull
    private static Locale fromLanguageTag(@NonNull String languageTag) {
        String[] codes = languageTag.split(SEPARATOR);
        if (codes.length == 1) {
            return new Locale(codes[0]);
        }
        if (codes.length == 2) {
            return new Locale(codes[0], StringUtils.capitalize(codes[1]));
        }
        return SyncAvoid.DEFAULT;
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
        private static final Locale DEFAULT = Locale.getDefault();
        private static final SortedMap<String, Locale> COUNTRIES_LOCALES;
        private static final List<Locale> AVAILABLE_LOCALES;
        private static final List<Locale> SUPPORTED_LOCALES;

        static {
            Set<String> countryCodes = new TreeSet<>(Arrays.asList(Locale.getISOCountries()));
            List<Locale> availableLocales = Arrays.asList(Locale.getAvailableLocales());
            AVAILABLE_LOCALES = new ArrayList<>(CollectionUtils.select(availableLocales, new CountriesPredicate(countryCodes)));
            COUNTRIES_LOCALES = new TreeMap<>();
            IterableUtils.forEach(AVAILABLE_LOCALES, new CountryClosure());
            SUPPORTED_LOCALES = new ArrayList<>(new HashSet<>(Arrays.asList(
                Locale.GERMAN,
                Locale.ENGLISH,
                SPANISH,
                Locale.FRENCH,
                Locale.ITALIAN,
                POLISH,
                PORTUGUESE,
                RUSSIAN,
                Locale.SIMPLIFIED_CHINESE,
                Locale.TRADITIONAL_CHINESE, DEFAULT)));
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
