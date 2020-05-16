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
package com.vrem.wifianalyzer.vendor

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.databinding.VendorDetailsBinding
import com.vrem.wifianalyzer.vendor.model.VendorService

@OpenClass
internal class VendorAdapter(context: Context, private val vendorService: VendorService) : ArrayAdapter<String>(context, R.layout.vendor_details, vendorService.findVendors()) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val binding: Binding = view?.let { Binding(it) } ?: Binding(create(parent))
        val vendorName: String = getItem(position).orEmpty()
        binding.vendorName.text = vendorName
        binding.vendorMacs.text = vendorService.findMacAddresses(vendorName).joinToString(separator = ", ")
        return binding.root
    }

    fun update(filter: String) {
        clear()
        addAll(vendorService.findVendors(filter))
    }

    private fun create(parent: ViewGroup): VendorDetailsBinding =
            VendorDetailsBinding.inflate(MainContext.INSTANCE.layoutInflater, parent, false)

    private inner class Binding {
        val root: View
        val vendorName: TextView
        val vendorMacs: TextView

        internal constructor(binding: VendorDetailsBinding) {
            root = binding.root
            vendorName = binding.vendorName
            vendorMacs = binding.vendorMacs
        }

        internal constructor(view: View) {
            root = view
            vendorName = view.findViewById(R.id.vendor_name)
            vendorMacs = view.findViewById(R.id.vendor_macs)
        }

    }

}