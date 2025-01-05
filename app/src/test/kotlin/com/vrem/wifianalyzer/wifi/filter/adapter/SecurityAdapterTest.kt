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
package com.vrem.wifianalyzer.wifi.filter.adapter

import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.model.Security
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

class SecurityAdapterTest {
    private val settings: Settings = mock()
    private val fixture = SecurityAdapter(Security.entries.toSet())

    @After
    fun tearDown() {
        verifyNoMoreInteractions(settings)
    }

    @Test
    fun isActive() {
        assertThat(fixture.isActive()).isFalse()
    }

    @Test
    fun isActiveWithChanges() {
        // setup
        fixture.toggle(Security.WPA)
        // execute & validate
        assertThat(fixture.isActive()).isTrue()
    }

    @Test
    fun getValues() {
        // setup
        val expected = Security.entries
        // execute
        val actual = fixture.selections
        // validate
        assertThat(actual).containsAll(expected)
    }

    @Test
    fun getValuesDefault() {
        // setup
        val expected = Security.entries
        // execute
        val actual = fixture.defaults
        // validate
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun toggleRemoves() {
        // execute
        val actual = fixture.toggle(Security.WEP)
        // validate
        assertThat(actual).isTrue()
        assertThat(fixture.contains(Security.WEP)).isFalse()
    }

    @Test
    fun toggleAdds() {
        // setup
        fixture.toggle(Security.WPA)
        // execute
        val actual = fixture.toggle(Security.WPA)
        // validate
        assertThat(actual).isTrue()
        assertThat(fixture.contains(Security.WPA)).isTrue()
    }

    @Test
    fun removingAllWillNotRemoveLast() {
        // setup
        val values: Set<Security> = Security.entries.toSet()
        // execute
        values.forEach { fixture.toggle(it) }
        // validate
        values.forEach { fixture.contains(it) }
        assertThat(fixture.contains(values.last())).isTrue()
    }

    @Test
    fun getColorWithExisting() {
        // execute & validate
        assertThat(fixture.color(Security.WPA)).isEqualTo(R.color.selected)
    }

    @Test
    fun getColorWithNonExisting() {
        // setup
        fixture.toggle(Security.WPA)
        // execute & validate
        assertThat(fixture.color(Security.WPA)).isEqualTo(R.color.regular)
    }

    @Test
    fun save() {
        // setup
        val expected = fixture.selections
        // execute
        fixture.save(settings)
        // validate
        verify(settings).saveSecurities(expected)
    }

}