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

package com.vrem.wifianalyzer.settings;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;

import java.util.Set;

class Repository {
    void initializeDefaultValues() {
        PreferenceManager.setDefaultValues(MainContext.INSTANCE.getMainActivity(), R.xml.preferences, false);
    }

    void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        getSharedPreferences().registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    void save(int key, int value) {
        save(key, "" + value);
    }

    void save(int key, String value) {
        save(MainContext.INSTANCE.getMainActivity().getString(key), value);
    }

    int getStringAsInteger(int key, int defaultValue) {
        try {
            return Integer.parseInt(getString(key, "" + defaultValue));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    String getString(int key, @NonNull String defaultValue) {
        String keyValue = MainContext.INSTANCE.getMainActivity().getString(key);
        try {
            return getSharedPreferences().getString(keyValue, defaultValue);
        } catch (Exception e) {
            save(keyValue, defaultValue);
            return defaultValue;
        }
    }

    boolean getBoolean(int key, boolean defaultValue) {
        String keyValue = MainContext.INSTANCE.getMainActivity().getString(key);
        try {
            return getSharedPreferences().getBoolean(keyValue, defaultValue);
        } catch (Exception e) {
            save(keyValue, defaultValue);
            return defaultValue;
        }
    }

    int getResourceInteger(int key) {
        return MainContext.INSTANCE.getMainActivity().getResources().getInteger(key);
    }

    boolean getResourceBoolean(int key) {
        return MainContext.INSTANCE.getMainActivity().getResources().getBoolean(key);
    }

    int getInteger(int key, int defaultValue) {
        String keyValue = MainContext.INSTANCE.getMainActivity().getString(key);
        try {
            return getSharedPreferences().getInt(keyValue, defaultValue);
        } catch (Exception e) {
            save(keyValue, "" + defaultValue);
            return defaultValue;
        }
    }

    Set<String> getStringSet(int key, @NonNull Set<String> defaultValues) {
        String keyValue = MainContext.INSTANCE.getMainActivity().getString(key);
        try {
            return getSharedPreferences().getStringSet(keyValue, defaultValues);
        } catch (Exception e) {
            save(keyValue, defaultValues);
            return defaultValues;
        }
    }

    void saveStringSet(int key, @NonNull Set<String> values) {
        save(MainContext.INSTANCE.getMainActivity().getString(key), values);
    }

    private SharedPreferences getSharedPreferences() {
        MainActivity mainActivity = MainContext.INSTANCE.getMainActivity();
        return PreferenceManager.getDefaultSharedPreferences(mainActivity);
    }

    private void save(@NonNull String key, @NonNull String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void save(@NonNull String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void save(@NonNull String key, @NonNull Set<String> values) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putStringSet(key, values);
        editor.apply();
    }

}
