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

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config
import java.util.Locale

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class AppLocalesTest {
    private val context: Context = mock()
    private val localeManager: LocaleManager = mock()
    private val mainActivity = RobolectricUtil.INSTANCE.mainActivity
    private val fixture = AppLocales(context)

    @After
    fun tearDown() {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
        verifyNoMoreInteractions(context)
        verifyNoMoreInteractions(localeManager)
    }

    @Test
    fun systemLocale() {
        // Arrange
        doReturn(localeManager).whenever(context).getSystemService(Context.LOCALE_SERVICE)
        doReturn(LocaleList.forLanguageTags("fr-CA,en-US")).whenever(localeManager).systemLocales
        // Act
        val actual = fixture.systemLocale()
        // Assert
        assertThat(actual).isEqualTo(Locale.forLanguageTag("fr-CA"))
        verify(context).getSystemService(Context.LOCALE_SERVICE)
        verify(localeManager).systemLocales
    }

    @Test
    fun systemLocaleWithoutSystemLocales() {
        // Act
        val actual = fixture.systemLocale()
        // Assert
        assertThat(actual).isNull()
        verify(context).getSystemService(Context.LOCALE_SERVICE)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.S])
    fun applicationLocale() {
        // Arrange
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("fr"))
        // Act
        val actual = fixture.applicationLocale()
        // Assert
        assertThat(actual).isEqualTo(Locale.FRENCH)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.S])
    fun applicationLocaleWithSystemDefault() {
        // Act
        val actual = fixture.applicationLocale()
        // Assert
        assertThat(actual).isNull()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.S])
    fun save() {
        // Act
        fixture.save("de")
        // Assert
        assertThat(AppCompatDelegate.getApplicationLocales().toLanguageTags()).isEqualTo("de")
    }

    @Test
    fun saveWithLocaleManager() {
        // Act
        fixture.save("de")
        // Assert
        assertThat(AppCompatDelegate.getApplicationLocales().toLanguageTags()).isEqualTo("de")
    }
}
