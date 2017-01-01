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

package com.vrem.wifianalyzer.settings;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.wifi.AccessPointView;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphLegend;
import com.vrem.wifianalyzer.wifi.model.GroupBy;
import com.vrem.wifianalyzer.wifi.model.SortBy;

import static android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class Settings {
    static final int GRAPH_Y_MULTIPLIER = -10;

    private final Context context;
    private Repository repository;

    public Settings(@NonNull Context context) {
        this.context = context;
        setRepository(new Repository());
    }

    public void setRepository(@NonNull Repository repository) {
        this.repository = repository;
    }

    public void initializeDefaultValues() {
        repository.initializeDefaultValues();
    }

    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        repository.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public int getScanInterval() {
        return repository.getInteger(R.string.scan_interval_key, repository.getResourceInteger(R.integer.scan_interval_default));
    }

    public SortBy getSortBy() {
        return SortBy.find(repository.getStringAsInteger(R.string.sort_by_key, SortBy.STRENGTH.ordinal()));
    }

    public GroupBy getGroupBy() {
        return GroupBy.find(repository.getStringAsInteger(R.string.group_by_key, GroupBy.NONE.ordinal()));
    }

    public AccessPointView getAccessPointView() {
        return AccessPointView.find(repository.getStringAsInteger(R.string.ap_view_key, AccessPointView.COMPLETE.ordinal()));
    }

    public int getGraphMaximumY() {
        int defaultValue = repository.getStringAsInteger(R.string.graph_maximum_y_default, 0);
        int result = repository.getStringAsInteger(R.string.graph_maximum_y_key, defaultValue);
        return result * GRAPH_Y_MULTIPLIER;
    }

    public GraphLegend getChannelGraphLegend() {
        return GraphLegend.find(repository.getStringAsInteger(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal()), GraphLegend.HIDE);
    }

    public GraphLegend getTimeGraphLegend() {
        return GraphLegend.find(repository.getStringAsInteger(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal()), GraphLegend.LEFT);
    }

    public WiFiBand getWiFiBand() {
        return WiFiBand.find(repository.getStringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal()));
    }

    public ThemeStyle getThemeStyle() {
        return ThemeStyle.find(repository.getStringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal()));
    }

    public void toggleWiFiBand() {
        repository.save(R.string.wifi_band_key, getWiFiBand().toggle().ordinal());
    }

    public String getCountryCode() {
        String countryCode = CountryPreference.getDefault(context);
        return repository.getString(R.string.country_code_key, countryCode);
    }

    public NavigationMenu getStartMenu() {
        return NavigationMenu.find(repository.getStringAsInteger(R.string.start_menu_key, NavigationMenu.ACCESS_POINTS.ordinal()));
    }
}
