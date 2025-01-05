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
package com.vrem.wifianalyzer

import com.vrem.wifianalyzer.settings.ThemeStyle
import com.vrem.wifianalyzer.wifi.accesspoint.ConnectionViewType
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.util.Locale

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
    fun shouldNotReloadWithNoThemeChanges() {
        // execute
        val actual = fixture.shouldReload(settings)
        // validate
        assertThat(actual).isFalse()
        assertThat(fixture.themeStyle).isEqualTo(ThemeStyle.DARK)
    }

    @Test
    fun shouldReloadWithThemeChange() {
        // setup
        val expected = ThemeStyle.LIGHT
        whenever(settings.themeStyle()).thenReturn(expected)
        // execute
        val actual = fixture.shouldReload(settings)
        // validate
        assertThat(actual).isTrue()
        assertThat(fixture.themeStyle).isEqualTo(expected)
    }

    @Test
    fun shouldNotReloadWithNoConnectionViewTypeChanges() {
        // execute
        val actual = fixture.shouldReload(settings)
        // validate
        assertThat(actual).isFalse()
        assertThat(fixture.connectionViewType).isEqualTo(ConnectionViewType.COMPLETE)
    }

    @Test
    fun shouldReloadWithConnectionViewTypeChange() {
        // setup
        val expected = ConnectionViewType.COMPACT
        whenever(settings.connectionViewType()).thenReturn(expected)
        // execute
        val actual = fixture.shouldReload(settings)
        // validate
        assertThat(actual).isTrue()
        assertThat(fixture.connectionViewType).isEqualTo(expected)
    }

    @Test
    fun shouldNotReloadWithNoLanguageLocaleChanges() {
        // execute
        val actual = fixture.shouldReload(settings)
        // validate
        assertThat(actual).isFalse()
        assertThat(fixture.languageLocale).isEqualTo(Locale.UK)
    }

    @Test
    fun shouldReloadWithLanguageLocaleChange() {
        // setup
        val expected = Locale.US
        whenever(settings.languageLocale()).thenReturn(expected)
        // execute
        val actual = fixture.shouldReload(settings)
        // validate
        assertThat(actual).isTrue()
        assertThat(fixture.languageLocale).isEqualTo(expected)
    }

}