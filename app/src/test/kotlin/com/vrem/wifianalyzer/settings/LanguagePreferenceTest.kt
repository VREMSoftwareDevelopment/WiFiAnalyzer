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
package com.vrem.wifianalyzer.settings

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.util.supportedLanguages
import com.vrem.util.toCapitalize
import com.vrem.util.toLanguageTag
import com.vrem.wifianalyzer.RobolectricUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class LanguagePreferenceTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val languages = supportedLanguages()
    private val fixture = LanguagePreference(mainActivity, Robolectric.buildAttributeSet().build())

    @Test
    fun entries() {
        // execute
        val actual: Array<CharSequence> = fixture.entries
        // validate
        assertThat(actual).hasSize(languages.size)
        languages.forEach {
            assertThat(actual).contains(it.getDisplayName(it).toCapitalize(Locale.getDefault()))
        }
    }

    @Test
    fun entryValues() {
        // execute
        val actual: Array<CharSequence> = fixture.entryValues
        // validate
        assertThat(actual).hasSize(languages.size)
        languages.forEach {
            assertThat(actual).contains(toLanguageTag(it))
        }
    }
}