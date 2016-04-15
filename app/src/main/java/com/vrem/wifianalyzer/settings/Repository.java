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
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;

class Repository {
    private Context context;
    private Resources resources;

    protected void initializeDefaultValues() {
        PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, false);
    }

    protected void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        getSharedPreferences().registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    protected void save(int key, int value) {
        save(getContext().getString(key), value);
    }

    private void save(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, "" + value);
        editor.apply();
    }

    protected int getStringAsInteger(int key, int defaultValue) {
        String keyValue = getContext().getString(key);
        try {
            return Integer.parseInt(getSharedPreferences().getString(keyValue, "" + defaultValue));
        } catch (Exception e) {
            save(keyValue, defaultValue);
            return defaultValue;
        }
    }

    protected int getResourceInteger(int key) {
        return getResources().getInteger(key);
    }

    protected int getInteger(int key, int defaultValue) {
        String keyValue = getContext().getString(key);
        try {
            return getSharedPreferences().getInt(keyValue, defaultValue);
        } catch (Exception e) {
            save(keyValue, defaultValue);
            return defaultValue;
        }
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    // injectors start
    private Context getContext() {
        if (context == null) {
            context = MainContext.INSTANCE.getContext();
        }
        return context;
    }

    protected void setContext(@NonNull Context context) {
        this.context = context;
    }

    private Resources getResources() {
        if (resources == null) {
            resources = MainContext.INSTANCE.getResources();
        }
        return resources;
    }

    protected void setResources(@NonNull Resources resources) {
        this.resources = resources;
    }
    // injectors end
}
