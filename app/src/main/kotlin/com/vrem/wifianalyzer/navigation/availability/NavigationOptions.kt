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
package com.vrem.wifianalyzer.navigation.availability

val navigationOptionFilterOff: NavigationOption = FilterOff()
val navigationOptionFilterOn: NavigationOption = FilterOn()
val navigationOptionScannerSwitchOff: NavigationOption = ScannerSwitchOff()
val navigationOptionScannerSwitchOn: NavigationOption = ScannerSwitchOn()
val navigationOptionWifiSwitchOff: NavigationOption = WiFiSwitchOff()
val navigationOptionWifiSwitchOn: NavigationOption = WiFiSwitchOn()
val navigationOptionBottomNavOff: NavigationOption = BottomNavOff()
val navigationOptionBottomNavOn: NavigationOption = BottomNavOn()
val navigationOptionAp = listOf(navigationOptionWifiSwitchOff, navigationOptionScannerSwitchOn, navigationOptionFilterOn, navigationOptionBottomNavOn)
val navigationOptionOff = listOf(navigationOptionWifiSwitchOff, navigationOptionScannerSwitchOff, navigationOptionFilterOff, navigationOptionBottomNavOff)
val navigationOptionOther = listOf(navigationOptionWifiSwitchOn, navigationOptionScannerSwitchOn, navigationOptionFilterOn, navigationOptionBottomNavOn)
val navigationOptionRating = listOf(navigationOptionWifiSwitchOn, navigationOptionScannerSwitchOn, navigationOptionFilterOff, navigationOptionBottomNavOn)
