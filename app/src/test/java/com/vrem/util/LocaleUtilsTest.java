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

import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testGetAllCountries() throws Exception {
        // execute
        List<Locale> actual = LocaleUtils.getAllCountries();
        // validate
        assertTrue(actual.size() >= 2);
        assertTrue(actual.get(0).getCountry().compareTo(actual.get(actual.size() - 1).getCountry()) < 0);
    }

    @Test
    public void testFindByCountryCode() throws Exception {
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
    public void testFindByCountryCodeWithUnknownCode() throws Exception {
        // execute
        Locale actual = LocaleUtils.findByCountryCode("WW");
        // validate
        assertEquals(Locale.getDefault(), actual);
    }

    @Test
    public void testFindByLanguageCodesWithSingleCode() throws Exception {
        // execute
        List<Locale> actual = LocaleUtils.findByLanguageCodes(Locale.ENGLISH.getLanguage());
        // validate
        assertTrue(actual.size() >= 2);
        assertEquals(Locale.ENGLISH.getLanguage(), actual.get(0).getLanguage());
        assertEquals(Locale.ENGLISH.getLanguage(), actual.get(actual.size() - 1).getLanguage());
    }

    @Test
    public void testFindByLanguageCodesWithMultipleCodes() throws Exception {
        // setup
        int expectedSize = LocaleUtils.findByLanguageCodes(Locale.ENGLISH.getLanguage()).size()
            + LocaleUtils.findByLanguageCodes(Locale.FRENCH.getLanguage()).size();
        // execute
        List<Locale> actual = LocaleUtils.findByLanguageCodes(Locale.ENGLISH.getLanguage(), Locale.FRENCH.getLanguage());
        // validate
        assertEquals(expectedSize, actual.size());
    }

    @Test
    public void testToLanguageTag() throws Exception {
        // setup
        String expected = Locale.US.getLanguage() + "_" + Locale.US.getCountry();
        // execute
        String actual = LocaleUtils.toLanguageTag(Locale.US);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByLanguageTagWithUnknownTag() throws Exception {
        // execute
        Locale actual = LocaleUtils.findByLanguageTag("WW");
        // validate
        assertEquals(Locale.getDefault(), actual);
    }

    @Test
    public void testFindByLanguageTag() throws Exception {
        validateFindByLanguageTag(Locale.SIMPLIFIED_CHINESE);
        validateFindByLanguageTag(Locale.TRADITIONAL_CHINESE);
        validateFindByLanguageTag(Locale.CANADA_FRENCH);
        validateFindByLanguageTag(Locale.CANADA);
    }

    private void validateFindByLanguageTag(Locale expected) {
        assertEquals(expected, LocaleUtils.findByLanguageTag(LocaleUtils.toLanguageTag(expected)));
    }

}
