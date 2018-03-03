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

package com.vrem.wifianalyzer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.vrem.util.ConfigurationUtils;
import com.vrem.wifianalyzer.settings.Settings;

import java.util.Locale;

public class ActivityUtils {
    private ActivityUtils() {
        throw new IllegalStateException("Utility class");
    }

    @NonNull
    public static Context createContext(@NonNull Context context) {
        Context result = context;
        Settings settings = MainContext.INSTANCE.getSettings();
        if (settings != null) {
            Locale newLocale = settings.getLanguageLocale();
            result = ConfigurationUtils.createContext(context, newLocale);
        }
        return result;
    }

    public static void setActionBarOptions(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
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
