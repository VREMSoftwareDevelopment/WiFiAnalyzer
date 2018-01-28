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

import com.vrem.wifianalyzer.R;

public enum ThemeStyle {
    DARK(R.style.ThemeAppCompatDark, R.style.ThemeDeviceDefaultDark),
    LIGHT(R.style.ThemeAppCompatLight, R.style.ThemeDeviceDefaultLight);

    private final int themeAppCompatStyle;
    private final int themeDeviceDefaultStyle;

    ThemeStyle(@StyleRes int themeAppCompatStyle, @StyleRes int themeDeviceDefaultStyle) {
        this.themeAppCompatStyle = themeAppCompatStyle;
        this.themeDeviceDefaultStyle = themeDeviceDefaultStyle;
    }

    public @StyleRes
    int themeAppCompatStyle() {
        return themeAppCompatStyle;
    }

    public @StyleRes
    int themeDeviceDefaultStyle() {
        return themeDeviceDefaultStyle;
    }
}
