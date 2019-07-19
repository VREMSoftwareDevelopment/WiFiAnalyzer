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

import android.os.Build;

import com.vrem.util.LocaleUtils;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
public class LanguagePreferenceTest {

    private List<Locale> languages;
    private LanguagePreference fixture;

    @Before
    public void setUp() {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = new LanguagePreference(mainActivity, Robolectric.buildAttributeSet().build());

        languages = LocaleUtils.getSupportedLanguages();
    }

    @Test
    public void testGetEntries() {
        // execute
        List<CharSequence> actual = Arrays.asList(fixture.getEntries());
        // validate
        assertEquals(languages.size(), actual.size());
        for (Locale language : languages) {
            String getDisplayName = StringUtils.capitalize(language.getDisplayName(language));
            assertTrue(getDisplayName, actual.contains(getDisplayName));
        }
    }

    @Test
    public void testGetEntryValues() {
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