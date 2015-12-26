/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.GroupBy;

public class Settings {

    private final Context context;

    public Settings(@NonNull Context context) {
        this.context = context;
    }

    public void initializeDefaultValues() {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
    }

    public SharedPreferences sharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int scanInterval() {
        int defaultValue = context.getResources().getInteger(R.integer.scan_interval_default);
        return sharedPreferences().getInt(context.getString(R.string.scan_interval_key), defaultValue);
    }

    public boolean hideWeakSignal() {
        boolean defaultValue = context.getResources().getBoolean(R.bool.hide_weak_signal_default);
        return sharedPreferences().getBoolean(context.getString(R.string.hide_weak_signal_key), defaultValue);
    }

    public GroupBy groupBy() {
        String defaultValue = context.getResources().getString(R.string.group_by_default);
        return GroupBy.find(sharedPreferences().getString(context.getString(R.string.group_by_key), defaultValue));
    }

    public ThemeStyle themeStyle() {
        String defaultValue = context.getResources().getString(R.string.theme_default);
        return ThemeStyle.find(sharedPreferences().getString(context.getString(R.string.theme_key), defaultValue));
    }
}
