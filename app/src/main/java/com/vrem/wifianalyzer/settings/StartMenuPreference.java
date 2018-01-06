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

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.vrem.wifianalyzer.navigation.NavigationGroup;
import com.vrem.wifianalyzer.navigation.NavigationMenu;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;

import java.util.ArrayList;
import java.util.List;

public class StartMenuPreference extends CustomPreference {
    public StartMenuPreference(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs, getData(context), getDefault());
    }

    private static List<Data> getData(@NonNull Context context) {
        return new ArrayList<>(CollectionUtils.collect(NavigationGroup.GROUP_FEATURE.getNavigationMenus(), new ToData(context)));
    }

    private static String getDefault() {
        return Integer.toString(NavigationGroup.GROUP_FEATURE.getNavigationMenus().get(0).ordinal());
    }

    private static class ToData implements Transformer<NavigationMenu, Data> {
        private final Context context;

        ToData(@NonNull Context context) {
            this.context = context;
        }

        @Override
        public Data transform(NavigationMenu input) {
            return new Data(Integer.toString(input.ordinal()), context.getString(input.getTitle()));
        }
    }

}
