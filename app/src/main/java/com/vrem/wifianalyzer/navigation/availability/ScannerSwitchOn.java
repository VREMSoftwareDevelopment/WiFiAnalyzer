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

class ScannerSwitchOn implements NavigationOption {

    @Override
    public void apply(@NonNull MainActivity mainActivity) {
        Menu menu = mainActivity.getOptionMenu().getMenu();
        if (menu != null) {
            MenuItem menuItem = menu.findItem(R.id.action_scanner);
            menuItem.setVisible(true);
            if (MainContext.INSTANCE.getScannerService().isRunning()) {
                menuItem.setTitle(R.string.scanner_pause);
                menuItem.setIcon(R.drawable.ic_pause);
            } else {
                menuItem.setTitle(R.string.scanner_play);
                menuItem.setIcon(R.drawable.ic_play_arrow);
            }
        }
    }

}
