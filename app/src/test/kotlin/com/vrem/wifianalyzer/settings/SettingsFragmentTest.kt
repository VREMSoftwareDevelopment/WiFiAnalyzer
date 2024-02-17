/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.os.Build
import androidx.preference.Preference
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class SettingsFragmentTest {

    @Test
    fun onCreate() {
        // setup
        val fixture = SettingsFragment()
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        assertThat(fixture.view).isNotNull()
    }

    @Config(sdk = [Build.VERSION_CODES.P])
    @Test
    fun wiFiOnExitIsVisible() {
        // setup
        val fixture = SettingsFragment()
        RobolectricUtil.INSTANCE.startFragment(fixture)
        val key = fixture.getString(R.string.wifi_off_on_exit_key)
        // execute
        val actual = fixture.findPreference<Preference>(key)
        // validate
        assertThat(actual!!.isVisible).isTrue()
    }

    @Test
    fun wiFiOnExitIsNotVisible() {
        // setup
        val fixture = SettingsFragment()
        RobolectricUtil.INSTANCE.startFragment(fixture)
        val key = fixture.getString(R.string.wifi_off_on_exit_key)
        // execute
        val actual = fixture.findPreference<Preference>(key)
        // validate
        assertThat(actual!!.isVisible).isFalse()
    }

}