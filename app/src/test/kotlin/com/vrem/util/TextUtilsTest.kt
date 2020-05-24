/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.text.Spanned
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class TextUtilsTest {
    @Test
    fun testTextToHtmlSmall() {
        // setup
        val color = 10
        val text = "ThisIsText"
        val expected = "<font color='$color'><small>$text</small></font>"
        // execute
        val actual: String = textToHtml(text, color, true)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testTextToHtml() {
        // setup
        val color = 10
        val text = "ThisIsText"
        val expected = "<font color='$color'><strong>$text</strong></font>"
        // execute
        val actual: String = textToHtml(text, color, false)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testJoin() {
        // setup
        val expected = "ABC JDS"
        val values: Set<String> = setOf("", " ", "ABC", " JDS ")
        // execute
        val actual: String = join(values)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testJoinWithNull() {
        // execute
        val actual: String = join(null)
        // validate
        assertTrue(actual.isEmpty())
    }

    @Test
    fun testSplit() {
        // setup
        val value = "    ABS    ADF    "
        val expected = arrayOf("ABS", "ADF")
        // execute
        val actual: Set<String> = split(value)
        // verify
        assertArrayEquals(expected, actual.toTypedArray())
    }

    @Test
    fun testSplitWithNull() {
        // execute
        val actual: Set<String> = split(null)
        // validate
        assertTrue(actual.isEmpty())
    }

    @Test
    fun testTrim() {
        // setup
        val expected = "ABS ADF"
        val value = "    ABS    ADF    "
        // execute
        val actual: String = trim(value)
        // verify
        assertEquals(expected, actual)
    }

    @Test
    fun testTrimWithNull() {
        // execute
        val actual: String = trim(null)
        // verify
        assertTrue(actual.isEmpty())
    }

    @Test
    fun testFromHtml() {
        // setup
        val expected = "ThisIsText"
        val text = "<font color='20'><small>$expected</small></font>"
        // execute
        val actual: Spanned = fromHtml(text)
        // verify
        assertEquals(expected, actual.toString())
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    fun testFromHtmlLegacy() {
        // setup
        val expected = "ThisIsText"
        val text = "<font color='20'><small>$expected</small></font>"
        // execute
        val actual: Spanned = fromHtml(text)
        // verify
        assertEquals(expected, actual.toString())
    }
}