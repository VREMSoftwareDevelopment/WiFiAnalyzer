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

package com.vrem.wifianalyzer.navigation.availability;

import android.view.Menu;
import android.view.MenuItem;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

class FilterOn implements NavigationOption {

    @Override
    public void apply(@NonNull MainActivity mainActivity) {
        Menu menu = mainActivity.getOptionMenu().getMenu();
        if (menu != null) {
            MenuItem menuItem = menu.findItem(R.id.action_filter);
            menuItem.setVisible(true);
            setIconColor(mainActivity, menuItem, MainContext.INSTANCE.getFilterAdapter().isActive());
        }
    }

    private void setIconColor(@NonNull MainActivity mainActivity, @NonNull MenuItem menuItem, boolean active) {
        int color = ContextCompat.getColor(mainActivity, active ? R.color.selected : R.color.regular);
        DrawableCompat.setTint(menuItem.getIcon(), color);
    }

}
