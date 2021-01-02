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
package com.vrem.wifianalyzer

import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.settings.ThemeStyle
import com.vrem.wifianalyzer.wifi.accesspoint.ConnectionViewType
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class MainReloadTest {
    private val settings = MainContextHelper.INSTANCE.settings
    private lateinit var fixture: MainReload

    @Before
    fun setUp() {
        whenever(settings.themeStyle()).thenReturn(ThemeStyle.DARK)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        whenever(settings.languageLocale()).thenReturn(Locale.UK)
        fixture = MainReload(settings)
    }

    @After
    fun tearDown() {
        verify(settings, atLeastOnce()).themeStyle()
        verify(settings, atLeastOnce()).connectionViewType()
        verify(settings, atLeastOnce()).languageLocale()
        verifyNoMoreInteractions(settings)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testShouldNotReloadWithNoThemeChanges() {
        // execute
        val actual = fixture.shouldReload(settings)
        // validate
        assertFalse(actual)
        assertEquals(ThemeStyle.DARK, fixture.themeStyle)
    }

    @Test
    fun testShouldReloadWithThemeChange() {
        // setup
        val expected = ThemeStyle.LIGHT
        whenever(settings.themeStyle()).thenReturn(expected)
        // execute
        val actual = fixture.shouldReload(settings)
        // validate
        assertTrue(actual)
        assertEquals(expected, fixture.themeStyle)
    }

    @Test
    fun testShouldNotReloadWithNoConnectionViewTypeChanges() {
        // execute
        val actual = fixture.shouldReload(settings)
        // validate
        assertFalse(actual)
        assertEquals(ConnectionViewType.COMPLETE, fixture.connectionViewType)
    }

    @Test
    fun testShouldReloadWithConnectionViewTypeChange() {
        // setup
        val expected = ConnectionViewType.COMPACT
        whenever(settings.connectionViewType()).thenReturn(expected)
        // execute
        val actual = fixture.shouldReload(settings)
        // validate
        assertTrue(actual)
        assertEquals(expected, fixture.connectionViewType)
    }

    @Test
    fun testShouldNotReloadWithNoLanguageLocaleChanges() {
        // execute
        val actual = fixture.shouldReload(settings)
        // validate
        assertFalse(actual)
        assertEquals(Locale.UK, fixture.languageLocale)
    }

    @Test
    fun testShouldReloadWithLanguageLocaleChange() {
        // setup
        val expected = Locale.US
        whenever(settings.languageLocale()).thenReturn(expected)
        // execute
        val actual = fixture.shouldReload(settings)
        // validate
        assertTrue(actual)
        assertEquals(expected, fixture.languageLocale)
    }

}