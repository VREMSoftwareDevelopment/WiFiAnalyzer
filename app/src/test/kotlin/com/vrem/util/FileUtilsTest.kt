/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.io.ByteArrayInputStream
import java.io.InputStream

class FileUtilsTest {

    private val resources: Resources = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(resources)
    }

    @Test
    fun readFile() {
        // setup
        val id = 11
        val expected = "Line-1\nLine-2\n"
        val inputStream: InputStream = ByteArrayInputStream(expected.toByteArray())
        whenever(resources.openRawResource(id)).thenReturn(inputStream)
        // execute
        val actual: String = readFile(resources, id)
        // validate
        assertThat(actual).isEqualTo(expected)
        verify(resources).openRawResource(id)
    }

    @Test
    fun readFileHandleException() {
        // setup
        val id = 11
        whenever(resources.openRawResource(id)).thenThrow(NotFoundException::class.java)
        // execute
        val actual: String = readFile(resources, id)
        // validate
        assertThat(actual).isEmpty()
        verify(resources).openRawResource(id)
    }
}