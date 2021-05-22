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
package com.vrem.wifianalyzer.settings

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.util.supportedLanguages
import com.vrem.util.toCapitalize
import com.vrem.util.toLanguageTag
import com.vrem.wifianalyzer.RobolectricUtil
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class LanguagePreferenceTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val languages = supportedLanguages()
    private val fixture = LanguagePreference(mainActivity, Robolectric.buildAttributeSet().build())

    @Test
    fun testEntries() {
        // execute
        val actual: Array<CharSequence> = fixture.entries
        // validate
        Assert.assertEquals(languages.size, actual.size)
        languages.forEach {
            val displayName: String = it.getDisplayName(it).toCapitalize(Locale.getDefault())
            Assert.assertTrue(displayName, actual.contains(displayName))
        }
    }

    @Test
    fun testEntryValues() {
        // execute
        val actual: Array<CharSequence> = fixture.entryValues
        // validate
        Assert.assertEquals(languages.size, actual.size)
        languages.forEach {
            val languageTag: String = toLanguageTag(it)
            Assert.assertTrue(languageTag, actual.contains(languageTag))
        }
    }
}