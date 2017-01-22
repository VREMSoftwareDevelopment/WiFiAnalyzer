/*
 * WiFi Analyzer
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

package com.vrem.wifianalyzer;

import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.settings.ThemeStyle;
import com.vrem.wifianalyzer.wifi.AccessPointView;

class MainReload {
    private ThemeStyle themeStyle;
    private AccessPointView accessPointView;
    private int graphMaximumY;

    MainReload(@NonNull Settings settings) {
        setThemeStyle(settings.getThemeStyle());
        setAccessPointView(settings.getAccessPointView());
        setGraphMaximumY(settings.getGraphMaximumY());
    }

    boolean shouldReload(@NonNull Settings settings) {
        return isThemeChanged(settings) || isAccessPointViewChanged(settings) || isGraphMaximumYChanged(settings);
    }

    private boolean isAccessPointViewChanged(Settings settings) {
        AccessPointView settingAccessPointView = settings.getAccessPointView();
        boolean accessPointViewChanged = !getAccessPointView().equals(settingAccessPointView);
        if (accessPointViewChanged) {
            setAccessPointView(settingAccessPointView);
        }
        return accessPointViewChanged;
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
        int graphMaximumY = settings.getGraphMaximumY();
        boolean graphMaximumYChanged = graphMaximumY != getGraphMaximumY();
        if (graphMaximumYChanged) {
            setGraphMaximumY(graphMaximumY);
        }
        return graphMaximumYChanged;
    }

    ThemeStyle getThemeStyle() {
        return themeStyle;
    }

    private void setThemeStyle(@NonNull ThemeStyle themeStyle) {
        this.themeStyle = themeStyle;
    }

    AccessPointView getAccessPointView() {
        return accessPointView;
    }

    private void setAccessPointView(@NonNull AccessPointView accessPointView) {
        this.accessPointView = accessPointView;
    }

    int getGraphMaximumY() {
        return graphMaximumY;
    }

    private void setGraphMaximumY(int graphMaximumY) {
        this.graphMaximumY = graphMaximumY;
    }

}
