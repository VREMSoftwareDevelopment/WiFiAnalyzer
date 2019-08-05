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

import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

class DrawerNavigation {
    private final ActionBarDrawerToggle drawerToggle;

    DrawerNavigation(@NonNull MainActivity mainActivity, @NonNull Toolbar toolbar) {
        DrawerLayout drawer = mainActivity.findViewById(R.id.drawer_layout);
        drawerToggle = create(
            mainActivity,
            toolbar,
            drawer,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerToggle);
        syncState();
    }

    void syncState() {
        drawerToggle.syncState();
    }

    void onConfigurationChanged(Configuration newConfig) {
        drawerToggle.onConfigurationChanged(newConfig);
    }

    ActionBarDrawerToggle create(
        @NonNull MainActivity mainActivity,
        @NonNull Toolbar toolbar,
        @NonNull DrawerLayout drawer,
        @StringRes int openDrawerContentDescRes,
        @StringRes int closeDrawerContentDescRes) {
        return new ActionBarDrawerToggle(
            mainActivity,
            drawer,
            toolbar,
            openDrawerContentDescRes,
            closeDrawerContentDescRes);
    }

}
