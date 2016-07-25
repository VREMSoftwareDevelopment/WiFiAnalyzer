/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
        for (NavigationMenu navigationMenu : NavigationGroup.GROUP_FEATURE.navigationMenu()) {
            result.add(new Data("" + navigationMenu.ordinal(), context.getString(navigationMenu.getTitle())));
        }
        return result;
    }

    private static String getDefault() {
        return "" + NavigationGroup.GROUP_FEATURE.navigationMenu()[0].ordinal();
    }

}
