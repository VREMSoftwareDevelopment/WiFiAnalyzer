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

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.DialogFragment
import androidx.preference.ListPreference
import androidx.preference.ListPreferenceDialogFragmentCompat
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class SettingsFragmentTest {
    private val fixture = SettingsFragment()

    @Before
    fun setUp() {
        RobolectricUtil.INSTANCE.startFragment(fixture)
    }

    @After
    fun tearDown() {
        (fixture.parentFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) as? DialogFragment)
            ?.dismissAllowingStateLoss()
        RobolectricUtil.INSTANCE.clearLooper()
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
    }

    @Test
    fun onCreatePreferencesCreatesView() {
        // Assert
        assertThat(fixture.view).isNotNull()
    }

    @Config(sdk = [Build.VERSION_CODES.P])
    @Test
    fun wifiOffOnExitPreferenceIsVisibleOnLegacyAndroid() {
        // Arrange
        val wifiOffOnExitKey = fixture.getString(R.string.wifi_off_on_exit_key)
        // Act
        val actual = fixture.findPreference<Preference>(wifiOffOnExitKey)
        // Assert
        assertThat(actual!!.isVisible).isTrue
    }

    @Test
    fun wifiOffOnExitPreferenceIsHiddenOnAndroidQOrLater() {
        // Arrange
        val wifiOffOnExitKey = fixture.getString(R.string.wifi_off_on_exit_key)
        // Act
        val actual = fixture.findPreference<Preference>(wifiOffOnExitKey)
        // Assert
        assertThat(actual!!.isVisible).isFalse
    }

    @Test
    fun resetPreferenceClearsPreferencesAndReloadsSettings() {
        // Arrange
        val wifiOffOnExitKey = fixture.getString(R.string.wifi_off_on_exit_key)
        val wifiPreference = fixture.findPreference<SwitchPreferenceCompat>(wifiOffOnExitKey)!!
        wifiPreference.isChecked = true
        val resetKey = fixture.getString(R.string.reset_key)
        val resetPreference = fixture.findPreference<Preference>(resetKey)!!
        // Act
        val actual = fixture.onPreferenceTreeClick(resetPreference)
        // Assert
        assertThat(actual).isTrue
        assertThat(fixture.preferenceScreen.preferenceCount).isGreaterThan(0)
        assertThat(fixture.findPreference<SwitchPreferenceCompat>(wifiOffOnExitKey)!!.isChecked).isFalse
        assertThat(fixture.findPreference<Preference>(wifiOffOnExitKey)!!.isVisible).isFalse
    }

    @Test
    fun resetPreferenceRestoresSystemLanguage() {
        // Arrange
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("de"))
        val resetKey = fixture.getString(R.string.reset_key)
        val resetPreference = fixture.findPreference<Preference>(resetKey)!!
        // Act
        val actual = fixture.onPreferenceTreeClick(resetPreference)
        // Assert
        assertThat(actual).isTrue
        assertThat(AppCompatDelegate.getApplicationLocales().isEmpty).isTrue
    }

    @Test
    fun unknownPreferenceClickReturnsFalse() {
        // Arrange
        val preference = Preference(fixture.requireContext())
        preference.key = "unknown_key"
        // Act
        val actual = fixture.onPreferenceTreeClick(preference)
        // Assert
        assertThat(actual).isFalse
    }

    @Config(sdk = [Build.VERSION_CODES.P])
    @Test
    fun resetPreferenceRestoresSettingsOnLegacyAndroid() {
        // Arrange
        val wifiOffOnExitKey = fixture.getString(R.string.wifi_off_on_exit_key)
        val resetKey = fixture.getString(R.string.reset_key)
        val resetPreference = fixture.findPreference<Preference>(resetKey)!!
        // Act
        val actual = fixture.onPreferenceTreeClick(resetPreference)
        // Assert
        assertThat(actual).isTrue
        assertThat(fixture.preferenceScreen.preferenceCount).isGreaterThan(0)
        assertThat(fixture.findPreference<Preference>(wifiOffOnExitKey)!!.isVisible).isTrue
    }

    @Test
    fun customPreferenceOpensCustomDialog() {
        // Arrange
        val preference = withCountryPreference()
        // Act
        fixture.onDisplayPreferenceDialog(preference)
        RobolectricUtil.INSTANCE.clearLooper()
        // Assert
        val actual = fixture.parentFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG)
        assertThat(actual).isInstanceOf(CustomPreferenceDialogFragment::class.java)
    }

    @Test
    fun nonCustomPreferenceUsesDefaultDialog() {
        // Arrange
        val scanSpeedKey = fixture.getString(R.string.scan_speed_key)
        val preference = fixture.findPreference<ListPreference>(scanSpeedKey)!!
        // Act
        fixture.onDisplayPreferenceDialog(preference)
        RobolectricUtil.INSTANCE.clearLooper()
        // Assert
        val actual = fixture.parentFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG)
        assertThat(actual).isInstanceOf(ListPreferenceDialogFragmentCompat::class.java)
    }

    @Test
    fun customPreferenceDialogIsNotShownWhenDefaultDialogIsShowing() {
        // Arrange
        val scanSpeed = fixture.findPreference<ListPreference>(fixture.getString(R.string.scan_speed_key))!!
        fixture.onDisplayPreferenceDialog(scanSpeed)
        RobolectricUtil.INSTANCE.clearLooper()
        // Act
        fixture.onDisplayPreferenceDialog(withCountryPreference())
        RobolectricUtil.INSTANCE.clearLooper()
        // Assert
        val actual = fixture.parentFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG)
        assertThat(actual).isInstanceOf(ListPreferenceDialogFragmentCompat::class.java)
    }

    @Test
    fun customPreferenceDialogIsNotReplacedWhenOneIsAlreadyShowing() {
        // Arrange
        val preference = withCountryPreference()
        fixture.onDisplayPreferenceDialog(preference)
        RobolectricUtil.INSTANCE.clearLooper()
        val existingDialog = fixture.parentFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG)
        // Act
        fixture.onDisplayPreferenceDialog(preference)
        RobolectricUtil.INSTANCE.clearLooper()
        // Assert
        val actual = fixture.parentFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG)
        assertThat(actual).isSameAs(existingDialog)
    }

    private fun withCountryPreference(): CustomPreference =
        fixture.findPreference(fixture.getString(R.string.country_code_key))!!
}
