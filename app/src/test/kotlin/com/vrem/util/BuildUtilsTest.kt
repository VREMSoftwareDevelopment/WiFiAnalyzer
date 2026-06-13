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
package com.vrem.util

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
class BuildUtilsTest {
    @Test
    @Config(sdk = [Build.VERSION_CODES.BAKLAVA])
    fun minVersionTrueOnCurrentSdk() {
        assertThat(buildMinVersionT()).isTrue()
        assertThat(buildMinVersionR()).isTrue()
        assertThat(buildMinVersionQ()).isTrue()
        assertThat(buildMinVersionP()).isTrue()
        assertThat(buildVersionP()).isFalse()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.TIRAMISU])
    fun minVersionTOnTiramisu() {
        assertThat(buildMinVersionT()).isTrue()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.S])
    fun minVersionTFalseBelowTiramisu() {
        assertThat(buildMinVersionT()).isFalse()
        assertThat(buildMinVersionR()).isTrue()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.R])
    fun minVersionROnR() {
        assertThat(buildMinVersionR()).isTrue()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.Q])
    fun minVersionRFalseBelowR() {
        assertThat(buildMinVersionR()).isFalse()
        assertThat(buildMinVersionQ()).isTrue()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.P])
    fun minVersionQFalseBelowQ() {
        assertThat(buildMinVersionQ()).isFalse()
        assertThat(buildMinVersionP()).isTrue()
        assertThat(buildVersionP()).isTrue()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.O])
    fun versionPFalseBelowP() {
        assertThat(buildMinVersionP()).isFalse()
        assertThat(buildVersionP()).isFalse()
    }
}
