/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.about.AboutActivity;
import com.vrem.wifianalyzer.settings.SettingActivity;
import com.vrem.wifianalyzer.vendor.VendorFragment;
import com.vrem.wifianalyzer.wifi.AccessPointsFragment;
import com.vrem.wifianalyzer.wifi.ChannelAvailableFragment;
import com.vrem.wifianalyzer.wifi.ChannelRatingFragment;
import com.vrem.wifianalyzer.wifi.graph.channel.ChannelGraphFragment;
import com.vrem.wifianalyzer.wifi.graph.time.TimeGraphFragment;

public enum NavigationMenu {
    ACCESS_POINTS(R.drawable.ic_network_wifi_grey_500_48dp, R.string.action_access_points, true, new AccessPointsFragment()),
    CHANNEL_RATING(R.drawable.ic_wifi_tethering_grey_500_48dp, R.string.action_channel_rating, true, new ChannelRatingFragment()),
    CHANNEL_GRAPH(R.drawable.ic_insert_chart_grey_500_48dp, R.string.action_channel_graph, true, new ChannelGraphFragment()),
    TIME_GRAPH(R.drawable.ic_show_chart_grey_500_48dp, R.string.action_time_graph, true, new TimeGraphFragment()),
    CHANNEL_AVAILABLE(R.drawable.ic_location_on_grey_500_48dp, R.string.action_channel_available, false, new ChannelAvailableFragment()),
    VENDOR_LIST(R.drawable.ic_list_grey_500_48dp, R.string.action_vendors, false, new VendorFragment()),
    SETTINGS(R.drawable.ic_settings_grey_500_48dp, R.string.action_settings, SettingActivity.class),
    ABOUT(R.drawable.ic_info_outline_grey_500_48dp, R.string.action_about, AboutActivity.class);

    private final int icon;
    private final int title;
    private final boolean wiFiBandSwitchable;
    private final Fragment fragment;
    private final Class<? extends Activity> activity;

    NavigationMenu(int icon, int title, boolean wiFiBandSwitchable, @NonNull Fragment fragment) {
        this.icon = icon;
        this.title = title;
        this.wiFiBandSwitchable = wiFiBandSwitchable;
        this.fragment = fragment;
        this.activity = null;
    }

    NavigationMenu(int icon, int title, @NonNull Class<? extends Activity> activity) {
        this.icon = icon;
        this.title = title;
        this.wiFiBandSwitchable = false;
        this.fragment = null;
        this.activity = activity;
    }

    public static NavigationMenu find(int index) {
        if (index < 0 || index >= values().length) {
            return ACCESS_POINTS;
        }
        return values()[index];
    }

    public Fragment getFragment() {
        return fragment;
    }

    public Class<? extends Activity> getActivity() {
        return activity;
    }

    public int getTitle() {
        return title;
    }

    public boolean isWiFiBandSwitchable() {
        return wiFiBandSwitchable;
    }

    int getIcon() {
        return icon;
    }

    public void activateNavigationMenu(@NonNull MainActivity mainActivity, MenuItem menuItem) {
        Fragment fragment = getFragment();
        if (fragment == null) {
            mainActivity.startActivity(new Intent(mainActivity, getActivity()));
        } else {
            mainActivity.getNavigationMenuView().setCurrentNavigationMenu(this);
            mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment).commit();
            mainActivity.setTitle(menuItem.getTitle());
            mainActivity.updateSubTitle();
        }
    }

}
