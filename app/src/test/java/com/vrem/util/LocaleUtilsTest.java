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

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testGetAllCountries() {
        // execute
        List<Locale> actual = LocaleUtils.getAllCountries();
        // validate
        assertTrue(actual.size() >= 2);
        assertTrue(actual.get(0).getCountry().compareTo(actual.get(actual.size() - 1).getCountry()) < 0);
    }

    @Test
    public void testFindByCountryCode() {
        // setup
        Locale expected = LocaleUtils.getAllCountries().get(0);
        // execute
        Locale actual = LocaleUtils.findByCountryCode(expected.getCountry());
        // validate
        assertEquals(expected, actual);
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getDisplayCountry(), actual.getDisplayCountry());

        assertNotEquals(expected.getCountry(), expected.getDisplayCountry());
        assertNotEquals(actual.getCountry(), actual.getDisplayCountry());
    }

    @Test
    public void testFindByCountryCodeWithUnknownCode() {
        // execute
        Locale actual = LocaleUtils.findByCountryCode("WW");
        // validate
        assertEquals(Locale.getDefault(), actual);
    }

    @Test
    public void testToLanguageTag() {
        assertEquals(Locale.US.getLanguage() + "_" + Locale.US.getCountry(), LocaleUtils.toLanguageTag(Locale.US));
        assertEquals(Locale.ENGLISH.getLanguage() + "_", LocaleUtils.toLanguageTag(Locale.ENGLISH));
    }

    @Test
    public void testFindByLanguageTagWithUnknownTag() {
        Locale defaultLocal = Locale.getDefault();
        assertEquals(defaultLocal, LocaleUtils.findByLanguageTag(StringUtils.EMPTY));
        assertEquals(defaultLocal, LocaleUtils.findByLanguageTag("WW"));
        assertEquals(defaultLocal, LocaleUtils.findByLanguageTag("WW_HH_TT"));
    }

    @Test
    public void testFindByLanguageTag() {
        assertEquals(Locale.SIMPLIFIED_CHINESE, LocaleUtils.findByLanguageTag(LocaleUtils.toLanguageTag(Locale.SIMPLIFIED_CHINESE)));
        assertEquals(Locale.TRADITIONAL_CHINESE, LocaleUtils.findByLanguageTag(LocaleUtils.toLanguageTag(Locale.TRADITIONAL_CHINESE)));
        assertEquals(Locale.ENGLISH, LocaleUtils.findByLanguageTag(LocaleUtils.toLanguageTag(Locale.ENGLISH)));
    }

    @Test
    public void testGetSupportedLanguages() {
        // setup
        Set<Locale> expected = new HashSet<>(Arrays.asList(
            Locale.GERMAN,
            Locale.ENGLISH,
            Locale.FRENCH,
            Locale.ITALIAN,
            Locale.SIMPLIFIED_CHINESE,
            Locale.TRADITIONAL_CHINESE,
            LocaleUtils.SPANISH,
            LocaleUtils.PORTUGUESE,
            LocaleUtils.RUSSIAN,
            Locale.getDefault()));
        // execute
        List<Locale> actual = LocaleUtils.getSupportedLanguages();
        // validate
        assertEquals(expected.size(), actual.size());
        for (Locale locale : expected) {
            assertTrue(actual.contains(locale));
        }
    }

    @Test
    public void testGetDefaultCountryCode() {
        assertEquals(Locale.getDefault().getCountry(), LocaleUtils.getDefaultCountryCode());
    }

    @Test
    public void testGetDefaultLanguageTag() {
        assertEquals(LocaleUtils.toLanguageTag(Locale.getDefault()), LocaleUtils.getDefaultLanguageTag());
    }

}
