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
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;

import static android.view.View.OnClickListener;

public class NavigationMenuView {
    private final NavigationView navigationView;
    private final MainActivity mainActivity;
    private NavigationMenu currentNavigationMenu;

    public NavigationMenuView(@NonNull MainActivity mainActivity, @NonNull NavigationMenu currentNavigationMenu) {
        this.mainActivity = mainActivity;
        navigationView = (NavigationView) mainActivity.findViewById(R.id.nav_view);
        mainActivity.findViewById(R.id.action_next).setOnClickListener(new NextOnClickListener());
        mainActivity.findViewById(R.id.action_prev).setOnClickListener(new PrevOnClickListener());
        populateNavigationMenu();
        setCurrentNavigationMenu(currentNavigationMenu);
        navigationView.setNavigationItemSelectedListener(mainActivity);
    }

    private void populateNavigationMenu() {
        Menu menu = navigationView.getMenu();
        for (NavigationGroup navigationGroup : NavigationGroup.values()) {
            for (NavigationMenu navigationMenu : navigationGroup.getNavigationMenus()) {
                MenuItem menuItem = menu.add(navigationGroup.ordinal(), navigationMenu.ordinal(), navigationMenu.ordinal(), navigationMenu.getTitle());
                menuItem.setIcon(navigationMenu.getIcon());
            }
        }
    }

    public MenuItem getCurrentMenuItem() {
        return navigationView.getMenu().getItem(getCurrentNavigationMenu().ordinal());
    }

    private NavigationMenu getNextNavigationMenu() {
        return NavigationGroup.find(getCurrentNavigationMenu()).next(getCurrentNavigationMenu());
    }

    private NavigationMenu getPreviousNavigationMenu() {
        return NavigationGroup.find(getCurrentNavigationMenu()).previous(getCurrentNavigationMenu());
    }

    private void activateNewMenuItem(@NonNull NavigationMenu navigationMenu) {
        MenuItem newMenuItem = navigationView.getMenu().findItem(navigationMenu.ordinal());
        MenuItem currentMenuItem = getCurrentMenuItem();
        if (!currentMenuItem.equals(newMenuItem)) {
            mainActivity.onNavigationItemSelected(newMenuItem);
        }
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

    private class NextOnClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            activateNewMenuItem(getNextNavigationMenu());
        }
    }

    private class PrevOnClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            activateNewMenuItem(getPreviousNavigationMenu());
        }
    }
}
