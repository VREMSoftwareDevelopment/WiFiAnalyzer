/*
 * WiFiAnalyzer
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

package com.vrem.wifianalyzer.navigation.items;

import com.vrem.wifianalyzer.about.AboutActivity;
import com.vrem.wifianalyzer.settings.SettingActivity;
import com.vrem.wifianalyzer.vendor.VendorFragment;
import com.vrem.wifianalyzer.wifi.accesspoint.AccessPointsFragment;
import com.vrem.wifianalyzer.wifi.channelavailable.ChannelAvailableFragment;
import com.vrem.wifianalyzer.wifi.channelgraph.ChannelGraphFragment;
import com.vrem.wifianalyzer.wifi.channelrating.ChannelRatingFragment;
import com.vrem.wifianalyzer.wifi.timegraph.TimeGraphFragment;

public class NavigationItemFactory {
    public final static NavigationItem ACCESS_POINTS = new FragmentItem(new AccessPointsFragment(), true);
    public final static NavigationItem CHANNEL_RATING = new FragmentItem(new ChannelRatingFragment(), true);
    public final static NavigationItem CHANNEL_GRAPH = new FragmentItem(new ChannelGraphFragment(), true);
    public final static NavigationItem TIME_GRAPH = new FragmentItem(new TimeGraphFragment(), true);
    public final static NavigationItem EXPORT = new ExportItem();
    public final static NavigationItem CHANNEL_AVAILABLE = new FragmentItem(new ChannelAvailableFragment());
    public final static NavigationItem VENDOR_LIST = new FragmentItem(new VendorFragment());
    public final static NavigationItem SETTINGS = new ActivityItem(SettingActivity.class);
    public final static NavigationItem ABOUT = new ActivityItem(AboutActivity.class);
}
