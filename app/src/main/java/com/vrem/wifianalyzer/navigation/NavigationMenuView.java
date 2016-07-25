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

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;

import com.vrem.wifianalyzer.R;

public class NavigationMenuView {
    private final NavigationView navigationView;
    private NavigationMenu currentNavigationMenu;

    public NavigationMenuView(@NonNull Activity activity, @NonNull NavigationMenu currentNavigationMenu) {
        navigationView = (NavigationView) activity.findViewById(R.id.nav_view);

        populateNavigationMenu();
        setCurrentNavigationMenu(currentNavigationMenu);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) activity);
    }

    private void populateNavigationMenu() {
        Menu menu = navigationView.getMenu();
        for (NavigationGroup navigationGroup : NavigationGroup.values()) {
            for (NavigationMenu navigationMenu : navigationGroup.navigationMenu()) {
                MenuItem menuItem = menu.add(navigationGroup.ordinal(), navigationMenu.ordinal(), navigationMenu.ordinal(), navigationMenu.getTitle());
                menuItem.setIcon(navigationMenu.getIcon());
            }
        }
    }

    public MenuItem getCurrentMenuItem() {
        return navigationView.getMenu().getItem(getCurrentNavigationMenu().ordinal());
    }

    public NavigationMenu getCurrentNavigationMenu() {
        return currentNavigationMenu;
    }

    public void setCurrentNavigationMenu(@NonNull NavigationMenu currentNavigationMenu) {
        this.currentNavigationMenu = currentNavigationMenu;
    }

    public NavigationMenu findNavigationMenu(int menuItemId) {
        NavigationMenu navigationMenu = NavigationMenu.find(menuItemId);
        if (navigationMenu.getFragment() != null) {
            Menu menu = navigationView.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                item.setCheckable(menuItemId == i);
                item.setChecked(menuItemId == i);
            }
        }
        return navigationMenu;
    }

    protected NavigationView getNavigationView() {
        return navigationView;
    }

}
