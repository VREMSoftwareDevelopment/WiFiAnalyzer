/*
 * WiFiAnalyzer
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
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;

import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;

public class NavigationMenuView {
    private final NavigationView navigationView;
    private NavigationMenu currentNavigationMenu;

    public NavigationMenuView(@NonNull MainActivity mainActivity, @NonNull NavigationMenu currentNavigationMenu) {
        navigationView = (NavigationView) mainActivity.findViewById(R.id.nav_view);
        populateNavigationMenu();
        setCurrentNavigationMenu(currentNavigationMenu);
        navigationView.setNavigationItemSelectedListener(mainActivity);
    }

    private void populateNavigationMenu() {
        IterableUtils.forEach(EnumUtils.values(NavigationGroup.class), new NavigationGroupClosure(navigationView.getMenu()));
    }

    public MenuItem getCurrentMenuItem() {
        return navigationView.getMenu().getItem(getCurrentNavigationMenu().ordinal());
    }

    public NavigationMenu getCurrentNavigationMenu() {
        return currentNavigationMenu;
    }

    public void setCurrentNavigationMenu(@NonNull NavigationMenu navigationMenu) {
        this.currentNavigationMenu = navigationMenu;
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            item.setCheckable(navigationMenu.ordinal() == i);
            item.setChecked(navigationMenu.ordinal() == i);
        }
    }

    NavigationView getNavigationView() {
        return navigationView;
    }

    private class NavigationGroupClosure implements Closure<NavigationGroup> {
        private final Menu menu;

        private NavigationGroupClosure(@NonNull Menu menu) {
            this.menu = menu;
        }

        @Override
        public void execute(final NavigationGroup navigationGroup) {
            IterableUtils.forEach(navigationGroup.getNavigationMenus(), new NavigationMenuClosure(menu, navigationGroup));
        }
    }

    private class NavigationMenuClosure implements Closure<NavigationMenu> {
        private final Menu menu;
        private final NavigationGroup navigationGroup;

        private NavigationMenuClosure(@NonNull Menu menu, @NonNull NavigationGroup navigationGroup) {
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
