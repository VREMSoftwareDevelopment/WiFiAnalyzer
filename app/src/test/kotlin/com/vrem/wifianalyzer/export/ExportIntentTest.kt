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
package com.vrem.wifianalyzer.export

import android.content.Intent
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.*

class ExportIntentTest {
    private val intentSend: Intent = mock()
    private val intentChooser: Intent = mock()

    private val fixture = spy(ExportIntent())

    @After
    fun tearDown() {
        verifyNoMoreInteractions(intentSend)
        verifyNoMoreInteractions(intentChooser)
    }

    @Test
    fun intent() {
        // setup
        val title = "title"
        val data = "data"
        doReturn(intentSend).whenever(fixture).intentSend()
        doReturn(intentChooser).whenever(fixture).intentChooser(intentSend, title)
        // execute
        val actual = fixture.intent(title, data)
        // validate
        assertThat(actual).isEqualTo(intentChooser)

        verify(intentSend).flags = Intent.FLAG_ACTIVITY_NEW_TASK
        verify(intentSend).type = "text/plain"
        verify(intentSend).putExtra(Intent.EXTRA_TITLE, title)
        verify(intentSend).putExtra(Intent.EXTRA_SUBJECT, title)
        verify(intentSend).putExtra(Intent.EXTRA_TEXT, data)

        verify(fixture).intentSend()
        verify(fixture).intentChooser(intentSend, title)
    }

}