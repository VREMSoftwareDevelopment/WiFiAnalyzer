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

package com.vrem.wifianalyzer.navigation.items;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.navigation.NavigationMenu;

class ActivityItem implements NavigationItem {
    private final Class<? extends Activity> activity;

    ActivityItem(@NonNull Class<? extends Activity> activity) {
        this.activity = activity;
    }

    @Override
    public void activate(@NonNull MainActivity mainActivity, @NonNull MenuItem menuItem, @NonNull NavigationMenu navigationMenu) {
        mainActivity.startActivity(createIntent(mainActivity));
    }

    @Override
    public boolean isRegistered() {
        return false;
    }

    Intent createIntent(@NonNull MainActivity mainActivity) {
        return new Intent(mainActivity, activity);
    }

    Class<? extends Activity> getActivity() {
        return activity;
    }
}
