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
package com.vrem.wifianalyzer.vendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.ListFragment
import com.vrem.util.specialTrim
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.databinding.VendorContentBinding

class VendorFragment : ListFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: VendorContentBinding = VendorContentBinding.inflate(inflater, container, false)
        val vendorAdapter = VendorAdapter(requireActivity(), MainContext.INSTANCE.vendorService)
        listAdapter = vendorAdapter
        binding.vendorSearchText.setOnQueryTextListener(Listener(vendorAdapter))
        return binding.root
    }

    internal class Listener(private val vendorAdapter: VendorAdapter) : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean = false

        override fun onQueryTextChange(newText: String): Boolean {
            vendorAdapter.update(newText.specialTrim())
            return true
        }

    }
}