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
        Context context = mainContext.getContext();
        int defaultValue = context.getResources().getInteger(R.integer.scan_interval_default);
        return getSharedPreferences().getInt(context.getString(R.string.scan_interval_key), defaultValue);
    }

    public SortBy getSortBy() {
        Context context = mainContext.getContext();
        String defaultValue = context.getResources().getString(R.string.sort_by_default);
        return SortBy.find(getSharedPreferences().getString(context.getString(R.string.sort_by_key), defaultValue));
    }

    public GroupBy getGroupBy() {
        Context context = mainContext.getContext();
        String defaultValue = context.getResources().getString(R.string.group_by_default);
        return GroupBy.find(getSharedPreferences().getString(context.getString(R.string.group_by_key), defaultValue));
    }

    public GraphLegend getChannelGraphLegend() {
        Context context = mainContext.getContext();
        String defaultValue = context.getResources().getString(R.string.channel_graph_legend_default);
        return GraphLegend.find(getSharedPreferences().getString(context.getString(R.string.channel_graph_legend_key), defaultValue), GraphLegend.HIDE);
    }

    public GraphLegend getTimeGraphLegend() {
        Context context = mainContext.getContext();
        String defaultValue = context.getResources().getString(R.string.time_graph_legend_default);
        return GraphLegend.find(getSharedPreferences().getString(context.getString(R.string.time_graph_legend_key), defaultValue), GraphLegend.LEFT);
    }

    public WiFiBand getWiFiBand() {
        Context context = mainContext.getContext();
        String defaultValue = context.getResources().getString(R.string.wifi_band_default);
        return WiFiBand.findByBand(getSharedPreferences().getString(context.getString(R.string.wifi_band_key), defaultValue));
    }

    public ThemeStyle getThemeStyle() {
        Context context = mainContext.getContext();
        String defaultValue = context.getResources().getString(R.string.theme_default);
        return ThemeStyle.find(getSharedPreferences().getString(context.getString(R.string.theme_key), defaultValue));
    }

    public void toggleWiFiBand() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(mainContext.getContext().getString(R.string.wifi_band_key), getWiFiBand().toggle().getBand());
        editor.commit();
    }
}
