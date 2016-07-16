/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.vrem.wifianalyzer.settings;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphLegend;
import com.vrem.wifianalyzer.wifi.model.GroupBy;
import com.vrem.wifianalyzer.wifi.model.SortBy;

import static android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class Settings {
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
        String countryCode = context.getResources().getConfiguration().locale.getCountry();
        return repository.getString(R.string.country_code_key, countryCode);
    }

    public NavigationMenu getStartMenu() {
        return NavigationMenu.find(repository.getStringAsInteger(R.string.start_menu_key, NavigationMenu.ACCESS_POINTS.ordinal()));
    }
}
