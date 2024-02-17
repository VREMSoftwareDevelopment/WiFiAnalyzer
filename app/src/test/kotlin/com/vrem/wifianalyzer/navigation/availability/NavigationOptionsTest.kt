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
package com.vrem.wifianalyzer.navigation.availability

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class NavigationOptionsTest {
    @Test
    fun rating() {
        val options: List<NavigationOption> = navigationOptionRating
        assertThat(options).hasSize(4)
        assertThat(options).contains(navigationOptionWiFiSwitchOn)
        assertThat(options).contains(navigationOptionScannerSwitchOn)
        assertThat(options).contains(navigationOptionFilterOff)
        assertThat(options).contains(navigationOptionBottomNavOn)
        assertThat(options).doesNotContain(navigationOptionWiFiSwitchOff)
        assertThat(options).doesNotContain(navigationOptionScannerSwitchOff)
        assertThat(options).doesNotContain(navigationOptionFilterOn)
        assertThat(options).doesNotContain(navigationOptionBottomNavOff)
    }

    @Test
    fun other() {
        val options: List<NavigationOption> = navigationOptionOther
        assertThat(options).hasSize(4)
        assertThat(options).contains(navigationOptionWiFiSwitchOn)
        assertThat(options).contains(navigationOptionScannerSwitchOn)
        assertThat(options).contains(navigationOptionFilterOn)
        assertThat(options).contains(navigationOptionBottomNavOn)
        assertThat(options).doesNotContain(navigationOptionWiFiSwitchOff)
        assertThat(options).doesNotContain(navigationOptionScannerSwitchOff)
        assertThat(options).doesNotContain(navigationOptionFilterOff)
        assertThat(options).doesNotContain(navigationOptionBottomNavOff)
    }

    @Test
    fun off() {
        val options: List<NavigationOption> = navigationOptionOff
        assertThat(options).hasSize(4)
        assertThat(options).contains(navigationOptionWiFiSwitchOff)
        assertThat(options).contains(navigationOptionScannerSwitchOff)
        assertThat(options).contains(navigationOptionFilterOff)
        assertThat(options).contains(navigationOptionBottomNavOff)
        assertThat(options).doesNotContain(navigationOptionWiFiSwitchOn)
        assertThat(options).doesNotContain(navigationOptionScannerSwitchOn)
        assertThat(options).doesNotContain(navigationOptionFilterOn)
        assertThat(options).doesNotContain(navigationOptionBottomNavOn)
    }

    @Test
    fun accessPoints() {
        val options: List<NavigationOption> = navigationOptionAp
        assertThat(options).hasSize(4)
        assertThat(options).contains(navigationOptionWiFiSwitchOff)
        assertThat(options).contains(navigationOptionScannerSwitchOn)
        assertThat(options).contains(navigationOptionFilterOn)
        assertThat(options).contains(navigationOptionBottomNavOn)
        assertThat(options).doesNotContain(navigationOptionWiFiSwitchOn)
        assertThat(options).doesNotContain(navigationOptionScannerSwitchOff)
        assertThat(options).doesNotContain(navigationOptionFilterOff)
        assertThat(options).doesNotContain(navigationOptionBottomNavOff)
    }
}