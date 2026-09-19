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
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.util.EMPTY
import com.vrem.util.supportedLanguages
import com.vrem.util.toCapitalize
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import org.xmlpull.v1.XmlPullParser

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class LanguagePreferenceTest {
    private val languageTag = "ja"
    private val mainActivity = RobolectricUtil.INSTANCE.mainActivity
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val languages = supportedLanguages()
    private val attributeSet = Robolectric.getAttributeSetFromXml(R.xml.test_attrs)
    private val settings: Settings = mock()
    private lateinit var fixture: LanguagePreference

    @Before
    fun setUp() {
        doReturn(languageTag).whenever(settings).languageTag()
        fixture = LanguagePreference(context, attributeSet, settings)
    }

    @After
    fun tearDown() {
        verify(settings).languageTag()
        verifyNoMoreInteractions(settings)
    }

    @Test
    fun entries() {
        // Act
        val actual: Array<CharSequence> = fixture.entries
        // Assert
        assertThat(actual).hasSize(languages.size + 1)
        assertThat(actual[0]).isEqualTo(context.getString(R.string.language_system_default))
        languages.forEach {
            assertThat(actual).contains(it.getDisplayName(it).toCapitalize(it))
        }
    }

    @Test
    @Config(qualifiers = "zh-rCN")
    fun entriesWithSimplifiedChinese() {
        // Act
        val actual: Array<CharSequence> = fixture.entries
        // Assert
        assertThat(actual[0]).isEqualTo("系统默认设置")
        assertThat(actual).contains("English", "Italiano", "Türkçe")
    }

    @Test
    @Config(qualifiers = "zh-rTW")
    fun entriesWithTraditionalChinese() {
        // Act
        val actual: Array<CharSequence> = fixture.entries
        // Assert
        assertThat(actual[0]).isEqualTo("系統預設")
        assertThat(actual).contains("English", "Italiano", "Türkçe")
    }

    @Test
    @Config(qualifiers = "tr")
    fun entriesWithTurkish() {
        // Act
        val actual: Array<CharSequence> = fixture.entries
        // Assert
        assertThat(actual[0]).isEqualTo("Sistem varsayılanı")
        assertThat(actual).contains("English", "Italiano", "Türkçe")
    }

    @Test
    fun entryValues() {
        // Act
        val actual: Array<CharSequence> = fixture.entryValues
        // Assert
        assertThat(actual).hasSize(languages.size + 1)
        assertThat(actual[0]).isEqualTo(String.EMPTY)
        languages.forEach {
            assertThat(actual).contains(it.toLanguageTag())
        }
    }

    @Test
    fun localeConfigListsEveryLanguage() {
        // Arrange
        val parser = mainActivity.resources.getXml(R.xml.locales_config)
        // Act
        val actual =
            generateSequence { parser.next() }
                .takeWhile { it != XmlPullParser.END_DOCUMENT }
                .filter { it == XmlPullParser.START_TAG && parser.name == "locale" }
                .map { parser.getAttributeValue("http://schemas.android.com/apk/res/android", "name") }
                .toList()
        // Assert
        assertThat(actual).containsExactlyInAnyOrderElementsOf(languages.map { it.toLanguageTag() })
    }

    @Test
    fun valueIsLanguageTagFromSettings() {
        // Arrange
        val preferenceScreen = PreferenceManager(mainActivity).createPreferenceScreen(mainActivity)
        // Act
        preferenceScreen.addPreference(fixture)
        // Assert
        assertThat(fixture.value).isEqualTo(languageTag)
        assertThat(fixture.isPersistent).isFalse
    }

    @Test
    fun changeSavesLanguageTag() {
        // Act
        val actual = fixture.callChangeListener(languageTag)
        // Assert
        assertThat(actual).isTrue
        verify(settings).saveLanguageTag(languageTag)
    }
}
