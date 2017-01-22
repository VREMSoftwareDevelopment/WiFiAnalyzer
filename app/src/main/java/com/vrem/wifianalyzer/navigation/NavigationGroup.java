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

package com.vrem.wifianalyzer.navigation;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public enum NavigationGroup {
    GROUP_FEATURE(NavigationMenu.ACCESS_POINTS, NavigationMenu.CHANNEL_RATING, NavigationMenu.CHANNEL_GRAPH, NavigationMenu.TIME_GRAPH),
    GROUP_OTHER(NavigationMenu.EXPORT, NavigationMenu.CHANNEL_AVAILABLE, NavigationMenu.VENDOR_LIST),
    GROUP_SETTINGS(NavigationMenu.SETTINGS, NavigationMenu.ABOUT);

    private final List<NavigationMenu> navigationMenus;

    NavigationGroup(@NonNull NavigationMenu... navigationMenus) {
        this.navigationMenus = Arrays.asList(navigationMenus);
    }

    public static NavigationGroup find(@NonNull NavigationMenu navigationMenu) {
        for (NavigationGroup navigationGroup : values()) {
            if (navigationGroup.getNavigationMenus().contains(navigationMenu)) {
                return navigationGroup;
            }
        }
        return GROUP_FEATURE;
    }

    @NonNull
    public List<NavigationMenu> getNavigationMenus() {
        return navigationMenus;
    }

    @NonNull
    public NavigationMenu next(@NonNull NavigationMenu navigationMenu) {
        int index = navigationMenus.indexOf(navigationMenu);
        if (index < 0) {
            return navigationMenu;
        }
        index++;
        if (index >= navigationMenus.size()) {
            index = 0;
        }
        return navigationMenus.get(index);
    }

    @NonNull
    public NavigationMenu previous(@NonNull NavigationMenu navigationMenu) {
        int index = navigationMenus.indexOf(navigationMenu);
        if (index < 0) {
            return navigationMenu;
        }
        index--;
        if (index < 0) {
            index = navigationMenus.size() - 1;
        }
        return navigationMenus.get(index);
    }

}
