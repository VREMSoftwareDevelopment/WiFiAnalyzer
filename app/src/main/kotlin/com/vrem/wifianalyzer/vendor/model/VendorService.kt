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
package com.vrem.wifianalyzer.vendor.model

import android.content.res.Resources
import com.vrem.annotation.OpenClass
import com.vrem.util.EMPTY
import com.vrem.util.readFile
import com.vrem.wifianalyzer.R
import java.util.Locale
import java.util.TreeMap

@OpenClass
class VendorService(private val resources: Resources) {
    private val vendorData: VendorData by lazy { load(resources) }

    fun findVendorName(address: String = String.EMPTY): String =
        vendorData.macs[address.clean()].orEmpty()

    fun findMacAddresses(vendorName: String = String.EMPTY): List<String> =
        vendorData.vendors[vendorName.uppercase(Locale.getDefault())].orEmpty()

    fun findVendors(vendorName: String = String.EMPTY): List<String> {
        val name = vendorName.uppercase(Locale.getDefault())
        return vendorData.vendors.filterKeys { filter(it, name) }.keys.toList()
    }

    internal fun findMacs(): List<String> = vendorData.macs.keys.toList()

    private fun filter(source: String, filter: String): Boolean =
        source.contains(filter) || macContains(source, filter)

    private fun macContains(source: String, filter: String): Boolean =
        findMacAddresses(source).any { it.contains(filter) }

    private fun load(resources: Resources): VendorData {
        val macs: MutableMap<String, String> = TreeMap()
        val vendors: MutableMap<String, List<String>> = TreeMap()
        readFile(resources, R.raw.data)
            .split("\n")
            .map { it.split("|").toTypedArray() }
            .filter { it.size == 2 }
            .forEach {
                val name = it[0]
                val results: List<String> = it[1].chunked(MAX_SIZE)
                results.forEach { key -> macs[key] = name }
                vendors[name] = results.map { value -> value.toMacAddress() }
            }
        return VendorData(vendors, macs)
    }

    private class VendorData(val vendors: Map<String, List<String>>, val macs: Map<String, String>)

}