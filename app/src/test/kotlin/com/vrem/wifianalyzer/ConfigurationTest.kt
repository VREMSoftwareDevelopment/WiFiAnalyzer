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

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ConfigurationTest {
    private val fixture = Configuration(true)

    @Test
    fun sizeAvailable() {
        // execute & validate
        assertThat(fixture.sizeAvailable).isTrue()
    }

    @Test
    fun sizeIsNotAvailable() {
        // execute
        fixture.size = SIZE_MIN
        // validate
        assertThat(fixture.sizeAvailable).isFalse()
    }

    @Test
    fun largeScreen() {
        // execute & validate
        assertThat(fixture.largeScreen).isTrue()
    }

}
