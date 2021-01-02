/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import org.junit.After
import org.junit.Assert.*
import org.junit.Test

class WiFiBandAdapterTest {
    private val settings: Settings = mock()
    private val fixture = WiFiBandAdapter(WiFiBand.values().toSet())

    @After
    fun tearDown() {
        verifyNoMoreInteractions(settings)
    }

    @Test
    fun testIsActive() {
        assertFalse(fixture.isActive())
    }

    @Test
    fun testIsActiveWithChanges() {
        // setup
        fixture.toggle(WiFiBand.GHZ2)
        // execute & validate
        assertTrue(fixture.isActive())
    }

    @Test
    fun testGetValues() {
        // setup
        val expected = WiFiBand.values()
        // execute
        val actual = fixture.selections
        // validate
        assertTrue(actual.containsAll(expected.toList()))
    }

    @Test
    fun testGetValuesDefault() {
        // setup
        val expected = WiFiBand.values()
        // execute
        val actual = fixture.defaults
        // validate
        assertArrayEquals(expected, actual)
    }

    @Test
    fun testToggleRemoves() {
        // execute
        val actual = fixture.toggle(WiFiBand.GHZ2)
        // validate
        assertTrue(actual)
        assertFalse(fixture.contains(WiFiBand.GHZ2))
    }

    @Test
    fun testToggleAdds() {
        // setup
        fixture.toggle(WiFiBand.GHZ5)
        // execute
        val actual = fixture.toggle(WiFiBand.GHZ5)
        // validate
        assertTrue(actual)
        assertTrue(fixture.contains(WiFiBand.GHZ5))
    }

    @Test
    fun testRemovingAllWillNotRemoveLast() {
        // setup
        val values: Set<WiFiBand> = WiFiBand.values().toSet()
        // execute
        values.forEach { fixture.toggle(it) }
        // validate
        values.toList().subList(0, values.size - 1).forEach { assertFalse(fixture.contains(it)) }
        assertTrue(fixture.contains(values.last()))
    }

    @Test
    fun testGetColorWithExisting() {
        // execute & validate
        assertEquals(R.color.selected, fixture.color(WiFiBand.GHZ2))
    }

    @Test
    fun testGetColorWithNonExisting() {
        // setup
        fixture.toggle(WiFiBand.GHZ2)
        // execute & validate
        assertEquals(R.color.regular, fixture.color(WiFiBand.GHZ2))
    }

    @Test
    fun testSave() {
        // setup
        val expected = fixture.selections
        // execute
        fixture.save(settings)
        // execute
        verify(settings).saveWiFiBands(expected)
    }
}
