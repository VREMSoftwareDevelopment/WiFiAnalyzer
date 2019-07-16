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

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.R;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;

import androidx.annotation.NonNull;

public class NavigationMenuController {
    private final NavigationView navigationView;
    private final BottomNavigationView bottomNavigationView;
    private NavigationMenu currentNavigationMenu;

    public NavigationMenuController(@NonNull NavigationMenuControl navigationMenuControl) {
        navigationView = navigationMenuControl.findViewById(R.id.nav_drawer);
        bottomNavigationView = navigationMenuControl.findViewById(R.id.nav_bottom);
        populateNavigationMenu();
        removePadding();
        navigationView.setNavigationItemSelectedListener(navigationMenuControl);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationMenuControl);

    }

    private void removePadding() {
        // https://github.com/material-components/material-components-android/issues/139
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
            View view = item.findViewById(R.id.largeLabel);
            if (view instanceof TextView) {
                view.setPadding(0, 0, 0, 0);
            }
        }
    }

    private void populateNavigationMenu() {
        IterableUtils.forEach(EnumUtils.values(NavigationGroup.class), new NavigationGroupClosure(navigationView.getMenu()));
        new NavigationGroupClosure(bottomNavigationView.getMenu()).execute(NavigationGroup.GROUP_FEATURE);
    }

    @NonNull
    public MenuItem getCurrentMenuItem() {
        return navigationView.getMenu().getItem(getCurrentNavigationMenu().ordinal());
    }

    @NonNull
    public NavigationMenu getCurrentNavigationMenu() {
        return currentNavigationMenu;
    }

    public void setCurrentNavigationMenu(@NonNull NavigationMenu navigationMenu) {
        this.currentNavigationMenu = navigationMenu;
        selectCurrentMenuItem(navigationMenu, navigationView.getMenu());
        selectCurrentMenuItem(navigationMenu, bottomNavigationView.getMenu());
    }

    private void selectCurrentMenuItem(@NonNull NavigationMenu navigationMenu, @NonNull Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setCheckable(false);
            menuItem.setChecked(false);
        }
        MenuItem menuItem = menu.findItem(navigationMenu.ordinal());
        if (menuItem != null) {
            menuItem.setCheckable(true);
            menuItem.setChecked(true);
        }
    }

    @NonNull
    public NavigationView getNavigationView() {
        return navigationView;
    }

    BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    private class NavigationGroupClosure implements Closure<NavigationGroup> {
        private final Menu menu;

        private NavigationGroupClosure(@NonNull Menu menu) {
            this.menu = menu;
        }

        @Override
        public void execute(final NavigationGroup navigationGroup) {
            IterableUtils.forEach(navigationGroup.getNavigationMenus(), new NavigationItemClosure(menu, navigationGroup));
        }
    }

    private class NavigationItemClosure implements Closure<NavigationMenu> {
        private final Menu menu;
        private final NavigationGroup navigationGroup;

        private NavigationItemClosure(@NonNull Menu menu, @NonNull NavigationGroup navigationGroup) {
            this.menu = menu;
            this.navigationGroup = navigationGroup;
        }

        @Override
        public void execute(NavigationMenu navigationMenu) {
            MenuItem menuItem = menu.add(navigationGroup.ordinal(), navigationMenu.ordinal(), navigationMenu.ordinal(), navigationMenu.getTitle());
            menuItem.setIcon(navigationMenu.getIcon());
        }
    }
}
