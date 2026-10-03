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
import android.os.Build
import android.widget.CheckedTextView
import android.widget.ListView
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

private const val UNITED_STATES_CODE = "US"
private const val UNITED_STATES_NAME = "United States"
private const val CANADA_CODE = "CA"
private const val CANADA_NAME = "Canada"
private const val GERMANY_CODE = "DE"
private const val GERMANY_NAME = "Germany"
private const val IVORY_COAST_CODE = "CI"
private const val IVORY_COAST_NAME = "Côte d'Ivoire"

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class CustomPreferenceAdapterTest {
    private val mainActivity: Context = RobolectricUtil.INSTANCE.mainActivity
    private val countries =
        listOf(
            Data(UNITED_STATES_CODE, UNITED_STATES_NAME),
            Data(CANADA_CODE, CANADA_NAME),
            Data(GERMANY_CODE, GERMANY_NAME),
        )
    private val fixture = CustomPreferenceAdapter(mainActivity, countries)

    @Test
    fun adapterStartsWithAllValues() {
        // Act
        val actual = (0 until fixture.count).mapNotNull(fixture::getItem)
        // Assert
        assertThat(actual).containsExactlyElementsOf(countries)
    }

    @Test
    fun getViewDisplaysCountryName() {
        // Arrange
        val listView = ListView(mainActivity)
        // Act
        val actual = fixture.getView(0, null, listView) as TextView
        // Assert
        assertThat(actual.text.toString()).isEqualTo(UNITED_STATES_NAME)
    }

    @Test
    fun getViewDrawsChoiceIndicatorAtStart() {
        // Arrange
        val listView = ListView(mainActivity)
        // Act
        val actual = fixture.getView(0, null, listView) as CheckedTextView
        // Assert
        assertThat(actual.compoundDrawablesRelative[0]).isNotNull()
        assertThat(actual.checkMarkDrawable).isNull()
    }

    @Test
    fun updateWithEmptyQueryShowsAllValues() {
        // Act
        fixture.update("")
        val actual = fixture.count
        // Assert
        assertThat(actual).isEqualTo(countries.size)
    }

    @Test
    fun updateWithNameQueryFiltersValues() {
        // Act
        fixture.update(CANADA_NAME)
        val actual = fixture.getItem(0)
        // Assert
        assertThat(fixture.count).isEqualTo(1)
        assertThat(actual?.code).isEqualTo(CANADA_CODE)
    }

    @Test
    fun updateWithCodeQueryFiltersValues() {
        // Act
        fixture.update(GERMANY_CODE)
        val actual = fixture.getItem(0)
        // Assert
        assertThat(fixture.count).isEqualTo(1)
        assertThat(actual?.code).isEqualTo(GERMANY_CODE)
    }

    @Test
    fun updateMatchesCountryNameIgnoringCase() {
        // Act
        fixture.update(GERMANY_NAME.lowercase())
        val actual = fixture.getItem(0)?.name
        // Assert
        assertThat(fixture.count).isEqualTo(1)
        assertThat(actual).isEqualTo(GERMANY_NAME)
    }

    @Test
    fun updateMatchesCountryNameIgnoringDiacritics() {
        // Arrange
        val fixture = CustomPreferenceAdapter(mainActivity, listOf(Data(IVORY_COAST_CODE, IVORY_COAST_NAME)))
        // Act
        fixture.update("cote")
        val actual = fixture.getItem(0)?.code
        // Assert
        assertThat(fixture.count).isEqualTo(1)
        assertThat(actual).isEqualTo(IVORY_COAST_CODE)
    }

    @Test
    fun updateMatchesQueryWithDiacriticsAgainstPlainName() {
        // Arrange
        val fixture = CustomPreferenceAdapter(mainActivity, listOf(Data(IVORY_COAST_CODE, "Cote d'Ivoire")))
        // Act
        fixture.update("Côte")
        val actual = fixture.getItem(0)?.code
        // Assert
        assertThat(fixture.count).isEqualTo(1)
        assertThat(actual).isEqualTo(IVORY_COAST_CODE)
    }

    @Test
    fun updateTrimsWhitespaceAroundQueryBeforeFiltering() {
        // Act
        fixture.update("  $CANADA_NAME  ")
        val actual = fixture.getItem(0)?.code
        // Assert
        assertThat(fixture.count).isEqualTo(1)
        assertThat(actual).isEqualTo(CANADA_CODE)
    }

    @Test
    fun updateWithNonMatchingQueryReturnsNoValues() {
        // Act
        fixture.update("NonExistingCountry")
        val actual = fixture.count
        // Assert
        assertThat(actual).isZero()
    }

    @Test
    fun indexOfCodeReturnsPositionMatchingSelectedCode() {
        // Act
        val actual = fixture.indexOfCode(CANADA_CODE)
        // Assert
        assertThat(actual).isEqualTo(1)
    }

    @Test
    fun indexOfCodeReturnsInvalidPositionWhenCodeIsNotFound() {
        // Act
        val actual = fixture.indexOfCode("UNKNOWN")
        // Assert
        assertThat(actual).isEqualTo(-1)
    }
}
