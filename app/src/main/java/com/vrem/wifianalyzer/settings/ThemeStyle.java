/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.support.annotation.StyleRes;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;

public enum ThemeStyle {
    DARK(R.style.ThemeDark, R.style.ThemeDarkNoActionBar),
    LIGHT(R.style.ThemeLight, R.style.ThemeLightNoActionBar);

    private final int theme;
    private final int themeNoActionBar;

    ThemeStyle(@StyleRes int theme, @StyleRes int themeNoActionBar) {
        this.theme = theme;
        this.themeNoActionBar = themeNoActionBar;
    }

    @StyleRes
    public static int getDefaultTheme() {
        Settings settings = MainContext.INSTANCE.getSettings();
        ThemeStyle themeStyle = (settings == null ? ThemeStyle.DARK : settings.getThemeStyle());
        return themeStyle.getTheme();
    }

    public @StyleRes
    int getTheme() {
        return theme;
    }

    public @StyleRes
    int getThemeNoActionBar() {
        return themeNoActionBar;
    }
}
