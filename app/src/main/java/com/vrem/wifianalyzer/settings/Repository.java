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
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;

class Repository {

    void initializeDefaultValues() {
        PreferenceManager.setDefaultValues(MainContext.INSTANCE.getContext(), R.xml.preferences, false);
    }

    void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        getSharedPreferences().registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    void save(int key, int value) {
        save(MainContext.INSTANCE.getContext().getString(key), value);
    }

    private void save(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, "" + value);
        editor.apply();
    }

    int getStringAsInteger(int key, int defaultValue) {
        String keyValue = MainContext.INSTANCE.getContext().getString(key);
        try {
            return Integer.parseInt(getSharedPreferences().getString(keyValue, "" + defaultValue));
        } catch (Exception e) {
            save(keyValue, defaultValue);
            return defaultValue;
        }
    }

    int getResourceInteger(int key) {
        return MainContext.INSTANCE.getResources().getInteger(key);
    }

    int getInteger(int key, int defaultValue) {
        String keyValue = MainContext.INSTANCE.getContext().getString(key);
        try {
            return getSharedPreferences().getInt(keyValue, defaultValue);
        } catch (Exception e) {
            save(keyValue, defaultValue);
            return defaultValue;
        }
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(MainContext.INSTANCE.getContext());
    }
}
