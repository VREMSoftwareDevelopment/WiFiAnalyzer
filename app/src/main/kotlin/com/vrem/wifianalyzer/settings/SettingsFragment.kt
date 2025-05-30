/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2025 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.os.Bundle
import androidx.core.content.edit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.vrem.util.buildMinVersionQ
import com.vrem.wifianalyzer.R

open class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        setupPreferences()
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference.key == getString(R.string.reset_key)) {
            preferenceManager.sharedPreferences!!.edit { clear() }
            preferenceScreen.removeAll()
            setupPreferences()
            return true
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun setupPreferences() {
        addPreferencesFromResource(R.xml.settings)
        findPreference<Preference>(getString(R.string.wifi_off_on_exit_key))!!
            .isVisible = !buildMinVersionQ()
    }
}