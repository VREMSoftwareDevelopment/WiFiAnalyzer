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
package com.vrem.util

import org.junit.Assert.assertEquals
import org.junit.Test

class StringUtilsTest {

    @Test
    fun testSpecialTrim() {
        // setup
        val expected = "ABS ADF"
        val value = "    ABS    ADF    "
        // execute
        val actual: String = value.specialTrim()
        // verify
        assertEquals(expected, actual)
    }

    @Test
    fun testToHtmlSmall() {
        // setup
        val color = 10
        val text = "ThisIsText"
        val expected = "<font color='$color'><small>$text</small></font>"
        // execute
        val actual: String = text.toHtml(color, true)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testToHtml() {
        // setup
        val color = 10
        val text = "ThisIsText"
        val expected = "<font color='$color'><strong>$text</strong></font>"
        // execute
        val actual: String = text.toHtml(color, false)
        // validate
        assertEquals(expected, actual)
    }

}