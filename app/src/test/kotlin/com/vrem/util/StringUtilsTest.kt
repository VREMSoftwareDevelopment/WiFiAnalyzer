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

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.Locale

class StringUtilsTest {
    @Test
    fun specialTrim() {
        // Arrange
        val expected = "ABS ADF"
        val value = "    ABS    ADF    "
        // Act
        val actual = value.specialTrim()
        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun toCapitalize() {
        // Arrange
        val expected = "Value"
        val value = "value"
        // Act
        val actual = value.toCapitalize(Locale.US)
        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun nullToEmptyWhenNull() {
        // Arrange
        val expected = String.EMPTY
        val value = null
        // Act
        val actual = String.nullToEmpty(value)
        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun nullToEmpty() {
        // Arrange
        val expected = "value"
        val value = "value"
        // Act
        val actual = String.nullToEmpty(value)
        // Assert
        assertThat(actual).isEqualTo(expected)
    }
}
