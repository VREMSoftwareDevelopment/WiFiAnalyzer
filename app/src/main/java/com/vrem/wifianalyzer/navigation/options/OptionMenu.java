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

package com.vrem.wifianalyzer.navigation.options;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import com.vrem.wifianalyzer.R;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;

public class OptionMenu {
    private Menu menu;

    public void create(@NonNull Activity activity, Menu menu) {
        activity.getMenuInflater().inflate(R.menu.optionmenu, menu);
        this.menu = menu;
        iconsVisible(menu);
    }

    public void select(@NonNull MenuItem item) {
        OptionAction.findOptionAction(item.getItemId()).getAction().execute();
    }

    public Menu getMenu() {
        return menu;
    }

    @SuppressLint("RestrictedApi")
    private void iconsVisible(Menu menu) {
        try {
            if (menu instanceof MenuBuilder) {
                ((MenuBuilder) menu).setOptionalIconsVisible(true);
            }
        } catch (Exception e) {
            // do nothing
        }
    }

}
