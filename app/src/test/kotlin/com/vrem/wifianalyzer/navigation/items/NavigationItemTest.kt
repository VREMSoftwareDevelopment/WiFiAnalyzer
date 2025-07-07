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
package com.vrem.wifianalyzer.navigation.items

import android.view.View
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.navigation.NavigationMenu
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class NavigationItemTest {
    private val fixture: NavigationItem = object : NavigationItem {
        override fun activate(mainActivity: MainActivity, navigationMenu: NavigationMenu) = Unit
    }

    @Test
    fun registered() {
        assertThat(fixture.registered).isFalse
    }

    @Test
    fun visibility() {
        assertThat(fixture.visibility).isEqualTo(View.GONE)
    }
}