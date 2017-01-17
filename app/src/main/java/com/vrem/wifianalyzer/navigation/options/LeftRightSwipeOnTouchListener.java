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

package com.vrem.wifianalyzer.navigation.options;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.navigation.NavigationGroup;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.navigation.NavigationMenuView;

class LeftRightSwipeOnTouchListener extends OnSwipeTouchListener {
    LeftRightSwipeOnTouchListener(@NonNull MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void onSwipeRight(@NonNull MainActivity mainActivity) {
        NavigationMenu previousNavigationMenu = getPreviousNavigationMenu(mainActivity);
        activateNewMenuItem(mainActivity, previousNavigationMenu);
    }

    @Override
    public void onSwipeLeft(@NonNull MainActivity mainActivity) {
        NavigationMenu nextNavigationMenu = getNextNavigationMenu(mainActivity);
        activateNewMenuItem(mainActivity, nextNavigationMenu);
    }

    private NavigationMenu getNextNavigationMenu(@NonNull MainActivity mainActivity) {
        NavigationMenu currentNavigationMenu = mainActivity.getNavigationMenuView().getCurrentNavigationMenu();
        return NavigationGroup.find(currentNavigationMenu).next(currentNavigationMenu);
    }

    private NavigationMenu getPreviousNavigationMenu(@NonNull MainActivity mainActivity) {
        NavigationMenu currentNavigationMenu = mainActivity.getNavigationMenuView().getCurrentNavigationMenu();
        return NavigationGroup.find(currentNavigationMenu).previous(currentNavigationMenu);
    }

    private void activateNewMenuItem(@NonNull MainActivity mainActivity, @NonNull NavigationMenu navigationMenu) {
        NavigationMenuView navigationMenuView = mainActivity.getNavigationMenuView();
        NavigationView navigationView = navigationMenuView.getNavigationView();
        MenuItem newMenuItem = navigationView.getMenu().findItem(navigationMenu.ordinal());
        MenuItem currentMenuItem = navigationMenuView.getCurrentMenuItem();
        if (!currentMenuItem.equals(newMenuItem)) {
            mainActivity.onNavigationItemSelected(newMenuItem);
        }
    }
}
