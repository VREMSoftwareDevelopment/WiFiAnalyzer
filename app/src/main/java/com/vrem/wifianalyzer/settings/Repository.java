/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import com.vrem.wifianalyzer.R;

import java.util.Set;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

public class Repository {
    private final Context context;

    public Repository(@NonNull Context context) {
        this.context = context;
    }

    void initializeDefaultValues() {
        PreferenceManager.setDefaultValues(context, R.xml.settings, false);
    }

    void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        getSharedPreferences().registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    void save(int key, int value) {
        save(key, Integer.toString(value));
    }

    void save(int key, String value) {
        save(context.getString(key), value);
    }

    int getStringAsInteger(int key, int defaultValue) {
        try {
            return Integer.parseInt(getString(key, Integer.toString(defaultValue)));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @NonNull
    String getString(int key, @NonNull String defaultValue) {
        String keyValue = context.getString(key);
        try {
            return getSharedPreferences().getString(keyValue, defaultValue);
        } catch (Exception e) {
            save(keyValue, defaultValue);
            return defaultValue;
        }
    }

    boolean getBoolean(int key, boolean defaultValue) {
        String keyValue = context.getString(key);
        try {
            return getSharedPreferences().getBoolean(keyValue, defaultValue);
        } catch (Exception e) {
            save(keyValue, defaultValue);
            return defaultValue;
        }
    }

    boolean getResourceBoolean(int key) {
        return context.getResources().getBoolean(key);
    }

    int getInteger(int key, int defaultValue) {
        String keyValue = context.getString(key);
        try {
            return getSharedPreferences().getInt(keyValue, defaultValue);
        } catch (Exception e) {
            save(keyValue, Integer.toString(defaultValue));
            return defaultValue;
        }
    }

    @NonNull
    Set<String> getStringSet(int key, @NonNull Set<String> defaultValues) {
        String keyValue = context.getString(key);
        try {
            return getSharedPreferences().getStringSet(keyValue, defaultValues);
        } catch (Exception e) {
            save(keyValue, defaultValues);
            return defaultValues;
        }
    }

    void saveStringSet(int key, @NonNull Set<String> values) {
        save(context.getString(key), values);
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void save(@NonNull String key, @NonNull String value) {
        Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void save(@NonNull String key, boolean value) {
        Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void save(@NonNull String key, @NonNull Set<String> values) {
        Editor editor = getSharedPreferences().edit();
        editor.putStringSet(key, values);
        editor.apply();
    }

}
