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

package com.vrem.wifianalyzer.navigation;

import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

public interface NavigationMenuControl extends
    NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    @NonNull
    MenuItem getCurrentMenuItem();

    @NonNull
    NavigationMenu getCurrentNavigationMenu();

    void setCurrentNavigationMenu(@NonNull NavigationMenu navigationMenu);

    @NonNull
    NavigationView getNavigationView();

    <T extends View> T findViewById(@IdRes int id);
}
