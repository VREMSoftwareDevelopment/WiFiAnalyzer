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

import com.vrem.util.LocaleUtils;
import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LanguagePreferenceTest {

    private List<Locale> languages;
    private LanguagePreference fixture;

    @Before
    public void setUp() {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = new LanguagePreference(mainActivity, Robolectric.buildAttributeSet().build());

        withLanguages();
    }

    private void withLanguages() {
        languages = new ArrayList<>(LocaleUtils.SUPPORTED_LOCALES);
        Locale defaultLocale = Locale.getDefault();
        if (!languages.contains(defaultLocale)) {
            languages.add(defaultLocale);
        }
    }

    @Test
    public void testGetEntries() throws Exception {
        // execute
        List<CharSequence> actual = Arrays.asList(fixture.getEntries());
        // validate
        assertEquals(languages.size(), actual.size());
        for (Locale language : languages) {
            String getDisplayName = language.getDisplayName(language);
            assertTrue(getDisplayName, actual.contains(getDisplayName));
        }
    }

    @Test
    public void testGetEntryValues() throws Exception {
        // execute
        List<CharSequence> actual = Arrays.asList(fixture.getEntryValues());
        // validate
        assertEquals(languages.size(), actual.size());
        for (Locale language : languages) {
            String languageTag = LocaleUtils.toLanguageTag(language);
            assertTrue(languageTag, actual.contains(languageTag));
        }
    }

}