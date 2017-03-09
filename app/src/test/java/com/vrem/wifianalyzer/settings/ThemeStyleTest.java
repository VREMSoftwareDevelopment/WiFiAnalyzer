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

import com.vrem.wifianalyzer.R;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ThemeStyleTest {

    @Test
    public void testThemeStyle() throws Exception {
        assertEquals(2, ThemeStyle.values().length);
    }

    @Test
    public void testThemeAppCompatStyle() throws Exception {
        assertEquals(R.style.ThemeAppCompatLight, ThemeStyle.LIGHT.themeAppCompatStyle());
        assertEquals(R.style.ThemeAppCompatDark, ThemeStyle.DARK.themeAppCompatStyle());
    }

    @Test
    public void testThemeDeviceDefaultStyle() throws Exception {
        assertEquals(R.style.ThemeDeviceDefaultLight, ThemeStyle.LIGHT.themeDeviceDefaultStyle());
        assertEquals(R.style.ThemeDeviceDefaultDark, ThemeStyle.DARK.themeDeviceDefaultStyle());
    }
}