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

package com.vrem.wifianalyzer;

import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.settings.ThemeStyle;
import com.vrem.wifianalyzer.wifi.accesspoint.ConnectionViewType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainReloadTest {
    private static final Locale TEST_LOCALE = Locale.UK;
    private Settings settings;
    private MainReload fixture;

    @Before
    public void setUp() {
        settings = MainContextHelper.INSTANCE.getSettings();

        when(settings.getThemeStyle()).thenReturn(ThemeStyle.DARK);
        when(settings.getConnectionViewType()).thenReturn(ConnectionViewType.COMPLETE);
        when(settings.getLanguageLocale()).thenReturn(TEST_LOCALE);

        fixture = new MainReload(settings);
    }

    @After
    public void tearDown() {
        verify(settings, atLeastOnce()).getThemeStyle();
        verify(settings, atLeastOnce()).getConnectionViewType();
        verify(settings, atLeastOnce()).getLanguageLocale();

        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testShouldNotReloadWithNoThemeChanges() {
        // execute
        boolean actual = fixture.shouldReload(settings);
        // validate
        assertFalse(actual);
        assertEquals(ThemeStyle.DARK, fixture.getThemeStyle());
    }

    @Test
    public void testShouldReloadWithThemeChange() {
        // setup
        ThemeStyle expected = ThemeStyle.LIGHT;
        when(settings.getThemeStyle()).thenReturn(expected);
        // execute
        boolean actual = fixture.shouldReload(settings);
        // validate
        assertTrue(actual);
        assertEquals(expected, fixture.getThemeStyle());
    }

    @Test
    public void testShouldNotReloadWithNoConnectionViewTypeChanges() {
        // execute
        boolean actual = fixture.shouldReload(settings);
        // validate
        assertFalse(actual);
        assertEquals(ConnectionViewType.COMPLETE, fixture.getConnectionViewType());
    }

    @Test
    public void testShouldReloadWithConnectionViewTypeChange() {
        // setup
        ConnectionViewType expected = ConnectionViewType.COMPACT;
        when(settings.getConnectionViewType()).thenReturn(expected);
        // execute
        boolean actual = fixture.shouldReload(settings);
        // validate
        assertTrue(actual);
        assertEquals(expected, fixture.getConnectionViewType());
    }

    @Test
    public void testShouldNotReloadWithNoLanguageLocaleChanges() {
        // execute
        boolean actual = fixture.shouldReload(settings);
        // validate
        assertFalse(actual);
        assertEquals(Locale.UK, fixture.getLanguageLocale());
    }

    @Test
    public void testShouldReloadWithLanguageLocaleChange() {
        // setup
        Locale expected = Locale.US;
        when(settings.getLanguageLocale()).thenReturn(expected);
        // execute
        boolean actual = fixture.shouldReload(settings);
        // validate
        assertTrue(actual);
        assertEquals(expected, fixture.getLanguageLocale());
    }

}
