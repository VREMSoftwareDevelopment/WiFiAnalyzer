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
package com.vrem.wifianalyzer.wifi.filter.adapter

import com.vrem.wifianalyzer.settings.Settings
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

class SSIDAdapterTest {
    private val ssidValues: Set<String> = setOf("value1", "value2", "value3")
    private val settings: Settings = mock()
    private val fixture = SSIDAdapter(ssidValues)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(settings)
    }

    @Test
    fun getValues() {
        // setup
        // execute
        val actual = fixture.selections
        // validate
        assertTrue(actual.containsAll(ssidValues))
    }

    @Test
    fun isActive() {
        assertTrue(fixture.isActive())
    }

    @Test
    fun isNotActiveWithEmptyValue() {
        // execute
        fixture.selections = setOf()
        // validate
        assertFalse(fixture.isActive())
        assertTrue(fixture.selections.isEmpty())
    }

    @Test
    fun isNotActiveWithReset() {
        // execute
        fixture.reset()
        // validate
        assertFalse(fixture.isActive())
        assertTrue(fixture.selections.isEmpty())
    }

    @Test
    fun save() {
        // execute
        fixture.save(settings)
        // execute
        verify(settings).saveSSIDs(ssidValues)
    }

    @Test
    fun setValues() {
        // setup
        val expected: Set<String> = setOf("ABC", "EDF", "123")
        val values: Set<String> = setOf("", "ABC", "", "EDF", "  ", "123", "")
        // execute
        fixture.selections = values
        // execute
        assertEquals(expected, fixture.selections)
    }

}