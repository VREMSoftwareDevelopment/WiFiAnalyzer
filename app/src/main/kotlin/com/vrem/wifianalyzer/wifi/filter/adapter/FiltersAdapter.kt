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
package com.vrem.wifianalyzer.wifi.filter.adapter

import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.MainContext.INSTANCE
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.settings.Settings
import java.io.Serializable

@OpenClass
class FiltersAdapter(private val settings: Settings) {
    private lateinit var ssidAdapter: SSIDAdapter
    private lateinit var wiFiBandAdapter: WiFiBandAdapter
    private lateinit var strengthAdapter: StrengthAdapter
    private lateinit var securityAdapter: SecurityAdapter

    fun reload() {
        ssidAdapter = SSIDAdapter(settings.findSSIDs())
        wiFiBandAdapter = WiFiBandAdapter(settings.findWiFiBands())
        strengthAdapter = StrengthAdapter(settings.findStrengths())
        securityAdapter = SecurityAdapter(settings.findSecurities())
    }

    fun reset(): Unit =
            filterAdapters(isAccessPoints()).forEach {
                it.reset()
                it.save(settings)
            }

    fun save(): Unit = filterAdapters(isAccessPoints()).forEach { it.save(settings) }

    fun ssidAdapter(): SSIDAdapter = ssidAdapter

    fun wiFiBandAdapter(): WiFiBandAdapter = wiFiBandAdapter

    fun strengthAdapter(): StrengthAdapter = strengthAdapter

    fun securityAdapter(): SecurityAdapter = securityAdapter

    internal fun isActive(): Boolean = filterAdapters(isAccessPoints()).any { it.isActive() }

    internal fun filterAdapters(accessPoints: Boolean): List<BasicFilterAdapter<out Serializable>> =
            if (accessPoints)
                listOf(ssidAdapter, strengthAdapter, securityAdapter, wiFiBandAdapter)
            else
                listOf(ssidAdapter, strengthAdapter, securityAdapter)

    private fun isAccessPoints(): Boolean =
            NavigationMenu.ACCESS_POINTS == INSTANCE.mainActivity.currentNavigationMenu()

    init {
        ssidAdapter = SSIDAdapter(settings.findSSIDs())
        wiFiBandAdapter = WiFiBandAdapter(settings.findWiFiBands())
        strengthAdapter = StrengthAdapter(settings.findStrengths())
        securityAdapter = SecurityAdapter(settings.findSecurities())
    }
}