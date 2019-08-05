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

package com.vrem.wifianalyzer.navigation.items;

import android.view.MenuItem;
import android.view.View;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.navigation.NavigationMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

class FragmentItem implements NavigationItem {
    private final Fragment fragment;
    private final boolean registered;
    private final int visibility;

    FragmentItem(@NonNull Fragment fragment) {
        this(fragment, true, View.VISIBLE);
    }

    FragmentItem(@NonNull Fragment fragment, boolean registered) {
        this(fragment, registered, View.VISIBLE);
    }

    FragmentItem(@NonNull Fragment fragment, boolean registered, int visibility) {
        this.fragment = fragment;
        this.registered = registered;
        this.visibility = visibility;
    }

    @Override
    public void activate(@NonNull MainActivity mainActivity, @NonNull MenuItem menuItem, @NonNull NavigationMenu navigationMenu) {
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
        if (fragmentManager.isStateSaved()) {
            return;
        }
        updateMainActivity(mainActivity, menuItem, navigationMenu);
        startFragment(fragmentManager);
    }

    @Override
    public boolean isRegistered() {
        return registered;
    }

    @Override
    public int getVisibility() {
        return visibility;
    }

    @NonNull
    Fragment getFragment() {
        return fragment;
    }

    private void startFragment(FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fragment).commit();
    }

    private void updateMainActivity(@NonNull MainActivity mainActivity, @NonNull MenuItem menuItem, @NonNull NavigationMenu navigationMenu) {
        mainActivity.setCurrentNavigationMenu(navigationMenu);
        mainActivity.setTitle(menuItem.getTitle());
        mainActivity.updateActionBar();
        mainActivity.mainConnectionVisibility(visibility);
    }

}
