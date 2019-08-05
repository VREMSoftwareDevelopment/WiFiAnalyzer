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

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import androidx.annotation.StyleRes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ThemeStyleTest {
    private Settings settings;

    @Before
    public void setUp() {
        settings = MainContextHelper.INSTANCE.getSettings();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testThemeStyle() {
        assertEquals(3, ThemeStyle.values().length);
    }

    @Test
    public void testGetTheme() {
        assertEquals(R.style.ThemeLight, ThemeStyle.LIGHT.getTheme());
        assertEquals(R.style.ThemeDark, ThemeStyle.DARK.getTheme());
        assertEquals(R.style.ThemeSystem, ThemeStyle.SYSTEM.getTheme());
    }

    @Test
    public void testGetThemeNoActionBar() {
        assertEquals(R.style.ThemeDarkNoActionBar, ThemeStyle.DARK.getThemeNoActionBar());
        assertEquals(R.style.ThemeLightNoActionBar, ThemeStyle.LIGHT.getThemeNoActionBar());
        assertEquals(R.style.ThemeSystemNoActionBar, ThemeStyle.SYSTEM.getThemeNoActionBar());
    }

    @Test
    public void testGetDefaultTheme() {
        // setup
        when(settings.getThemeStyle()).thenReturn(ThemeStyle.LIGHT);
        // execute
        @StyleRes int actual = ThemeStyle.getDefaultTheme();
        // validate
        assertEquals(ThemeStyle.LIGHT.getTheme(), actual);
        verify(settings).getThemeStyle();
    }

}