/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.settings

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.preference.PreferenceManager
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.R

@OpenClass
class Repository(private val context: Context) {
    fun initializeDefaultValues() {
        setDefaultValues(context, R.xml.settings, false)
    }

    fun registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener: OnSharedPreferenceChangeListener?) {
        sharedPreferences().registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    fun save(key: Int, value: Int) {
        save(key, value.toString())
    }

    fun save(key: Int, value: String) {
        save(context.getString(key), value)
    }

    fun getStringAsInteger(key: Int, defaultValue: Int): Int {
        return try {
            getString(key, defaultValue.toString()).toInt()
        } catch (e: Exception) {
            defaultValue
        }
    }

    fun getString(key: Int, defaultValue: String): String {
        val keyValue: String = context.getString(key)
        return try {
            sharedPreferences().getString(keyValue, defaultValue) ?: defaultValue
        } catch (e: Exception) {
            save(keyValue, defaultValue)
            defaultValue
        }
    }

    fun getBoolean(key: Int, defaultValue: Boolean): Boolean {
        val keyValue: String = context.getString(key)
        return try {
            sharedPreferences().getBoolean(keyValue, defaultValue)
        } catch (e: Exception) {
            save(keyValue, defaultValue)
            defaultValue
        }
    }

    fun getResourceBoolean(key: Int): Boolean {
        return context.resources.getBoolean(key)
    }

    fun getInteger(key: Int, defaultValue: Int): Int {
        val keyValue: String = context.getString(key)
        return try {
            sharedPreferences().getInt(keyValue, defaultValue)
        } catch (e: Exception) {
            save(keyValue, Integer.toString(defaultValue))
            defaultValue
        }
    }

    fun getStringSet(key: Int, defaultValues: Set<String>): Set<String> {
        val keyValue: String = context.getString(key)
        return try {
            sharedPreferences().getStringSet(keyValue, defaultValues)!!
        } catch (e: Exception) {
            save(keyValue, defaultValues)
            defaultValues
        }
    }

    fun saveStringSet(key: Int, values: Set<String>) {
        save(context.getString(key), values)
    }

    private fun sharedPreferences(): SharedPreferences = getDefaultSharedPreferences(context)

    private fun save(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPreferences().edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun save(key: String, value: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences().edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun save(key: String, values: Set<String>) {
        val editor: SharedPreferences.Editor = sharedPreferences().edit()
        editor.putStringSet(key, values)
        editor.apply()
    }

    fun setDefaultValues(context: Context?, resId: Int, readAgain: Boolean) {
        PreferenceManager.setDefaultValues(context, resId, readAgain)
    }

    fun getDefaultSharedPreferences(context: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

}