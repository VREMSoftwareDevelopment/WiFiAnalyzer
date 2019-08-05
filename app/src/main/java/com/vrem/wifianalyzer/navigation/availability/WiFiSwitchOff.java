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

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.navigation.options.OptionMenu;

import org.apache.commons.lang3.StringUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

class WiFiSwitchOff implements NavigationOption {

    @Override
    public void apply(@NonNull MainActivity mainActivity) {
        applyToActionBar(mainActivity);
        applyToMenu(mainActivity);
    }

    private void applyToActionBar(@NonNull MainActivity mainActivity) {
        ActionBar actionBar = mainActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(StringUtils.EMPTY);
        }
    }

    private void applyToMenu(@NonNull MainActivity mainActivity) {
        OptionMenu optionMenu = mainActivity.getOptionMenu();
        if (optionMenu != null) {
            Menu menu = optionMenu.getMenu();
            if (menu != null) {
                menu.findItem(R.id.action_wifi_band).setVisible(false);
            }
        }
    }
}
