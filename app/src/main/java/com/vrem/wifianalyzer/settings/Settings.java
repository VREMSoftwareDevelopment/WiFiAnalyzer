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

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.graph.GraphLegend;
import com.vrem.wifianalyzer.wifi.model.GroupBy;
import com.vrem.wifianalyzer.wifi.model.SortBy;

public class Settings {
    private final MainContext mainContext = MainContext.INSTANCE;

    public Settings() {
    }

    public void initializeDefaultValues() {
        PreferenceManager.setDefaultValues(mainContext.getContext(), R.xml.preferences, false);
    }

    public SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mainContext.getContext());
    }

    public int getScanInterval() {
        int defaultValue = mainContext.getContext().getResources().getInteger(R.integer.scan_interval_default);
        return getSharedPreferences().getInt(mainContext.getContext().getString(R.string.scan_interval_key), defaultValue);
    }

    public SortBy getSortBy() {
        return SortBy.find(getInt(R.string.sort_by_key, SortBy.STRENGTH.ordinal()));
    }

    public GroupBy getGroupBy() {
        return GroupBy.find(getInt(R.string.group_by_key, GroupBy.NONE.ordinal()));
    }

    public GraphLegend getChannelGraphLegend() {
        return GraphLegend.find(getInt(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal()), GraphLegend.HIDE);
    }

    public GraphLegend getTimeGraphLegend() {
        return GraphLegend.find(getInt(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal()), GraphLegend.LEFT);
    }

    public WiFiBand getWiFiBand() {
        return WiFiBand.find(getInt(R.string.wifi_band_key, WiFiBand.GHZ_2.ordinal()));
    }

    public ThemeStyle getThemeStyle() {
        return ThemeStyle.find(getInt(R.string.theme_key, ThemeStyle.DARK.ordinal()));
    }

    public void toggleWiFiBand() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(mainContext.getContext().getString(R.string.wifi_band_key), ""+getWiFiBand().toggle().ordinal());
        editor.apply();
    }

    private int getInt(int key, int defaultValue) {
        try {
            return Integer.parseInt(getSharedPreferences().getString(mainContext.getContext().getString(key), "" + defaultValue));
        } catch (Exception e) {
            return defaultValue;
        }
    }

}
