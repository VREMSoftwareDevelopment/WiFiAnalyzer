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

package com.vrem.wifianalyzer.navigation;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.gestures.SwipeAction;
import com.vrem.wifianalyzer.gestures.SwipeDirection;
import com.vrem.wifianalyzer.navigation.NavigationGroup.NavigationPredicate;

class NavigationSwipe implements SwipeAction {

    private final MainActivity mainActivity;

    NavigationSwipe(@NonNull MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void swipe(@NonNull SwipeDirection swipeDirection) {
        if (SwipeDirection.LEFT.equals(swipeDirection)) {
            NavigationMenu nextNavigationMenu = getNextNavigationMenu();
            activateNewMenuItem(nextNavigationMenu);
        } else if (SwipeDirection.RIGHT.equals(swipeDirection)) {
            NavigationMenu previousNavigationMenu = getPreviousNavigationMenu();
            activateNewMenuItem(previousNavigationMenu);
        }
    }

    @NonNull
    private NavigationMenu getNextNavigationMenu() {
        NavigationMenu currentNavigationMenu = mainActivity.getCurrentNavigationMenu();
        return getNavigationGroup(currentNavigationMenu).next(currentNavigationMenu);
    }

    @NonNull
    private NavigationMenu getPreviousNavigationMenu() {
        NavigationMenu currentNavigationMenu = mainActivity.getCurrentNavigationMenu();
        return getNavigationGroup(currentNavigationMenu).previous(currentNavigationMenu);
    }

    @NonNull
    private NavigationGroup getNavigationGroup(NavigationMenu currentNavigationMenu) {
        NavigationPredicate navigationPredicate = new NavigationPredicate(currentNavigationMenu);
        return EnumUtils.find(NavigationGroup.class, navigationPredicate, NavigationGroup.GROUP_FEATURE);
    }

    private void activateNewMenuItem(@NonNull NavigationMenu navigationMenu) {
        NavigationView navigationView = mainActivity.getNavigationView();
        MenuItem newMenuItem = navigationView.getMenu().findItem(navigationMenu.ordinal());
        MenuItem currentMenuItem = mainActivity.getCurrentMenuItem();
        if (!currentMenuItem.equals(newMenuItem)) {
            mainActivity.onNavigationItemSelected(newMenuItem);
        }
    }

}
