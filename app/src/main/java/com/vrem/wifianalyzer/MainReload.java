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

import java.util.Locale;

import androidx.annotation.NonNull;

class MainReload {
    private ThemeStyle themeStyle;
    private ConnectionViewType connectionViewType;
    private Locale languageLocale;

    MainReload(@NonNull Settings settings) {
        setThemeStyle(settings.getThemeStyle());
        setConnectionViewType(settings.getConnectionViewType());
        setLanguageLocale(settings.getLanguageLocale());
    }

    boolean shouldReload(@NonNull Settings settings) {
        return isThemeChanged(settings)
            || isConnectionViewTypeChanged(settings)
            || isLanguageChanged(settings);
    }

    private boolean isConnectionViewTypeChanged(Settings settings) {
        ConnectionViewType currentConnectionViewType = settings.getConnectionViewType();
        boolean connectionViewTypeChanged = !getConnectionViewType().equals(currentConnectionViewType);
        if (connectionViewTypeChanged) {
            setConnectionViewType(currentConnectionViewType);
        }
        return connectionViewTypeChanged;
    }

    private boolean isThemeChanged(Settings settings) {
        ThemeStyle settingThemeStyle = settings.getThemeStyle();
        boolean themeChanged = !getThemeStyle().equals(settingThemeStyle);
        if (themeChanged) {
            setThemeStyle(settingThemeStyle);
        }
        return themeChanged;
    }

    private boolean isLanguageChanged(Settings settings) {
        Locale settingLanguageLocale = settings.getLanguageLocale();
        boolean languageLocaleChanged = !getLanguageLocale().equals(settingLanguageLocale);
        if (languageLocaleChanged) {
            setLanguageLocale(settingLanguageLocale);
        }
        return languageLocaleChanged;
    }

    @NonNull
    ThemeStyle getThemeStyle() {
        return themeStyle;
    }

    private void setThemeStyle(@NonNull ThemeStyle themeStyle) {
        this.themeStyle = themeStyle;
    }

    @NonNull
    ConnectionViewType getConnectionViewType() {
        return connectionViewType;
    }

    private void setConnectionViewType(@NonNull ConnectionViewType connectionViewType) {
        this.connectionViewType = connectionViewType;
    }

    @NonNull
    Locale getLanguageLocale() {
        return languageLocale;
    }

    private void setLanguageLocale(Locale languageLocale) {
        this.languageLocale = languageLocale;
    }

}
