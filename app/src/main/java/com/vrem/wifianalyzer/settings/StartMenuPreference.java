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

package com.vrem.wifianalyzer.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.vrem.wifianalyzer.navigation.NavigationGroup;
import com.vrem.wifianalyzer.navigation.NavigationMenu;

import java.util.ArrayList;
import java.util.List;

public class StartMenuPreference extends CustomPreference {
    public StartMenuPreference(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs, getData(context), getDefault());
    }

    private static List<Data> getData(@NonNull Context context) {
        List<Data> result = new ArrayList<>();
        for (NavigationMenu navigationMenu : NavigationGroup.GROUP_FEATURE.getNavigationMenus()) {
            result.add(new Data("" + navigationMenu.ordinal(), context.getString(navigationMenu.getTitle())));
        }
        return result;
    }

    private static String getDefault() {
        return "" + NavigationGroup.GROUP_FEATURE.getNavigationMenus().get(0).ordinal();
    }

}
