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
package com.vrem.wifianalyzer.wifi.filter.adapter

import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

class WiFiBandAdapterTest {
    private val settings: Settings = mock()
    private val fixture = WiFiBandAdapter(WiFiBand.entries.toSet())

    @After
    fun tearDown() {
        verifyNoMoreInteractions(settings)
    }

    @Test
    fun isActive() {
        assertThat(fixture.isActive()).isFalse
    }

    @Test
    fun isActiveWithChanges() {
        // Arrange
        fixture.toggle(WiFiBand.GHZ2)
        // Act & Assert
        assertThat(fixture.isActive()).isTrue
    }

    @Test
    fun getValues() {
        // Arrange
        val expected = WiFiBand.entries
        // Act
        val actual = fixture.selections
        // Assert
        assertThat(actual).containsAll(expected)
    }

    @Test
    fun getValuesDefault() {
        // Arrange
        val expected = WiFiBand.entries
        // Act
        val actual = fixture.defaults
        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun toggleRemoves() {
        // Act
        val actual = fixture.toggle(WiFiBand.GHZ2)
        // Assert
        assertThat(actual).isTrue
        assertThat(fixture.contains(WiFiBand.GHZ2)).isFalse
    }

    @Test
    fun toggleAdds() {
        // Arrange
        fixture.toggle(WiFiBand.GHZ5)
        // Act
        val actual = fixture.toggle(WiFiBand.GHZ5)
        // Assert
        assertThat(actual).isTrue
        assertThat(fixture.contains(WiFiBand.GHZ5)).isTrue
    }

    @Test
    fun removingAllWillNotRemoveLast() {
        // Arrange
        val values = WiFiBand.entries.toSet()
        // Act
        values.forEach { fixture.toggle(it) }
        // Assert
        values.toList().subList(0, values.size - 1).forEach { assertThat(fixture.contains(it)).isFalse }
        assertThat(fixture.contains(values.last())).isTrue
    }

    @Test
    fun getColorWithExisting() {
        // Act & Assert
        assertThat(fixture.color(WiFiBand.GHZ2)).isEqualTo(R.color.selected)
    }

    @Test
    fun getColorWithNonExisting() {
        // Arrange
        fixture.toggle(WiFiBand.GHZ2)
        // Act & Assert
        assertThat(fixture.color(WiFiBand.GHZ2)).isEqualTo(R.color.regular)
    }

    @Test
    fun save() {
        // Arrange
        val expected = fixture.selections
        // Act
        fixture.save(settings)
        // Act
        verify(settings).saveWiFiBands(expected)
    }
}
