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

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.View
import android.widget.ListView
import android.widget.SearchView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

private const val PREFERENCE_KEY = "test_key"
private const val UNITED_STATES_CODE = "US"
private const val CANADA_CODE = "CA"
private const val CANADA_NAME = "Canada"
private const val GERMAN_LANGUAGE_TAG = "de"
private const val LIST_WIDTH = 100
private const val LIST_HEIGHT = 10

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class CustomPreferenceDialogFragmentTest {
    private val mainActivity: Context = RobolectricUtil.INSTANCE.mainActivity
    private val countries = listOf(Data(UNITED_STATES_CODE, "United States"), Data(CANADA_CODE, CANADA_NAME))
    private val settingsFragment = SettingsFragment()

    @Before
    fun setUp() {
        RobolectricUtil.INSTANCE.startFragment(settingsFragment)
    }

    @After
    fun tearDown() {
        (shownDialog() as? DialogFragment)?.dismissAllowingStateLoss()
        RobolectricUtil.INSTANCE.clearLooper()
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
    }

    @Test
    fun newInstanceStoresPreferenceKeyInArguments() {
        // Act
        val actual = CustomPreferenceDialogFragment.newInstance(PREFERENCE_KEY).arguments?.getString("key")
        // Assert
        assertThat(actual).isEqualTo(PREFERENCE_KEY)
    }

    @Test
    fun dialogChecksCurrentValue() {
        // Arrange
        val preference = withPreference(R.string.country_code_key)
        // Act
        val listView = withDialog(preference).list()
        val actual = listView.checkedItemPosition
        // Assert
        assertThat(actual).isNotEqualTo(ListView.INVALID_POSITION)
        assertThat((listView.adapter as CustomPreferenceAdapter).getItem(actual)!!.code).isEqualTo(preference.value)
    }

    @Test
    fun dialogListHasNoDividers() {
        // Arrange
        val preference = withPreference(R.string.country_code_key)
        // Act
        val actual = withDialog(preference).list().divider
        // Assert
        assertThat(actual).isNull()
    }

    @Test
    fun dialogHeightIsSixtyPercentOfScreenHeight() {
        // Arrange
        val preference = withPreference(R.string.country_code_key)
        val expected = (mainActivity.resources.displayMetrics.heightPixels * 0.6f).toInt()
        // Act
        val actual = (withDialog(preference).searchView().parent as View).layoutParams.height
        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun dialogHasNoPositiveButton() {
        // Arrange
        val preference = withPreference(R.string.country_code_key)
        // Act
        val actual = (withDialog(preference).dialog as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE)
        // Assert
        assertThat(actual.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun selectingCountryUpdatesValueAndClosesDialog() {
        // Arrange
        val preference = withPreference(R.string.country_code_key)
        // Act
        val actual = selectDifferentValue(preference)
        // Assert
        assertThat(preference.value).isEqualTo(actual)
        assertThat(shownDialog()).isNull()
    }

    @Test
    fun rejectedCountryChangeKeepsValueAndClosesDialog() {
        // Arrange
        val preference = withPreference(R.string.country_code_key)
        val originalCode = preference.value
        preference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ -> false }
        // Act
        val actual = selectDifferentValue(preference)
        // Assert
        assertThat(preference.value).isEqualTo(originalCode)
        assertThat(actual).isNotEqualTo(originalCode)
        assertThat(shownDialog()).isNull()
    }

    @Test
    fun selectingLanguageAppliesApplicationLocalesAndClosesDialog() {
        // Arrange
        val listView = withDialog(withPreference(R.string.language_key)).list()
        val position = (listView.adapter as CustomPreferenceAdapter).indexOfCode(GERMAN_LANGUAGE_TAG)
        // Act
        select(listView, position)
        // Assert
        assertThat(AppCompatDelegate.getApplicationLocales().toLanguageTags()).isEqualTo(GERMAN_LANGUAGE_TAG)
        assertThat(shownDialog()).isNull()
    }

    @Test
    fun searchQueryIsRestoredWhenDialogIsRecreated() {
        // Arrange
        val preference = withPreference(R.string.country_code_key)
        // Act
        val restoredDialog = withRecreatedDialog(preference)
        // Assert
        assertThat(restoredDialog.searchView().query.toString()).isEqualTo(CANADA_NAME)
        val restoredAdapter = restoredDialog.list().adapter as CustomPreferenceAdapter
        assertThat(restoredAdapter.count).isEqualTo(1)
        assertThat(restoredAdapter.getItem(0)?.code).isEqualTo(CANADA_CODE)
    }

    @Test
    fun queryTextChangeFiltersResultsAndChecksSelectedValue() {
        // Arrange
        val adapter = CustomPreferenceAdapter(mainActivity, countries)
        val listView = withListView(adapter)
        val listener = CustomPreferenceDialogFragment.Listener(adapter, listView, CANADA_CODE)
        // Act
        val actual = listener.onQueryTextChange(CANADA_NAME)
        // Assert
        assertThat(actual).isTrue()
        assertThat(adapter.count).isEqualTo(1)
        assertThat(adapter.getItem(0)?.code).isEqualTo(CANADA_CODE)
        assertThat(listView.checkedItemPosition).isEqualTo(0)
    }

    @Test
    fun queryTextChangeScrollsToSelectedValue() {
        // Arrange
        val adapter = CustomPreferenceAdapter(mainActivity, countries)
        val listView = withListView(adapter)
        val listener = CustomPreferenceDialogFragment.Listener(adapter, listView, CANADA_CODE)
        // Act
        listener.onQueryTextChange("a")
        withLayout(listView)
        // Assert
        assertThat(listView.firstVisiblePosition).isEqualTo(1)
    }

    @Test
    fun queryTextChangeScrollsToTopWhenSelectedValueIsNotListed() {
        // Arrange
        val adapter = CustomPreferenceAdapter(mainActivity, countries)
        val listView = withListView(adapter)
        val listener = CustomPreferenceDialogFragment.Listener(adapter, listView, "DE")
        listView.setSelection(1)
        // Act
        listener.onQueryTextChange("a")
        withLayout(listView)
        // Assert
        assertThat(listView.firstVisiblePosition).isEqualTo(0)
        assertThat(listView.checkedItemPosition).isEqualTo(ListView.INVALID_POSITION)
    }

    @Test
    fun queryTextSubmitDoesNotHandleSubmission() {
        // Arrange
        val adapter = CustomPreferenceAdapter(mainActivity, countries)
        val listener = CustomPreferenceDialogFragment.Listener(adapter, ListView(mainActivity), UNITED_STATES_CODE)
        // Act
        val actual = listener.onQueryTextSubmit("test")
        // Assert
        assertThat(actual).isFalse()
    }

    @Test
    fun nullQueryRestoresAllValuesAndPreviousSelection() {
        // Arrange
        val adapter = CustomPreferenceAdapter(mainActivity, countries)
        val listView = withListView(adapter)
        val listener = CustomPreferenceDialogFragment.Listener(adapter, listView, CANADA_CODE)
        // Act
        val actual = listener.onQueryTextChange(null)
        // Assert
        assertThat(actual).isTrue()
        assertThat(adapter.count).isEqualTo(countries.size)
        assertThat(listView.checkedItemPosition).isEqualTo(1)
    }

    private fun withPreference(
        @StringRes keyId: Int,
    ): CustomPreference = settingsFragment.findPreference(settingsFragment.getString(keyId))!!

    private fun withDialog(
        preference: CustomPreference,
        savedState: Fragment.SavedState? = null,
    ): CustomPreferenceDialogFragment {
        val dialog = CustomPreferenceDialogFragment.newInstance(preference.key)
        @Suppress("DEPRECATION")
        dialog.setTargetFragment(settingsFragment, 0)
        dialog.setInitialSavedState(savedState)
        dialog.show(settingsFragment.parentFragmentManager, DIALOG_FRAGMENT_TAG)
        RobolectricUtil.INSTANCE.clearLooper()
        return dialog
    }

    private fun withRecreatedDialog(preference: CustomPreference): CustomPreferenceDialogFragment {
        val dialog = withDialog(preference)
        dialog.searchView().setQuery(CANADA_NAME, false)
        val savedState = settingsFragment.parentFragmentManager.saveFragmentInstanceState(dialog)
        dialog.dismissAllowingStateLoss()
        RobolectricUtil.INSTANCE.clearLooper()
        return withDialog(preference, savedState)
    }

    private fun selectDifferentValue(preference: CustomPreference): String {
        val originalCode = preference.value
        val listView = withDialog(preference).list()
        val adapter = listView.adapter as CustomPreferenceAdapter
        val position = (0 until adapter.count).first { adapter.getItem(it)?.code != originalCode }
        select(listView, position)
        return adapter.getItem(position)!!.code
    }

    private fun select(
        listView: ListView,
        position: Int,
    ) {
        listView.performItemClick(View(mainActivity), position, listView.adapter.getItemId(position))
        RobolectricUtil.INSTANCE.clearLooper()
        settingsFragment.parentFragmentManager.executePendingTransactions()
    }

    private fun shownDialog(): Fragment? = settingsFragment.parentFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG)

    private fun CustomPreferenceDialogFragment.searchView(): SearchView =
        dialog!!.findViewById(R.id.customPreferenceSearchText)

    private fun CustomPreferenceDialogFragment.list(): ListView = dialog!!.findViewById(android.R.id.list)

    private fun withListView(adapter: CustomPreferenceAdapter): ListView {
        val listView = ListView(mainActivity)
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE
        listView.adapter = adapter
        return listView
    }

    private fun withLayout(listView: ListView) {
        listView.measure(
            View.MeasureSpec.makeMeasureSpec(LIST_WIDTH, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(LIST_HEIGHT, View.MeasureSpec.EXACTLY),
        )
        listView.layout(0, 0, LIST_WIDTH, LIST_HEIGHT)
    }
}
