/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2026 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.vrem.util.EMPTY
import com.vrem.util.buildMinVersionQ
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R

internal const val DIALOG_FRAGMENT_TAG = "androidx.preference.PreferenceFragment.DIALOG"

open class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(
        bundle: Bundle?,
        rootKey: String?,
    ) {
        setupPreferences()
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        when (preference) {
            is CustomPreference -> showCustomPreferenceDialog(preference)
            else -> super.onDisplayPreferenceDialog(preference)
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference.key != getString(R.string.reset_key)) return super.onPreferenceTreeClick(preference)
        resetPreferences()
        return true
    }

    private fun showCustomPreferenceDialog(preference: CustomPreference) {
        if (parentFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) return
        val dialogFragment = CustomPreferenceDialogFragment.newInstance(preference.key)
        @Suppress("DEPRECATION")
        dialogFragment.setTargetFragment(this, 0)
        dialogFragment.show(parentFragmentManager, DIALOG_FRAGMENT_TAG)
    }

    private fun resetPreferences() {
        preferenceManager.sharedPreferences!!.edit { clear() }
        MainContext.INSTANCE.settings.saveLanguageTag(String.EMPTY)
        preferenceScreen.removeAll()
        setupPreferences()
    }

    private fun setupPreferences() {
        addPreferencesFromResource(R.xml.settings)
        findPreference<Preference>(getString(R.string.wifi_off_on_exit_key))!!.isVisible = !buildMinVersionQ()
    }
}
