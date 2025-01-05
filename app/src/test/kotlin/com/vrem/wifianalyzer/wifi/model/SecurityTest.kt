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
package com.vrem.wifianalyzer.wifi.model

import com.vrem.wifianalyzer.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SecurityTest {
    @Test
    fun size() {
        assertThat(Security.entries).hasSize(6)
    }

    @Test
    fun imageResource() {
        assertThat(Security.NONE.imageResource).isEqualTo(R.drawable.ic_lock_open)
        assertThat(Security.WPS.imageResource).isEqualTo(R.drawable.ic_lock_outline)
        assertThat(Security.WEP.imageResource).isEqualTo(R.drawable.ic_lock_outline)
        assertThat(Security.WPA.imageResource).isEqualTo(R.drawable.ic_lock)
        assertThat(Security.WPA2.imageResource).isEqualTo(R.drawable.ic_lock)
        assertThat(Security.WPA3.imageResource).isEqualTo(R.drawable.ic_lock)
    }

    @Test
    fun order() {
        // setup
        val expected = listOf(Security.NONE, Security.WPS, Security.WEP, Security.WPA, Security.WPA2, Security.WPA3)
        // execute
        val actual = Security.entries
        // validate
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun extras() {
        assertThat(Security.NONE.extras).isEmpty()
        assertThat(Security.WPS.extras).isEmpty()
        assertThat(Security.WEP.extras).isEmpty()
        assertThat(Security.WPA.extras).isEmpty()
        assertThat(Security.WPA2.extras).isEmpty()
        assertThat(Security.WPA3.extras).isEqualTo(listOf("SAE", "EAP_SUITE_B_192", "OWE"))
    }
}