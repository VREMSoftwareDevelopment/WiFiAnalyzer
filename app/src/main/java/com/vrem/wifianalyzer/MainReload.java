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

package com.vrem.wifianalyzer;

import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.settings.ThemeStyle;
import com.vrem.wifianalyzer.wifi.accesspoint.AccessPointViewType;
import com.vrem.wifianalyzer.wifi.accesspoint.ConnectionViewType;

import java.util.Locale;

class MainReload {
    private ThemeStyle themeStyle;
    private AccessPointViewType accessPointViewType;
    private ConnectionViewType connectionViewType;
    private int graphMaximumY;
    private Locale languageLocale;

    MainReload(@NonNull Settings settings) {
        setThemeStyle(settings.getThemeStyle());
        setAccessPointViewType(settings.getAccessPointView());
        setConnectionViewType(settings.getConnectionViewType());
        setGraphMaximumY(settings.getGraphMaximumY());
        setLanguageLocale(settings.getLanguageLocale());
    }

    boolean shouldReload(@NonNull Settings settings) {
        return isThemeChanged(settings) || isAccessPointViewChanged(settings)
            || isConnectionViewTypeChanged(settings) || isGraphMaximumYChanged(settings)
            || isLanguageChanged(settings);
    }

    private boolean isAccessPointViewChanged(Settings settings) {
        AccessPointViewType settingAccessPointViewType = settings.getAccessPointView();
        boolean accessPointViewChanged = !getAccessPointViewType().equals(settingAccessPointViewType);
        if (accessPointViewChanged) {
            setAccessPointViewType(settingAccessPointViewType);
        }
        return accessPointViewChanged;
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

    private boolean isGraphMaximumYChanged(Settings settings) {
        int currentGraphMaximumY = settings.getGraphMaximumY();
        boolean graphMaximumYChanged = currentGraphMaximumY != getGraphMaximumY();
        if (graphMaximumYChanged) {
            setGraphMaximumY(currentGraphMaximumY);
        }
        return graphMaximumYChanged;
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
    AccessPointViewType getAccessPointViewType() {
        return accessPointViewType;
    }

    private void setAccessPointViewType(@NonNull AccessPointViewType accessPointViewType) {
        this.accessPointViewType = accessPointViewType;
    }

    @NonNull
    ConnectionViewType getConnectionViewType() {
        return connectionViewType;
    }

    private void setConnectionViewType(@NonNull ConnectionViewType connectionViewType) {
        this.connectionViewType = connectionViewType;
    }

    int getGraphMaximumY() {
        return graphMaximumY;
    }

    private void setGraphMaximumY(int graphMaximumY) {
        this.graphMaximumY = graphMaximumY;
    }

    @NonNull
    Locale getLanguageLocale() {
        return languageLocale;
    }

    private void setLanguageLocale(Locale languageLocale) {
        this.languageLocale = languageLocale;
    }

}
