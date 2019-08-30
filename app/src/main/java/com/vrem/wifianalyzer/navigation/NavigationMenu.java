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

package com.vrem.wifianalyzer.navigation;

import android.view.MenuItem;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.navigation.availability.NavigationOption;
import com.vrem.wifianalyzer.navigation.availability.NavigationOptionFactory;
import com.vrem.wifianalyzer.navigation.items.NavigationItem;
import com.vrem.wifianalyzer.navigation.items.NavigationItemFactory;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;

import java.util.List;

import androidx.annotation.NonNull;

public enum NavigationMenu {
    ACCESS_POINTS(R.drawable.ic_network_wifi, R.string.action_access_points, NavigationItemFactory.ACCESS_POINTS, NavigationOptionFactory.AP),
    CHANNEL_RATING(R.drawable.ic_wifi_tethering, R.string.action_channel_rating, NavigationItemFactory.CHANNEL_RATING, NavigationOptionFactory.RATING),
    CHANNEL_GRAPH(R.drawable.ic_insert_chart, R.string.action_channel_graph, NavigationItemFactory.CHANNEL_GRAPH, NavigationOptionFactory.OTHER),
    TIME_GRAPH(R.drawable.ic_show_chart, R.string.action_time_graph, NavigationItemFactory.TIME_GRAPH, NavigationOptionFactory.OTHER),
    EXPORT(R.drawable.ic_import_export, R.string.action_export, NavigationItemFactory.EXPORT),
    CHANNEL_AVAILABLE(R.drawable.ic_location_on, R.string.action_channel_available, NavigationItemFactory.CHANNEL_AVAILABLE),
    VENDORS(R.drawable.ic_list_grey, R.string.action_vendors, NavigationItemFactory.VENDORS),
    PORT_AUTHORITY(R.drawable.ic_lan, R.string.action_port_authority, NavigationItemFactory.PORT_AUTHORITY),
    SETTINGS(R.drawable.ic_settings, R.string.action_settings, NavigationItemFactory.SETTINGS),
    ABOUT(R.drawable.ic_info_outline, R.string.action_about, NavigationItemFactory.ABOUT);

    private final int icon;
    private final int title;
    private final List<NavigationOption> navigationOptions;
    private final NavigationItem navigationItem;

    NavigationMenu(int icon, int title, @NonNull NavigationItem navigationItem, @NonNull List<NavigationOption> navigationOptions) {
        this.icon = icon;
        this.title = title;
        this.navigationItem = navigationItem;
        this.navigationOptions = navigationOptions;
    }

    NavigationMenu(int icon, int title, @NonNull NavigationItem navigationItem) {
        this(icon, title, navigationItem, NavigationOptionFactory.OFF);
    }

    public int getTitle() {
        return title;
    }

    public void activateNavigationMenu(@NonNull MainActivity mainActivity, @NonNull MenuItem menuItem) {
        navigationItem.activate(mainActivity, menuItem, this);
    }

    public void activateOptions(@NonNull MainActivity mainActivity) {
        IterableUtils.forEach(navigationOptions, new ActivateClosure(mainActivity));
    }

    public boolean isWiFiBandSwitchable() {
        return navigationOptions.contains(NavigationOptionFactory.WIFI_SWITCH_ON);
    }

    public boolean isRegistered() {
        return navigationItem.isRegistered();
    }

    int getIcon() {
        return icon;
    }

    @NonNull
    NavigationItem getNavigationItem() {
        return navigationItem;
    }

    @NonNull
    List<NavigationOption> getNavigationOptions() {
        return navigationOptions;
    }

    private class ActivateClosure implements Closure<NavigationOption> {
        private final MainActivity mainActivity;

        private ActivateClosure(@NonNull MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void execute(NavigationOption input) {
            input.apply(mainActivity);
        }
    }
}
