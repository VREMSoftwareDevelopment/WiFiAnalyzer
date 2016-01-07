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
package com.vrem.wifianalyzer.navigation;

import android.support.annotation.NonNull;

enum NavigationGroup {
    GROUP_FEATURE(NavigationMenu.WIFI_LIST),
    GROUP_OTHER(NavigationMenu.VENDOR_LIST),
    GROUP_SETTINGS(NavigationMenu.SETTINGS);

    private final NavigationMenu[] navigationMenu;

    NavigationGroup(@NonNull NavigationMenu... navigationMenu) {
        this.navigationMenu = navigationMenu;
    }

    NavigationMenu[] navigationMenu() {
        return navigationMenu;
    }
}
