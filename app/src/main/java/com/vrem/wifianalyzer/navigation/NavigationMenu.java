/*
 * WiFi Analyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.support.annotation.NonNull;
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

import java.util.Arrays;
import java.util.List;

public enum NavigationMenu {
    ACCESS_POINTS(R.drawable.ic_network_wifi_grey_500_48dp, R.string.action_access_points, new FragmentItem(new AccessPointsFragment()), Options.values()),
    CHANNEL_RATING(R.drawable.ic_wifi_tethering_grey_500_48dp, R.string.action_channel_rating, new FragmentItem(new ChannelRatingFragment()), Options.values()),
    CHANNEL_GRAPH(R.drawable.ic_insert_chart_grey_500_48dp, R.string.action_channel_graph, new FragmentItem(new ChannelGraphFragment()), Options.values()),
    TIME_GRAPH(R.drawable.ic_show_chart_grey_500_48dp, R.string.action_time_graph, new FragmentItem(new TimeGraphFragment()), Options.values()),
    EXPORT(R.drawable.ic_import_export_grey_500_48dp, R.string.action_export, new ExportItem()),
    CHANNEL_AVAILABLE(R.drawable.ic_location_on_grey_500_48dp, R.string.action_channel_available, new FragmentItem(new ChannelAvailableFragment())),
    VENDOR_LIST(R.drawable.ic_list_grey_500_48dp, R.string.action_vendors, new FragmentItem(new VendorFragment())),
    SETTINGS(R.drawable.ic_settings_grey_500_48dp, R.string.action_settings, new ActivityItem(SettingActivity.class)),
    ABOUT(R.drawable.ic_info_outline_grey_500_48dp, R.string.action_about, new ActivityItem(AboutActivity.class));

    private final int icon;
    private final int title;
    private final List<Options> options;
    private final NavigationMenuItem item;

    NavigationMenu(int icon, int title, @NonNull NavigationMenuItem item, Options... options) {
        this.icon = icon;
        this.title = title;
        this.item = item;
        this.options = Arrays.asList(options);
    }

    NavigationMenu(int icon, int title, @NonNull NavigationMenuItem item) {
        this(icon, title, item, new Options[]{});
    }

    public static NavigationMenu find(int index) {
        if (index < 0 || index >= values().length) {
            return ACCESS_POINTS;
        }
        return values()[index];
    }

    public int getTitle() {
        return title;
    }

    public List<Options> getOptions() {
        return options;
    }

    int getIcon() {
        return icon;
    }

    public void activateNavigationMenu(@NonNull MainActivity mainActivity, @NonNull MenuItem menuItem) {
        item.activate(mainActivity, menuItem, this);
    }

    NavigationMenuItem getItem() {
        return item;
    }
}
