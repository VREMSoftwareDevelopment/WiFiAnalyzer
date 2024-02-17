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

package com.vrem.wifianalyzer.settings

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class DataTest {

    @Test
    fun compareToWhenAreSame() {
        // setup
        val data1 = Data("name", "code")
        val data2 = Data("name", "code")
        // execute
        val actual = data1.compareTo(data2)
        // validate
        assertThat(actual).isEqualTo(0)
    }

    @Test
    fun compareToWhenCodesAreDifferent() {
        // setup
        val data1 = Data("name", "code1")
        val data2 = Data("name", "code2")
        // execute
        val actual = data1.compareTo(data2)
        // validate
        assertThat(actual).isEqualTo(-1)
    }

    @Test
    fun compareToWhenNamesAreDifferent() {
        // setup
        val data1 = Data("name1", "code")
        val data2 = Data("name2", "code")
        // execute
        val actual = data1.compareTo(data2)
        // validate
        assertThat(actual).isEqualTo(-1)
    }

}