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

package com.vrem.wifianalyzer;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.vrem.wifianalyzer.settings.Settings;

class ActivityUtils {
    private ActivityUtils() {
        throw new IllegalStateException("Utility class");
    }

    static void setActionBarOptions(ActionBar actionBar) {
        if (actionBar == null) {
            return;
        }
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    static void keepScreenOn(@NonNull MainActivity mainActivity) {
        Settings settings = MainContext.INSTANCE.getSettings();
        if (settings == null) {
            return;
        }
        Window window = mainActivity.getWindow();
        if (settings.isKeepScreenOn()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @NonNull
    static Toolbar setupToolbar(@NonNull MainActivity mainActivity) {
        Toolbar toolbar = mainActivity.findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new WiFiBandToggle(mainActivity));
        mainActivity.setSupportActionBar(toolbar);
        setActionBarOptions(mainActivity.getSupportActionBar());
        return toolbar;
    }

    static class WiFiBandToggle implements View.OnClickListener {
        private final MainActivity mainActivity;

        WiFiBandToggle(@NonNull MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onClick(View view) {
            if (mainActivity.getCurrentNavigationMenu().isWiFiBandSwitchable()) {
                MainContext.INSTANCE.getSettings().toggleWiFiBand();
            }
        }
    }

}
