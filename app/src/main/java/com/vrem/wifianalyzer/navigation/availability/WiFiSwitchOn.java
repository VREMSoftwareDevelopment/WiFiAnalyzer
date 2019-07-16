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

import android.content.res.Resources;
import android.view.Menu;

import com.vrem.util.TextUtils;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.navigation.options.OptionMenu;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;

class WiFiSwitchOn implements NavigationOption {

    static final String SPACER = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

    @Override
    public void apply(@NonNull MainActivity mainActivity) {
        applyToActionBar(mainActivity);
        applyToMenu(mainActivity);
    }

    private void applyToActionBar(@NonNull MainActivity mainActivity) {
        ActionBar actionBar = mainActivity.getSupportActionBar();
        if (actionBar != null) {
            int colorSelected = ContextCompat.getColor(mainActivity, R.color.selected);
            int colorNotSelected = ContextCompat.getColor(mainActivity, R.color.regular);
            Resources resources = mainActivity.getResources();
            String wiFiBand2 = resources.getString(WiFiBand.GHZ2.getTextResource());
            String wiFiBand5 = resources.getString(WiFiBand.GHZ5.getTextResource());
            WiFiBand wiFiBand = MainContext.INSTANCE.getSettings().getWiFiBand();
            String subtitle = makeSubtitle(WiFiBand.GHZ2.equals(wiFiBand), wiFiBand2, wiFiBand5, colorSelected, colorNotSelected);
            actionBar.setSubtitle(TextUtils.fromHtml(subtitle));
        }
    }

    private void applyToMenu(@NonNull MainActivity mainActivity) {
        OptionMenu optionMenu = mainActivity.getOptionMenu();
        if (optionMenu != null) {
            Menu menu = optionMenu.getMenu();
            if (menu != null) {
                menu.findItem(R.id.action_wifi_band).setVisible(true);
            }
        }
    }

    @NonNull
    String makeSubtitle(boolean wiFiBand2Selected, String wiFiBand2, String wiFiBand5, int colorSelected, int colorNotSelected) {
        StringBuilder stringBuilder = new StringBuilder();
        if (wiFiBand2Selected) {
            stringBuilder.append(TextUtils.textToHtml(wiFiBand2, colorSelected, false));
        } else {
            stringBuilder.append(TextUtils.textToHtml(wiFiBand2, colorNotSelected, true));
        }
        stringBuilder.append(SPACER);
        if (wiFiBand2Selected) {
            stringBuilder.append(TextUtils.textToHtml(wiFiBand5, colorNotSelected, true));
        } else {
            stringBuilder.append(TextUtils.textToHtml(wiFiBand5, colorSelected, false));
        }
        return stringBuilder.toString();
    }

}
